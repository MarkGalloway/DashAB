package ab.dash.ast;

/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
***/
import org.antlr.runtime.TokenStream;

import java.util.ArrayList;
import java.util.List;

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
    
    // arithmetic types defined in order from narrowest to widest
    public static final int tTUPLE = 0;
    public static final int tBOOLEAN = 1;
    public static final int tCHARACTER = 2;
    public static final int tINTEGER = 3;
    public static final int tREAL = 4;
    public static final int tOUTSTREAM = 5;
    public static final int tINSTREAM = 6;
    

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
            new BuiltInTypeSymbol("std_out()", tOUTSTREAM);
    public static final BuiltInTypeSymbol _instream =
            new BuiltInTypeSymbol("std_input()", tINSTREAM);
    
    public static final BuiltInSpecifierSymbol _const =
    	new BuiltInSpecifierSymbol("const", sCONST);
    public static final BuiltInSpecifierSymbol _var =
    	new BuiltInSpecifierSymbol("var", sVAR);

    public DashListener listener =
        new DashListener() {
    		public void info(String msg) { System.out.println(msg); }
        	public void error(String msg) { System.err.println(msg); }
        };

    /** arithmetic types defined in order from narrowest to widest */
    public static final Type[] indexToType = {
        // 0, 	1,        2,     	  3,    	4,		5,				6,
    	_tuple, _boolean, _character, _integer, _real, _outstream, _instream
    };
    
    public static final Specifier[] indexToSpecifier = {
        // 0, 	1
        _const, _var
    };

    /** Map t1 op t2 to result type (null implies illegal) */
    public static final Type[][] arithmeticResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream*/
    	/*tuple*/		{null,		null,    	null,   	null,   	null,	null,	null},
        /*boolean*/ 	{null,		null,    	null,   	null,   	null, 	null, 	null},
        /*character*/   {null,		null,  		null,    	null,   	null, 	null,	null},
        /*integer*/     {null,		null,  		null,    	_integer,   _real,	null,	null},
        /*real*/   		{null,		null,  		null,    	_real,   	_real,	null,	null},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	null}
    };
    
    public static final Type[][] logicResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream*/
    	/*tuple*/		{null,		null,    	null,   	null,   	null,	null,	null},
        /*boolean*/ 	{null,		_boolean,   null,   	null,   	null, 	null, 	null},
        /*character*/   {null,		null,  		null,    	null,   	null, 	null,	null},
        /*integer*/     {null,		null,  		null,    	null,   	null,	null,	null},
        /*real*/   		{null,		null,  		null,    	null,   	null,	null,	null},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	null}
    };

    public static final Type[][] relationalResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream*/
    	/*tuple*/		{null,		null,    	null,   	null,   	null,	null,	null},
        /*boolean*/ 	{null,		_boolean,  	null,   	null,   	null,	null,	null},
        /*character*/   {null,		null,  		_boolean,   null,   	null,	null,	null},
        /*integer*/     {null,		null,  		null,    	_boolean,   _boolean,	null,	null},
        /*real*/   		{null,		null,  		null,    	_boolean,   _boolean,	null,	null},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	null}
    };

    public static final Type[][] equalityResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream*/
    	/*tuple*/		{_boolean,	null,    	null,   	null,   	null,	null,	null},
        /*boolean*/ 	{null,		_boolean,  	null,   	null,   	null,	null,	null},
        /*character*/   {null,		null,  		_boolean,   null,   	null,	null,	null},
        /*integer*/     {null,		null,  		null,    	_boolean,	_boolean,	null,	null},
        /*real*/   		{null,		null,  		null,    	_boolean,   _boolean,	null,	null},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	null}
    };
    
    public static final Type[][] castResultType = new Type[][] {
    	/*          	tuple		boolean  	character 	integer 	real	outstream	instream*/
    	/*tuple*/		{_tuple,	null,    	null,   	null,   	null,	null,	null},
        /*boolean*/ 	{null,		_boolean,  _character, _integer,   	_real,	null,	null},
        /*character*/   {null,		_boolean,  _character,	_integer,   _real,	null,	null},
        /*integer*/     {null,		_boolean,  _character, _integer,   	_real,	null,	null},
        /*real*/   		{null,		null,  		null,    	_integer,   _real,	null,	null},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	null}
   };

    /** Indicate whether a type needs a promotion to a wider type.
     *  If not null, implies promotion required.  Null does NOT imply
     *  error--it implies no promotion.  This works for
     *  arithmetic, equality, and relational operators in Dash.
     */
    public static final Type[][] promoteFromTo = new Type[][] {
        /*          	tuple		boolean  	character 	integer 	real	outstream	instream*/
    	/*tuple*/		{null,		null,    	null,   	null,   	null,	null,	null},
        /*boolean*/ 	{null,		null,    	null,   	null,   	null,	null,	null},
        /*character*/   {null,		null,  		null,    	null,   	null,	null,	null},
        /*integer*/     {null,		null,  		null,    	null,   	_real,	null,	null},
        /*real*/   		{null,		null,  		null,    	null,   	null, 	null, null},
        /*outstream*/   {null,		null,  		null,    	null,   	null, 	null,	null},
        /*instream*/   	{null,		null,  		null,    	null,   	null, 	null,	null}
    };

    public static int getID() {
		ID_COUNTER++;
		return ID_COUNTER;
	}
    
    public GlobalScope globals = new GlobalScope();
    public ArrayList<ArrayList<Type>> tuples;
    
    private int error_count;
	private int warnings_count;

    /** Need to have token buffer to print out expressions, errors */
    TokenStream tokens;

    public SymbolTable(TokenStream tokens) {
        this.tokens = tokens;
        
        this.error_count = 0;
        this.warnings_count = 0;
        
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
    }
    
    public int getWarningCount() {
		return this.warnings_count;
	}
	
	public int getErrorCount() {
		return this.error_count;
	}
    
    private void warning(String msg) {
		this.warnings_count++;
		this.listener.info(msg);
	}
	
	private void error(String msg) {
		this.error_count++;
		this.listener.error(msg);
	}
	
	public boolean checkIfDefined(DashAST a) {
		if (a.symbol != null) {
	        if ( a.symbol.type != null ) {
	            return true;
	        }
		}
		
		error("line " + a.getLine() + ": " +
   			 text(a)+ " is not defined in the program.");
        return false;
    }

    public Type getResultType(Type[][] typeTable, DashAST a, DashAST b) {
        int ta = a.evalType.getTypeIndex(); // type index of left operand
        int tb = b.evalType.getTypeIndex(); // type index of right operand
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
        if ( !(a.evalType==_integer || a.evalType==_real) ) {
            error("line " + a.getLine() + ": " +
            		text(a)+" must have integer or real type in "+
                           text((DashAST)a.getParent()));
            return null;
        }
        return a.evalType;
    }
    
    public Type unot(DashAST a) {
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

    public Type call(DashAST id, List args) {
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
            DashAST argAST = (DashAST)args.get(i++);
			
            // get argument expression type and expected type
            Type actualArgType = argAST.evalType;
            Type formalArgType = ((VariableSymbol)a).type;
            int targ = actualArgType.getTypeIndex();
            int tformal = formalArgType.getTypeIndex();
			
            // do we need to promote argument type to defined type?
            argAST.promoteToType = promoteFromTo[targ][tformal];
            if ( !canAssignTo(actualArgType, formalArgType,
                              argAST.promoteToType) ) {
                error(text(argAST)+", argument "+
                               a.name+":<"+a.type+"> of "+ms.name+
                               "() have incompatible types in "+
                               text((DashAST)id.getParent()));
            }
        }
        return ms.type;
    }

    public Type member(DashAST id, DashAST field) {
        Type type = id.symbol.type;
        if ( type.getTypeIndex() != tTUPLE ) {
            error("line " + id.getLine() + ": " +
            		text(id)+" must have tuple type in "+
                           text((DashAST)id.getParent()));
            return null;
        }
        TupleSymbol scope = (TupleSymbol)id.symbol;			// get scope of left
        Symbol s = scope.resolveMember(field.getText());	// resolve ID in scope
        field.symbol = s;
        return s.type;           // return ID's type
    }

    // assignnment stuff (arg assignment in call())

    //TODO: add outstream/instream declarations, ensure streams are not redeclared
    public void declinit(DashAST declID, DashAST init) {
        int te = init.evalType.getTypeIndex(); // promote expr to decl type?
        
        // Check for Type Inference
        if (declID.symbol.type == null) {
        	declID.symbol.type = init.evalType;
        }
        
        int tdecl = declID.symbol.type.getTypeIndex();
        declID.evalType = declID.symbol.type;
        init.promoteToType = promoteFromTo[te][tdecl];
        if ( !canAssignTo(init.evalType, declID.symbol.type,
                          init.promoteToType) ) {
            error("line " + declID.getLine() + ": " +
            		text(declID)+", "+
            		text(init)+" have incompatible types in "+
            		text((DashAST)declID.getParent()));
        }
    }
    
    // For the following format: var tuple(type id?, ...)tuple = (arg1, ...);
    public void declTuple(DashAST declID, ArrayList<DashAST> args, ArrayList<Type> fields) {
    	if (fields.size() != args.size()) {
    		error("line " + declID.getLine() + ": Tuple's have mismatched sizes in "+
                    text((DashAST)declID.getParent()));
    		return;
    	}
    	
    	for (int i = 0; i < fields.size(); i++) {
    		Type f = fields.get(i);
    		Type a = args.get(i).evalType;
    		
    		int tf = f.getTypeIndex();
    		int ta = a.getTypeIndex();
    		
    		Type promoteToType = promoteFromTo[ta][tf];
    		args.get(i).promoteToType = promoteToType;
    		
            if ( !canAssignTo(a, f, promoteToType) ) {
    			error("line " + declID.getLine() + ": Tuple argument at index " + (i+1) +
    					" has incompatible types in " +
                        text((DashAST)declID.getParent()));
    		}
    	}
    	
//    	System.out.println("\nFields:");
//    	for (Type t : fields)
//    		System.out.println(t);
//    	
//    	System.out.println("\nArgs:");
//    	for (DashAST n : args)
//    		System.out.println(n.evalType);
    }
    
    // For the following format: var tuple = (arg1, ...);
    public void declUndefinedTuple(DashAST declID, ArrayList<DashAST> args) {
    	for (int i = 0; i < args.size(); i++) {
    		Type type = args.get(i).evalType;
    		
    		TupleSymbol scope = (TupleSymbol)declID.symbol;	// get scope of tuple
    		
    		VariableSymbol vs = new VariableSymbol(null, type, _var);
	        vs.def = null;
	        scope.define(vs);
    	}
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

    public void assign(DashAST lhs, DashAST rhs) {
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
    
    //TODO: This should evaluate whether it receives a variable for output stream
    // and an expression to output
    public void output(DashAST lhs, DashAST outputStream) {

    }

    //TODO: This should evaluate whether it receives a valid input stream
    // on the RHS
    public void input(DashAST lhs, DashAST inputStream) {

    }

    public void ifstat(DashAST cond) {
        if ( cond.evalType != _boolean ) {
            error("line " + cond.getLine() + ": " +
            		"if condition "+text(cond)+
                           " must have boolean type in "+
                           text((DashAST)cond.getParent()));
        }
    }

    //TODO: prevent reassignment to input/output streams
    public boolean canAssignTo(Type valueType,Type destType,Type promotion) {
        // either types are same or value was successfully promoted
        return valueType==destType || promotion==destType;
    }

    public String text(DashAST t) {
        String ts = "";
        if ( t.evalType!=null ) ts = ":<"+t.evalType+">";
        return tokens.toString(t.getTokenStartIndex(),
                               t.getTokenStopIndex())+ts;
    }
    
    public String toString() { return globals.toString(); }
}