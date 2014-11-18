package ab.dash.ast;

/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
***/
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.TokenStream;

import ab.dash.DashLexer;

public class SymbolTable {
	private static int ID_COUNTER = 0;
	
	// Scope
    public static final int scGLOBAL = 0;
    public static final int scLOCAL = 1;
    public static final int scMETHOD = 2;
    public static final int scTUPLE = 3;
	
	// Specifiers
    public static final int sCONST = 0;
    public static final int sVAR = 1;
    
    /* tVOID is for procedures with no return and should not be added 
     * to the globals. This is mostly used for debugging type checking.
     */
    public static final int tVOID = 9;		
    public static final BuiltInTypeSymbol _void =
            new BuiltInTypeSymbol("void", tVOID);
    
    // arithmetic types defined in order from narrowest to widest
    public static final int tTUPLE = 0;
    public static final int tBOOLEAN = 1;
    public static final int tCHARACTER = 2;
    public static final int tINTEGER = 3;
    public static final int tREAL = 4;
    public static final int tOUTSTREAM = 5;
    public static final int tINSTREAM = 6;
    public static final int tNULL = 7;
    public static final int tIDENTITY = 8;
    
    public static final BuiltInTypeSymbol _tuple =
            new BuiltInTypeSymbol("tuple", tTUPLE);
    public static final BuiltInTypeSymbol _boolean =
        new BuiltInTypeSymbol("boolean", tBOOLEAN);
    public static final BuiltInTypeSymbol _character =
        new BuiltInTypeSymbol("character", tCHARACTER);
    public static final BuiltInTypeSymbol _integer =
        new BuiltInTypeSymbol("integer", tINTEGER);
    public static final BuiltInTypeSymbol _real=
        new BuiltInTypeSymbol("real", tREAL);
    public static final BuiltInTypeSymbol _outstream =
            new BuiltInTypeSymbol("std_output()", tOUTSTREAM);
    public static final BuiltInTypeSymbol _instream =
            new BuiltInTypeSymbol("std_input()", tINSTREAM);
    public static final BuiltInTypeSymbol _null =
            new BuiltInTypeSymbol("null", tNULL);
    public static final BuiltInTypeSymbol _identity =
            new BuiltInTypeSymbol("identity", tIDENTITY);
    
    public static final BuiltInSpecifierSymbol _const =
    	new BuiltInSpecifierSymbol("const", sCONST);
    public static final BuiltInSpecifierSymbol _var =
    	new BuiltInSpecifierSymbol("var", sVAR);
    
    // null values
    public String _booleanNull = "false";
    public String _characterNull = "\0";
    public String _integerNull = "0";
    public String _realNull = "0.0";

    public DashListener listener =
        new DashListener() {
    		public void info(String msg) { System.out.println(msg); }
        	public void error(String msg) { System.err.println(msg); }
        };

    /** arithmetic types defined in order from narrowest to widest */
    public static final Type[] indexToType = {
        // 0, 	1,        2,     	  3,    	4,		5,				6,      7,	8
    	_tuple, _boolean, _character, _integer, _real, _outstream, _instream, _null, _identity
    };
    
    public static final Specifier[] indexToSpecifier = {
        // 0, 	1
        _const, _var
    };

    /** Map t1 op t2 to result type (null implies illegal) */
    public static final Type[][] arithmeticResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream  null       identity*/
    	/*tuple*/		{null,		null,    	null,   	null,   	null,	null,	null,         null,      null},
        /*boolean*/ 	{null,		null,    	null,   	null,   	null, 	null, 	null,         null,      null},
        /*character*/   {null,		null,  		null,    	null,   	null, 	null,	null,         null,      null},
        /*integer*/     {null,		null,  		null,    	_integer,   _real,	null,	null,         _integer,  _integer},
        /*real*/   		{null,		null,  		null,    	_real,   	_real,	null,	null,         _real,     _real},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	null,         null,      null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	null,         null,      null},
        /*null*/        {null,      null,       null,       _integer,   _real,  null,   null,         null,      null},
        /*identity*/    {null,      null,       null,       _integer,   _real,  null,   null,         null,      null}
    };
    
    public static final Type[][] logicResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream  null       identity*/
    	/*tuple*/		{null,		null,    	null,   	null,   	null,	null,	null,         null,      null},
        /*boolean*/ 	{null,		_boolean,   null,   	null,   	null, 	null, 	null,         _boolean,   _boolean},
        /*character*/   {null,		null,  		null,    	null,   	null, 	null,	null,         null,      null},
        /*integer*/     {null,		null,  		null,    	null,   	null,	null,	null,         null,      null},
        /*real*/   		{null,		null,  		null,    	null,   	null,	null,	null,         null,      null},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	null,         null,      null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	null,         null,      null},
        /*null*/        {null,    	_boolean,   null,       null,       null,   null,   null,         null,      null},
        /*identity*/    {null,    	_boolean,   null,       null,       null,   null,   null,         null,      null}
    };

    public static final Type[][] relationalResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream  null       identity*/
    	/*tuple*/		{null,		null,    	null,   	null,   	null,       null,	null,     _boolean,      _boolean},
        /*boolean*/ 	{null,		_boolean,  	null,   	null,   	null,	    null,	null,     _boolean,      _boolean},
        /*character*/   {null,		null,  		_boolean,   null,   	null,	    null,	null,     _boolean,      _boolean},
        /*integer*/     {null,		null,  		null,    	_boolean,   _boolean,	null,	null,     _boolean,      _boolean},
        /*real*/   		{null,		null,  		null,    	_boolean,   _boolean,	null,	null,     _boolean,      _boolean},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	    null,	null,     null,          null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	    null,	null,     null,          null},
        /*null*/        {null,   	_boolean,   _boolean,   _boolean,   _boolean,   null,   null,     null,          null},
        /*identity*/    {null,    	_boolean,   _boolean,   _boolean,   _boolean,   null,   null,     null,          null}
    };

    public static final Type[][] equalityResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream null       identity*/
    	/*tuple*/		{_boolean,	null,    	null,   	null,   	null,	    null,	null,    _boolean,      _boolean},
        /*boolean*/ 	{null,		_boolean,  	null,   	null,   	null,	    null,	null,    _boolean,      _boolean},
        /*character*/   {null,		null,  		_boolean,   null,   	null,	    null,	null,    _boolean,      _boolean},
        /*integer*/     {null,		null,  		null,    	_boolean,	_boolean,	null,	null,    _boolean,      _boolean},
        /*real*/   		{null,		null,  		null,    	_boolean,   _boolean,	null,	null,    _boolean,      _boolean},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	    null,	null,    null,          null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	    null,	null,    null,          null},
        /*null*/        {_boolean,  _boolean,       _boolean,   _boolean,   _boolean,   null,   null,    null,          null},
        /*identity*/    {_boolean,  _boolean,       _boolean,   _boolean,   _boolean,   null,   null,    null,          null},
    };
    
    public static final Type[][] castResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream null       identity*/
    	/*tuple*/		{_tuple,	null,    	null,   	null,   	null,	null,	null,        null,      null},
        /*boolean*/ 	{null,		_boolean,  _character, _integer,   	_real,	null,	null,        null,      null},
        /*character*/   {null,		_boolean,  _character,	_integer,   _real,	null,	null,        null,      null},
        /*integer*/     {null,		_boolean,  _character, _integer,   	_real,	null,	null,        null,      null},
        /*real*/   		{null,		null,  		null,    	_integer,   _real,	null,	null,        null,      null},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	null,        null,      null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	null,        null,      null},
        /*null*/        {_tuple,    _boolean,  _character,  _integer,   _real,  null,   null,        null,      null},
        /*identity*/    {_tuple,    _boolean,  _character,  _integer,   _real,  null,   null,        null,      null},
   };

    /** Indicate whether a type needs a promotion to a wider type.
     *  If not null, implies promotion required.  Null does NOT imply
     *  error--it implies no promotion.  This works for
     *  arithmetic, equality, and relational operators in Dash.
     */
    public static final Type[][] promoteFromTo = new Type[][] {
        /*          	tuple		boolean  	character 	integer 	real	outstream	instream   null       identity*/
    	/*tuple*/		{null,		null,    	null,   	null,   	null,	null,	    null,      null,      null},
        /*boolean*/ 	{null,		null,    	null,   	null,   	null,	null,	    null,      null,      null},
        /*character*/   {null,		null,  		null,    	null,   	null,	null,	    null,      null,      null},
        /*integer*/     {null,		null,  		null,    	null,   	_real,	null,	    null,      null,      null},
        /*real*/   		{null,		null,  		null,    	null,   	null, 	null,       null,      null,      null},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	    null,      null,      null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	    null,      null,      null},
        /*null*/        {_tuple,    _boolean,   _character, _integer,   _real,  null,       null,      null,      null},
        /*identity*/    {_tuple,    _boolean,   _character, _integer,   _real,  null,       null,      null,      null},
    };    

    

    public static int getID() {
		ID_COUNTER++;
		return ID_COUNTER;
	}
    
    public GlobalScope globals = new GlobalScope();
    public ArrayList<ArrayList<Type>> tuples;
    
    private int error_count;
	private int warnings_count;
    private StringBuffer errorSB;
    private StringBuffer warningSB;

    /** Need to have token buffer to print out expressions, errors */
    TokenStream tokens;

    public SymbolTable(TokenStream tokens) {
        this.tokens = tokens;
        
        this.error_count = 0;
        this.warnings_count = 0;
        this.errorSB = new StringBuffer();
        this.warningSB = new StringBuffer();
        
        this.tuples = new ArrayList<ArrayList<Type>>();
        
        initTypeSystem();
    }

    protected void initTypeSystem() {
        for (Type t : indexToType) {
            if ( t!=null ) globals.define((BuiltInTypeSymbol)t);
        }
        
        for (Specifier s : indexToSpecifier) {
        	if ( s!=null ) globals.define((BuiltInSpecifierSymbol)s);
        }
        
        MethodSymbol stream_state =
            	new MethodSymbol("stream_state", _integer, globals);
        
        VariableSymbol stream_state_input = new VariableSymbol("inp", _instream, _const);
        stream_state.define(stream_state_input);
        
        globals.define(stream_state);
    }
    
    public int getWarningCount() {
		return this.warnings_count;
	}
	
	public int getErrorCount() {
		return this.error_count;
	}
	
	public String getErrors() { return this.errorSB.toString(); }
	public String getWarnings() { return this.warningSB.toString(); }
	
    private void warning(String msg) {
		this.warnings_count++;
		this.listener.info(msg);
		this.warningSB.append(msg);
	}
	
	public void error(String msg) {
		this.error_count++;
		this.listener.error(msg);
		this.errorSB.append(msg);
	}
	
	public boolean checkIfDefined(DashAST a) {
		if (a.symbol != null && a.symbol.type != null ) {
		    return true;
		}
		
		error("line " + a.getLine() + ": " +
   			 text(a)+ " is not defined in the program.");
        return false;
    }

    public Type getResultType(Type[][] typeTable, DashAST a, DashAST b) {
    	 Type aType = a.evalType; // type of left operand
         Type bType = b.evalType; // type of right operand
         
         int ta = aType.getTypeIndex();
         int tb = bType.getTypeIndex();
         if (ta == tNULL || ta == tIDENTITY) {
        	 CommonToken token = null;
        	 if (ta == tNULL)
        		 token = convertFromNull(bType);
        	 else
        		 token = convertFromIdentity(bType);
             if (token != null) {
                 a.evalType = b.evalType;
                 a.token = token;
             }  
         }
         
         if (tb == tNULL || tb == tIDENTITY) { 
        	 CommonToken token = null;
        	 if (tb == tNULL)
        		 token = convertFromNull(aType);
        	 else
        		 token = convertFromIdentity(aType);
             if (token != null) {
                 b.evalType = a.evalType;
                 b.token = token;
             }  
         }
        
        Type result = typeTable[ta][tb];    // operation result type
        if ( result==null ) {
            error("line " + a.getLine() + ": " +
            		text(a)+", "+
                    text(b)+" have incompatible types in "+
                    text((DashAST)a.getParent()));
        }
        else {
            a.promoteToType = promoteFromTo[ta][tb];
            b.promoteToType = promoteFromTo[tb][ta];
        }
        return result;
    }



    public Type bop(DashAST a, DashAST b) {
        return getResultType(arithmeticResultType, a, b);
    }
    
    public Type lop(DashAST a, DashAST b) {
        return getResultType(logicResultType, a, b);
    }
    
    public Type relop(DashAST a, DashAST b) {
        getResultType(relationalResultType, a, b);
        // even if the operands are incompatible, the type of
        // this operation must be boolean
        return _boolean;
    }
    
    public Type eqop(DashAST a, DashAST b) {
        getResultType(equalityResultType, a, b);
        // even if the operands are incompatible, the type of
        // this operation must be boolean
        return _boolean;
    }

    public Type uminus(DashAST a) {
        if (a.evalType  == _null) { 
            CommonToken token = convertFromNull(_integer);
            a.evalType = _integer;
            a.token = token;
        }  
        
        if (a.evalType  == _identity) { 
            CommonToken token = convertFromIdentity(_integer);
            a.evalType = _integer;
            a.token = token;
        }  

        if ( !(a.evalType==_integer || a.evalType==_real) ) {
            error("line " + a.getLine() + ": " +
            		text(a)+" must have integer or real type in "+
                           text((DashAST)a.getParent()));
            return null;
        }
        return a.evalType;
    }
    
    public Type upositive(DashAST a) {
        if (a.evalType  == _null) { 
            CommonToken token = convertFromNull(_integer);
            a.evalType = _integer;
            a.token = token;
        }  
        
        if (a.evalType  == _identity) { 
            CommonToken token = convertFromIdentity(_integer);
            a.evalType = _integer;
            a.token = token;
        }  

        if ( !(a.evalType==_integer || a.evalType==_real) ) {
            error("line " + a.getLine() + ": " +
                    text(a)+" must have integer or real type in "+
                           text((DashAST)a.getParent()));
            return null;
        }
        return a.evalType;
    }
    
    public Type unot(DashAST a) {
        if (a.evalType  == _null) { 
            CommonToken token = convertFromNull(_boolean);
            a.evalType = _boolean;
            a.token = token;
        }  
        
        if (a.evalType  == _identity) { 
            CommonToken token = convertFromIdentity(_boolean);
            a.evalType = _boolean;
            a.token = token;
        }  

        if ( a.evalType!=_boolean ) {
            error("line " + a.getLine() + ": " +
            		text(a)+" must have boolean type in "+
                           text((DashAST)a.getParent()));
            return _boolean; // even though wrong, assume result boolean
        }
        return a.evalType;
    }

//    public Type arrayIndex(DashAST id, DashAST index) {
//        Symbol s = id.scope.resolve(id.getText());
//        id.symbol = s;                               // annotate AST
//        if ( s.getClass() != VariableSymbol.class || // ensure it's an array
//             s.type.getClass() != ArrayType.class )
//        {
//            error(text(id)+" must be an array variable in "+
//                           text((DashAST)id.getParent()));
//            return null;
//        }
//        VariableSymbol vs = (VariableSymbol)s;
//        Type t = ((ArrayType)vs.type).elementType;   // get element type
//        int texpr = index.evalType.getTypeIndex();
//        // promote the index expr if necessary to int
//        index.promoteToType = promoteFromTo[texpr][tINT];
//        if ( !canAssignTo(index.evalType, _integer, index.promoteToType) ) {
//            error(text(index)+" index must have integer type in "+
//                           text((DashAST)id.getParent()));
//        }        
//        return t;
//    }

    public Type call(DashAST id, List<?> args) {
        Symbol s = id.scope.resolve(id.getText());
        if ( s.getClass() != MethodSymbol.class ) {
            error(text(id)+" must be a function or procedure in "+
                           text((DashAST)id.getParent()));
            return null;
        }
		
        MethodSymbol ms = (MethodSymbol)s;
        id.symbol = ms;
        int i=0;
		
        for (Symbol a : ms.orderedArgs.values() ) { // for each arg
            DashAST argAST = null;
            try {
                argAST = (DashAST)args.get(i++);
            }
            catch (IndexOutOfBoundsException e) {
                error("line " + id.getLine() + ": invalid number of args to '" + id.getText() + "'");
                break;
            }

            // get argument expression type and expected type
            Type actualArgType = argAST.evalType;
            Type formalArgType = ((VariableSymbol)a).type;
            int targ = actualArgType.getTypeIndex();
            int tformal = formalArgType.getTypeIndex();
			
            // do we need to promote argument type to defined type?
            argAST.promoteToType = promoteFromTo[targ][tformal];
            if ( !canAssignTo(actualArgType, formalArgType,
                              argAST.promoteToType) ) {
                error("line " + id.getLine() + ": argument "+
                               a.name+":<"+a.type+"> of "+ms.name+
                               "() is incompatible type");
            }
        }
        
        return ms.type;
    }

    public Type member(DashAST id, DashAST field) {
    	Type type = null;
    	if (id.getToken().getType() == DashLexer.ID) {
			VariableSymbol st = (VariableSymbol)id.scope.resolve(id.getText());
	        id.symbol = st;
	        type = st.type;
    	} else {
    		type = id.evalType;
    	}
    	
        if ( type.getTypeIndex() != tTUPLE ) {
            error("line " + id.getLine() + ": " +
            		text(id)+" must have tuple type in "+
                           text((DashAST)id.getParent()));
            return null;
        }
        
        TupleTypeSymbol scope = (TupleTypeSymbol) type;
        Symbol s = scope.resolveMember(field.getText());	// resolve ID in scope
        field.symbol = s;
        
        if (s == null) {
        	 error("line " + id.getLine() + ": " +
             		text(id)+" tuple member not found "+
                            text((DashAST)id.getParent()));
        	return null;
        }
        
        return s.type;           // return ID's type
    }
    
    public void ret(MethodSymbol ms, DashAST expr) {
        Type retType = ms.type; // promote return expr to function decl type?
        Type exprType = expr.evalType;
        int texpr = exprType.getTypeIndex();
        int tret = retType.getTypeIndex(); 
        expr.promoteToType = promoteFromTo[texpr][tret];
        if ( !canAssignTo(exprType, retType, expr.promoteToType) ) {
            error("line " + expr.getLine() + ": " +
            		text(expr)+", "+
            		ms.name+"():<"+ms.type+"> have incompatible types in "+
            		text((DashAST)expr.getParent()));
        }
    }

    // assignnment stuff (arg assignment in call())
    
    public void declinit(DashAST declID, DashAST init) {
        int te = init.evalType.getTypeIndex(); // promote expr to decl type?
        
        // Check for Type Inference
        if (declID.symbol.type == null) {
        	if (te != tNULL && te != tIDENTITY)
        		declID.symbol.type = init.evalType;
        	else {
        		error("line " + declID.getLine() + ": type cannot be inferred for " + text((DashAST) init.getParent()));
        		return;
        	}
        }
        
        if (declID.symbol.type instanceof TypedefSymbol) {
        	TypedefSymbol typedef = (TypedefSymbol) declID.symbol.type;
        	declID.symbol.type = typedef.def_type;
        }
        
        if (declID.symbol.type.getTypeIndex() == tTUPLE) {
        	if (te == tNULL || te == tIDENTITY) {
        		DashAST parent = (DashAST) init.getParent();
        		int index = init.getChildIndex();
        		
        		DashAST expr = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
        		DashAST tuple_list = new DashAST(new CommonToken(DashLexer.TUPLE_LIST, "TUPLE_LIST"));
        		
        		TupleTypeSymbol tuple = (TupleTypeSymbol) declID.symbol.type;
        		ArrayList<Symbol> fields = tuple.fields;
        		
        		for (int i = 0; i < fields.size(); i++) {
        			VariableSymbol field = (VariableSymbol) fields.get(i);
        			Type type = field.type;
        			
        			DashAST expr_t = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
        			DashAST expr_e = null;
        			
        			if (te == tNULL)
        				expr_e = new DashAST(convertFromNull(type));
        			else if (te == tIDENTITY)
        				expr_e = new DashAST(convertFromIdentity(type));
        			
        			expr_e.evalType = type;
        			expr_t.evalType = type;
        			
        			expr_t.addChild(expr_e);
        			tuple_list.addChild(expr_t);
        		}
        		
        		tuple_list.evalType = tuple;
        		expr.evalType = tuple;
        		init = expr;
        		te = tTUPLE;
        		
        		expr.addChild(tuple_list);
        		parent.replaceChildren(index, index, expr);

        	}
        } 
        
		int tdecl = declID.symbol.type.getTypeIndex();
		declID.evalType = declID.symbol.type;
		init.promoteToType = promoteFromTo[te][tdecl];
		if (!canAssignTo(init.evalType, declID.symbol.type, init.promoteToType)) {
			error("line " + declID.getLine() + ": " + declID.evalType + " "
					+ declID.getText() + " is incompatible with "
					+ init.evalType + " type");
		}
    }
    
    private boolean isConstantVariable(DashAST t) {
    	switch (t.token.getType()) {
    	case DashLexer.ID: {
    		if (t.symbol != null) {
    			if (t.symbol instanceof VariableSymbol) {
    				VariableSymbol vs = (VariableSymbol)t.symbol;
    				return vs.specifier.getSpecifierIndex() == sCONST;
    			}
    		}
    	}
    	case DashLexer.DOT: {
    		DashAST id = (DashAST) t.getChild(0);
    		DashAST field = (DashAST) t.getChild(1);
    		if (id.getToken().getType() == DashLexer.ID) {
    			VariableSymbol st = (VariableSymbol)id.scope.resolve(id.getText());
    			if (st.specifier.getSpecifierIndex() == sCONST) {
    				return true;
    			}
    	        
    			TupleTypeSymbol scope = (TupleTypeSymbol) st.type;
    			VariableSymbol s = (VariableSymbol)scope.resolveMember(field.getText());
    			
    			if (s == null)
    				return true;
    			
    			return s.specifier.getSpecifierIndex() == sCONST;
        	}
    	}
    	case DashLexer.EXPR:
    		break;
    		
    	default:
    		return true;
    	}
    	
    	for (int i = 0; i < t.getChildCount(); i++) {
    		if (isConstantVariable((DashAST) t.getChild(i)))
    			return true;
    	}
    	
    	return false;
    }

    public void assign(DashAST lhs, DashAST rhs) {
    	if (isConstantVariable(lhs)) {
			error("line " + lhs.getLine() + ": " +
        			"The specifier for " + text(lhs) + 
        			" is constant and can not be reassigned in "+
                    text((DashAST)lhs.getParent()));
			return;
		}
    	
        int tlhs = lhs.evalType.getTypeIndex(); // promote right to left type?
        int trhs = rhs.evalType.getTypeIndex();
        rhs.promoteToType = promoteFromTo[trhs][tlhs];
        if ( !canAssignTo(rhs.evalType, lhs.evalType, rhs.promoteToType) ) {
            error("line " + lhs.getLine() + ": " +
            			text(lhs)+", "+
                        text(rhs)+" have incompatible types in "+
                        text((DashAST)lhs.getParent()));
        }
    }
    
    public void checkOutput(DashAST print) {
    	if (print.getChildCount() != 2) {
    		 error("line " + print.getLine() + ": " +
    				 " not enough arguments in "+
                     text(print));
    		 return;
    	}
    	
    	DashAST stream = (DashAST) print.getChild(0);
    	DashAST lhs = (DashAST) print.getChild(1);

    	VariableSymbol s = (VariableSymbol)stream.scope.resolve(stream.getText());
    	stream.symbol = s;
    	
        //String s = super.toString();
        if ( lhs.evalType != _boolean &&
                lhs.evalType != _integer && 
                lhs.evalType != _real && 
                lhs.evalType!= _character) {
            error("line " + print.getLine() + ": invalid type " + lhs.evalType + " sent to outstream");
        }
        
    	if (s.type != null) {
    		if (s.type.getTypeIndex() != SymbolTable.tOUTSTREAM) {
    			error("line " + print.getLine() + ": " +
       				 " the ouput stream is not a valid stream in "+
                     text(print) + ", the stream needst to be of "+
       				 "type std_output(). Currently, it is type " + s);
       		 	return;
    		}
    	} else {
    		error("line " + print.getLine() + ": " +
    				"the ouput stream is currently undefined in "+
                    text(print));
    		return;
    	}
    	
    	// Remove output stream since it is not needed after this point
    	print.deleteChild(0);
    }

    public void checkInput(DashAST input) {
    	if (input.getChildCount() != 2) {
			error("line " + input.getLine() + ": "
					+ " not enough arguments in " + text(input));
			return;
		}

		DashAST stream = (DashAST) input.getChild(0);
		DashAST lhs = (DashAST) input.getChild(1);

		VariableSymbol s = (VariableSymbol) stream.scope.resolve(stream
				.getText());
		stream.symbol = s;
		
		if ( lhs.evalType != _boolean &&
                lhs.evalType != _integer && 
                lhs.evalType != _real && 
                lhs.evalType!= _character) {
            error("line " + input.getLine() + ": invalid type " + lhs.evalType + " sent to instream");
        }

		if (s.type != null) {
			if (s.type.getTypeIndex() != SymbolTable.tINSTREAM) {
				error("line " + input.getLine() + ": "
						+ " the input stream is not a valid stream in "
						+ text(input) + ", the stream needst to be of "
						+ "type std_input(). Currently, it is type " + s);
				return;
			}
		} else {
			error("line " + input.getLine() + ": "
					+ "the input stream is currently undefined in "
					+ text(input));
			return;
		}

		// Remove output stream since it is not needed after this point
		input.deleteChild(0);
    }

    public void ifstat(DashAST cond) {
        if ( cond.evalType != _boolean ) {
            error("line " + cond.getLine() + ": " +
            		"if condition "+text(cond)+
                           " must have boolean type in "+
                           text((DashAST)cond.getParent()));
        }
    }
    
    public void loopstat(DashAST cond) {
        if ( cond.evalType != _boolean ) {
            error("line " + cond.getLine() + ": " +
            		"loop condition "+text(cond)+
                           " must have boolean type in "+
                           text((DashAST)cond.getParent()));
        }
    }
    
    public boolean typeCast(DashAST typecast, DashAST list) {
    	
    	if (typecast.evalType.getTypeIndex() == tTUPLE) {
    		TupleTypeSymbol tuple = (TupleTypeSymbol) typecast.evalType;
    		
    		if (list.getType() == DashLexer.TUPLE_LIST) {
    			list.evalType = tuple;
    			
    			for (int i = 0; i < tuple.fields.size(); i++) {
        			VariableSymbol var = (VariableSymbol) tuple.fields.get(i);
        			Type type = var.type;
        			
        			DashAST expr = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
        			expr.evalType = type;
        			
        			DashAST type_cast = new DashAST(new CommonToken(DashLexer.TYPECAST, "TYPECAST"));
        			type_cast.evalType = type;
    
        			type_cast.addChild(list.getChild(i));
        			expr.addChild(type_cast);
        			
        			list.replaceChildren(i, i, expr);
        		}
    			
    			return true;
    		}	
    	}
    	
    	return false;
    }
    
    public boolean canAssignTo(Type valueType,Type destType,Type promotion) {
    	if (valueType.getTypeIndex() == tTUPLE  && destType.getTypeIndex() == tTUPLE) {
    		TupleTypeSymbol valueTuple = (TupleTypeSymbol)valueType;
    		TupleTypeSymbol destTuple = (TupleTypeSymbol)destType;
    		
    		if (valueTuple.fields.size() != destTuple.fields.size()) {
        		return false;
        	}
        	
        	for (int i = 0; i < valueTuple.fields.size(); i++) {
        		VariableSymbol f_var = (VariableSymbol) valueTuple.fields.get(i);
        		VariableSymbol a_var = (VariableSymbol) destTuple.fields.get(i);
        		
        		Type f = f_var.type;
        		Type a = a_var.type;
        		
        		int tf = f.getTypeIndex();
        		int ta = a.getTypeIndex();
        		
        		Type promoteToType = promoteFromTo[tf][ta];
        		
                if ( !canAssignTo(f, a, promoteToType) ) {
        			return false;
        		}
        	}
        	
        	return true;
    	}
    	
        // either types are same or value was successfully promoted
        return valueType==destType || promotion==destType;
    }
    
    private CommonToken convertFromNull(Type evalType) {
        int typeIndex = evalType.getTypeIndex();
        
        if (typeIndex == tINTEGER) {
            return new CommonToken(DashLexer.INTEGER, "0");
          }
          // if real declaration add ^(EXPR 0.0)
          else if (typeIndex == tREAL) {
            return new CommonToken(DashLexer.REAL, "0.0");
          }
          // if character declaration add ^(EXPR '\0')
          else if (typeIndex == tCHARACTER) {
            return new CommonToken(DashLexer.CHARACTER, "'\\0'");
          }
          // if boolean declaration add ^(EXPR false)
          else if (typeIndex == tBOOLEAN) {
              return new CommonToken(DashLexer.False, "false");
          }
        return null;
    }
    
    private CommonToken convertFromIdentity(Type evalType) {
        int typeIndex = evalType.getTypeIndex();
        
        if (typeIndex == tINTEGER) {
            return new CommonToken(DashLexer.INTEGER, "1");
          }
          // if real declaration add ^(EXPR 0.0)
          else if (typeIndex == tREAL) {
            return new CommonToken(DashLexer.REAL, "1.0");
          }
          // if character declaration add ^(EXPR '\0')
          else if (typeIndex == tCHARACTER) {
            return new CommonToken(DashLexer.CHARACTER, "'" + Character.toString ((char) 1) + "'");
          }
          // if boolean declaration add ^(EXPR false)
          else if (typeIndex == tBOOLEAN) {
              return new CommonToken(DashLexer.True, "true");
          }
        return null;
    }
    
    public DashAST getExprForNull(Type type) {
        if (type == null) {
          return null;
        }
        
        DashAST expr = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
        CommonToken token = convertFromNull(type);
        
        if (token == null)
            return null;

        expr.addChild(new DashAST(token));
        return expr;
      }
    
    public DashAST getExprForIdentity(Type type) {
        if (type == null) {
          return null;
        }
        
        DashAST expr = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
        CommonToken token = convertFromIdentity(type);
        
        if (token == null)
            return null;

        expr.addChild(new DashAST(token));
        return expr;
      }

    public String text(DashAST t) {
        String ts = "";
        if ( t.evalType!=null ) ts = ":<"+t.evalType+">";
        return tokens.toString(t.getTokenStartIndex(),
                               t.getTokenStopIndex())+ts;
    }
    
    public String toString() { return globals.toString(); }
}