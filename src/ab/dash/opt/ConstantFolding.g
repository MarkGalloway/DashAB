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
  import ab.dash.ast.*;
}

@members {
 	boolean fixed_state = true;
    SymbolTable symtab;
    public ConstantFolding(TreeNodeStream input, SymbolTable symtab) {
        this(input);
        this.symtab = symtab;
    }
    
    public void resetFixed() {
    	fixed_state = true;
    }
    
    public boolean isFixed() {
    	return fixed_state;
    }
    
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
@after {fixed_state = false;}
	// Integers
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
	// Reals
	|	^(ADD op1=REAL op2=REAL) 
	{ 
		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
		float f3 = f1 + f2;
		
		result = Float.toString(f3);
	} -> REAL[result]
	|	^(SUBTRACT op1=REAL op2=REAL) 
	{ 
		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
		float f3 = f1 - f2;
		
		result = Float.toString(f3);
	} -> REAL[result]
	|	^(MULTIPLY op1=REAL op2=REAL) 
	{ 
		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
		float f3 = f1 * f2;
		
		result = Float.toString(f3);
	} -> REAL[result]
	|	^(DIVIDE op1=REAL op2=REAL) 
	{ 
		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
		float f3 = f1 / f2;
		
		result = Float.toString(f3);
	} -> REAL[result]
	|	^(MODULAR op1=REAL op2=REAL)
	{ 
		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
		float f3 = f1 \% f2;
		
		result = Float.toString(f3);
	} -> REAL[result]
	|	^(POWER op1=REAL op2=REAL) 
	{ 
		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
		float f2 = Float.parseFloat($op2.getText().replaceAll("_", ""));
		float f3 = (float)Math.pow(f1, f2);
		
		result = Float.toString(f3);
	} -> REAL[result]
	|	^(UNARY_MINUS op1=REAL) 
	{ 
		float f1 = Float.parseFloat($op1.getText().replaceAll("_", ""));
		result = Float.toString(-f1);
	} -> REAL[result]
	;
