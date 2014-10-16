// START: header
tree grammar Def;

options {
  tokenVocab = Dash;
  ASTLabelType = DashAST;
  filter = true;
}

@members {
    SymbolTable symtab;
    Scope currentScope;
    MethodSymbol currentMethod;
    public Def(TreeNodeStream input, SymbolTable symtab) {
        this(input);
        this.symtab = symtab;
        currentScope = symtab.globals;
    }
}
// END: header

topdown
    :   enterBlock
    |   enterMethod
//    |   enterTuple
    |   atoms
    |   varDeclaration
    |   ret
    ;

bottomup
    :   exitBlock
    |   exitMethod
//    |   exitTuple
    ;

// S C O P E S

enterBlock
    :   BLOCK {currentScope = new LocalScope(currentScope);} // push scope
    ;
exitBlock
    :   BLOCK
        {
        //System.out.println("locals: "+currentScope);
        currentScope = currentScope.getEnclosingScope();    // pop scope
        }
    ;
    

// START: tuple
//enterTuple
//    :   ^(Tuple .+)
//        {
//        //System.out.println("line "+$ID.getLine()+": def struct "+$ID.text);
//        TupleSymbol ss = new TupleSymbol($ID.text, currentScope);
//        ss.def = $ID;
//        $ID.symbol = ss;
//        currentScope.define(ss); // def struct in current scope
//        currentScope = ss;       // set current scope to struct scope
//        }
//    ;
//exitTuple
//    :   Tuple
//        {
//        //System.out.println("fields: "+currentScope);
//        currentScope = currentScope.getEnclosingScope();    // pop scope
//        }
//    ;
// END: struct

enterMethod
    :   ^((FUNCTION_DECL | PROCEDURE_DECL) type ID .*) // match method subtree with 0-or-more args
        {
        //System.out.println("line "+$ID.getLine()+": def method "+$ID.text);
        MethodSymbol ms = new MethodSymbol($ID.text,$type.type,currentScope);
        currentMethod = ms;
        ms.def = $ID;            // track AST location of def's ID
        $ID.symbol = ms;         // track in AST
        currentScope.define(ms); // def method in globals
        currentScope = ms;       // set current scope to method scope
        }
    |	^(PROCEDURE_DECL ID .*) // match method subtree with 0-or-more args
        {
        //System.out.println("line "+$ID.getLine()+": def method "+$ID.text);
        MethodSymbol ms = new MethodSymbol($ID.text, null, currentScope);
        currentMethod = ms;
        ms.def = $ID;            // track AST location of def's ID
        $ID.symbol = ms;         // track in AST
        currentScope.define(ms); // def method in globals
        currentScope = ms;       // set current scope to method scope
        }
    ;

/** Track method associated with this return. */
ret :   ^(Return .) {$ret.start.symbol = currentMethod;}
    ;
    
exitMethod
    :   (FUNCTION_DECL | PROCEDURE_DECL)
        {
        currentScope = currentScope.getEnclosingScope();    // pop method scope
        }
    ;
    

// D e f i n e  s y m b o l s

// START: atoms
/** Set scope for any identifiers in expressions or assignments */
atoms
@init {DashAST t = (DashAST)input.LT(1);}
    :  {t.hasAncestor(EXPR)||t.hasAncestor(ASSIGN)}? ID
       {t.scope = currentScope;}
    ;
//END: atoms

// START: var
varDeclaration // global, parameter, or local variable
    :   ^((FIELD_DECL|VAR_DECL|ARG_DECL) type ID .?)
        {
        //System.out.println("line "+$ID.getLine()+": def "+$ID.text);
        VariableSymbol vs = new VariableSymbol($ID.text,$type.type);
        vs.def = $ID;            // track AST location of def's ID
        $ID.symbol = vs;         // track in AST
        currentScope.define(vs);
        }
    ;
// END: field

/** Not included in tree pattern matching directly.  Needed by declarations */
type returns [Type type]
    :  typeElement         {$type = $typeElement.type;}
    ;   
        
typeElement returns [Type type]
@init {DashAST t = (DashAST)input.LT(1);}
@after {
    t.symbol = currentScope.resolve(t.getText()); // return Type
    t.scope = currentScope;
    $type = (Type)t.symbol;
}
    :   Real
    |   Integer
    |   Character
    |   Boolean
    ;

