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
  UNPACK;
  TYPECAST;
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

methodType
	:	tupleType
	|	primitiveType
	;

type
	:	primitiveType
	|	ID
	;

primitiveType
	: REAL_TYPE
  | INTEGER_TYPE
  |	CHARACTER_TYPE
  |	BOOLEAN_TYPE
  ;
  
    
// START: method
methodForwardDeclaration
	: Function ID LPAREN formalParameters? RPAREN Returns methodType DELIM
	    -> ^(FUNCTION_DECL methodType ID formalParameters?)
  | Procedure ID LPAREN informalParameters? RPAREN (Returns methodType)? DELIM
      -> ^(PROCEDURE_DECL methodType? ID informalParameters?)
  ;

methodDeclaration
  : Function ID LPAREN formalParameters? RPAREN Returns methodType ASSIGN expression DELIM
    { if ($ID.text.equals("main")) {emitErrorMessage("error: main must be a procedure not a function");}}
       -> ^(FUNCTION_DECL methodType ID formalParameters? ^(Return expression))    
	| Function ID LPAREN formalParameters? RPAREN Returns methodType block
	  { if ($ID.text.equals("main")) {emitErrorMessage("error: main must be a procedure not a function");}}
	    -> ^(FUNCTION_DECL methodType ID formalParameters? block)
	| Procedure ID LPAREN informalParameters? RPAREN Returns methodType ASSIGN expression DELIM
	    -> ^(PROCEDURE_DECL methodType ID informalParameters? ^(Return expression))
	| Procedure ID LPAREN informalParameters? RPAREN (Returns methodType)? block
	    -> ^(PROCEDURE_DECL methodType? ID informalParameters? block)
  ;

formalParameters
   : functionParameter (',' functionParameter)* -> functionParameter+
   ;
   
informalParameters
  : procedureParameter (',' procedureParameter)* -> procedureParameter+
  ;
    
functionParameter
	:	specifier? type ID -> ^(ARG_DECL Const["const"] type ID)
	| specifier? tupleType ID -> ^(ARG_DECL Const["const"] tupleType ID)
	;
	
procedureParameter
  : specifier (a=type|b=tupleType) ID -> ^(ARG_DECL specifier $a* $b* ID)
  | (c=type|d=tupleType) ID -> ^(ARG_DECL Const["const"] $c* $d* ID)
  ;
	
// END: method

// START: block
block
@after {varDeclConstraint.pop();}
  : LBRACE {varDeclConstraint.push(true);} varDeclaration* statement* RBRACE -> ^(BLOCK varDeclaration* statement*)
  ;
// END: block

// START: tuple
tupleType
	: Tuple LPAREN tupleMember (',' tupleMember)+ RPAREN -> ^(Tuple tupleMember+)
	| ID  // typedef
	| Tuple LPAREN tupleMember RPAREN
	  { emitErrorMessage("line " + $LPAREN.getLine() + ": tuples must have more than one element"); }
	| Tuple LPAREN RPAREN
	  { emitErrorMessage("line " + $LPAREN.getLine() + ": tuples cannot be empty"); }
	;
	
tupleMember
	:	type ID? -> ^(FIELD_DECL Var["var"] type ID?)
	;
// END: tuple

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
  |	tupleType ID (ASSIGN expression)? DELIM 
    { if(varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); } 
      -> ^(VAR_DECL Var["var"] tupleType ID expression?)
  |	specifier tupleType ID (ASSIGN expression)? DELIM
    { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier tupleType ID expression?)
  | specifier ID ASSIGN expression DELIM 
    { if($specifier.text.equals("var") && varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Global variables must be declared with the const specifier."); }
      -> ^(VAR_DECL specifier ID expression)
  | inputDeclaration
  | streamDeclaration
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
// END: var

typedef
  : Typedef primitiveType ID DELIM 
    { if(!varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Typedef must only be declared in global scope."); }
      -> ^(TYPEDEF primitiveType ID)
  | Typedef tupleType ID DELIM 
    { if(!varDeclConstraint.empty()) emitErrorMessage("line " + $ID.getLine() + ": Typedef must only be declared in global scope."); }
      -> ^(TYPEDEF tupleType ID)
  ;

statement
  : block
  |	varDeclaration 
    {  
      if(!varDeclConstraint.empty() && varDeclConstraint.peek()) 
          emitErrorMessage("line " + input.LT(1).getLine()  + ": Declarations can only appear at the start of a block."); 
    }
  | typedef
  |	If LPAREN expression RPAREN s=statement (Else e=statement)? -> ^(If expression $s $e?)
  | If LPAREN expression statement (Else statement)?
    {
      emitErrorMessage("line " + $If.getLine() + ": Missing right parenthesis."); 
    }
  | If expression RPAREN statement (Else statement)?
    {
      emitErrorMessage("line " + $If.getLine() + ": Missing left parenthesis."); 
    }
  | If expression s=statement (Else e=statement)? -> ^(If expression $s $e?)
  | Else // Catch danging else statements missing corresponding if.
    {
      emitErrorMessage("line " + $Else.getLine() + ": else statement missing matching if."); 
    }
  | Loop {loopDepth++;} While LPAREN expression RPAREN statement {loopDepth--;} -> ^(WHILE expression statement)
  | Loop While LPAREN expression statement
    {
      emitErrorMessage("line " + $Loop.getLine() + ": Missing right parenthesis."); 
    }
  | Loop While expression RPAREN statement
    {
      emitErrorMessage("line " + $Loop.getLine() + ": Missing left parenthesis."); 
    } 
  | Loop {loopDepth++;} While expression statement {loopDepth--;} -> ^(WHILE expression statement)
  | Loop {loopDepth++;} statement While LPAREN expression RPAREN {loopDepth--;} -> ^(DOWHILE expression statement)
  | Loop statement While LPAREN expression
    {
      emitErrorMessage("line " + $Loop.getLine() + ": Missing right parenthesis."); 
    }
  | Loop statement While expression RPAREN
    {
      emitErrorMessage("line " + $Loop.getLine() + ": Missing left parenthesis."); 
    } 
  | Loop {loopDepth++;} statement While expression {loopDepth--;} -> ^(DOWHILE expression statement)
  | Loop {loopDepth++;} statement {loopDepth--;} -> ^(Loop statement) // infinite loop
  |	CALL postfixExpression DELIM ->  ^(EXPR postfixExpression)
  | Return expression? DELIM -> ^(Return expression?)
  |	lhs ASSIGN expression DELIM -> ^(ASSIGN lhs expression)
  | expression OUTSTREAM ID DELIM -> ^(PRINT ID expression)
  | lhs INSTREAM ID DELIM -> ^(INPUT ID lhs)
  | a=postfixExpression DELIM // handles function calls like f(i);
  		-> ^(EXPR postfixExpression)
  //| ID ASSIGN tupleMemberList DELIM -> ^(ASSIGN ID tupleMemberList)
  //| ID (',' ID)+ ASSIGN tupleMemberList DELIM -> ^(UNPACK ID+ tupleMemberList)
  | ID (',' ID)+ ASSIGN expression DELIM -> ^(UNPACK ID+ expression)
  | Break DELIM!
    {
      if(loopDepth == 0) 
          emitErrorMessage("line " + $Break.getLine() + ": Break statements can only be used within loops."); 
    }
  | Continue DELIM!
    {
      if(loopDepth == 0) 
          emitErrorMessage("line " + $Continue.getLine() + ": Continue statements can only be used within loops."); 
    }
  ;
    
lhs 
	:	postfixExpression -> ^(EXPR postfixExpression)
	;

expressionList
    : expression (',' expression)* -> ^(ELIST expression+)
    | -> ELIST
    ;

expression
    : expr -> ^(EXPR expr)
    ;
	
expr
	:	orExpression
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
	:	additiveExpression
		(	(	(	LESS^
				|	GREATER^
				|	LESS_EQUAL^
				|	GREATER_EQUAL^
				)
				additiveExpression
			)*
		)
	;
	
additiveExpression
	:	multiplicativeExpression ((ADD^ | SUBTRACT^) multiplicativeExpression)*
	;
	
multiplicativeExpression
	:	powerExpression ((MULTIPLY^ | DIVIDE^ | MODULAR^) powerExpression)*
	;
	
powerExpression
	:	unaryExpression (POWER^ unaryExpression)*
	;
	
unaryExpression
	:	ADD! unaryExpression
	|	op=SUBTRACT unaryExpression -> ^(UNARY_MINUS[$op] unaryExpression)
	|	Not unaryExpression -> ^(Not unaryExpression)
	|	postfixExpression
	;
	

// START: call
postfixExpression
    : 	ID 
    (
    	(	DOT^ (INTEGER | ID | {emitErrorMessage("line " + $DOT.getLine() + ": Only integers and identifiers are allowed to index tuples.");})
    	|	r=LPAREN^ expressionList RPAREN!	{ $r.setType(CALL); $r.setText("CALL"); }
    	// TODO: Part 2
    	//|	r=LBRACK^ expr RBRACK!				{$r.setType(INDEX);}
    	)*
    )
    |	primary -> primary
    ;
// END: call

primary
    : r=(INTEGER | INTEGER_UNDERSCORES)			{$r.setType(INTEGER);}
    |	REAL
    |	CHARACTER
    |	True
    |	False
    | Identity
    | Null
    | LPAREN expr RPAREN -> expr
    | LPAREN expression (',' expression)+ RPAREN -> ^(TUPLE_LIST expression+)
    | As LESS LPAREN primitiveType (',' primitiveType)+ RPAREN  GREATER LPAREN expression RPAREN -> ^(TYPECAST primitiveType+ expression) 
    | As LESS type GREATER LPAREN expression RPAREN -> ^(TYPECAST type expression)
    | INVALID_CHARACTER {emitErrorMessage("line " + $INVALID_CHARACTER.getLine() + ": expected single quotes for character");}
    ;
    

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
Rows : 'rows';
Columns : 'columns';
Length : 'length';
//Out : 'out';
//Inp : 'inp';
Tuple : 'tuple';
Stream_state : 'stream_state';
Revserse : 'reverse';
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
DOT : '.';
DELIM : ';';
STDOUT : 'std_output()';
STDIN : 'std_input()';

ID : (UNDERSCORE | LETTER) (UNDERSCORE |LETTER | DIGIT)*;
INTEGER : DIGIT+;
INTEGER_UNDERSCORES : DIGIT (DIGIT | UNDERSCORE)*;

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
	: 	(
			(DIGIT (DIGIT | UNDERSCORE)* DOT (DIGIT | UNDERSCORE)*)
			| {!member_access}?=> (DOT (DIGIT | UNDERSCORE)*)
			| (DIGIT (DIGIT | UNDERSCORE)*)
		) (DecimalExponent1 | DecimalExponent2)? FloatTypeSuffix?
	;
	
CHARACTER :	'\'' '\\'? . '\'' ;

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

fragment DecimalExponent1 : ('e' | 'E') UNDERSCORE* ('+' | '-') UNDERSCORE* DIGIT (DIGIT | UNDERSCORE)*;
fragment DecimalExponent2 : ('e' | 'E') UNDERSCORE* DIGIT (DIGIT | UNDERSCORE)*;
fragment FloatTypeSuffix : 'f' | 'F' | 'l' | 'L' UNDERSCORE*;
fragment UNDERSCORE : '_';
fragment DIGIT : '0'..'9';
fragment LETTER : 'a'..'z' |'A'..'Z';
