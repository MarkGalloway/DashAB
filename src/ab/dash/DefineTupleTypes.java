package ab.dash;

import java.util.ArrayList;

import ab.dash.ast.DashAST;
import ab.dash.ast.Symbol;
import ab.dash.ast.SymbolTable;
import ab.dash.ast.TupleTypeSymbol;
import ab.dash.ast.Type;

public class DefineTupleTypes {
	
boolean debug_mode = false;
	
    SymbolTable symtab;
    public DefineTupleTypes(SymbolTable symtab) {
        this.symtab = symtab;
    }
    
    public void debug_on() {
    	debug_mode = true;
    }
    
    public void debug_off() {
    	debug_mode = false;
    }
    
    private void debug(Object msg) {
    	if (debug_mode)
    		System.out.println(msg);
    }
    
    private boolean compare(ArrayList<Type> fields1, ArrayList<Type> fields2) {
    	if (fields1.size() != fields2.size())
    		return false;
    	
    	boolean same = true;
    	for (int i = 0; i < fields1.size(); i++) {
    		int t1 = fields1.get(i).getTypeIndex();
    		int t2 = fields2.get(i).getTypeIndex();
    		
    		if (t1 != t2) {
    			same = false;
    		}
    	}
    	
    	return same;
    }
    
    private int getIndex(ArrayList<Type> fields) {
    	for (int i = 0; i < symtab.tuples.size(); i++) {
    		if (compare(symtab.tuples.get(i), fields))
    			return i;
    	}
    	
    	return -1;
    }


	public void define(DashAST t) {
		TupleTypeSymbol tuple = null;
		
		if (t.symbol != null) {
			if (t.symbol.type != null) {
				if (t.symbol.type.getTypeIndex() == SymbolTable.tTUPLE) {
					tuple = (TupleTypeSymbol) t.symbol.type;
				}
			}
		}
		
		if (tuple == null) {
			if (t.evalType != null) {
				if (t.evalType.getTypeIndex() == SymbolTable.tTUPLE) {
					tuple = (TupleTypeSymbol) t.evalType;
				}
			}
		}
		
		if (tuple != null) {
		    	debug(t);
		    	//debug("Fields:");
		    	
		    	
		    	ArrayList<Symbol> fields = (ArrayList<Symbol>) tuple.getDefined();
		    	ArrayList<Type> field_types = new ArrayList<Type>();
		    	for (int i = 0; i < fields.size(); i++) {
		    		Symbol field = fields.get(i);
		    		Type type = field.type;
		    		field_types.add(type);
		    	}
		    	
		    	int index = getIndex(field_types);
		    	
		    	if (index < 0) {
		    		symtab.tuples.add(field_types);
		    		debug("Not Defined");
		    		debug(field_types);
		    		index = getIndex(field_types);
		    	}
		    	
		    	debug(index);
		    	
		    	tuple.tupleTypeIndex = index;
		    	
//		    	for (int i = 0; i < field_types.size(); i++) {
//		    		debug(field_types.get(i));
//		    	}
		    }
		
		for (int i = 0; i < t.getChildCount(); i++) {
			define((DashAST) t.getChild(i));
		}
	}
	
}
