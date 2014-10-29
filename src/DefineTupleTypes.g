tree grammar DefineTupleTypes;

options {
  language = Java;
  tokenVocab = Dash;
  ASTLabelType = DashAST;
  filter = true;
  backtrack=true; 
}

@header {
  package ab.dash;
  import ab.dash.ast.*;
}

@members {
	boolean debug_mode = false;
	
    SymbolTable symtab;
    public DefineTupleTypes(TreeNodeStream input, SymbolTable symtab) {
        this(input);
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
}

bottomup
    :   tuple
    ;
    
tuple
	:	ID 
	{
	if ($ID.symbol != null)
		if ($ID.symbol.type != null) {
			if ($ID.symbol instanceof TupleSymbol) {
				
		    	debug($ID);
		    	//debug("Fields:");
		    	
		    	TupleSymbol tuple = (TupleSymbol) $ID.symbol;
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
		}
	}
	;
