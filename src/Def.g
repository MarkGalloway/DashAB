// START: header
tree grammar Def;

options {
  tokenVocab = Dash;
  ASTLabelType = DashAST;
  filter = true;
}

@header {
  package ab.dash;
  import ab.dash.ast.*;
  import java.io.PrintStream;
}

@members {
    SymbolTable symtab;
    Scope currentScope;
    MethodSymbol currentMethod;
    boolean debug_mode;
    
    public Def(TreeNodeStream input, SymbolTable symtab) {
        this(input);
        this.symtab = symtab;
        this.currentScope = symtab.globals;
        this.debug_mode = false;
    }
    
    public Def(TreeNodeStream input, SymbolTable symtab, Boolean debug) {
        this(input);
        this.symtab = symtab;
        this.currentScope = symtab.globals;
        this.debug_mode = debug;
    }
    
    private void debug(String msg) {
    	if (debug_mode)
    		System.out.println(msg);
    }
}
// END: header

topdown
    :   enterBlock
    |   enterMethod
    |	typeDef
    |   atoms
    |   varDeclaration
    |   ret
    ;

bottomup
    :   exitBlock
    |   exitMethod
    |   exitTuple
    ;

// S C O P E S

enterBlock
    :   BLOCK 
    {
    currentScope = new LocalScope(currentScope);
    $BLOCK.scope = currentScope;
    } // push scope
    ;
exitBlock
    :   BLOCK
        {
        debug("locals: " + currentScope);
        currentScope = currentScope.getEnclosingScope();    // pop scope
        }
    ;
    

// START: tuple
exitTuple
    :   Tuple
        {
        debug("fields: "+currentScope);
        currentScope = currentScope.getEnclosingScope();    // pop scope
        }
    ;
// END: tuple

enterMethod
    :   ^(method_node = (FUNCTION_DECL | PROCEDURE_DECL) typeElement ID function_block=.*) // match method subtree with 0-or-more args
        {
        debug("line "+$ID.getLine()+": def method "+$ID.text);
        MethodSymbol ms = new MethodSymbol($ID.text,$typeElement.type,currentScope);
        currentMethod = ms;
        ms.def = $ID;            // track AST location of def's ID
        $ID.symbol = ms;         // track in AST
        currentScope.define(ms); // def method in globals
        currentScope = ms;       // set current scope to method scope
        
        $method_node.deleteChild(0);
        } 
    |	^(PROCEDURE_DECL ID .*) // match method subtree with 0-or-more args
        {
        debug("line "+$ID.getLine()+": def method "+$ID.text);
        MethodSymbol ms = new MethodSymbol($ID.text, null, currentScope);
        currentMethod = ms;
        ms.def = $ID;            // track AST location of def's ID
        $ID.symbol = ms;         // track in AST
        currentScope.define(ms); // def method in globals
        currentScope = ms;       // set current scope to method scope
        }
    ;

/** Track method associated with this return. */
ret :   ^(Return .) 
	{
	debug("line "+$Return.getLine()+": return " + currentMethod);
	$ret.start.symbol = currentMethod;
	}
    ;
    
exitMethod
    :   (FUNCTION_DECL | PROCEDURE_DECL)
        {
        currentScope = currentScope.getEnclosingScope();    // pop method scope
        }
    ;
    

// D e f i n e  s y m b o l s

typeDef
	:	^(TYPEDEF typeElement ID)
	{
		debug("line " + $ID.getLine() + ": typedef " + $typeElement.type + " " + $ID.text);
		Type type = $typeElement.type;
		if (type != null) {
			debug(type.getName());
			TypedefSymbol ts = new TypedefSymbol($ID.text, type);
			ts.def = $ID;            // track AST location of def's ID
			$ID.symbol = ts;         // track in AST
			currentScope.define(ts);
		} else {
			// TODO: Undefined type
		}
	}
	;

// START: atoms
/** Set scope for any identifiers in expressions or assignments */
atoms
@init {DashAST t = (DashAST)input.LT(1);}
    :  {t.hasAncestor(EXPR)||t.hasAncestor(ASSIGN)}? ID
       {
       debug("line " + $ID.getLine() + ": ref " + $ID.text);
       t.scope = currentScope;
       }
    ;
//END: atoms

// START: var
varDeclaration // global, parameter, or local variable
/* Remember specifiers are added to the tree during parsing
 * Therfore, the following case is handled:
 * 		integer x = 0;
 */
    :   ^(var_node = (VAR_DECL|ARG_DECL) specifier type ID .?)	
        {
	         debug("line " + $ID.getLine() +
	         ": def " + $ID.text + 
	         " type ( " + $type.type +  " ) " + 
	         " specifier ( " + $specifier.specifier +  " )");
	         if ($type.type != null) {
		        if ($type.type.getTypeIndex() == SymbolTable.tTUPLE) {
			        TupleSymbol ts = new TupleSymbol($ID.text, $type.type, $specifier.specifier, currentScope);
			        ts.def = $ID;
			        $ID.symbol = ts;
			        currentScope.define(ts); // def tuple in current scope
			        currentScope = ts;       // set current tuple to struct scope
			    } else {
			        VariableSymbol vs = new VariableSymbol($ID.text, $type.type, $specifier.specifier);
			        vs.def = $ID;            // track AST location of def's ID
			        $ID.symbol = vs;         // track in AST
			        currentScope.define(vs);
			        
			        $var_node.deleteChild(0);
			    }
			    
			    $var_node.deleteChild(0);
		    } else {
		    	// TODO: Throw error type undefined
		    }
        }
    |	^(VAR_DECL specifier ID ^(TUPLE_LIST .+))	// Tuple
        {
	        debug("line " + $ID.getLine() +
	         ": def " + $ID.text + 
	         " type ( tuple ) " + 
	         " specifier ( " + $specifier.specifier +  " )");
	        Type type = (Type) currentScope.resolve("tuple"); // return Type
	        TupleSymbol ts = new TupleSymbol($ID.text, type, $specifier.specifier, currentScope);
		    ts.def = $ID;
		    $ID.symbol = ts;
		    currentScope.define(ts); // def tuple in current scope
		    
		    $VAR_DECL.deleteChild(0);
        }
    |	^(VAR_DECL specifier ID ^(EXPR .+)) // Buit-in
        {
	        debug("line " + $ID.getLine() +
	         ": def " + $ID.text + 
	         " type ( unknown ) " + 
	         " specifier ( " + $specifier.specifier +  " )");
	        VariableSymbol vs = new VariableSymbol($ID.text, null, $specifier.specifier);
	        vs.def = $ID;            // track AST location of def's ID
	        $ID.symbol = vs;         // track in AST
	        currentScope.define(vs);
	        
	        $VAR_DECL.deleteChild(0);
        }
    | 	^(FIELD_DECL specifier type ID?) //TODO if no ID then find location in parent example 2nd child.
    	{
	        debug("line "+$FIELD_DECL.getLine()+": def "+ $ID.text);
	        String name = null;
	        
	        if ($ID!= null)
	        	name = $ID.text;
	        	
	        VariableSymbol vs = new VariableSymbol(name, $type.type, $specifier.specifier);
	        vs.def = $ID;            // track AST location of def's ID
	        
	        if ($ID != null)
	        	$ID.symbol = vs;         // track in AST
	        	
	        currentScope.define(vs);
        }
    ;
// END: field

/** Not included in tree pattern matching directly.  Needed by declarations */
specifier returns [Specifier specifier]
    :	specifierElement         {$specifier = $specifierElement.specifier;}
    ;   
        
specifierElement returns [Specifier specifier]
@init {DashAST t = (DashAST)input.LT(1);}
@after {
    t.symbol = currentScope.resolve(t.getText()); // return Type
    t.scope = currentScope;
    $specifier = (Specifier)t.symbol;
}
    :   Const
    |   Var
    ;

type returns [Type type]
    :	typeElement         
    {
    $type = $typeElement.type;
    }
    |	ID
    {
    TypedefSymbol s = (TypedefSymbol) currentScope.resolve($ID.text);
    $ID.symbol = s;
    $type = s;
    }
    ;   
        
typeElement returns [Type type]
@init {
DashAST t = (DashAST)input.LT(1);
}
@after {
	t.symbol = currentScope.resolve(t.getText()); // return Type
    t.scope = currentScope;
    $type = (Type)t.symbol;
}
    :   REAL_TYPE
    | 	INTEGER_TYPE
    |	CHARACTER_TYPE
    |	BOOLEAN_TYPE
    |	^(Tuple .+)
    ;

