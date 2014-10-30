tree grammar ConstantPropagation_old;

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
    SymbolTable symtab;
    public ConstantPropagation_old(TreeNodeStream input, SymbolTable symtab) {
        this(input);
        this.symtab = symtab;
    }
    
    private String getValue(DashAST id) {
    	String value = null;
    	if (id.symbol instanceof VariableSymbol) {
	    	VariableSymbol s = (VariableSymbol)id.symbol;
			if (!s.initialValue.equals("") && 
				s.scope.getScopeIndex() == SymbolTable.scGLOBAL) {
				
				if (id.hasAncestor(EXPR)) {
					if (id.hasAncestor(BLOCK) ||
						id.hasAncestor(FUNCTION_DECL) ||
						id.hasAncestor(PROCEDURE_DECL)) {
						if (s.specifier.getSpecifierIndex() == SymbolTable.sCONST) {
							value = s.initialValue;
							System.out.println(id.symbol);
						}
					} else {
						value = s.initialValue;
						System.out.println(id.symbol);
					}
				}
			}
		}
		
		return value;
    }
}

bottomup
	:	var_decl
	|	expression
	;

var_decl
	// Integers
	: ^(VAR_DECL ID ^(EXPR INTEGER))
	{
		VariableSymbol s = (VariableSymbol)$ID.symbol;
		s.initialValue = $INTEGER.getText().replaceAll("_", "");
	}
	// Reals
	| ^(VAR_DECL ID ^(EXPR REAL))
	{
		VariableSymbol s = (VariableSymbol)$ID.symbol;
		s.initialValue = $REAL.getText().replaceAll("_", "");
	}
	;
	
expression
@init {boolean update = false; String value = null;}
	: opt=ID {$opt.symbol.type.getTypeIndex() == SymbolTable.tREAL}?=>
	{ 
		int type = -1;
		value = getValue($opt);
		
		if (value != null) {
			type = $opt.symbol.type.getTypeIndex();
			
			if (type == SymbolTable.tINTEGER) {
				update = true;
	        }
		}
	} {update == true}?=> -> ^(INTEGER[value])
	|	opt=ID {$opt.symbol.type.getTypeIndex() == SymbolTable.tINTEGER}?=>
	{ 
		int type = -1;
		value = getValue($opt);
		
		if (value != null) {
			type = $opt.symbol.type.getTypeIndex();
			
			if (type == SymbolTable.tINTEGER) {
				update = true;
	        }
		}
	} {update == true}?=> -> ^(INTEGER[value])
	;
