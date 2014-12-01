// START: header
tree grammar Types;

options {
  tokenVocab = Dash;
  ASTLabelType = DashAST;
  filter = true;
  backtrack=true; 
  output = AST;
  rewrite = true;
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
        
        setTreeAdaptor(DashAST.dashAdaptor);
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
	
topdown
	: iterator
	;

// promotion and type checking

// START: ifstat
ifstat 
  : ^(If cond=. s=. e=.?) {symtab.ifstat($cond);} ;
// END: ifstat

loopstat 
  : ^(Loop s=.)
  | ^(WHILE cond=. s=.) {symtab.loopstat($cond);} 
  | ^(DOWHILE cond=. s=.) {symtab.loopstat($cond);}
  ;
  
iterator
	:^(ITERATOR (^(IN id=ID e=exprRoot) {symtab.iterator($id, $e.start);})+ .)
	;

decl
	: ^(var_node=VAR_DECL . ID (init=.)?) 
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
    |   Null        {$type = SymbolTable._null;}
    |   Identity    {$type = SymbolTable._identity;}
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
    |	vector_list {$type = $vector_list.type;}
    |	typecast	{$type = $typecast.type;}
    |   member      {$type = $member.type;}
    |   call        {$type = $call.type;}
    |	index		{$type = $index.type;}
    |   binaryOps   {$type = $binaryOps.type;}
    ;

typecast returns [Type type]
	:	^(TYPECAST ^(EXPR e=.))	
		{
			$type = $TYPECAST.evalType;
			if (symtab.typeCast($TYPECAST, $e)) {
				DashAST parent = (DashAST) $TYPECAST.getParent();
              	int index = $TYPECAST.getChildIndex();
              	parent.replaceChildren(index, index, $e);
            }
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
    
index returns [Type type]
	:	^(VECTOR_INDEX id=expr m=exprRoot)
		{
			$type = symtab.vectorIndex($id.start, $m.start);
			$start.evalType = $type;
		}
	|	^(MATRIX_INDEX id=expr opt1=exprRoot opt2=exprRoot)
		{
			$type = symtab.matrixIndex($id.start, $opt1.start, $opt2.start);
			$start.evalType = $type;
		}
    ;
	
tuple_list returns [Type type]
@init { 
ArrayList<DashAST> arg_nodes = new ArrayList<DashAST>();
}
	:	^(TUPLE_LIST (^(EXPR expr) { $EXPR.evalType = $expr.type; arg_nodes.add($EXPR); } )+)
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
	
vector_list returns [Type type]
@init { 
ArrayList<DashAST> arg_nodes = new ArrayList<DashAST>();
}
	:	^(VECTOR_LIST (^(EXPR expr) { $EXPR.evalType = $expr.type; arg_nodes.add($EXPR); } )+)
	{
		boolean isMatrix = false;
		Type arg_type = null;
		for (int i = 0; i < arg_nodes.size(); i++) {
			DashAST arg = arg_nodes.get(i);
			Type compareType = arg.evalType;
			if (arg.evalType.getTypeIndex() == SymbolTable.tVECTOR) {
				isMatrix = true;
				compareType = ((VectorType)arg.evalType).elementType;
			}
			
			if (arg_type != null) {
				if (compareType != arg_type) {
					symtab.error("line " + arg.getLine() + ": Mismatched types in vector literal.");
				}
			}
			arg_type = compareType;  
	    }
	    
	    Type returnType = null;
	    if (isMatrix)
	    	returnType = new MatrixType(arg_type, arg_nodes.size(), 0);
	    else
	    	returnType = new VectorType(arg_type, arg_nodes.size());
	    	
	    $VECTOR_LIST.evalType = returnType;
	    $type = returnType;
	}
	;

binaryOps returns [Type type]
@after { $start.evalType = $type; }
	:	(	^(bop a=expr b=expr)    	{$type=symtab.bop($a.start, $b.start);}
		|	^(lop a=expr b=expr)    	{$type=symtab.lop($a.start, $b.start);}
		|	^(relop a=expr b=expr)  	{$type=symtab.relop($a.start, $b.start);}
		|	^(eqop a=expr b=expr)   	{$type=symtab.eqop($a.start, $b.start);}
		|	^(RANGE a=expr b=expr)  	{$type=symtab.range($a.start, $b.start);}
		|	^(By a=expr b=expr) 		{$type=symtab.by($a.start, $b.start);}
		|	^(DOTPRODUCT a=expr b=expr) {$type=symtab.dotProduct($a.start, $b.start);}
		|	^(CONCAT a=expr b=expr) 	{$type=symtab.concat($a.start, $b.start);}
		)
	;
	
lop : 	And | Or | Xor;

bop	:	ADD | SUBTRACT | MULTIPLY | DIVIDE | MODULAR | POWER ;

relop:	LESS | GREATER | LESS_EQUAL | GREATER_EQUAL;

eqop:	EQUALITY | INEQUALITY ;
