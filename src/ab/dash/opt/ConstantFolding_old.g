tree grammar ConstantFolding;

options {
  language = Java;
  tokenVocab = Dash;
  ASTLabelType = DashAST;
  filter = true;
  backtrack=true; 
  rewrite=true; 
  output = AST;
}

@header {
  package ab.dash.opt;
  import ab.dash.DashLexer;
  import ab.dash.ast.*;
}

@members {
    
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
}

bottomup
	:	expression
	;

expression
@init {String result = null;}
	// Integers
	// Binary Ops
	:	^(ADD op1=INTEGER op2=INTEGER)
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		int i3 = i1 + i2;
		
		result = Integer.toString(i3);
	} -> INTEGER[result]
	|	^(SUBTRACT op1=INTEGER op2=INTEGER) 
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		int i3 = i1 - i2;
		
		result = Integer.toString(i3);
	} -> INTEGER[result]
	|	^(MULTIPLY op1=INTEGER op2=INTEGER) 
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		int i3 = i1 * i2;
		
		result = Integer.toString(i3);
	} -> INTEGER[result]
	|	^(DIVIDE op1=INTEGER op2=INTEGER) 
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		int i3 = i1 / i2;
		
		result = Integer.toString(i3);
	} -> INTEGER[result]
	|	^(MODULAR op1=INTEGER op2=INTEGER)
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		int i3 = i1 \% i2;	// Slash is removed
		
		result = Integer.toString(i3);
	} -> INTEGER[result]
	|	^(POWER op1=INTEGER op2=INTEGER) 
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		int i3 = ipow(i1, i2);
		
		result = Integer.toString(i3);
	} -> INTEGER[result]
	|	^(UNARY_MINUS op1=INTEGER) 
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		result = Integer.toString(-i1);
	} -> INTEGER[result]
	// Equality Ops
	|	^(EQUALITY op1=INTEGER op2=INTEGER)
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		
		if (i1 == i2) {
			$EQUALITY.token = new CommonToken(DashLexer.True, "true");
		} else {
			$EQUALITY.token = new CommonToken(DashLexer.False, "false");
		}
		
		$EQUALITY.deleteChild(0);
		$EQUALITY.deleteChild(0);
	}
	|	^(INEQUALITY op1=INTEGER op2=INTEGER)
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		
		if (i1 != i2) {
			$INEQUALITY.token = new CommonToken(DashLexer.True, "true");
		} else {
			$INEQUALITY.token = new CommonToken(DashLexer.False, "false");
		}
		
		$INEQUALITY.deleteChild(0);
		$INEQUALITY.deleteChild(0);
	}
	// Relational Ops
	|	^(LESS op1=INTEGER op2=INTEGER)
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		
		if (i1 < i2) {
			$LESS.token = new CommonToken(DashLexer.True, "true");
		} else {
			$LESS.token = new CommonToken(DashLexer.False, "false");
		}
		
		$LESS.deleteChild(0);
		$LESS.deleteChild(0);
	}
	|	^(GREATER op1=INTEGER op2=INTEGER)
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		
		if (i1 > i2) {
			$GREATER.token = new CommonToken(DashLexer.True, "true");
		} else {
			$GREATER.token = new CommonToken(DashLexer.False, "false");
		}
		
		$GREATER.deleteChild(0);
		$GREATER.deleteChild(0);
	}
	|	^(LESS_EQUAL op1=INTEGER op2=INTEGER)
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		
		if (i1 <= i2) {
			$LESS_EQUAL.token = new CommonToken(DashLexer.True, "true");
		} else {
			$LESS_EQUAL.token = new CommonToken(DashLexer.False, "false");
		}
		
		$LESS_EQUAL.deleteChild(0);
		$LESS_EQUAL.deleteChild(0);
	}
	|	^(GREATER_EQUAL op1=INTEGER op2=INTEGER)
	{ 
		int i1 = Integer.parseInt($op1.getText().replaceAll("_", ""));
		int i2 = Integer.parseInt($op2.getText().replaceAll("_", ""));
		
		if (i1 >= i2) {
			$GREATER_EQUAL.token = new CommonToken(DashLexer.True, "true");
		} else {
			$GREATER_EQUAL.token = new CommonToken(DashLexer.False, "false");
		}
		
		$GREATER_EQUAL.deleteChild(0);
		$GREATER_EQUAL.deleteChild(0);
	}
	// Reals
//	|	^(ADD op1=REAL op2=REAL) 
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		float f3 = f1 + f2;
//		
//		result = Float.toString(f3);
//	} -> REAL[result]
//	|	^(SUBTRACT op1=REAL op2=REAL) 
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		float f3 = f1 - f2;
//		
//		result = Float.toString(f3);
//	} -> REAL[result]
//	|	^(MULTIPLY op1=REAL op2=REAL) 
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		float f3 = f1 * f2;
//		
//		result = Float.toString(f3);
//	} -> REAL[result]
//	|	^(DIVIDE op1=REAL op2=REAL) 
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		float f3 = f1 / f2;
//		
//		result = Float.toString(f3);
//	} -> REAL[result]
//	|	^(MODULAR op1=REAL op2=REAL)
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		float f3 = f1 \% f2;
//		
//		result = Float.toString(f3);
//	} -> REAL[result]
//	|	^(POWER op1=REAL op2=REAL) 
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		float f3 = (float)Math.pow(f1, f2);
//		
//		result = Float.toString(f3);
//	} -> REAL[result]
//	|	^(UNARY_MINUS op1=REAL) 
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		result = Float.toString(-f1);
//	} -> REAL[result]
//	// Equality Ops
//	|	^(EQUALITY op1=REAL op2=REAL)
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		
//		if (f1 == f2) {
//			$EQUALITY.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$EQUALITY.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$EQUALITY.deleteChild(0);
//		$EQUALITY.deleteChild(0);
//	}
//	|	^(INEQUALITY op1=REAL op2=REAL)
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		
//		if (f1 != f2) {
//			$INEQUALITY.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$INEQUALITY.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$INEQUALITY.deleteChild(0);
//		$INEQUALITY.deleteChild(0);
//	}
//	// Relational Ops
//	|	^(LESS op1=REAL op2=REAL)
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		
//		if (f1 < f2) {
//			$LESS.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$LESS.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$LESS.deleteChild(0);
//		$LESS.deleteChild(0);
//	}
//	|	^(GREATER op1=REAL op2=REAL)
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		
//		if (f1 > f2) {
//			$GREATER.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$GREATER.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$GREATER.deleteChild(0);
//		$GREATER.deleteChild(0);
//	}
//	|	^(LESS_EQUAL op1=REAL op2=REAL)
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		
//		if (f1 <= f2) {
//			$LESS_EQUAL.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$LESS_EQUAL.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$LESS_EQUAL.deleteChild(0);
//		$LESS_EQUAL.deleteChild(0);
//	}
//	|	^(GREATER_EQUAL op1=REAL op2=REAL)
//	{ 
//		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
//		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
//		
//		if (f1 >= f2) {
//			$GREATER_EQUAL.token = new CommonToken(DashLexer.True, "true");
//		} else {
//			$GREATER_EQUAL.token = new CommonToken(DashLexer.False, "false");
//		}
//		
//		$GREATER_EQUAL.deleteChild(0);
//		$GREATER_EQUAL.deleteChild(0);
//	}
	// Booleans
	// Not
	|	^(Not op1=False) -> True["true"]
	|	^(Not op1=True) -> False["false"]
	// Equality Ops
	// EQUALITY
	|	^(EQUALITY op1=False op2=False) -> True["true"]
	|	^(EQUALITY op1=False op2=True) -> False["false"]
	|	^(EQUALITY op1=True op2=False) -> False["false"]
	|	^(EQUALITY op1=True op2=True) -> True["true"]
	// INEQUALITY
	|	^(INEQUALITY op1=False op2=False) -> False["false"]
	|	^(INEQUALITY op1=False op2=True) -> True["true"]
	|	^(INEQUALITY op1=True op2=False) -> True["true"]
	|	^(INEQUALITY op1=True op2=True) -> False["false"]
	// And
	|	^(And op1=False op2=False) -> False["false"]
	|	^(And op1=False op2=True) -> False["false"]
	|	^(And op1=True op2=False) -> False["false"]
	|	^(And op1=True op2=True) -> True["true"]
	// Or
	|	^(Or op1=False op2=False) -> False["false"]
	|	^(Or op1=False op2=True) -> True["true"]
	|	^(Or op1=True op2=False) -> True["true"]
	|	^(Or op1=True op2=True) -> True["true"]
	// Xor
	|	^(Xor op1=False op2=False) -> False["false"]
	|	^(Xor op1=False op2=True) -> True["true"]
	|	^(Xor op1=True op2=False) -> True["true"]
	|	^(Xor op1=True op2=True) -> False["false"]
	// Characters
	// Equality Ops
	|	^(EQUALITY op1=CHARACTER op2=CHARACTER)
	{ 
		char c1 = $op1.getText().charAt(1);
		char c2 = $op2.getText().charAt(1);
		
		if (c1 == c2) {
			$EQUALITY.token = new CommonToken(DashLexer.True, "true");
		} else {
			$EQUALITY.token = new CommonToken(DashLexer.False, "false");
		}
		
		$EQUALITY.deleteChild(0);
		$EQUALITY.deleteChild(0);
	}
	|	^(INEQUALITY op1=CHARACTER op2=CHARACTER)
	{ 
		char c1 = $op1.getText().charAt(1);
		char c2 = $op2.getText().charAt(1);
		
		if (c1 != c2) {
			$INEQUALITY.token = new CommonToken(DashLexer.True, "true");
		} else {
			$INEQUALITY.token = new CommonToken(DashLexer.False, "false");
		}
		
		$INEQUALITY.deleteChild(0);
		$INEQUALITY.deleteChild(0);
	}
	// Relational Ops
	|	^(LESS op1=CHARACTER op2=CHARACTER)
	{ 
		char c1 = $op1.getText().charAt(1);
		char c2 = $op2.getText().charAt(1);
		
		if (c1 < c2) {
			$LESS.token = new CommonToken(DashLexer.True, "true");
		} else {
			$LESS.token = new CommonToken(DashLexer.False, "false");
		}
		
		$LESS.deleteChild(0);
		$LESS.deleteChild(0);
	}
	|	^(GREATER op1=CHARACTER op2=CHARACTER)
	{ 
		char c1 = $op1.getText().charAt(1);
		char c2 = $op2.getText().charAt(1);
		
		if (c1 > c2) {
			$GREATER.token = new CommonToken(DashLexer.True, "true");
		} else {
			$GREATER.token = new CommonToken(DashLexer.False, "false");
		}
		
		$GREATER.deleteChild(0);
		$GREATER.deleteChild(0);
	}
	|	^(LESS_EQUAL op1=CHARACTER op2=CHARACTER)
	{ 
		char c1 = $op1.getText().charAt(1);
		char c2 = $op2.getText().charAt(1);
		
		if (c1 <= c2) {
			$LESS_EQUAL.token = new CommonToken(DashLexer.True, "true");
		} else {
			$LESS_EQUAL.token = new CommonToken(DashLexer.False, "false");
		}
		
		$LESS_EQUAL.deleteChild(0);
		$LESS_EQUAL.deleteChild(0);
	}
	|	^(GREATER_EQUAL op1=CHARACTER op2=CHARACTER)
	{ 
		char c1 = $op1.getText().charAt(1);
		char c2 = $op2.getText().charAt(1);
		
		if (c1 >= c2) {
			$GREATER_EQUAL.token = new CommonToken(DashLexer.True, "true");
		} else {
			$GREATER_EQUAL.token = new CommonToken(DashLexer.False, "false");
		}
		
		$GREATER_EQUAL.deleteChild(0);
		$GREATER_EQUAL.deleteChild(0);
	}
	;
