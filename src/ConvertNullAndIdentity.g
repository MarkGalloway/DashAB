tree grammar ConvertNullAndIdentity;

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

  public ConvertNullAndIdentity(TreeNodeStream input, SymbolTable symtab) {
    this(input);
    this.symtab = symtab;
    setTreeAdaptor(DashAST.dashAdaptor);
  } 
}

bottomup
  : 	identExpr
	|	nullExpr
	;
	

nullExpr
  : ^(EXPR ^(UNARY_MINUS Null))
  {
    $EXPR.evalType = $EXPR.promoteToType;
    $EXPR.promoteToType = null;
    
    if ($EXPR.evalType == null)
      $EXPR.evalType = ((DashAST)$EXPR.getChild(0)).promoteToType;
      
    DashAST expr = SymbolTable.getExprForNull($EXPR.evalType);
    DashAST minus = new DashAST(new CommonToken(DashLexer.UNARY_MINUS, "-"));
    if (expr != null) {
      ((DashAST)expr.getChild(0)).evalType = $EXPR.evalType;
      //((DashAST)expr.getChild(0)).promoteToType = null;
      minus.addChild(expr.getChild(0));
      minus.evalType = $EXPR.evalType;
      $EXPR.deleteChild(0);
      $EXPR.addChild(minus);
    }
  } 
  |	^(node = EXPR Null)
  { 
    $node.evalType = $node.promoteToType;
    $node.promoteToType = null;
    
    if ($node.evalType == null)
      $node.evalType = ((DashAST)$node.getChild(0)).promoteToType;
      
    DashAST expr = SymbolTable.getExprForNull($node.evalType);
    if (expr != null) {
      ((DashAST)expr.getChild(0)).evalType = $node.evalType;
      //((DashAST)expr.getChild(0)).promoteToType = null;
      $node.deleteChild(0);
      $node.addChild(expr.getChild(0));
    }
  } 
  ;
  
identExpr
  : ^(EXPR ^(UNARY_MINUS Identity))
  {
    $EXPR.evalType = $EXPR.promoteToType;
    $EXPR.promoteToType = null;
    
    if ($EXPR.evalType == null)
      $EXPR.evalType = ((DashAST)$EXPR.getChild(0)).promoteToType;
      
    DashAST expr = SymbolTable.getExprForIdentity($EXPR.evalType);
    DashAST minus = new DashAST(new CommonToken(DashLexer.UNARY_MINUS, "-"));
    if (expr != null) {
      ((DashAST)expr.getChild(0)).evalType = $EXPR.evalType;
      //((DashAST)expr.getChild(0)).promoteToType = null;
      minus.addChild(expr.getChild(0));
      minus.evalType = $EXPR.evalType;
      $EXPR.deleteChild(0);
      $EXPR.addChild(minus);
    }
  } 
  |	^(node = EXPR Identity)
  { 
    $node.evalType = $node.promoteToType;
    $node.promoteToType = null;
    
    if ($node.evalType == null)
      $node.evalType = ((DashAST)$node.getChild(0)).promoteToType;
      
    DashAST expr = SymbolTable.getExprForIdentity($node.evalType);
    if (expr != null) {
      ((DashAST)expr.getChild(0)).evalType = $node.evalType;
      //((DashAST)expr.getChild(0)).promoteToType = null;
      $node.deleteChild(0);
      $node.addChild(expr.getChild(0));
    }
  } 
  ;