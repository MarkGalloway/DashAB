tree grammar AddNullToUninitialized;

options {
  language = Java;
  tokenVocab = Dash;
  ASTLabelType = DashAST;
  filter = true;
  output = AST;
}

@header {
  package ab.dash;
  import ab.dash.DashLexer;
  import ab.dash.ast.*;
}

@members {
    
  SymbolTable symtab;

  public AddNullToUninitialized(TreeNodeStream input, SymbolTable symtab) {
    this(input);
    this.symtab = symtab;
    setTreeAdaptor(DashAST.dashAdaptor);
  }
}

bottomup
	:	varDeclaration
	;

// add null initialization for each non-initializaed variable declaration
varDeclaration
  : ^(var_node = VAR_DECL id=ID)
    { 
      // if tuple declaration add ^(EXPR ^(TUPLE_LIST ^(EXPR AppropriateNullValue) ^(EXPR AppropriateNullValue) ...)) 
      if ($ID.symbol.type.getTypeIndex() == SymbolTable.tTUPLE) {
         TupleTypeSymbol tuple = (TupleTypeSymbol)$ID.symbol.type;
         DashAST tupleList = new DashAST(new CommonToken(DashLexer.TUPLE_LIST, "TUPLE_LIST"));
         TupleTypeSymbol ts = new TupleTypeSymbol($ID.scope);
         tupleList.symbol = ts;
         
         for (int i=0; i<tuple.fields.size(); ++i) {
            DashAST listExpr = SymbolTable.getExprForNull(tuple.fields.get(i).type);
            
            if (listExpr == null) { 
              symtab.error("line " + $ID.getLine() + ": type cannot be inferred for " + $ID.text); 
              break;
            }
            tupleList.addChild(listExpr);
         }
         
         DashAST expr = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
         expr.addChild(tupleList);
         var_node.addChild(expr);
      }
      // otherwise add ^(EXPR AppropriateNullValue)
      else {
        DashAST expr = SymbolTable.getExprForNull($ID.symbol.type);
        if (expr == null) {
          symtab.error("line " + $ID.getLine() + ": type cannot be inferred for " + $ID.text); 
        }
        var_node.addChild(expr);
      }
      
    } 
  ;