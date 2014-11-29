

// START: header
tree grammar ConvertStrings;

options {
  tokenVocab   = Dash;
  ASTLabelType = DashAST;
  filter       = true;
  output       = AST;
}

@header {
package ab.dash;
  import ab.dash.ast.*;
  import java.io.PrintStream;
}

@members {
SymbolTable symtab;
Scope currentScope;
MethodSymbol currentMethod;
boolean debug_mode;

public ConvertStrings(TreeNodeStream input, SymbolTable symtab) {
	this(input, symtab, false);
}

public ConvertStrings(TreeNodeStream input, SymbolTable symtab, Boolean debug) {
	this(input);
	this.symtab = symtab;
	this.currentScope = symtab.globals;
	this.debug_mode = debug;

	setTreeAdaptor(DashAST.dashAdaptor);
}

private void debug(String msg) {
	if (debug_mode)
		System.out.println(msg);
}
}
// END: header

topdown
  :
  string_decl
  | string_literal
  ;

string_decl
	:
	String 
	{
	DashAST type = new DashAST(new CommonToken(DashLexer.CHARACTER_TYPE, "character"));
	DashAST inferred = new DashAST(new CommonToken(DashLexer.INFERRED, "INFERRED"));
	$String.token = new CommonToken(DashLexer.VECTOR, "VECTOR");
	$String.addChild(type);
	$String.addChild(inferred);
	}
	;

string_literal
	:
	STRING 
	{
		char[] charArray = $STRING.text.toCharArray();
		
		$STRING.token = new CommonToken(DashLexer.VECTOR_LIST, "VECTOR_LIST");
		
		boolean escape = false;
		for (char c : charArray) {
			if (c == '\\') {
				escape = true;
			} else {
				DashAST expr = new DashAST(new CommonToken(DashLexer.EXPR, "EXPR"));
				String character = "";
				if (escape)
					character = "\\";
				
				character += c;
				DashAST character_node = new DashAST(new CommonToken(DashLexer.CHARACTER, character));
				expr.addChild(character_node);
				
				$STRING.addChild(expr);
				
				escape = false;
			}
		}
	}
	;
