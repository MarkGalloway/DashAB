package ab.dash;

import java.util.ArrayList;

import org.antlr.runtime.CommonToken;

import ab.dash.DashLexer;
import ab.dash.ast.DashAST;
import ab.dash.ast.Symbol;
import ab.dash.ast.SymbolTable;
import ab.dash.ast.TupleTypeSymbol;
import ab.dash.ast.Type;
import ab.dash.ast.VariableSymbol;

public class TupleConvertNullAndIdentity {
	int error_count = 0;
	StringBuffer errorSB = new StringBuffer();

	public TupleConvertNullAndIdentity() {
	}

	public void emitErrorMessage(String msg) {
		System.err.println(msg);
		error_count++;
		errorSB.append(msg);
	}

	public int getErrorCount() {
		return error_count;
	}

	public String getErrors() {
		return errorSB.toString();
	}

	public void check(DashAST t) {
		switch (t.getToken().getType()) {
		// Create update tuple declaration
		case DashLexer.VAR_DECL:
		case DashLexer.ASSIGN:
		// Create update for == and !=
		case DashLexer.EQUALITY:
		case DashLexer.INEQUALITY: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			boolean arg1_list = arg1.getToken().getType() == DashLexer.TUPLE_LIST;
			boolean arg2_list = arg2.getToken().getType() == DashLexer.TUPLE_LIST;
			
			if (arg1.evalType.getTypeIndex() == SymbolTable.tTUPLE &&
					arg2.evalType.getTypeIndex() == SymbolTable.tTUPLE) {
				TupleTypeSymbol tuple1 = (TupleTypeSymbol) arg1.evalType;
				TupleTypeSymbol tuple2 = (TupleTypeSymbol) arg2.evalType;
				
				TupleTypeSymbol cast1 = new TupleTypeSymbol(tuple1.getEnclosingScope());
				TupleTypeSymbol cast2 = new TupleTypeSymbol(tuple2.getEnclosingScope());
				
				boolean promotion1 = false;
				boolean promotion2 = false;
				
				ArrayList<Symbol> fields1 = tuple1.fields;
				ArrayList<Symbol> fields2 = tuple2.fields;
				
				for (int i = 0; i < fields1.size(); i++) {
					VariableSymbol field1 = (VariableSymbol) fields1.get(i);
					VariableSymbol field2 = (VariableSymbol) fields2.get(i);
					
					Type type1 = field1.type;
					Type type2 = field2.type;
					
					if (arg1_list) {
						VariableSymbol nfield = null;
						if (type1.getTypeIndex() == SymbolTable.tNULL) {
							DashAST node = (DashAST) arg1.getChild(i);
							DashAST expr = SymbolTable.getExprForNull(type2);
						    if (expr == null) {
						    	emitErrorMessage("line " + node.getLine() + ": type cannot be inferred"); 
						    }
						    else {
						      ((DashAST)expr.getChild(0)).evalType = type2;
						      expr.evalType = type2;
						      arg1.replaceChildren(i, i, expr);
						    }
						} else if (type1.getTypeIndex() == SymbolTable.tIDENTITY) {
							DashAST node = (DashAST) arg1.getChild(i);
							DashAST expr = SymbolTable.getExprForIdentity(type2);
						    if (expr == null) {
						    	emitErrorMessage("line " + node.getLine() + ": type cannot be inferred"); 
						    }
						    else {
						      ((DashAST)expr.getChild(0)).evalType = type2;
						      expr.evalType = type2;
						      arg1.replaceChildren(i, i, expr);
						    }
						    
						} 
						if (type1.getTypeIndex() == SymbolTable.tNULL || type1.getTypeIndex() == SymbolTable.tIDENTITY) {
							nfield = new VariableSymbol(null, type2, SymbolTable._const);
							promotion1 = true;
						} else {
							nfield = new VariableSymbol(null, type1, SymbolTable._const);
						}
						
						cast1.define(nfield);
					}
					
					if (arg2_list) {
						VariableSymbol nfield = null;
						if (type2.getTypeIndex() == SymbolTable.tNULL) {
							DashAST node = (DashAST) arg2.getChild(i);
							DashAST expr = SymbolTable.getExprForNull(type1);
						    if (expr == null) {
						    	emitErrorMessage("line " + node.getLine() + ": type cannot be inferred"); 
						    }
						    else {
						      ((DashAST)expr.getChild(0)).evalType = type1;
						      expr.evalType = type1;
						      arg2.replaceChildren(i, i, expr);
						    }
						} else if (type2.getTypeIndex() == SymbolTable.tIDENTITY) {
							DashAST node = (DashAST) arg2.getChild(i);
							DashAST expr = SymbolTable.getExprForIdentity(type1);
						    if (expr == null) {
						    	emitErrorMessage("line " + node.getLine() + ": type cannot be inferred"); 
						    }
						    else {
						      ((DashAST)expr.getChild(0)).evalType = type1;
						      expr.evalType = type1;
						      arg2.replaceChildren(i, i, expr);
						    }
						    
						} 
						if (type2.getTypeIndex() == SymbolTable.tNULL || type2.getTypeIndex() == SymbolTable.tIDENTITY) {
							nfield = new VariableSymbol(null, type1, SymbolTable._const);
							promotion2 = true;
						} else {
							nfield = new VariableSymbol(null, type2, SymbolTable._const);
						}
						
						cast2.define(nfield);
					}
				}
				
				if (promotion1) {
					arg1.evalType = cast1;
				}
				
				if (promotion2) {
					arg2.evalType = cast2;
				}
			}
		}
		}
		
		for (int i = 0; i < t.getChildCount(); i++) {
			DashAST child = (DashAST) t.getChild(i);
			check(child);
		}
	}
}
