

// START: header
tree grammar Def;

options {
  tokenVocab   = Dash;
  ASTLabelType = DashAST;
  filter       = true;
  output       = AST;
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
	this(input, symtab, false);
}

public Def(TreeNodeStream input, SymbolTable symtab, Boolean debug) {
	this(input);
	this.symtab = symtab;
	this.currentScope = symtab.globals;
	this.debug_mode = debug;

	setTreeAdaptor(DashAST.dashAdaptor);
}

private void debug(String msg) {
	if (debug_mode)
		System.out.println(msg);
}
}
// END: header

topdown
  :
  enterBlock
  | enterIterator
  | enterMethod
  | typecast
  | typeDef
  | atoms
  | tuple_list
  | varDeclaration
  | tupleMembers
  | streamDeclaration
  | ret
  ;

bottomup
  :
  exitBlock
  | exitIterator
  | exitMethod
  ;

// S C O P E S

enterBlock
  :
  BLOCK 
       {
        currentScope = new LocalScope(currentScope);
        $BLOCK.scope = currentScope;
       } // push scope
  ;

exitBlock
  :
  BLOCK 
       {
        debug("locals: " + currentScope);
        currentScope = currentScope.getEnclosingScope(); // pop scope
       }
  ;

enterIterator
	:
	^(ITERATOR ID . .)
		{
        currentScope = new LocalScope(currentScope);
        $ITERATOR.scope = currentScope;
        
        debug("line " + $ID.getLine() + ": def " + $ID.text + " type ( unknown ) "
        + " specifier ( const )");
   
	   // test for double declaration
	   Symbol s = currentScope.resolveInCurrentScope($ID.text);
	   if (s != null) {
	   	symtab.error("line " + $ID.getLine() + ": Identifier " + $ID.text
	   			+ " declared twice in the same scope.");
	   }
	   
	   VariableSymbol vs = new VariableSymbol($ID.text, null, SymbolTable._const);
	   vs.def = $ID; // track AST location of def's ID
	   vs.scope = currentScope;
	   $ID.symbol = vs; // track in AST
	   
	   currentScope.define(vs);
       } // push scope
    ;
    
exitIterator
	:
	ITERATOR
		{
        debug("locals: " + currentScope);
        currentScope = currentScope.getEnclosingScope(); // pop scope
       } // push scope
    ;

// START: tuple

tuple_list
  :
  TUPLE_LIST 
            {
             TupleTypeSymbol ts = new TupleTypeSymbol(currentScope);
             $TUPLE_LIST.symbol = ts;
            }
  ;
// END: tuple

enterMethod
  // it's alright that alternative 2 is disabled by antlr in this case, that's what we want
  :
  ^(
    method_node=
    (
      FUNCTION_DECL
      | PROCEDURE_DECL
    )
    type id=ID init=.*
   ) // need .+ to match 0-or-more args
  
  {
   debug("line " + $id.getLine() + ": def method " + $id.text + " return "
   		+ $type.type);
   MethodSymbol ms = (MethodSymbol) currentScope.resolve($id.text);
   if (ms == null) {
   	ms = new MethodSymbol($id.text, $type.type, currentScope);
   	currentScope.define(ms); // def method in globals
   }
   
   currentMethod = ms;
   $id.symbol = ms; // track in AST
   
   if ($init != null) {
   	ms.def = $id; // track AST location of def's ID
   }
   
   currentScope = ms; // set current scope to method scope
   
   $method_node.deleteChild(0);
  }
  |
  ^(PROCEDURE_DECL id=ID init=.*) // need .+ to match 0-or-more args
  
  {
   debug("line " + $id.getLine() + ": def method " + $id.text + " return null");
   MethodSymbol ms = (MethodSymbol) currentScope.resolve($id.text);
   if (ms == null) {
   	ms = new MethodSymbol($id.text, SymbolTable._void, currentScope);
   	currentScope.define(ms); // def method in globals
   }
   
   currentMethod = ms;
   $id.symbol = ms; // track in AST
   
   if ($init != null) {
   	ms.def = $id; // track AST location of def's ID
   }
   
   currentScope = ms; // set current scope to method scope
  }
  ;

/** Track method associated with this return. */
ret
  :
  ^(Return .)
  
  {
   debug("line " + $Return.getLine() + ": return " + currentMethod);
   $ret.start.symbol = currentMethod;
  }
  ;

exitMethod
  :
  (
    FUNCTION_DECL
    | PROCEDURE_DECL
  )
  
  {
   currentScope = currentScope.getEnclosingScope();
  }
  ;

typecast
@init {
ArrayList<Type> types = new ArrayList<Type>();
}
  :
  ^(
    node=TYPECAST
    (
      typeElement 
                 {
                  types.add($typeElement.type);
                 }
    )+
    e=.
   )
  
  {
   if (types.size() == 1) {
   	$node.evalType = types.get(0);
   	debug("Typecast: " + $node);
   } else {
   	TupleTypeSymbol ts = new TupleTypeSymbol(currentScope);
   	for (int i = 0; i < types.size(); i++) {
   		Type type = types.get(i);
   		VariableSymbol vs = new VariableSymbol(null, type, SymbolTable._const);
   		ts.define(vs);
   	}
   
   	$node.evalType = ts;
   	debug("Typecast: " + $node);
   }
   
   for (int i = $node.getChildCount() - 2; i >= 0; i--) {
   	$node.deleteChild(i);
   }
  }
  ;

// D e f i n e  s y m b o l s

typeDef
  :
  ^(TYPEDEF type_tyepdef ID)
  
  {
   debug("line " + $ID.getLine() + ": typedef " + $type_tyepdef.type + " "
   		+ $ID.text);
   Type type = $type_tyepdef.type;
   if (type != null) {
   	TypedefSymbol ts = new TypedefSymbol($ID.text, type);
   	ts.def = $ID; // track AST location of def's ID
   	$ID.symbol = ts; // track in AST
   	currentScope.define(ts);
   } else {
   	// TODO: Undefined type
   }
  }
  ;

/** Set scope for any identifiers in expressions or assignments */
atoms
@init {
DashAST t = (DashAST) input.LT(1);
}
  :
  {(t.hasAncestor(EXPR) || t.hasAncestor(ASSIGN) || t.hasAncestor(PRINT) || t
		.hasAncestor(INPUT))}? ID
  
  {
   debug("line " + $ID.getLine() + ": ref " + $ID.text);
   t.scope = currentScope;
   
   Symbol vs = currentScope.resolve($ID.text);
   if (vs == null && !t.hasAncestor(DOT))
   	symtab.error("line " + $ID.getLine() + ": unknown identifier " + $ID.text);
  }
  ;

streamDeclaration
  :
  ^(
    (
      DECL_OUTSTREAM
      | DECL_INSTREAM
    )
    specifier ID std_type
   )
  
  {
   debug("line " + $ID.getLine() + ": def " + $ID.text + " stream ( "
   		+ $std_type.type + " ) " + " specifier ( " + $specifier.specifier
   		+ " )");
   VariableSymbol vs = new VariableSymbol($ID.text, $std_type.type,
   		$specifier.specifier);
   vs.def = $ID; // track AST location of def's ID
   $ID.symbol = vs; // track in AST
   currentScope.define(vs);
  }
  ;

// START: var

varDeclaration // global, parameter, or local variable
/* Remember specifiers are added to the tree during parsing
 * Therfore, the following case is handled:
 * 		integer x = 0;
 */

  :
  ^(
    var_node=
    (
      ARG_DECL
      | VAR_DECL
    )
    specifier type ID .?
   )
  
  {
   debug("line " + $ID.getLine() + ": def " + $ID.text + " type ( " + $type.type
   		+ " ) " + " specifier ( " + $specifier.specifier + " )" + " scope: "
   		+ currentScope.toString());
   
   // test for double declaration
   Symbol s = currentScope.resolveInCurrentScope($ID.text);
   if (s != null) {
   	symtab.error("line " + $ID.getLine() + ": Identifier " + $ID.text
   			+ " declared twice in the same scope.");
   }
   
   if ($type.type != null) {
   	VariableSymbol vs = new VariableSymbol($ID.text, $type.type,
   			$specifier.specifier);
   	vs.def = $ID; // track AST location of def's ID
   	vs.scope = currentScope;
   	$ID.symbol = vs; // track in AST
   
   	currentScope.define(vs);
   	
   	$var_node.deleteChild(0);
   } else {
   	// TODO: Throw error type undefined
   }
  }
  |
  ^(
    var_node=VAR_DECL specifier ID
    ^(EXPR .)
   )
  
  {
   debug("line " + $ID.getLine() + ": def " + $ID.text + " type ( unknown ) "
   		+ " specifier ( " + $specifier.specifier + " )");
   
   // test for double declaration
   Symbol s = currentScope.resolveInCurrentScope($ID.text);
   if (s != null) {
   	symtab.error("line " + $ID.getLine() + ": Identifier " + $ID.text
   			+ " declared twice in the same scope.");
   }
   
   VariableSymbol vs = new VariableSymbol($ID.text, null, $specifier.specifier);
   vs.def = $ID; // track AST location of def's ID
   vs.scope = currentScope;
   $ID.symbol = vs; // track in AST
   
   currentScope.define(vs);
   
   $var_node.replaceChildren(0, 0, new DashAST(new CommonToken(DashLexer.INFERRED, "INFERRED")));
  }
  ;
// END: field

tupleMembers
  // check that t.member or t.index (t.1 ... t.n) is defined
  :
  ^(
    DOT id=ID m=
    (
      ID
      | INTEGER
    )
   )
  
  {
   // check that the tuple is defined first
   Symbol tuple = currentScope.resolve($id.text);
   if (tuple == null) {
   	symtab.error("line " + $DOT.getLine() + ": unknown identifier " + $id.text);
   }
   
   else {
   	// check that the member variable is defined
   	if ($m.token.getType() == ID) {
   		Symbol member = ((TupleTypeSymbol) tuple.type).resolveMember($m.text);
   		if (member == null) {
   			symtab.error("line " + $DOT.getLine() + ": unknown member '"
   					+ $m.text + "' for tuple " + $id.text);
   		}
   	}
   }
  }
  ;

/** Not included in tree pattern matching directly.  Needed by declarations */
specifier returns [Specifier specifier]
  :
  specifierElement 
                  {
                   $specifier = $specifierElement.specifier;
                  }
  ;

specifierElement returns [Specifier specifier]
@init {
DashAST t = (DashAST) input.LT(1);
}
@after {
t.symbol = currentScope.resolve(t.getText()); // return Type
t.scope = currentScope;
$specifier = (Specifier) t.symbol;
}
  :
  Const
  | Var
  ;

type returns [Type type]
  :
  type_tyepdef 
              {
               $type = $type_tyepdef.type;
              }
  | ID 
      {
       TypedefSymbol s = (TypedefSymbol) currentScope.resolve($ID.text);
       $ID.symbol = s;
       s.def = $ID;
       $type = s;
      }
  ;

type_tyepdef returns [Type type]
  :
  Interval 
          {
           IntervalType stype = new IntervalType(0, 0);
           $Interval.symbol = stype;
           stype.def = $Interval;
           stype.scope = currentScope;
           $type = stype;
          }
  |
  ^(VECTOR typeElement size=.)
  
  {
   VectorType stype = new VectorType($typeElement.type, 0);
   $VECTOR.symbol = stype;
   stype.def = $VECTOR;
   stype.scope = currentScope;
   $type = stype;
  }
  |
  ^(MATRIX typeElement row=. column=.)
  
  {
   MatrixType stype = new MatrixType($typeElement.type, 0, 0);
   $MATRIX.symbol = stype;
   stype.def = $MATRIX;
   stype.scope = currentScope;
   $type = stype;
  }
  |
  ^(
    Tuple 
         {
          TupleTypeSymbol stype = new TupleTypeSymbol(currentScope);
          $Tuple.symbol = stype;
          stype.def = $Tuple;
          stype.scope = currentScope;
         }
    (
      ^(FIELD_DECL field_specifier=specifier field_type=type ID?)
      
      {
       String name = null;
       if ($ID != null)
       	name = $ID.getText();
       
       VariableSymbol vs = new VariableSymbol(name, $field_type.type,
       		$field_specifier.specifier);
       ((TupleTypeSymbol) $Tuple.symbol).define(vs);
      }
    )+
   )
  
  {
   TupleTypeSymbol ts = (TupleTypeSymbol) $Tuple.symbol;
   debug("fields: " + ts);
   $type = ts;
  }
  | typeElement 
               {
                $type = $typeElement.type;
               }
  ;

typeElement returns [Type type]
@init {
DashAST t = (DashAST) input.LT(1);
}
@after {
t.symbol = currentScope.resolve(t.getText()); // return Type
t.scope = currentScope;
$type = (Type) t.symbol;
}
  :
  REAL_TYPE
  | INTEGER_TYPE
  | CHARACTER_TYPE
  | BOOLEAN_TYPE
  ;

std_type returns [Type type]
@init {
DashAST t = (DashAST) input.LT(1);
}
@after {
t.symbol = currentScope.resolve(t.getText()); // return Type
t.scope = currentScope;
$type = (Type) t.symbol;
}
  :
  STDOUT
  | STDIN
  ;
