grammar Dash;

options {
	backtrack = true;
	memoize = true;
	language = Java;
	output = AST;
	ASTLabelType = DashAST;
}

tokens {
	PROGRAM;
	BLOCK;
	FUNCTION_DECL;
	PROCEDURE_DECL;
	VAR_DECL;
	FIELD_DECL;
	ARG_DECL;
	TUPLE_LIST;
	ELIST;       	// expression list
	EXPR; 	   		// root of an expression
	UNARY_MINUS;
	DECL_OUTSTREAM;
  DECL_INSTREAM;
  PRINT;
  INPUT;
  TYPEDEF;
  WHILE;
  DOWHILE;
  ITERATOR;
  UNPACK;
  TYPECAST;
  VECTOR_LIST;
  VECTOR;
  VECTOR_INDEX;
  INFERRED;
  MATRIX;
  MATRIX_INDEX;
  FILTER;
  IN;
  GENERATOR;
}

// Parser Rules


@header {
	package ab.dash;
	import ab.dash.ast.*;
}

@members {
  boolean member_access = false;
  Stack<Boolean> varDeclConstraint = new Stack<Boolean>();
  int loopDepth = 0;
  
  int error_count = 0;
  StringBuffer errorSB = new StringBuffer();
  StringBuffer antlrErrorsSB = new StringBuffer();
  @Override
  public void emitErrorMessage(String msg) {
    System.err.println(msg);
    error_count++;
    errorSB.append(msg);
  }
  
  public void displayRecognitionError(String[] tokenNames,RecognitionException e) {
        antlrErrorsSB.append(getErrorHeader(e));
  }
  
  public int getErrorCount() { return error_count; }
  public String getErrors() { return errorSB.toString(); }
  public String getAntlrErrors() { return antlrErrorsSB.toString(); }
}


@lexer::header {
  package ab.dash;
}

@lexer::members {
	boolean member_access = false;
	public boolean inComment = false;
	
	int error_count = 0;
  StringBuffer errorSB = new StringBuffer();
	
	@Override
	public void emitErrorMessage(String msg) {
		System.err.println(msg);
		error_count++;
		errorSB.append(msg);
	}
	
	public int getErrorCount() { return error_count; }
	public String getErrors() { return errorSB.toString(); }
	
	@Override
	public void emit(Token token) {
		if (token.getType() == ID) {
    		member_access = true;
    	} else if (token.getType() == RPAREN) {
    		member_access = true;
    	} else {
    		member_access = false;
    	}
		super.emit(token);
	}
}




program 
	: line* EOF -> ^(PROGRAM line*)
	;
  
line
	:	methodForwardDeclaration
	|	methodDeclaration
	|	statement
	;
	
/* BEGIN TYPES */

type
	: tupleType
	| primitiveType
	| ID
	;

tupleType
  : Tuple LPAREN tupleMember (',' tupleMember)+ RPAREN -> ^(Tuple tupleMember+)
  | Tuple LPAREN tupleMember RPAREN  { emitErrorMessage("line " + $LPAREN.getLine() + ": tuples must have more than one element"); }
  | Tuple LPAREN RPAREN              { emitErrorMessage("line " + $LPAREN.getLine() + ": tuples cannot be empty"); }
  ;
  
tupleMember
  : String ID? LBRACK expression RBRACK -> ^(FIELD_DECL Var["var"] ^(VECTOR CHARACTER_TYPE["character"] expression) ID?)
  | primitiveType ID? -> ^(FIELD_DECL Var["var"] primitiveType ID?)
  | a=ID b=ID? -> ^(FIELD_DECL Var["var"] $a $b?)
  ;
  
primitiveType
	: REAL_TYPE
  | INTEGER_TYPE
  |	CHARACTER_TYPE
  |	BOOLEAN_TYPE
  ;
  
/* END TYPES */ 
    
// START: method
methodForwardDeclaration
	: Function ID LPAREN (functionParameter (',' functionParameter)*)? RPAREN Returns methodReturnType DELIM
	    -> ^(FUNCTION_DECL methodReturnType ID functionParameter*)
  | Procedure ID LPAREN (procedureParameter (',' procedureParameter)*)? RPAREN (Returns methodReturnType)? DELIM
      -> ^(PROCEDURE_DECL methodReturnType? ID procedureParameter*)
  ;

methodDeclaration
  : function
	| procedure
  ;

function
@init { Token id = null;}
@after {if (id.getText().equals("main")) emitErrorMessage("error: main must be a procedure not a function");}
  : Function ID LPAREN (functionParameter (',' functionParameter)*)? RPAREN Returns methodReturnType ASSIGN expression DELIM {id = $ID;}
      -> ^(FUNCTION_DECL methodReturnType ID functionParameter* ^(Return expression))    
  | Function ID LPAREN (functionParameter (',' functionParameter)*)? RPAREN Returns methodReturnType block {id = $ID;}
      -> ^(FUNCTION_DECL methodReturnType ID functionParameter* block)
  ;

functionParameter
  : Var primitiveType ID LBRACK MULTIPLY RBRACK 
      { emitErrorMessage("line " + $Var.getLine() + ": Function parameters cannot be declared as var."); }
  | Var primitiveType (Vector | Matrix)? ID LBRACK (expression|MULTIPLY) (',' (expression|MULTIPLY))? RBRACK
      { emitErrorMessage("line " + $Var.getLine() + ": Function parameters cannot be declared as var."); }
  | Var String ID LBRACK expression RBRACK 
       { emitErrorMessage("line " + $Var.getLine() + ": Function parameters cannot be declared as var."); }
  | Var String ID LBRACK MULTIPLY RBRACK 
       { emitErrorMessage("line " + $Var.getLine() + ": Function parameters cannot be declared as var."); }
  | Var String ID
       { emitErrorMessage("line " + $Var.getLine() + ": Function parameters cannot be declared as var."); }
  | Var type ID { emitErrorMessage("line " + $Var.getLine() + ": Function parameters cannot be declared as var."); }  
  | Var primitiveType (Vector | Matrix)? ID
    { emitErrorMessage("line " + $Var.getLine() + ": Function parameters cannot be declared as var."); }
  
       
  // vectors 
  | specifier? primitiveType Vector? ID LBRACK expression RBRACK 
      -> ^(ARG_DECL Const["const"] ^(VECTOR primitiveType expression) ID)
  | specifier? primitiveType Vector? ID LBRACK MULTIPLY RBRACK 
      -> ^(ARG_DECL Const["const"] ^(VECTOR primitiveType INFERRED) ID)
  | specifier? primitiveType Vector ID
  	  -> ^(ARG_DECL Const["const"] ^(VECTOR primitiveType INFERRED) ID)
  	  
  // matrices
  | specifier? primitiveType Matrix? ID LBRACK MULTIPLY ',' MULTIPLY RBRACK
    -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType INFERRED INFERRED) ID)
  | specifier? primitiveType Matrix? ID LBRACK MULTIPLY ',' expression RBRACK 
    -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType INFERRED expression) ID)
  | specifier? primitiveType Matrix? ID LBRACK expression ',' MULTIPLY RBRACK 
  -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType expression INFERRED) ID)
  | specifier? primitiveType Matrix? ID LBRACK expression ',' expression RBRACK 
      -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType expression+) ID)
  | specifier? primitiveType Matrix ID
    -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType INFERRED INFERRED) ID)
    
    // strings
  | specifier? String ID LBRACK expression RBRACK 
      -> ^(ARG_DECL Const["const"] ^(VECTOR CHARACTER_TYPE["character"] expression) ID)
  | specifier? String ID LBRACK MULTIPLY RBRACK 
      -> ^(ARG_DECL Const["const"] ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID)
  | specifier? String ID
  	  -> ^(ARG_DECL Const["const"] ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID)
  	  
  // interval
  | specifier? INTEGER_TYPE Interval ID
    -> ^(ARG_DECL Const["const"] Interval ID)
      
  | specifier? type ID  -> ^(ARG_DECL Const["const"] type ID)
  ;
  
procedure
  : Procedure ID LPAREN (procedureParameter (',' procedureParameter)*)? RPAREN Returns methodReturnType ASSIGN expression DELIM
     -> ^(PROCEDURE_DECL methodReturnType ID procedureParameter* ^(Return expression))
  | Procedure ID LPAREN (procedureParameter (',' procedureParameter)*)? RPAREN (Returns methodReturnType)? block
     -> ^(PROCEDURE_DECL methodReturnType? ID procedureParameter* block)
  ;
	
procedureParameter
  // vectors
  : primitiveType Vector? ID LBRACK expression RBRACK 
      -> ^(ARG_DECL Const["const"] ^(VECTOR primitiveType expression) ID)
  | primitiveType Vector? ID LBRACK MULTIPLY RBRACK 
      -> ^(ARG_DECL Const["const"] ^(VECTOR primitiveType INFERRED) ID)
  | specifier primitiveType Vector? ID LBRACK expression RBRACK 
      -> ^(ARG_DECL specifier ^(VECTOR primitiveType expression) ID)
  | specifier primitiveType Vector? ID LBRACK MULTIPLY RBRACK 
      -> ^(ARG_DECL specifier ^(VECTOR primitiveType INFERRED) ID)
  | specifier primitiveType Vector ID
      -> ^(ARG_DECL specifier ^(VECTOR primitiveType INFERRED) ID)
  | primitiveType Vector ID
  	  -> ^(ARG_DECL Const["const"] ^(VECTOR primitiveType INFERRED) ID)
  
  // matrices
  | primitiveType Matrix? ID LBRACK expression ',' expression RBRACK 
      -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType expression+) ID)
  | specifier primitiveType Matrix? ID LBRACK expression ',' expression RBRACK 
      -> ^(ARG_DECL specifier ^(MATRIX primitiveType expression+) ID)
      
  | primitiveType Matrix? ID LBRACK MULTIPLY ',' expression RBRACK 
      -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType INFERRED expression) ID)
  | specifier primitiveType Matrix? ID LBRACK MULTIPLY ',' expression RBRACK 
      -> ^(ARG_DECL specifier ^(MATRIX primitiveType INFERRED expression) ID)
      
  | primitiveType Matrix? ID LBRACK expression ',' MULTIPLY RBRACK 
      -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType expression INFERRED) ID)
  | specifier primitiveType Matrix? ID LBRACK expression ',' MULTIPLY RBRACK 
      -> ^(ARG_DECL specifier ^(MATRIX primitiveType expression INFERRED) ID)
      
  | primitiveType Matrix? ID LBRACK MULTIPLY ',' MULTIPLY RBRACK 
      -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType INFERRED INFERRED) ID)
  | specifier primitiveType Matrix? ID LBRACK MULTIPLY ',' MULTIPLY RBRACK 
      -> ^(ARG_DECL specifier ^(MATRIX primitiveType INFERRED INFERRED) ID)
      
  | primitiveType Matrix ID
      -> ^(ARG_DECL Const["const"] ^(MATRIX primitiveType INFERRED INFERRED) ID)
  | specifier primitiveType Matrix ID
      -> ^(ARG_DECL specifier ^(MATRIX primitiveType INFERRED INFERRED) ID)
  
  // strings
  | String ID LBRACK expression RBRACK 
      -> ^(ARG_DECL Const["const"] ^(VECTOR CHARACTER_TYPE["character"] expression) ID)
  | String ID LBRACK MULTIPLY RBRACK 
      -> ^(ARG_DECL Const["const"] ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID)
  | specifier String ID LBRACK expression RBRACK 
      -> ^(ARG_DECL specifier ^(VECTOR CHARACTER_TYPE["character"] expression) ID)
  | specifier String ID LBRACK MULTIPLY RBRACK 
      -> ^(ARG_DECL specifier ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID)
  | String ID
  	  -> ^(ARG_DECL Const["const"] ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID)
  | specifier String ID
  	  -> ^(ARG_DECL specifier ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID)
  	  
  // interval
  | INTEGER_TYPE Interval ID
    -> ^(ARG_DECL Const["const"] Interval ID)
  | specifier INTEGER_TYPE Interval ID
    -> ^(ARG_DECL specifier Interval ID)
  	  
  | type ID -> ^(ARG_DECL Const["const"] type ID) 
  | specifier type ID -> ^(ARG_DECL specifier type ID)
  ;

methodReturnType
  : primitiveType Vector? LBRACK expression RBRACK -> ^(VECTOR primitiveType expression)
  | primitiveType Vector? LBRACK MULTIPLY RBRACK -> ^(VECTOR primitiveType INFERRED)
  | String LBRACK expression RBRACK -> ^(VECTOR CHARACTER_TYPE["character"] expression)
  | String LBRACK MULTIPLY RBRACK -> ^(VECTOR CHARACTER_TYPE["character"] INFERRED)
  | primitiveType Matrix? LBRACK expression ',' expression RBRACK -> ^(MATRIX primitiveType expression+)
  | primitiveType Matrix? LBRACK MULTIPLY ',' expression RBRACK -> ^(MATRIX primitiveType INFERRED expression)
  | primitiveType Matrix? LBRACK expression ',' MULTIPLY RBRACK -> ^(MATRIX primitiveType expression INFERRED)
  | primitiveType Matrix? LBRACK MULTIPLY ',' MULTIPLY RBRACK -> ^(MATRIX primitiveType INFERRED INFERRED)
  | primitiveType Vector -> ^(VECTOR primitiveType INFERRED)
  | primitiveType Matrix -> ^(MATRIX primitiveType INFERRED INFERRED)
  | String -> ^(VECTOR CHARACTER_TYPE["character"] INFERRED)
  | tupleType
  | type
  ;
	
// END: method

// START: block
block
  : LBRACE {varDeclConstraint.push(true);} varDeclaration* nonDeclarableStatement* RBRACE {varDeclConstraint.pop();} -> ^(BLOCK varDeclaration* nonDeclarableStatement*)
  | LBRACE {varDeclConstraint.push(true);} varDeclaration* nonDeclarableStatement* varDeclaration
    {
      emitErrorMessage("In the block starting on line " + $LBRACE.getLine() + ": Declarations can only appear at the start of this block."); 
    }
  ;
// END: block

// START: var
specifier
	:	Var
	|	Const
	;

varDeclaration
	: type ID (ASSIGN expression)? DELIM 
	  { if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); } 
	    -> ^(VAR_DECL Var["var"] type ID expression?)
  | specifier type ID (ASSIGN expression)? DELIM  
    { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier type ID expression?)
  | specifier ID ASSIGN expression DELIM 
    {if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier ID expression)
  | inputDeclaration
  | streamDeclaration
  | intervalDeclaration 
  | vectorDeclaration
  | stringDeclaration
  | matrixDeclaration
	;

inputDeclaration
  : type decl=ID INSTREAM instream=ID DELIM -> ^(INSTREAM Var["var"] type $decl $instream)
  | specifier type decl=ID INSTREAM instream=ID DELIM -> ^(INSTREAM specifier type $decl $instream)
  | specifier decl=ID INSTREAM ID instream=DELIM -> ^(INSTREAM specifier $decl $instream)
  ;
  
streamDeclaration throws ParserError
  : specifier ID ASSIGN STDOUT DELIM -> ^(DECL_OUTSTREAM specifier ID STDOUT)
  | specifier ID ASSIGN STDIN DELIM -> ^(DECL_INSTREAM specifier ID STDIN)
  | specifier? type ID ASSIGN STDIN DELIM
      { emitErrorMessage("line " + $STDIN.getLine() + ": " + "unexpected type for " + $STDIN.text); }
  | specifier? type ID ASSIGN STDOUT DELIM
      { emitErrorMessage("line " + $STDOUT.getLine() + ": " + "unexpected type for " + $STDOUT.text); }
  ;
  
intervalDeclaration
  : INTEGER_TYPE Interval ID (ASSIGN expression)? DELIM
    { if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL Var["var"] Interval ID expression?)
  | specifier INTEGER_TYPE? Interval ID (ASSIGN expression)? DELIM
    { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier Interval ID expression?)
  | specifier? type Interval ID (ASSIGN expression)? DELIM
      { emitErrorMessage("line " + $ID.getLine() + ": Intervals only support integer base types."); }
  ;
  
vectorDeclaration
  : primitiveType Vector? ID LBRACK size=expression RBRACK (ASSIGN init=expression)? DELIM  // Explicit size no specifier
    { if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL Var["var"] ^(VECTOR primitiveType $size) ID $init?)
  | specifier primitiveType Vector? ID LBRACK size=expression RBRACK (ASSIGN init=expression)? DELIM  // Explicit size w/ specifier
    { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier ^(VECTOR primitiveType $size) ID $init?)
  | primitiveType Vector? ID (LBRACK MULTIPLY RBRACK)? ASSIGN init=expression DELIM // Implicit size no specifier
    { if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL Var["var"] ^(VECTOR primitiveType INFERRED) ID $init)
  | specifier primitiveType Vector? ID (LBRACK MULTIPLY RBRACK)? ASSIGN init=expression DELIM // Implicit size w/ specifier
    { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier ^(VECTOR primitiveType INFERRED) ID $init)
  ;
  
matrixDeclaration
  :  primitiveType Matrix? ID LBRACK (s1=expression | s2=MULTIPLY) ',' (s3=expression|s4=MULTIPLY) RBRACK (ASSIGN init=expression)? DELIM
      {if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); 
       if($s2 != null) { $s2.setType(INFERRED); $s2.setText("INFERRED");}
       if($s4 != null) { $s4.setType(INFERRED); $s4.setText("INFERRED");}}
      -> ^(VAR_DECL Var["var"] ^(MATRIX primitiveType $s1? $s2? $s3? $s4?) ID $init?)
  |  specifier primitiveType Matrix? ID LBRACK (s1=expression | s2=MULTIPLY) ',' (s3=expression|s4=MULTIPLY) RBRACK (ASSIGN init=expression)? DELIM
      {if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); 
       if($s2 != null) { $s2.setType(INFERRED); $s2.setText("INFERRED");}
       if($s4 != null) { $s4.setType(INFERRED); $s4.setText("INFERRED");}}
      -> ^(VAR_DECL specifier ^(MATRIX primitiveType $s1? $s2? $s3? $s4?) ID $init?)
  | primitiveType Matrix? ID ASSIGN init=expression DELIM
     { if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
     -> ^(VAR_DECL Var["var"] ^(MATRIX primitiveType INFERRED INFERRED) ID $init)
  | specifier primitiveType Matrix? ID ASSIGN init=expression DELIM
     { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
     -> ^(VAR_DECL specifier ^(MATRIX primitiveType INFERRED INFERRED) ID $init)
  ;
  
stringDeclaration
  : String ID LBRACK size=expression RBRACK (ASSIGN init=expression)? DELIM  // Explicit size no specifier
    { if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL Var["var"] ^(VECTOR CHARACTER_TYPE["character"] $size) ID $init?)
  | specifier String ID LBRACK size=expression RBRACK (ASSIGN init=expression)? DELIM  // Explicit size w/ specifier
    { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier ^(VECTOR CHARACTER_TYPE["character"] $size) ID $init?)
  | String ID LBRACK MULTIPLY RBRACK ASSIGN init=expression DELIM // Implicit size no specifier
    { if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL Var["var"] ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID $init)
  | specifier String ID LBRACK MULTIPLY RBRACK ASSIGN init=expression DELIM // Implicit size w/ specifier
    { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID $init)
  | String ID (ASSIGN init=expression)? DELIM // Implicit size no specifier
    { if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL Var["var"] ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID $init?)
  | specifier String ID (ASSIGN init=expression)? DELIM // Implicit size w/ specifier
    { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID $init?)
  ;

  
// END: var

typedef
@init { int line = -1; }
@after { if(!varDeclConstraint.empty()) emitErrorMessage("line " + line + ": Typedef must only be declared in global scope.");}
  : Typedef type ID DELIM {line = $ID.getLine();} 
      -> ^(TYPEDEF type ID)
  | Typedef primitiveType LBRACK expression RBRACK ID DELIM                     {line = $ID.getLine();}  // Vector explicit size
      -> ^(TYPEDEF ^(VECTOR primitiveType expression) ID)
  | Typedef primitiveType LBRACK MULTIPLY RBRACK ID DELIM                     {line = $ID.getLine();}  // Vector implicit size
      -> ^(TYPEDEF ^(VECTOR primitiveType INFERRED) ID)
  | Typedef primitiveType Vector ID DELIM                                       {line = $ID.getLine();}  // Vector implicit size
      -> ^(TYPEDEF ^(VECTOR primitiveType INFERRED) ID)
  | Typedef String LBRACK expression RBRACK ID DELIM                     		{line = $ID.getLine();}  // String explicit size
      -> ^(TYPEDEF ^(VECTOR CHARACTER_TYPE["character"] expression) ID)
  | Typedef String LBRACK MULTIPLY RBRACK ID DELIM                              {line = $ID.getLine();}  // String implicit size
      -> ^(TYPEDEF ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID)
  | Typedef String ID DELIM                                       				{line = $ID.getLine();}  // String implicit size
      -> ^(TYPEDEF ^(VECTOR CHARACTER_TYPE["character"] INFERRED) ID)
  | Typedef primitiveType LBRACK expression ',' expression RBRACK ID DELIM      {line = $ID.getLine();}  // Matrix explicit size
      -> ^(TYPEDEF ^(MATRIX primitiveType expression) ID)
  | Typedef primitiveType Matrix ID DELIM                                       {line = $ID.getLine();}  // Matrix implicit size
      -> ^(TYPEDEF ^(MATRIX primitiveType INFERRED INFERRED) ID)
  | Typedef INTEGER_TYPE Interval ID DELIM                                      {line = $ID.getLine();}  // Interval
      -> ^(TYPEDEF Interval ID)
  ;

statement
  : varDeclaration
  | nonDeclarableStatement
  ;

nonDeclarableStatement
  : block
  | typedef
  |	conditionalStatement
  | loopStatement
  |	CALL postfixExpression DELIM ->  ^(EXPR postfixExpression)
  | Return expression? DELIM -> ^(Return expression?)
  |	lhs ASSIGN expression DELIM -> ^(ASSIGN lhs expression)
  | expression OUTSTREAM ID DELIM -> ^(PRINT ID expression)
  | lhs INSTREAM ID DELIM -> ^(INPUT ID lhs)
  | a=postfixExpression DELIM // handles function calls like f(i);
  		-> ^(EXPR postfixExpression)
  | ID (',' ID)+ ASSIGN expression DELIM -> ^(UNPACK ^(EXPR ID)+ expression)
  | flowControlStatement
  ;

conditionalStatement
  : If LPAREN expression RPAREN s=statement (Else e=statement)? -> ^(If expression $s $e?)
  | If LPAREN expression statement (Else statement)? { emitErrorMessage("line " + $If.getLine() + ": Missing right parenthesis."); }
  | If expression RPAREN statement (Else statement)? { emitErrorMessage("line " + $If.getLine() + ": Missing left parenthesis."); }
  | If expression s=statement (Else e=statement)? -> ^(If expression $s $e?)
  | // Catch danging else statements missing corresponding if.
    Else {  emitErrorMessage("line " + $Else.getLine() + ": else statement missing matching if."); }
  ;

loopStatement
  : Loop {loopDepth++;} While LPAREN expression RPAREN statement {loopDepth--;} -> ^(WHILE expression statement)
  | Loop While LPAREN expression statement { emitErrorMessage("line " + $Loop.getLine() + ": Missing right parenthesis."); }
  | Loop While expression RPAREN statement { emitErrorMessage("line " + $Loop.getLine() + ": Missing left parenthesis."); } 
  | Loop {loopDepth++;} While expression statement {loopDepth--;} -> ^(WHILE expression statement)
  | Loop {loopDepth++;} statement While LPAREN expression RPAREN {loopDepth--;} -> ^(DOWHILE expression statement)
  | Loop statement While LPAREN expression { emitErrorMessage("line " + $Loop.getLine() + ": Missing right parenthesis."); }
  | Loop statement While expression RPAREN { emitErrorMessage("line " + $Loop.getLine() + ": Missing left parenthesis."); } 
  | Loop {loopDepth++;} statement While expression {loopDepth--;} -> ^(DOWHILE expression statement)
  | Loop {loopDepth++;} statement {loopDepth--;} -> ^(Loop statement) // infinite loop
  |	Loop {loopDepth++;} LPAREN domainExpression (',' domainExpression)* RPAREN statement {loopDepth--;} -> ^(ITERATOR domainExpression+ statement)
  | Loop LPAREN domainExpression (',' domainExpression)* statement { emitErrorMessage("line " + $Loop.getLine() + ": Missing right parenthesis."); }
  | Loop domainExpression (',' domainExpression)* RPAREN statement { emitErrorMessage("line " + $Loop.getLine() + ": Missing left parenthesis."); } 
  | Loop {loopDepth++;} domainExpression (',' domainExpression)* statement {loopDepth--;} -> ^(ITERATOR domainExpression+ statement)
  ;

lhs 
	:	postfixExpression -> ^(EXPR postfixExpression)
	;

flowControlStatement
  : Break DELIM!  
      { if(loopDepth == 0)  emitErrorMessage("line " + $Break.getLine() + ": Break statements can only be used within loops."); }
  | Continue DELIM! 
      { if(loopDepth == 0) emitErrorMessage("line " + $Continue.getLine() + ": Continue statements can only be used within loops."); }
  ;

/* END STATEMENTS */


/* BEGIN EXPRESSIONS */

expressionList
    : expression (',' expression)* -> ^(ELIST expression+)
    | -> ELIST
    ;

expression
    : expr -> ^(EXPR expr)
    ;
	
expr
	:	concatenationExpression
	;

concatenationExpression
  : orExpression (CONCAT^ orExpression )*
  ;

orExpression
	:	andExpression ((Or^ | Xor^) andExpression)*
	;

andExpression
	:	equalityExpression (And^ equalityExpression)*
	;
	
equalityExpression
	:	relationalExpression ((INEQUALITY^ | EQUALITY^) relationalExpression)*
	;

relationalExpression
	:	byExpression
		(	(	(	LESS^
				|	GREATER^
				|	LESS_EQUAL^
				|	GREATER_EQUAL^
				)
				byExpression
			)*
		)
	;

byExpression
  : additiveExpression (By^ additiveExpression)?
  ;

additiveExpression
	:	multiplicativeExpression ((ADD^ | SUBTRACT^) multiplicativeExpression)*
	;
	
multiplicativeExpression
	:	powerExpression ((MULTIPLY^ | DIVIDE^ | MODULAR^ | DOTPRODUCT^) powerExpression)*
	;
	
powerExpression
	:	unaryExpression (POWER^ unaryExpression)*
	;
	
unaryExpression
	:	op=ADD unaryExpression -> unaryExpression
	|	op=SUBTRACT unaryExpression -> ^(UNARY_MINUS[$op] unaryExpression)
	|	Not unaryExpression -> ^(Not unaryExpression)
	|	rangeExpression
	;
	
rangeExpression
  : postfixExpression (RANGE^ postfixExpression)?
  ;
	

// START: call
postfixExpression
    : primary
    (
    	(	DOT^ (INTEGER | ID | {emitErrorMessage("line " + $DOT.getLine() + ": Only integers and identifiers are allowed to index tuples.");})
    	|	r=LPAREN^ expressionList RPAREN!	{ $r.setType(CALL); $r.setText("CALL"); }
    	|	r=LBRACK^ expression ','! expression RBRACK!	{ $r.setType(MATRIX_INDEX); $r.setText("MATRIX_INDEX");}
    	|	r=LBRACK^ expression RBRACK!				{ $r.setType(VECTOR_INDEX); $r.setText("VECTOR_INDEX");}
    	)*
    )
    ;
// END: call

primary
    : ID
    | r=(INTEGER | INTEGER_UNDERSCORES)			{$r.setType(INTEGER);}
    |	(r=REAL) 
			{  if (r.getText().endsWith(".")) {r.setText(r.getText() + "0");}  }
    |	CHARACTER
    | STRING
    |	True
    |	False
    | Identity
    | Null
    | LPAREN expr RPAREN -> expr
    | LPAREN expression (',' expression)+ RPAREN -> ^(TUPLE_LIST expression+)
    | generator
    | LBRACK expression (',' expression)* RBRACK -> ^(VECTOR_LIST expression+)
    | filter
    | typecast
    | INVALID_CHARACTER {emitErrorMessage("line " + $INVALID_CHARACTER.getLine() + ": expected single quotes for character");}
    | LPAREN RPAREN {emitErrorMessage("line " + $LPAREN.getLine() + ": Empty tuple lists are not allowed.");}
    | LBRACK RBRACK {emitErrorMessage("line " + $LBRACK.getLine() + ": Empty vector construction is not allowed.");}
    ;

filter
  : Filter LPAREN domainExpression PIPE RPAREN 
      {emitErrorMessage("line " + $LPAREN.getLine() + ": Filters must have at least one predicate.");}
  | Filter LPAREN domainExpression PIPE expressionList RPAREN -> ^(FILTER domainExpression expressionList)
  ;

generator
  : LBRACK d+=domainExpression (',' d+=domainExpression)* PIPE expression RBRACK 
        {if($d.size() > 2) emitErrorMessage("line " + $LBRACK.getLine() + ": Generators cannot have more than two domain expressions.");} 
        -> ^(GENERATOR domainExpression+ expression)
  ;

typecast
  : As LESS LPAREN primitiveType (',' primitiveType)+ RPAREN  GREATER LPAREN expression RPAREN -> ^(TYPECAST primitiveType+ expression) 
  | As LESS type GREATER LPAREN expression RPAREN -> ^(TYPECAST type expression)
  ;

domainExpression
  : ID In expression -> ^(IN ID expression)
  ;
   
/* END EXPRESSIONS */



// DashAB reserved words
CALL : 'call';
In : 'in';
By : 'by';
As : 'as';
Var : 'var';
Const : 'const';
Matrix : 'matrix';
Vector : 'vector';
Interval : 'interval';
INTEGER_TYPE : 'integer';
BOOLEAN_TYPE : 'boolean';
True : 'true';
False : 'false';
REAL_TYPE : 'real';
CHARACTER_TYPE : 'character';
String : 'string';
Procedure : 'procedure';
Function : 'function';
Returns : 'returns';
Typedef : 'typedef';
If : 'if';
Else : 'else';
Loop : 'loop';
While : 'while';
Break : 'break';
Continue : 'continue';
Return : 'return';
Filter : 'filter';
Not : 'not';
And : 'and';
Or : 'or';
Xor : 'xor';
//Rows : 'rows';
//Columns : 'columns';
//Length : 'length'; built in function
//Out : 'out';
//Inp : 'inp';
Tuple : 'tuple';
//Stream_state : 'stream_state';
//Reverse : 'reverse';
Identity : 'identity';
Null : 'null';


// Lexer Rules
OUTSTREAM : '->';
INSTREAM : '<-';
EQUALITY : '==';
INEQUALITY : '!=';
ASSIGN : '=';
GREATER : '>';
LESS : '<';
GREATER_EQUAL : '>=';
LESS_EQUAL : '<=';
ADD : '+';
SUBTRACT : '-';
DOTPRODUCT: '**';
MULTIPLY : '*';
DIVIDE : '/';
MODULAR : '%';
POWER : '^';
LPAREN : '(';
RPAREN : ')';
LBRACK : '[';
RBRACK : ']';
LBRACE : '{';
RBRACE : '}';
PIPE : '|';
RANGE: '..';
DOT : '.';
DELIM : ';';
STDOUT : 'std_output()';
STDIN : 'std_input()';
CONCAT : '||';

ID : (UNDERSCORE | LETTER) (UNDERSCORE |LETTER | DIGIT)*;
INTEGER : DIGIT+;
INTEGER_UNDERSCORES
@after {
  setText(getText().replaceAll("_", ""));
}
  : DIGIT (DIGIT | UNDERSCORE)*
  ;

/*
How to read diagram: 
http://www-01.ibm.com/support/knowledgecenter/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0006726.html?cp=SSEPGG_9.7.0%2F2-10-0


Read syntax diagramSkip visual syntax diagram
Binary floating-point literal syntax

     .-----------.     .-------.                          
     V           |     V       |                          
>>-+---+-------+-+--.----digit-+--| exponent |-+--+---+--------><
   |   '-digit-'                               |  +-f-+   
   | .-------.                                 |  +-F-+   
   | V       |                                 |  +-l-+   
   +---digit-+--.--| exponent |----------------+  '-L-'   
   | .-------.                                 |          
   | V       |                                 |          
   '---digit-+--| exponent |-------------------'          

Exponent

                  .-------.   
                  V       |   
|--+-e-+--+----+----digit-+-------------------------------------|
   '-E-'  +-+--+              
          '- --'   
          
Examples:          
	5.3876e4 			53,876
	4e-11 				0.00000000004
	1e+5 				100000
	7.321E-3 			0.007321
	3.2E+4 				32000
	0.5e-6 				0.0000005
	0.45 				0.45
	6.e10 				60000000000
	45.        45.0
*/

REAL
@after {
  setText(getText().replaceAll("_", ""));
}
	: 	DIGIT (DIGIT | UNDERSCORE)*
	(
		(DOT ~(DOT))=> DOT (DIGIT | UNDERSCORE)* DecimalExponent1? FloatTypeSuffix?
		| (RANGE)=> {_type=INTEGER;} 
		| DecimalExponent1 FloatTypeSuffix?
	)	
		| {!member_access}?=> (DOT (DIGIT | UNDERSCORE)*) DecimalExponent1? FloatTypeSuffix?
	;

STRING
@after {
  setText(getText().substring(1, getText().length()-1).replaceAll("\\\\(\")", "$1"));
}
  : '"' (~('"' | '\\') | '\\' ('\\' | '"'| 'a' | 'b' | 'n' | 'r' | 't' | '0'))* '"'
  ;

fragment Quote : 	'\'';
CHARACTER 
  : a=Quote 
        (~'\\'  // Any non-escape character here
        | '\\' ('a'| 'b' | 'n' | 'r' | 't' | '\\' | '\'' | '"' | '0'| // Invalid escape, fallthrough
          {emitErrorMessage("line " + $a.getLine() + ": invalid escape sequence");})
        ) 
      Quote
  ;

INVALID_CHARACTER : '"' '\\'? . '"' ;

WS : (' ' | '\t' | '\f')+ {$channel=HIDDEN;};

SL_COMMENT:   '//' ~('\r'|'\n')* (NL | EOF) {$channel=HIDDEN;};
MULTILINE_COMMENT : {inComment = true;} COMMENT_NESTED { $channel=HIDDEN; inComment = false; };

fragment
COMMENT_NESTED
options {backtrack = false;}
  : '/*'
    ( options {greedy=false;} : . )*
    ( COMMENT_NESTED 
    {
    	if(!$COMMENT_NESTED.getText().equals(""))
    		emitErrorMessage("line " + $COMMENT_NESTED.getLine() + ": comments cannot be nested");
    		
    }( options {greedy=false;} : . )* )*
    MULTILINE_COMMENT_END
  ;
  

COMMENT_END_ERROR
  : MULTILINE_COMMENT_END
    { emitErrorMessage("line " + $MULTILINE_COMMENT_END.getLine() + ": missing opening comment '/*'");}
  ;
  
fragment MULTILINE_COMMENT_END
  : '*/'
  ;
  
  
NL : ('\r' '\n' | '\r' | '\n') {$channel=HIDDEN;};

fragment DecimalExponent1 : ('e' | 'E') (UNDERSCORE* ('+' | '-') UNDERSCORE*)? DIGIT (DIGIT | UNDERSCORE)*;
//fragment DecimalExponent2 : ('e' | 'E') UNDERSCORE* DIGIT (DIGIT | UNDERSCORE)*;
fragment FloatTypeSuffix : 'f' | 'F' | 'l' | 'L' UNDERSCORE*;
fragment UNDERSCORE : '_';
fragment DIGIT : '0'..'9';
fragment LETTER : 'a'..'z' |'A'..'Z';
