package ab.dash;

import ab.dash.ast.DashAST;

public class CompileOutput {
	public String llvm;
	public DashAST tree;
	
	public CompileOutput(String llvm, DashAST tree) {
		this.llvm = llvm;
		this.tree = tree;
	}
}
