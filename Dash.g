grammar Dash;

options {
  language = Java;
  output = AST;
  ASTLabelType = DashAST;
}

tokens {
  PROGRAM;
}

@header {
  import ab.dash.ast.*;
}

program 
  : .* EOF -> ^(PROGRAM)
  ; 


// Dash reserved words
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


ID : LETTER ((LETTER | DIGIT))*;
INTEGER : DIGIT+;

WS : (' ' | '\t' | '\f')+ {$channel=HIDDEN;};
NL : ('\r' '\n' | '\r' | '\n' | EOF) {$channel=HIDDEN;};

fragment DIGIT : '0'..'9';
fragment LETTER : 'a'..'z' |'A'..'Z';