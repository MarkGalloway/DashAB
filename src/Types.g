// START: header
tree grammar Types;

options {
  tokenVocab = Dash;
  ASTLabelType = DashAST;
  filter = true;
  backtrack=true; 
}

@header {
  package ab.dash;
  import ab.dash.ast.*;
}

@members {
    SymbolTable symtab;
    public Types(TreeNodeStream input, SymbolTable symtab) {
        this(input);
        this.symtab = symtab;
    }
}
// END: header

bottomup // match subexpressions innermost to outermost
	: 	exprRoot
	|	decl
	|	ret
	|	assignment
	|	ifstat
	|	loopstat
	|	print
	|	input
	;

// promotion and type checking

// START: ifstat
ifstat 
  : ^(If cond=. s=. e=.?) {symtab.ifstat($cond);} ;
// END: ifstat

loopstat 
  : ^(Loop s=.)
  | ^(WHILE cond=. s=.) {symtab.loopstat($cond);} 
  | ^(DOWHILE cond=. s=.) {symtab.loopstat($cond);} ;

decl
	: ^(VAR_DECL ID (init=.)?) 
      {
	      if ( $init!=null && $init.evalType!=null ) {
	           symtab.declinit($ID, $init);
	      }
      }
  ;

ret 
  : ^(Return v=.) {symtab.ret((MethodSymbol)$start.symbol, $v);} ;

assignment // don't walk expressions, just examine types
    : ^(ASSIGN lhs=. rhs=.) {symtab.assign($lhs, $rhs);}
    ;
    
print
	: ^(PRINT ID .)
	{
    	symtab.checkOutput($PRINT);
	}
	;
	
input
	: ^(INPUT ID .)
	{
    	symtab.checkInput($INPUT);
	}
	;

// type computations and checking

exprRoot // invoke type computation rule after matching EXPR
    : ^(EXPR expr) {$EXPR.evalType = $expr.type;} // annotate AST
    ;

expr returns [Type type]
//@init { System.out.println($start); }
@after { $start.evalType = $type; }
    :   True      	{$type = SymbolTable._boolean;}
    |   False    	{$type = SymbolTable._boolean;}
    |   CHARACTER   {$type = SymbolTable._character;}
    |   INTEGER     {$type = SymbolTable._integer;}
    |   REAL      	{$type = SymbolTable._real;}
    |   ID 
    {
    	Symbol s = (Symbol)$ID.scope.resolve($ID.text);
    	$ID.symbol = s;
    	if (symtab.checkIfDefined($ID)) {
    		$ID.evalType = s.type;
            $type = s.type;
    	}
    }
    |   ^(UNARY_MINUS a=expr)   {$type=symtab.uminus($a.start);}
    |   ^(Not a=expr) {$type=symtab.unot($a.start);}
    |	tuple_list	{$type = $tuple_list.type;}
    |	typecast	{$type = $typecast.type;}
    |   member      {$type = $member.type;}
    |   call        {$type = $call.type;}
    |   binaryOps   {$type = $binaryOps.type;}
    ;

typecast returns [Type type]
	:	^(TYPECAST e=.)	
		{
			symtab.typeCast($TYPECAST, $e);
			$type = $TYPECAST.evalType;
		}
    ;

member returns [Type type]
	:	^(DOT id=expr m=(ID | INTEGER))	
		{
			$type = symtab.member($id.start, $m);
			$start.evalType = $type;
		}
    ;


call returns [Type type]
@init {List args = new ArrayList();}
	:	^(CALL ID ^(ELIST (^(EXPR expr {args.add($expr.start);}))*))
		{
		$type = symtab.call($ID, args);
		$start.evalType = $type;
		}
    ;
	
tuple_list returns [Type type]
@init { 
ArrayList<DashAST> arg_nodes = new ArrayList<DashAST>();
}
	:	^(TUPLE_LIST (^(EXPR expr) {$EXPR.evalType = $expr.type; arg_nodes.add($EXPR);} )+)
	{
		TupleTypeSymbol ts = (TupleTypeSymbol)$TUPLE_LIST.symbol;
		
		for (int i = 0; i < arg_nodes.size(); i++) {
			DashAST arg = arg_nodes.get(i);
		    VariableSymbol vs = new VariableSymbol(null, arg.evalType, SymbolTable._const);    	
		    ts.define(vs);
	    }
	    
	    $type = ts;
	}
	;

binaryOps returns [Type type]
@after { $start.evalType = $type; }
	:	(	^(bop a=expr b=expr)    {$type=symtab.bop($a.start, $b.start);}
		|	^(lop a=expr b=expr)    {$type=symtab.lop($a.start, $b.start);}
		|	^(relop a=expr b=expr)  {$type=symtab.relop($a.start, $b.start);}
		|	^(eqop a=expr b=expr)   {$type=symtab.eqop($a.start, $b.start);}
		)
	;
	
lop : 	And | Or | Xor;

bop	:	ADD | SUBTRACT | MULTIPLY | DIVIDE | MODULAR | POWER ;

relop:	LESS | GREATER | LESS_EQUAL | GREATER_EQUAL;

eqop:	EQUALITY | INEQUALITY ;
