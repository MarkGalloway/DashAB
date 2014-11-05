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
    int error_count = 0;
    StringBuffer errorSB = new StringBuffer();
    
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
    

	  @Override
	  public void emitErrorMessage(String msg) {
	    System.err.println(msg);
	    error_count++;
	    errorSB.append(msg);
	  }
  
	  public int getErrorCount() { return error_count; }
	  public String getErrors() { return errorSB.toString(); }
}
// END: header

topdown
    : enterBlock
    | enterMethod
    |	typeDef
    | atoms
    |	tuple_list
    | varDeclaration
    |	streamDeclaration
    | ret
    ;

bottomup
    :   exitBlock
    |   exitMethod
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
tuple_list
	:	TUPLE_LIST
	{
	    TupleTypeSymbol ts = new TupleTypeSymbol(currentScope);
		$TUPLE_LIST.symbol = ts;
    }
	;
// END: tuple

enterMethod
    // it's alright that alternative 2 is disabled by antlr in this case, that's what we want
    :   ^(method_node = (FUNCTION_DECL | PROCEDURE_DECL) type ID .+) // need .+ to match 0-or-more args
        {
        debug("line " + $ID.getLine() + ": def method " + $ID.text + " return " + $type.type);
        MethodSymbol ms = new MethodSymbol($ID.text, $type.type, currentScope);
        currentMethod = ms;
        ms.def = $ID;            // track AST location of def's ID
        $ID.symbol = ms;         // track in AST
        currentScope.define(ms); // def method in globals
        currentScope = ms;       // set current scope to method scope
        
        $method_node.deleteChild(0);
        } 
    |	^(PROCEDURE_DECL id=ID .+) // need .+ to match 0-or-more args
        {
        debug("line " + $id.getLine() + ": def method " + $id.text + " return null" );
        MethodSymbol ms = new MethodSymbol($id.text, SymbolTable._void, currentScope);
        currentMethod = ms;
        ms.def = $id;            // track AST location of def's ID
        $id.symbol = ms;         // track in AST
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
        currentScope = currentScope.getEnclosingScope();
        }
    ;
    

// D e f i n e  s y m b o l s

typeDef
	:	^(TYPEDEF typeElement ID)
	{
		debug("line " + $ID.getLine() + ": typedef " + $typeElement.type + " " + $ID.text);
		Type type = $typeElement.type;
		if (type != null) {
			TypedefSymbol ts = new TypedefSymbol($ID.text, type);
			ts.def = $ID;            // track AST location of def's ID
			$ID.symbol = ts;         // track in AST
			currentScope.define(ts);
		} else {
			// TODO: Undefined type
		}
	}
	;

/** Set scope for any identifiers in expressions or assignments */
atoms
@init {DashAST t = (DashAST)input.LT(1);}
    :  {t.hasAncestor(EXPR)||t.hasAncestor(ASSIGN)
    	||t.hasAncestor(PRINT)||t.hasAncestor(INPUT)}? ID
       {
       debug("line " + $ID.getLine() + ": ref " + $ID.text);
       t.scope = currentScope;
       }
    ;

streamDeclaration
	:	^((DECL_OUTSTREAM | DECL_INSTREAM) specifier ID std_type)
	{
		debug("line " + $ID.getLine() +
	         ": def " + $ID.text + 
	         " stream ( " + $std_type.type +  " ) " +
	         " specifier ( " + $specifier.specifier +  " )");
	    VariableSymbol vs = new VariableSymbol($ID.text, $std_type.type, $specifier.specifier);
		vs.def = $ID;            // track AST location of def's ID
		$ID.symbol = vs;         // track in AST
		currentScope.define(vs);
	}
	;

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
	         " specifier ( " + $specifier.specifier +  " )" +
	         " scope: " + currentScope.toString());
	         if ($type.type != null) {
		        VariableSymbol vs = new VariableSymbol($ID.text, $type.type, $specifier.specifier);
			    vs.def = $ID;            // track AST location of def's ID
			    $ID.symbol = vs;         // track in AST
			    currentScope.define(vs);
			    $var_node.deleteChild(0);
			    $var_node.deleteChild(0);
		    } else {
		    	// TODO: Throw error type undefined
		    }
        }
    |	^(VAR_DECL specifier ID ( ^(TUPLE_LIST .+) | ^(EXPR .) ) )
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
    :	^(Tuple 
    {
    TupleTypeSymbol ts = new TupleTypeSymbol(currentScope);
	$Tuple.symbol = ts;
    }
    (
    	^(FIELD_DECL field_specifier=specifier field_type=type ID?) 
    {
    	String name = null;
    	if ($ID != null)
    		name = $ID.getText();
    		
    	VariableSymbol vs = new VariableSymbol(name, $field_type.type, $field_specifier.specifier);
    	((TupleTypeSymbol)$Tuple.symbol).define(vs);
    } 
    )+)
    {
    TupleTypeSymbol ts = (TupleTypeSymbol)$Tuple.symbol;
    debug("fields: "+ ts);
	$type = ts;
    }
    |	typeElement         
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
    ;
    
std_type returns [Type type]
@init {
DashAST t = (DashAST)input.LT(1);
}
@after {
	t.symbol = currentScope.resolve(t.getText()); // return Type
    t.scope = currentScope;
    $type = (Type)t.symbol;
}
    :	STDOUT
    |	STDIN
    ;

