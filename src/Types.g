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
    :   exprRoot
    |	decl
    |	ret
    |	assignment
    |	ifstat
    ;

// promotion and type checking

// START: ifstat
ifstat : ^(If cond=. s=. e=.?) {symtab.ifstat($cond);} ;
// END: ifstat

decl:   ^(VAR_DECL . .? ID (init=.)?) // call declinit if we have init expr
        {if ( $init!=null && $init.evalType!=null )
             symtab.declinit($ID, $init);}
    ;    

ret :   ^(Return v=.) {symtab.ret((MethodSymbol)$start.symbol, $v);} ;

assignment // don't walk expressions, just examine types
    :   ^(ASSIGN lhs=. rhs=.) {symtab.assign($lhs, $rhs);}
    ;

// type computations and checking

exprRoot // invoke type computation rule after matching EXPR
    :   ^(EXPR expr) {$EXPR.evalType = $expr.type;} // annotate AST
    ;

expr returns [Type type]
@after { $start.evalType = $type; }
    :   True      	{$type = SymbolTable._boolean;}
    |   False    	{$type = SymbolTable._boolean;}
    |   CHARACTER   {$type = SymbolTable._character;}
    |   INTEGER     {$type = SymbolTable._integer;}
    |   REAL      	{$type = SymbolTable._real;}
    |   ID {VariableSymbol s=(VariableSymbol)$ID.scope.resolve($ID.text);
            $ID.symbol = s; $type = s.type;}
    |   ^(UNARY_MINUS a=expr)   {$type=symtab.uminus($a.start);}
    |   ^(Not a=expr) {$type=symtab.unot($a.start);}
    |   member      {$type = $member.type;}
//    |   call        {$type = $call.type;}
    |   binaryOps   {$type = $binaryOps.type;}
    ;
    
member returns [Type type]
	:	^(DOT id=ID m=(ID | INTEGER))	
		{
			TupleSymbol st=(TupleSymbol)$id.scope.resolve($id.text);
	        $id.symbol = st; 
	        
			$type = symtab.member($id, $m);
			$start.evalType = $type;
		}
    ;

binaryOps returns [Type type]
@after { $start.evalType = $type; }
	:	(	^(bop a=expr b=expr)    {$type=symtab.bop($a.start, $b.start);}
		|	^(relop a=expr b=expr)  {$type=symtab.relop($a.start, $b.start);}
		|	^(eqop a=expr b=expr)   {$type=symtab.eqop($a.start, $b.start);}
		)
	;
	

bop	:	ADD | SUBTRACT | MULTIPLY | DIVIDE | MODULAR | POWER ;

relop:	LESS | GREATER | LESS_EQUAL | GREATER_EQUAL ;

eqop:	EQUALITY | INEQUALITY ;
