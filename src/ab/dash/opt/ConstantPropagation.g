tree grammar ConstantPropagation;

options {
  language = Java;
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

@members {
 	boolean fixed_state = true;
    SymbolTable symtab;
    public ConstantPropagation(TreeNodeStream input, SymbolTable symtab) {
        this(input);
        this.symtab = symtab;
    }
    
    public void resetFixed() {
    	fixed_state = true;
    }
    
    public boolean isFixed() {
    	return fixed_state;
    }
}

bottomup
	:	var_decl
	|	expression
	;

var_decl
@after {fixed_state = false;}
	// Integers
	: ^(VAR_DECL ID ^(EXPR INTEGER))
	{
		VariableSymbol s = (VariableSymbol)$ID.symbol;
		s.initialValue = $INTEGER.getText().replaceAll("_", "");
	}
	;
	
expression
@init {boolean update = false; String value = null;}
@after {fixed_state = false;}
	:	opt1=ID
	{ 
		if ($opt1.symbol instanceof VariableSymbol) {
	    	VariableSymbol s = (VariableSymbol)$opt1.symbol;
			if (!s.initialValue.equals("") && 
				s.scope.getScopeIndex() == SymbolTable.scGLOBAL) {
				
				if ($opt1.hasAncestor(EXPR)) {
					if ($opt1.hasAncestor(BLOCK) ||
						$opt1.hasAncestor(FUNCTION_DECL) ||
						$opt1.hasAncestor(PROCEDURE_DECL)) {
						if (s.specifier.getSpecifierIndex() == SymbolTable.sCONST) {
							update = true;
							value = s.initialValue;
						}
					} else {
						update = true;
						value = s.initialValue;
					}
				}
			}
		}
	}
	{update}?=> -> ^(INTEGER[value])
	;
