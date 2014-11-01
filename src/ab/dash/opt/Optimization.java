package ab.dash.opt;

import java.util.TreeSet;

import org.antlr.runtime.tree.CommonTreeNodeStream;

import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;

public class Optimization {
	SymbolTable symtab;
	DashAST tree;
	CommonTreeNodeStream nodes;
	boolean debug_mode;
	
	
	// Optimization Options
	public enum Options {
	    CONSTANT_FOLDING,
	    CONSTANT_PROPAGATION,
	    REMOVE_UNUSED_VARIABLES
	}
	
	// States
	boolean constant_folding;
	boolean constant_propagation;
	boolean remove_unused_variables;
	
	public Optimization(CommonTreeNodeStream nodes, DashAST tree, SymbolTable symtab) {
		this.nodes = nodes;
		this.tree = tree;
		this.symtab = symtab;
		this.debug_mode = false;
		
		this.constant_folding = true;
		this.constant_propagation = true;
		this.remove_unused_variables = false;
	}
	
	public Optimization(CommonTreeNodeStream nodes, DashAST tree, SymbolTable symtab, boolean debug_mode) {
		this(nodes, tree, symtab);
		this.debug_mode = debug_mode;
	}
	
	public void setOptimization(Options state, boolean on) {
		switch (state) {
		case CONSTANT_FOLDING:
			this.constant_folding = on;
			break;
		case CONSTANT_PROPAGATION:
			this.constant_propagation = on;
			break;
		case REMOVE_UNUSED_VARIABLES:
			this.remove_unused_variables = on;
			break;
		}
	}
	
	private void debug(String msg) {
    	if (debug_mode)
    		System.out.println(msg);
    }
	
	public void optimize() {
		// Print Options
		debug("Optimization Options:");
		debug("Constant Folding: " + (constant_folding ? "On" : "Off"));
		debug("Constant Propagation: " + (constant_propagation ? "On" : "Off"));
		debug("Remove Unused Variables: " + (remove_unused_variables ? "On" : "Off"));
		
		boolean fixed_state = false;
		
		ConstantFolding opt1 = new ConstantFolding(nodes);
		opt1.setTreeAdaptor(DashAST.dashAdaptor);
	
		ConstantPropagation opt2 = new ConstantPropagation();
		int iterations = 0;
		String tree_before = tree.toStringTree();
		
		// Loop until fixed state
		while (!fixed_state) {
			iterations++;
			debug("Iteration: " + iterations);
			
			// Perform constant folding
			if (constant_folding) {
				nodes.reset();
				opt1.downup(tree);
			}
			
			// Perform constant propagation
			if (constant_propagation) {
				nodes.reset();
				opt2.optimize(tree);
			}
			
			// Remove unused variables
			if (remove_unused_variables) {
				Ref opt3 = new Ref(nodes, debug_mode);
				nodes.reset();
				opt3.downup(tree);
				
				TreeSet<Integer> refs = opt3.getRefs();
				
				RemoveUnusedVariables opt4 = new RemoveUnusedVariables(refs, debug_mode);
				nodes.reset();
				opt4.optimize(tree);
			}
			
			String tree_after = tree.toStringTree();
			fixed_state = tree_before.equals(tree_after);
			tree_before = tree_after;
		}
		
		debug(tree.toStringTree());
	}
}
