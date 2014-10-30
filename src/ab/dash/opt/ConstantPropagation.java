package ab.dash.opt;

import org.antlr.runtime.CommonToken;

import ab.dash.DashLexer;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;
import ab.dash.ast.VariableSymbol;

public class ConstantPropagation {
	SymbolTable symtab;
    public ConstantPropagation(SymbolTable symtab) {
        this.symtab = symtab;
    }
    
    private String getValue(DashAST id) {
    	String value = null;
    	if (id.symbol instanceof VariableSymbol) {
	    	VariableSymbol s = (VariableSymbol)id.symbol;
			if (!s.initialValue.equals("") && 
				s.scope.getScopeIndex() == SymbolTable.scGLOBAL) {
				
				if (id.hasAncestor(DashLexer.EXPR)) {
					if (id.hasAncestor(DashLexer.BLOCK) ||
						id.hasAncestor(DashLexer.FUNCTION_DECL) ||
						id.hasAncestor(DashLexer.PROCEDURE_DECL)) {
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
    
    public void optimize(DashAST tree) {
		exec(tree);
	}
	
	protected void exec(DashAST t) {
		switch (t.getToken().getType()) {
		case DashLexer.VAR_DECL: {
			
			if (t.getChildCount() == 2) {
				DashAST id = (DashAST) t.getChild(0);
				DashAST expr = (DashAST) t.getChild(1);
				
				if (expr.getChildCount() == 1) {
					DashAST value = (DashAST) expr.getChild(0);
					if (id.getToken().getType() == DashLexer.ID) {
						if (expr.getToken().getType() == DashLexer.EXPR) {
							int value_type = value.getToken().getType();
							if (value_type == DashLexer.INTEGER ||
									value_type == DashLexer.REAL) {
								VariableSymbol s = (VariableSymbol)id.symbol;
								s.initialValue = value.getText().replaceAll("_", "");
							}
						}
					}
				}
			}
		}
		case DashLexer.ID: {
			int type = -1;
			String value = getValue(t);
			
			if (value != null) {
				type = t.symbol.type.getTypeIndex();
				
				if (type == SymbolTable.tINTEGER) {
					t.token = new CommonToken(DashLexer.INTEGER, value);
		        } else if (type == SymbolTable.tREAL) {
					t.token = new CommonToken(DashLexer.REAL, value);
		        }
			}
		}
		case DashLexer.INTEGER: {
			if (t.promoteToType != null) {
				if (t.promoteToType.getTypeIndex() == SymbolTable.tREAL) {
					int integer = Integer.parseInt(t.getText().replaceAll("_", ""));
					float real = (float)integer;
					String value = Float.toString(real);
					t.token = new CommonToken(DashLexer.REAL, value);
					t.evalType = SymbolTable._real;
					t.promoteToType = null;
				}
			}
		}
		default:
		{
			for (int i = 0; i < t.getChildCount(); i++)
				exec((DashAST) t.getChild(i));
		}
		
		}
	}
}
