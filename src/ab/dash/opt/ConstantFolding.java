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
//		case DashLexer.ID: {
//			if (t.symbol != null) {
//				if (t.symbol.type != null) {
//					if (t.symbol.type.getClass() == VectorType.class) {
//						VectorType vector = (VectorType) t.symbol.type;
//						DashAST size = (DashAST) vector.def.getChild(1);
//						exec((DashAST) size);
//						if (size.token.getType() == DashLexer.EXPR) {
//							DashAST size_value = (DashAST) size.getChild(0);
//							if (size_value.token.getType() == DashLexer.INTEGER) {
//								int s = Integer.parseInt(size_value.getText().replaceAll("_", ""));
//								vector.size = s;
//							}
//						}
//					}
//				}
//			}
//			break;
//		}
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
		
		case DashLexer.Not: {
			DashAST arg1 = (DashAST) t.getChild(0);
			
			// Booleans
			if ((arg1.getToken().getType() == DashLexer.True ||
					arg1.getToken().getType() == DashLexer.False)	&&
					t.promoteToType == null) {
				
				if (arg1.getToken().getType() == DashLexer.True)
					t.token = new CommonToken(DashLexer.False, "false");
				else if (arg1.getToken().getType() == DashLexer.False)
					t.token = new CommonToken(DashLexer.True, "true");
				
				t.deleteChild(0);
			}
			
			break;
		}
		
		// Equality Ops
		case DashLexer.EQUALITY: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			int op1 = arg1.getToken().getType();
			int op2 = arg2.getToken().getType();
			
			// Booleans
			if ((op1 == DashLexer.True ||
					op1 == DashLexer.False) &&
					(op2 == DashLexer.True ||
					op2 == DashLexer.False) && 
					t.promoteToType == null) {
				if (op1 == op2) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			// Integers
			if (op1 == DashLexer.INTEGER
					&& op2 == DashLexer.INTEGER
					&& t.promoteToType == null) {
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
			
			int op1 = arg1.getToken().getType();
			int op2 = arg2.getToken().getType();
			
			// Booleans
			if ((op1 == DashLexer.True ||
					op1 == DashLexer.False) &&
					(op2 == DashLexer.True ||
					op2 == DashLexer.False) && 
					t.promoteToType == null) {

				if (op1 != op2) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}

				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			// Integers
			if (op1 == DashLexer.INTEGER
					&& op2 == DashLexer.INTEGER &&
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
		
		// Logic Ops
		case DashLexer.And: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			int op1 = arg1.getToken().getType();
			int op2 = arg2.getToken().getType();
			
			// Booleans
			if ((op1 == DashLexer.True ||
					op1 == DashLexer.False) &&
					(op2 == DashLexer.True ||
					op2 == DashLexer.False) && 
					t.promoteToType == null) {
				if (op1 == DashLexer.True && op2 == DashLexer.True) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.Or: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			int op1 = arg1.getToken().getType();
			int op2 = arg2.getToken().getType();
			
			// Booleans
			if ((op1 == DashLexer.True ||
					op1 == DashLexer.False) &&
					(op2 == DashLexer.True ||
					op2 == DashLexer.False) && 
					t.promoteToType == null) {
				if (op1 == DashLexer.True || op2 == DashLexer.True) {
					t.token = new CommonToken(DashLexer.True, "true");
				} else {
					t.token = new CommonToken(DashLexer.False, "false");
				}
				
				t.deleteChild(1);
				t.deleteChild(0);
			}
			
			break;
		}
		
		case DashLexer.Xor: {
			DashAST arg1 = (DashAST) t.getChild(0);
			DashAST arg2 = (DashAST) t.getChild(1);
			
			int op1 = arg1.getToken().getType();
			int op2 = arg2.getToken().getType();
			
			// Booleans
			if ((op1 == DashLexer.True ||
					op1 == DashLexer.False) &&
					(op2 == DashLexer.True ||
					op2 == DashLexer.False) && 
					t.promoteToType == null) {
				if ((op1 == DashLexer.True && op2 == DashLexer.False) ||
						(op1 == DashLexer.False && op2 == DashLexer.True)) {
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
}
