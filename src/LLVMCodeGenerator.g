tree grammar LLVMCodeGenerator;

options {
  tokenVocab   = Dash;
  ASTLabelType = DashAST;
  backtrack    = true;
  output       = template;
}

@header {
package ab.dash;
  import ab.dash.ast.*;
}

build
  :
  ^(PROGRAM l+=line*)
    ->
      program(type_defs={null}, globals={null}, code={l})
  ;

line
  :
  methodDeclaration
    ->
      {$methodDeclaration.st}
  | statement
    ->
      {$statement.st}
  ;

methodDeclaration
  :
  ^(
    (
      PROCEDURE_DECL
      | FUNCTION_DECL
    )
    .? ID
    ^(BLOCK s+=statement*)
   )
    ->
      function_main(code={s})
  //    |	^((PROCEDURE_DECL | FUNCTION_DECL) .? ID formalParameters? expression)
  //    |   ^((PROCEDURE_DECL | FUNCTION_DECL) .? ID formalParameters? block)
  ;

//formalParameters
//	:	^(ARG_DECL Const type ID)
//	;


statement
  :
  ^(BLOCK b_s+=statement*)
    ->
      block(code={b_s})
  |
  ^(var_decl_node=VAR_DECL . .? var_decl_id=ID var_expression=expression)
  |
  ^(If expression s=statement e=statement?)
  |
  ^(return_node=Return return_expression=expression)
  {
	   DashAST t = (DashAST) return_node;
	   
	   int id = t.llvmResultID;
	   int type = ((DashAST) t.getChild(0)).evalType.getTypeIndex();
	   int expr_id = ((DashAST) t.getChild(0).getChild(0)).llvmResultID;
	   
	   StringTemplate template = templateLib.getInstanceOf("return");
	   StringTemplate type_template = null;
	   if (type == SymbolTable.tINTEGER) {
	   		type_template = templateLib.getInstanceOf("int_type");
	   } else if (type == SymbolTable.tBOOLEAN) {
	   		type_template = templateLib.getInstanceOf("bool_type");
	   }
	   
	   template.setAttribute("expr_id", expr_id);
	   template.setAttribute("expr", return_expression);
	   template.setAttribute("type", type_template);
	   template.setAttribute("id", id);
	   retval.st = template;
  }
  |
  ^(ASSIGN lhs expression)
  |
  ^(EXPR postfixExpression)
  //    | 	^(ASSIGN ID tupleMemberList)
  ;

lhs
  :
  ^(EXPR postfixExpression)
    ->
      {$postfixExpression.st}
  ;

postfixExpression
  :
  ^(
    DOT ID
    (
      mem=INTEGER
      | mem=ID
    )
   )
  | primary
    ->
      {$primary.st}
  ;

primary
  :
  INTEGER
    ->
      int_literal(id={((DashAST)$INTEGER).llvmResultID}, val={$INTEGER.text})
  | ID
  ;

expression
  :
  ^(EXPR expr)
    ->
      {$expr.st}
  ;

expr
  :
  ^(MULTIPLY opt1=expr opt2=expr)
  |
  ^(DIVIDE opt1=expr opt2=expr)
  |
  ^(ADD opt1=expr opt2=expr)
  |
  ^(SUBTRACT opt1=expr opt2=expr)
  |
  ^(LESS opt1=expr opt2=expr)
  |
  ^(GREATER opt1=expr opt2=expr)
  |
  ^(EQUALITY opt1=expr opt2=expr)
  |
  ^(INEQUALITY opt1=expr opt2=expr)
  | primary
    ->
      {$primary.st}
  ;
