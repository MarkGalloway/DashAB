tree grammar Ref;

options {
  language = Java;
  tokenVocab = Dash;
  ASTLabelType = DashAST;
  filter = true;
  backtrack=true; 
}

@header {
  package ab.dash.opt;
  
  import java.util.TreeSet;
  
  import ab.dash.DashLexer;
  import ab.dash.ast.*;
}

@members {
	TreeSet<Integer> refs;
	boolean debug_mode = false;
	
    public Ref(TreeNodeStream input, boolean debug) {
        this(input);
        this.debug_mode = debug;
    }
    
    public void downup(Object t) {
    	this.refs = new TreeSet<Integer>();
    	super.downup(t);
    }
    
    public TreeSet<Integer> getRefs() {
    	return this.refs;
    }
    
    private void debug(String msg) {
    	if (debug_mode)
    		System.out.println(msg);
    }
}

bottomup
	:	atoms
	;

// START: atoms
/** Set scope for any identifiers in expressions or assignments */
atoms
    :  ID
       {
	       if ($ID.hasAncestor(EXPR) || $ID.hasAncestor(ASSIGN)) {
		       if($ID.symbol != null) {
					if ($ID.symbol instanceof VariableSymbol) {
						debug("line " + $ID.getLine() + ": ref " + $ID.text);
						VariableSymbol s = (VariableSymbol)$ID.symbol;
						refs.add(new Integer(s.id));
					}
				}
			}
		}
    ;
//END: atoms

