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

import java.util.List;

public class SymbolTable {
    // arithmetic types defined in order from narrowest to widest
    public static final int tUSER = 0; // user-defined type (tuple)
    public static final int tBOOLEAN = 1;
    public static final int tCHARACTER = 2;
    public static final int tINTEGER = 3;
    public static final int tREAL = 4;

    public static final BuiltInTypeSymbol _boolean =
        new BuiltInTypeSymbol("boolean", tBOOLEAN);
    public static final BuiltInTypeSymbol _character =
        new BuiltInTypeSymbol("character", tCHARACTER);
    public static final BuiltInTypeSymbol _integer =
        new BuiltInTypeSymbol("integer", tINTEGER);
    public static final BuiltInTypeSymbol _real=
        new BuiltInTypeSymbol("real", tREAL);

    public DashListener listener =
        new DashListener() {
            public void info(String msg) { System.out.println(msg); }
            public void error(String msg) { System.err.println(msg); }
        };

    /** arithmetic types defined in order from narrowest to widest */
    public static final Type[] indexToType = {
        // 0, 1,        2,     		3,    	  4
        null, _boolean, _character, _integer, _real
    };

    /** Map t1 op t2 to result type (null implies illegal) */
    public static final Type[][] arithmeticResultType = new Type[][] {
    	 /*          	boolean  	character 	integer 	real*/
        /*boolean*/ 	{null,    	null,   	null,   	null},
        /*character*/   {null,  	null,    	null,   	null},
        /*integer*/     {null,  	null,    	_integer,   null},
        /*real*/   		{null,  	null,    	null,   	_real}
    };

    public static final Type[][] relationalResultType = new Type[][] {
    	 /*          	boolean  	character 	integer 	real*/
        /*boolean*/ 	{_boolean,  null,   	null,   	null},
        /*character*/   {null,  	_boolean,   null,   	null},
        /*integer*/     {null,  	null,    	_boolean,   null},
        /*real*/   		{null,  	null,    	null,   	_boolean}
    };

    public static final Type[][] equalityResultType = new Type[][] {
    	 /*          	boolean  	character 	integer 	real*/
        /*boolean*/ 	{_boolean,  null,   	null,   	null},
        /*character*/   {null,  	_boolean,   null,   	null},
        /*integer*/     {null,  	null,    	_boolean,	null},
        /*real*/   		{null,  	null,    	null,   	_boolean}
    };
    
    public static final Type[][] castResultType = new Type[][] {
    	/*          	boolean  	character 	integer 	real*/
        /*boolean*/ 	{_boolean,  _character, _integer,   _real},
        /*character*/   {_boolean,  _character,	_integer,   _real},
        /*integer*/     {_boolean,  _character, _integer,   _real},
        /*real*/   		{null,  	null,    	_integer,   _real}
   };

    /** Indicate whether a type needs a promotion to a wider type.
     *  If not null, implies promotion required.  Null does NOT imply
     *  error--it implies no promotion.  This works for
     *  arithmetic, equality, and relational operators in Dash.
     */
    public static final Type[][] promoteFromTo = new Type[][] {
        /*          	boolean  	character 	integer 	real*/
        /*boolean*/ 	{null,    	null,   	null,   	null},
        /*character*/   {null,  	null,    	null,   	null},
        /*integer*/     {null,  	null,    	null,   	_real},
        /*real*/   		{null,  	null,    	null,   	null}
    };

    public GlobalScope globals = new GlobalScope();

    /** Need to have token buffer to print out expressions, errors */
    TokenStream tokens;

    public SymbolTable(TokenStream tokens) {
        this.tokens = tokens;
        initTypeSystem();
    }

    protected void initTypeSystem() {
        for (Type t : indexToType) {
            if ( t!=null ) globals.define((BuiltInTypeSymbol)t);
        }
    }

    public Type getResultType(Type[][] typeTable, DashAST a, DashAST b) {
        int ta = a.evalType.getTypeIndex(); // type index of left operand
        int tb = b.evalType.getTypeIndex(); // type index of right operand
        Type result = typeTable[ta][tb];    // operation result type
        if ( result==null ) {
            listener.error(text(a)+", "+
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
    
    public Type relop(DashAST a, DashAST b) {
        getResultType(relationalResultType, a, b);
        // even if the operands are incompatible, the type of
        // this operation must be boolean
        return _boolean;
    }
    
    public Type eqop(DashAST a, DashAST b) {
        getResultType(equalityResultType, a, b);
        return _boolean;
    }

    public Type uminus(DashAST a) {
        if ( !(a.evalType==_integer || a.evalType==_real) ) {
            listener.error(text(a)+" must have integer or real type in "+
                           text((DashAST)a.getParent()));
            return null;
        }
        return a.evalType;
    }
    
    public Type unot(DashAST a) {
        if ( a.evalType!=_boolean ) {
            listener.error(text(a)+" must have boolean type in "+
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
//            listener.error(text(id)+" must be an array variable in "+
//                           text((DashAST)id.getParent()));
//            return null;
//        }
//        VariableSymbol vs = (VariableSymbol)s;
//        Type t = ((ArrayType)vs.type).elementType;   // get element type
//        int texpr = index.evalType.getTypeIndex();
//        // promote the index expr if necessary to int
//        index.promoteToType = promoteFromTo[texpr][tINT];
//        if ( !canAssignTo(index.evalType, _integer, index.promoteToType) ) {
//            listener.error(text(index)+" index must have integer type in "+
//                           text((DashAST)id.getParent()));
//        }        
//        return t;
//    }

    public Type call(DashAST id, List args) {
        Symbol s = id.scope.resolve(id.getText());
        if ( s.getClass() != MethodSymbol.class ) {
            listener.error(text(id)+" must be a function or procedure in "+
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
                listener.error(text(argAST)+", argument "+
                               a.name+":<"+a.type+"> of "+ms.name+
                               "() have incompatible types in "+
                               text((DashAST)id.getParent()));
            }
        }
        return ms.type;
    }

    public Type member(DashAST expr, DashAST field) {
        Type type = expr.evalType;
        if ( type.getClass() != TupleSymbol.class ) {
            listener.error(text(expr)+" must have tuple type in "+
                           text((DashAST)expr.getParent()));
            return null;
        }
        TupleSymbol scope = (TupleSymbol)expr.evalType;		// get scope of left
        Symbol s = scope.resolveMember(field.getText());	// resolve ID in scope
        field.symbol = s;
        return s.type;           // return ID's type
    }

    // assignnment stuff (arg assignment in call())

    public void declinit(DashAST declID, DashAST init) {
        int te = init.evalType.getTypeIndex(); // promote expr to decl type?
        int tdecl = declID.symbol.type.getTypeIndex();
        declID.evalType = declID.symbol.type;
        init.promoteToType = promoteFromTo[te][tdecl];
        if ( !canAssignTo(init.evalType, declID.symbol.type,
                          init.promoteToType) ) {
            listener.error(text(declID)+", "+
                text(init)+" have incompatible types in "+
                text((DashAST)declID.getParent()));
        }
    }

    public void ret(MethodSymbol ms, DashAST expr) {
        Type retType = ms.type; // promote return expr to function decl type?
        Type exprType = expr.evalType;
        int texpr = exprType.getTypeIndex();
        int tret = retType.getTypeIndex(); 
        expr.promoteToType = promoteFromTo[texpr][tret];
        if ( !canAssignTo(exprType, retType, expr.promoteToType) ) {
            listener.error(text(expr)+", "+
                ms.name+"():<"+ms.type+"> have incompatible types in "+
                text((DashAST)expr.getParent()));
        }
    }

    public void assign(DashAST lhs, DashAST rhs) {
        int tlhs = lhs.evalType.getTypeIndex(); // promote right to left type?
        int trhs = rhs.evalType.getTypeIndex();
        rhs.promoteToType = promoteFromTo[trhs][tlhs];
        if ( !canAssignTo(rhs.evalType, lhs.evalType, rhs.promoteToType) ) {
            listener.error(text(lhs)+", "+
                           text(rhs)+" have incompatible types in "+
                           text((DashAST)lhs.getParent()));
        }
    }

    public void ifstat(DashAST cond) {
        if ( cond.evalType != _boolean ) {
            listener.error("if condition "+text(cond)+
                           " must have boolean type in "+
                           text((DashAST)cond.getParent()));
        }
    }

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