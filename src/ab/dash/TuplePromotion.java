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

public class TuplePromotion {
	int error_count = 0;
	StringBuffer errorSB = new StringBuffer();

	public TuplePromotion() {
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
		boolean var_decl = false;
		switch (t.getToken().getType()) {
		// Create cast tuple declaration
		case DashLexer.VAR_DECL:
			var_decl = true;
		// Create cast for == and !=
		case DashLexer.EQUALITY:
		case DashLexer.INEQUALITY: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
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
					
					VariableSymbol nfield1 = null;
					if (type1.getTypeIndex() == SymbolTable.tINTEGER &&
							type2.getTypeIndex() == SymbolTable.tREAL) {
						promotion1 = true;
						nfield1 = new VariableSymbol(null, SymbolTable._real, SymbolTable._const);
					} else {
						nfield1 = new VariableSymbol(null, type1, SymbolTable._const);
					}
					cast1.define(nfield1);
					
					VariableSymbol nfield2 = null;
					if (type2.getTypeIndex() == SymbolTable.tINTEGER &&
							type1.getTypeIndex() == SymbolTable.tREAL) {
						promotion2 = true;
						nfield2 = new VariableSymbol(null, SymbolTable._real, SymbolTable._const);
					} else {
						nfield2 = new VariableSymbol(null, type2, SymbolTable._const);
					}
					cast2.define(nfield2);
				}
				
				if (promotion1 && !var_decl) {
					DashAST expr = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
					expr.evalType = tuple1;
					expr.addChild(arg1);
					
					DashAST typecast = new DashAST(new CommonToken(DashLexer.TYPECAST, "TYPECAST"));
					typecast.evalType = cast1;
					typecast.addChild(expr);
					
					t.replaceChildren(0, 0, typecast);
				}
				
				if (promotion2 && !var_decl) {
					DashAST expr = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
					expr.evalType = tuple2;
					expr.addChild(arg2);
					
					DashAST typecast = new DashAST(new CommonToken(DashLexer.TYPECAST, "TYPECAST"));
					typecast.evalType = cast2;
					typecast.addChild(expr);
					
					t.replaceChildren(1, 1, typecast);
				}
				
				if (promotion2 && var_decl) {
					DashAST typecast = new DashAST(new CommonToken(DashLexer.TYPECAST, "TYPECAST"));
					typecast.evalType = cast2;
					typecast.addChild(arg2);
					
					DashAST expr = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
					expr.evalType = cast2;
					expr.addChild(typecast);
					
					t.replaceChildren(1, 1, expr);
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
