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
	VAR_DECL;
	FIELD_DECL;
	TUPLE_LIST;
	ELIST;       	// expression list
	EXPR; 	   		// root of an expression
	UNARY_MINUS;
}

@header {
  package ab.dash;
  import ab.dash.ast.*;
}

@lexer::header {
  package ab.dash;
}

program 
  : statement* EOF -> ^(PROGRAM statement*)
  ;

type
	:	primitiveType
	|	ID
	;

primitiveType
	:   Real
    |   Integer
    |	Character
    |	Boolean
    ;

// START: block
block 
    :   LBRACE statement* RBRACE -> ^(BLOCK statement*)
    ;
// END: block

// START: tuple
tupleType
	: Tuple LPAREN tupleMember (',' tupleMember)* RPAREN -> ^(Tuple tupleMember+)
	;
	
tupleMember
	:	type ID? -> ^(FIELD_DECL type ID?)
	|	tupleType ID? -> ^(FIELD_DECL tupleType ID?)
	;
	
tupleMemeberList
	:   LPAREN tupleMemeberList (',' tupleMemeberList)* RPAREN -> ^(TUPLE_LIST tupleMemeberList+)
	|	expr -> expr
	;
// END: tuple

// START: var
specifier
	:	Var
	|	Const
	;

varDeclaration
	:   type ID (ASSIGN expression)? DELIM 	-> ^(VAR_DECL Var type ID expression?)
    |   specifier type ID (ASSIGN expression)? DELIM 	-> ^(VAR_DECL specifier type ID expression?)
    |	tupleType ID (ASSIGN tupleMemeberList)? DELIM 	-> ^(VAR_DECL Var tupleType ID tupleMemeberList?)
    |	specifier tupleType ID (ASSIGN tupleMemeberList)? DELIM 	-> ^(VAR_DECL specifier tupleType ID tupleMemeberList?)
	;
// END: var

statement
    :   block
    |	varDeclaration
    |	If LPAREN expression RPAREN s=statement (Else e=statement)?
    	-> ^(If expression $s $e?)
    |   Return expression? DELIM -> ^(Return expression?)
    |	lhs ASSIGN expression DELIM -> ^(ASSIGN lhs expression)
    |   a=postfixExpression DELIM // handles function calls like f(i);
    		-> ^(EXPR postfixExpression)
    ;
    
lhs 
	:	postfixExpression -> ^(EXPR postfixExpression)
	;

expressionList
    :   expr (',' expr)* -> ^(ELIST expr+)
    |   -> ELIST
    ;
    
expression
    :   expr -> ^(EXPR expr)
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
    :   primary
//    	(
//    		(	r=LPAREN^ expressionList RPAREN!	{$r.setType(CALL);}
//	    	|	r=LBRACK^ expr RBRACK!				{$r.setType(INDEX);}
//    		|	r=DOT^ ID
//    		)
//    	)*
    ;
// END: call

primary
    :   ID
    |   INTEGER
    |	REAL
    |	CHARACTER
    |	True
    |	False
    |   LPAREN expression RPAREN -> expression
    ;


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

// DashAB reserved words
In : 'in';
By : 'by';
As : 'as';
Var : 'var';
Const : 'const';
Matrix : 'matrix';
Vector : 'vector';
Interval : 'interval';
Integer : 'integer';
Boolean : 'boolean';
True : 'true';
False : 'false';
Real : 'real';
Character : 'character';
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
Out : 'out';
Inp : 'inp';
Tuple : 'tuple';
Stream_state : 'stream_state';
Revserse : 'reverse';


ID : (UNDERSCORE | LETTER) ((UNDERSCORE |LETTER | DIGIT))*;
INTEGER : DIGIT+;

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
	: (DIGIT* '.' DIGIT+
	| DIGIT+ '.'  
	| DIGIT+) DecimalExponent? FloatTypeSuffix?;
	
CHARACTER :	'\'' . '\'' ;

WS : (' ' | '\t' | '\f')+ {$channel=HIDDEN;};
SL_COMMENT:   '//' ~('\r'|'\n')* '\r'? '\n' {$channel=HIDDEN;};
COMMENT: '/*' .* '*/' {$channel=HIDDEN;};
NL : ('\r' '\n' | '\r' | '\n' | EOF) {$channel=HIDDEN;};

fragment DecimalExponent : ('e' | 'E') ('+' | '-')? DIGIT+;
fragment FloatTypeSuffix : 'f' | 'F' | 'l' | 'L';
fragment UNDERSCORE : '_';
fragment DIGIT : '0'..'9';
fragment LETTER : 'a'..'z' |'A'..'Z';