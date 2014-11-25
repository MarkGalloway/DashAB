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
	:	tupleType
	| String
	| primitiveType
	|	ID
	;

tupleType
  : Tuple LPAREN tupleMember (',' tupleMember)+ RPAREN -> ^(Tuple tupleMember+)
  | Tuple LPAREN tupleMember RPAREN  { emitErrorMessage("line " + $LPAREN.getLine() + ": tuples must have more than one element"); }
  | Tuple LPAREN RPAREN              { emitErrorMessage("line " + $LPAREN.getLine() + ": tuples cannot be empty"); }
  ;
  
tupleMember
  : type ID? -> ^(FIELD_DECL Var["var"] type ID?)
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
	: Function ID LPAREN formalParameters? RPAREN Returns type DELIM
	    -> ^(FUNCTION_DECL type ID formalParameters?)
  | Procedure ID LPAREN informalParameters? RPAREN (Returns type)? DELIM
      -> ^(PROCEDURE_DECL type? ID informalParameters?)
  ;

methodDeclaration
  : Function ID LPAREN formalParameters? RPAREN Returns type ASSIGN expression DELIM
    { if ($ID.text.equals("main")) {emitErrorMessage("error: main must be a procedure not a function");}}
       -> ^(FUNCTION_DECL type ID formalParameters? ^(Return expression))    
	| Function ID LPAREN formalParameters? RPAREN Returns type block
	  { if ($ID.text.equals("main")) {emitErrorMessage("error: main must be a procedure not a function");}}
	    -> ^(FUNCTION_DECL type ID formalParameters? block)
	| Procedure ID LPAREN informalParameters? RPAREN Returns type ASSIGN expression DELIM
	    -> ^(PROCEDURE_DECL type ID informalParameters? ^(Return expression))
	| Procedure ID LPAREN informalParameters? RPAREN (Returns type)? block
	    -> ^(PROCEDURE_DECL type? ID informalParameters? block)
  ;

formalParameters
   : functionParameter (',' functionParameter)* -> functionParameter+
   ;
   
informalParameters
  : procedureParameter (',' procedureParameter)* -> procedureParameter+
  ;
    
functionParameter
	:	specifier? type ID -> ^(ARG_DECL Const["const"] type ID)
	;
	
procedureParameter
  : specifier type ID -> ^(ARG_DECL specifier type ID)
  | type ID -> ^(ARG_DECL Const["const"] type ID)
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
     -> ^(VAR_DECL Var["var"] ^(MATRIX primitiveType INFERRED) ID $init)
  | specifier primitiveType Matrix? ID ASSIGN init=expression DELIM
     { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
     -> ^(VAR_DECL specifier ^(MATRIX primitiveType INFERRED) ID $init)
  ;

  
// END: var

typedef
@init { int line = -1; }
@after { if(!varDeclConstraint.empty()) emitErrorMessage("line " + line + ": Typedef must only be declared in global scope.");}
  : Typedef type ID DELIM {line = $ID.getLine();} 
      -> ^(TYPEDEF type ID)
  | Typedef primitiveType LBRACK expression RBRACK ID DELIM {line = $ID.getLine();}   // Vector explicit size.. INVALID?
      -> ^(TYPEDEF ^(VECTOR primitiveType expression) ID)
  | Typedef primitiveType Vector ID DELIM {line = $ID.getLine();}
      -> ^(TYPEDEF ^(VECTOR primitiveType INFERRED) ID)
  | Typedef primitiveType LBRACK expression ',' expression RBRACK ID DELIM {line = $ID.getLine();}  // Matrix explicit size.. INVALID?
      -> ^(TYPEDEF ^(VECTOR primitiveType expression) ID)
  | Typedef primitiveType Matrix ID DELIM {line = $ID.getLine();}
      -> ^(TYPEDEF ^(MATRIX primitiveType INFERRED INFERRED) ID)
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
  |	Loop {loopDepth++;} LPAREN ID In expression RPAREN statement {loopDepth--;} -> ^(ITERATOR ID expression statement)
  | Loop LPAREN ID In  expression statement { emitErrorMessage("line " + $Loop.getLine() + ": Missing right parenthesis."); }
  | Loop ID In expression RPAREN statement { emitErrorMessage("line " + $Loop.getLine() + ": Missing left parenthesis."); } 
  | Loop {loopDepth++;} ID In expression statement {loopDepth--;} -> ^(ITERATOR ID expression statement)
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
    : 	ID 
    (
    	(	DOT^ (INTEGER | ID | {emitErrorMessage("line " + $DOT.getLine() + ": Only integers and identifiers are allowed to index tuples.");})
    	|	r=LPAREN^ expressionList RPAREN!	{ $r.setType(CALL); $r.setText("CALL"); }
    	|	r=LBRACK^ expr ','! expr RBRACK!	{ $r.setType(MATRIX_INDEX); $r.setText("MATRIX_INDEX");}
    	|	r=LBRACK^ expr RBRACK!				{ $r.setType(VECTOR_INDEX); $r.setText("VECTOR_INDEX");}
    	)*
    )
    |	primary
    ;
// END: call

primary
    : r=(INTEGER | INTEGER_UNDERSCORES)			{$r.setType(INTEGER);}
    |	REAL
    |	CHARACTER
    | STRING_LIT
    |	True
    |	False
    | Identity
    | Null
    | LPAREN expr RPAREN -> expr
    | LPAREN expression (',' expression)+ RPAREN -> ^(TUPLE_LIST expression+)
    | LBRACK domainExpression (',' domainExpression)? PIPE expression RBRACK -> ^(GENERATOR domainExpression+ expression)
    | LBRACK expression (',' expression)* RBRACK -> ^(VECTOR_LIST expression+)
    | Filter LPAREN domainExpression PIPE expressionList RPAREN -> ^(FILTER domainExpression expressionList)
    | As LESS LPAREN primitiveType (',' primitiveType)+ RPAREN  GREATER LPAREN expression RPAREN -> ^(TYPECAST primitiveType+ expression) 
    | As LESS type GREATER LPAREN expression RPAREN -> ^(TYPECAST type expression)
    | INVALID_CHARACTER {emitErrorMessage("line " + $INVALID_CHARACTER.getLine() + ": expected single quotes for character");}
    | LPAREN RPAREN {emitErrorMessage("line " + $LPAREN.getLine() + ": Empty tuple lists are not allowed.");}
    | LBRACK RBRACK {emitErrorMessage("line " + $LBRACK.getLine() + ": Empty vector construction is not allowed.");}
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
*/

REAL
@after {
  setText(getText().replaceAll("_", ""));
}
	: 	DIGIT (DIGIT | UNDERSCORE)* 
	(
		(DOT (DIGIT | UNDERSCORE | DecimalExponent1))=> DOT (DIGIT | UNDERSCORE)* DecimalExponent1? FloatTypeSuffix?
		| (RANGE)=> {_type=INTEGER;} 
		| DecimalExponent1 FloatTypeSuffix?
	)
		| {!member_access}?=> (DOT (DIGIT | UNDERSCORE)*) DecimalExponent1? FloatTypeSuffix?
	;

STRING_LIT
@after {
  setText(getText().substring(1, getText().length()-1).replaceAll("\\\\(\")", "$1"));
}
  : '"' (~('"' | '\\') | '\\' ('\\' | '"'| 'a' | 'b' | 'n' | 'r' | 't' | '0'))* '"'
  ;

fragment Quote : 	'\'';
//CHARACTER :	'\'' '\\'? . '\'' ;
CHARACTER : Quote (~'\\' | '\\' ('a'| 'b' | 'n' | 'r' | 't' | '\\' | '\'' | '"' | '0')) Quote
          | a=Quote ('\\' .) Quote {emitErrorMessage("line " + $a.getLine() + ": invalid escape sequence");} 
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
