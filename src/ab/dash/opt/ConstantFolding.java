package ab.dash.opt;

import org.antlr.runtime.CommonToken;

import ab.dash.DashLexer;
import ab.dash.ast.DashAST;
import ab.dash.ast.VectorType;

public class ConstantFolding {

	private int ipow(int base, int exp) {
	    int result = 1;
    	    while (exp != 0)
    	    {
    	    	int bit = exp & 1;
    	        if (bit != 0)
    	            result *= base;
    	        exp >>= 1;
    	        base *= base;
    	    }
    	
    	    return result;
	}
	
	public void optimize(DashAST tree) {
		exec(tree);
	}
	
	protected void exec(DashAST t) {
		for (int i = 0; i < t.getChildCount(); i++)
			exec((DashAST) t.getChild(i));
		
		switch (t.getToken().getType()) {
		case DashLexer.ID: {
			if (t.symbol != null) {
				if (t.symbol.type != null) {
					if (t.symbol.type.getClass() == VectorType.class) {
						VectorType vector = (VectorType) t.symbol.type;
						DashAST size = (DashAST) vector.def.getChild(1);
						exec((DashAST) size);
						if (size.token.getType() == DashLexer.EXPR) {
							DashAST size_value = (DashAST) size.getChild(0);
							if (size_value.token.getType() == DashLexer.INTEGER) {
								int s = Integer.parseInt(size_value.getText().replaceAll("_", ""));
								vector.size = s;
								System.out.println(vector.size);
							}
						}
						System.out.println(vector.def.toStringTree());
					}
				}
			}
			break;
		}
		// Binary Ops
		case DashLexer.ADD: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				int i3 = i1 + i2;
				
				String result = Integer.toString(i3);
				t.token = new CommonToken(DashLexer.INTEGER, result);
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.SUBTRACT: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				int i3 = i1 - i2;
				
				String result = Integer.toString(i3);
				t.token = new CommonToken(DashLexer.INTEGER, result);
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.MULTIPLY: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				int i3 = i1 * i2;
				
				String result = Integer.toString(i3);
				t.token = new CommonToken(DashLexer.INTEGER, result);
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.DIVIDE: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				int i3 = i1 / i2;
				
				String result = Integer.toString(i3);
				t.token = new CommonToken(DashLexer.INTEGER, result);
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.MODULAR: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				int i3 = i1 % i2;
				
				String result = Integer.toString(i3);
				t.token = new CommonToken(DashLexer.INTEGER, result);
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.POWER: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				int i3 = ipow(i1, i2);
				
				String result = Integer.toString(i3);
				t.token = new CommonToken(DashLexer.INTEGER, result);
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		// Unary Op
		case DashLexer.UNARY_MINUS: {
			DashAST arg1 = (DashAST) t.getChild(0);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				
				String result = Integer.toString(-i1);
				t.token = new CommonToken(DashLexer.INTEGER, result);
				
				t.deleteChild(0);
			}
			
			break;
		}
		
		// Equality Ops
		case DashLexer.EQUALITY: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				
				if (i1 == i2) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.INEQUALITY: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				
				if (i1 != i2) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		// Relational Ops
		case DashLexer.LESS: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				
				if (i1 < i2) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.GREATER: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				
				if (i1 > i2) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.LESS_EQUAL: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				
				if (i1 <= i2) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.GREATER_EQUAL: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			// Integers
			if (arg1.getToken().getType() == DashLexer.INTEGER &&
					arg2.getToken().getType() == DashLexer.INTEGER &&
					t.promoteToType == null) {
				int i1 = Integer.parseInt(arg1.getText().replaceAll("_", ""));
				int i2 = Integer.parseInt(arg2.getText().replaceAll("_", ""));
				
				if (i1 >= i2) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		}
	}
//
//	// Booleans
//	// Not
//	|	^(Not op1=False) -> True["true"]
//	|	^(Not op1=True) -> False["false"]
//	// Equality Ops
//	// EQUALITY
//	|	^(EQUALITY op1=False op2=False) -> True["true"]
//	|	^(EQUALITY op1=False op2=True) -> False["false"]
//	|	^(EQUALITY op1=True op2=False) -> False["false"]
//	|	^(EQUALITY op1=True op2=True) -> True["true"]
//	// INEQUALITY
//	|	^(INEQUALITY op1=False op2=False) -> False["false"]
//	|	^(INEQUALITY op1=False op2=True) -> True["true"]
//	|	^(INEQUALITY op1=True op2=False) -> True["true"]
//	|	^(INEQUALITY op1=True op2=True) -> False["false"]
//	// And
//	|	^(And op1=False op2=False) -> False["false"]
//	|	^(And op1=False op2=True) -> False["false"]
//	|	^(And op1=True op2=False) -> False["false"]
//	|	^(And op1=True op2=True) -> True["true"]
//	// Or
//	|	^(Or op1=False op2=False) -> False["false"]
//	|	^(Or op1=False op2=True) -> True["true"]
//	|	^(Or op1=True op2=False) -> True["true"]
//	|	^(Or op1=True op2=True) -> True["true"]
//	// Xor
//	|	^(Xor op1=False op2=False) -> False["false"]
//	|	^(Xor op1=False op2=True) -> True["true"]
//	|	^(Xor op1=True op2=False) -> True["true"]
//	|	^(Xor op1=True op2=True) -> False["false"]
//	// Characters
//	// Equality Ops
//	|	^(EQUALITY op1=CHARACTER op2=CHARACTER)
//	{ 
//		char c1 = $op1.getText().charAt(1);
//		char c2 = $op2.getText().charAt(1);
//		
//		if (c1 == c2) {
//			$EQUALITY.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$EQUALITY.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$EQUALITY.deleteChild(0);
//		$EQUALITY.deleteChild(0);
//	}
//	|	^(INEQUALITY op1=CHARACTER op2=CHARACTER)
//	{ 
//		char c1 = $op1.getText().charAt(1);
//		char c2 = $op2.getText().charAt(1);
//		
//		if (c1 != c2) {
//			$INEQUALITY.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$INEQUALITY.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$INEQUALITY.deleteChild(0);
//		$INEQUALITY.deleteChild(0);
//	}
//	// Relational Ops
//	|	^(LESS op1=CHARACTER op2=CHARACTER)
//	{ 
//		char c1 = $op1.getText().charAt(1);
//		char c2 = $op2.getText().charAt(1);
//		
//		if (c1 < c2) {
//			$LESS.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$LESS.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$LESS.deleteChild(0);
//		$LESS.deleteChild(0);
//	}
//	|	^(GREATER op1=CHARACTER op2=CHARACTER)
//	{ 
//		char c1 = $op1.getText().charAt(1);
//		char c2 = $op2.getText().charAt(1);
//		
//		if (c1 > c2) {
//			$GREATER.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$GREATER.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$GREATER.deleteChild(0);
//		$GREATER.deleteChild(0);
//	}
//	|	^(LESS_EQUAL op1=CHARACTER op2=CHARACTER)
//	{ 
//		char c1 = $op1.getText().charAt(1);
//		char c2 = $op2.getText().charAt(1);
//		
//		if (c1 <= c2) {
//			$LESS_EQUAL.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$LESS_EQUAL.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$LESS_EQUAL.deleteChild(0);
//		$LESS_EQUAL.deleteChild(0);
//	}
//	|	^(GREATER_EQUAL op1=CHARACTER op2=CHARACTER)
//	{ 
//		char c1 = $op1.getText().charAt(1);
//		char c2 = $op2.getText().charAt(1);
//		
//		if (c1 >= c2) {
//			$GREATER_EQUAL.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$GREATER_EQUAL.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$GREATER_EQUAL.deleteChild(0);
//		$GREATER_EQUAL.deleteChild(0);
//	}
//	;
}
