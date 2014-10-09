grammar Dash;

options {
  language = Java;
  output = AST;
}

tokens {
  PROGRAM;
}

program 
  : .* EOF -> ^(PROGRAM)
  ; 
  


ID : LETTER ((LETTER | DIGIT))*;
INTEGER : DIGIT+;

WS : (' ' | '\t' | '\f')+ {$channel=HIDDEN;};
NL : ('\r' '\n' | '\r' | '\n' | EOF) {$channel=HIDDEN;};

fragment DIGIT : '0'..'9';
fragment LETTER : 'a'..'z' |'A'..'Z';