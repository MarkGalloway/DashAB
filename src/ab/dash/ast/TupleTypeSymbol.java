package ab.dash.ast;

/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
***/
import java.util.ArrayList;
import java.util.Collection;

public class TupleTypeSymbol extends Symbol implements Type, Scope {
    Scope enclosingScope;
    public int tupleTypeIndex;
    
    ArrayList<Symbol> fields = new ArrayList<Symbol>();

    public TupleTypeSymbol(Scope enclosingScope) {
        super("tuple");
        this.enclosingScope = enclosingScope;
    }
    
    /** For a.b, only look in a only to resolve b, not up scope tree */
    public Symbol resolveMember(String name) 
    {
    	if (name == null)
    		return null;
    	
    	try {
    	      int i = Integer.parseInt(name);
    	      return fields.get(i-1);
    	} catch (NumberFormatException e) {
    		for (Symbol s : fields) {
    			if (s != null) {
    				if (s.getName() != null)
    					if (s.getName().equals(name))
    						return s;
    			}
    		}
    	} catch (IndexOutOfBoundsException e) {
    	    return null;
    	}
    	
    	return null;
    }
    
    public int getMemberIndex(String name) 
    {
    	if (name == null)
    		return -1;
    	
    	try {
    	      int i = Integer.parseInt(name);
    	      return i-1;
    	} catch (NumberFormatException e) {
    		for (int i = 0; i < fields.size(); i++) {
    			Symbol s = fields.get(i);
    			if (s != null) {
    				if (s.getName() != null)
    					if (s.getName().equals(name))
    						return i;
    			}
    		}
    	}
    	
    	return -1;
    }


    public Symbol resolve(String name) {
		Symbol s = resolveMember(name);
        if ( s!=null ) return s;
		// if not here, check any enclosing scope
		if ( getEnclosingScope() != null ) {
			return getEnclosingScope().resolve(name);
		}
		return null; // not found
	}

    public Symbol resolveType(String name) { return resolve(name); }
    
    public void define(Symbol sym) {
    	fields.add(sym);
		sym.scope = this; // track the scope in each symbol
	}
    
    public Collection<Symbol> getDefined() { return fields; }

    public Scope getEnclosingScope() { return enclosingScope; }
    
    public String getScopeName() { return name; }
    
    public String getName() 
    { 
    	String out = "(";
    	for (int i = 0; i < fields.size(); i++) {
    		if (i < fields.size() - 1)
    			out += fields.get(i) + ", ";
    		else
    			out += fields.get(i);
    	}
    	out += ") ";
        return out;
    }
    
    public String toString() {
    	return getName();
    }
    
    public int getTypeIndex() { return SymbolTable.tTUPLE; }
    
    public int getScopeIndex() { return SymbolTable.scTUPLE; }
}
