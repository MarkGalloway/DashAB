tree grammar SimplifyExpressions;

options {
  tokenVocab = Dash;
  ASTLabelType = DashAST;
  filter = true;
  backtrack=true; 
  rewrite=true; 
  output = AST;
}

@header {
  package ab.dash.opt;
  import ab.dash.ast.*;
}

bottomup
	:	expression
	;

expression
	// Logic
	:	^(And . False) -> False["false"]
	|	^(And False .) -> False["false"]
	|	^(Or . True) -> True["true"]
	|	^(Or True .) -> True["true"]
	// Arithmetic integers
	|	^(ADD ID INTEGER {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 0}?) -> ID
	|	^(ADD INTEGER ID {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 0}?) -> ID
	|	^(SUBTRACT ID INTEGER {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 0}?) -> ID
	|	^(MULTIPLY ID INTEGER {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 0}?) -> INTEGER["0"]
	|	^(MULTIPLY INTEGER ID {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 0}?) -> INTEGER["0"]
	|	^(MULTIPLY ID INTEGER {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 1}?) -> ID
	|	^(MULTIPLY INTEGER ID {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 1}?) -> ID
	|	^(DIVIDE ID INTEGER {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 1}?) -> ID
	|	^(POWER ID INTEGER {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 0}?) -> INTEGER["1"]
	|	^(POWER ID INTEGER {Integer.parseInt($INTEGER.getText().replaceAll("_", "")) == 1}?) -> ID
	;
