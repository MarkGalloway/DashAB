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
  : identExpr
	|	nullExpr
	;
	

nullExpr
  : ^(node = EXPR Null)
  { 
    $node.evalType = $node.promoteToType;
    $node.promoteToType = null;
    DashAST expr = symtab.getExprForNull($node.evalType);
    ((DashAST)expr.getChild(0)).evalType = $node.evalType;
    $node.deleteChild(0);
    $node.addChild(expr.getChild(0));
  } 
  ;
  
identExpr
  : ^(node = EXPR Identity)
  { 
    $node.evalType = $node.promoteToType;
    $node.promoteToType = null;
    DashAST expr = symtab.getExprForIdentity($node.evalType);
    ((DashAST)expr.getChild(0)).evalType = $node.evalType;
    $node.deleteChild(0);
    $node.addChild(expr.getChild(0));
  } 
  ;