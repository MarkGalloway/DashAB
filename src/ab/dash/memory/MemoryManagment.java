package ab.dash.memory;

import java.util.LinkedList;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import ab.dash.ast.DashAST;
import ab.dash.ast.Symbol;
import ab.dash.ast.SymbolTable;

public class MemoryManagment {
	private LinkedList<MemoryNode> nodes;
	private StringTemplateGroup stg;
	private int counter;
	
	public static int VARIABLE_MEMORY_NODE = 0;
	public static int BLOCK_MEMORY_NODE = 1;
	public static int METHOD_MEMORY_NODE = 2;
	public static int RETURN_MEMORY_NODE = 3;
	
	public MemoryManagment(StringTemplateGroup stg) {
		this.stg = stg;
		this.nodes = new LinkedList<MemoryNode>();
		
		this.counter = 0;
	}
	
	private String freeVariable(VariableMemoryNode var) {
		if (var.local) {
			boolean valid_symbol = false;
			StringTemplate template = null;
			if (var.type == SymbolTable.tINTERVAL){
				template = stg.getInstanceOf("interval_free_local");
				valid_symbol = true;
			} else if (var.type == SymbolTable.tVECTOR){
				template = stg.getInstanceOf("vector_free_local");
				valid_symbol = true;
			}
			
			if (valid_symbol) {
				this.counter++;
				template.setAttribute("sym_id", var.id);
				template.setAttribute("id", counter);
				return template.toString() + "\n";
			}
		}
		
		return null;
	}
	
	public void addLocalVariable(int id, int type) {
		nodes.push(new VariableMemoryNode(id, type, true));
	}
	
	public void addTempVariable(int id, int type) {
		nodes.push(new VariableMemoryNode(id, type, false));
	}
	
	public void addBlock() {
		nodes.push(new BlockMemoryNode());
	}
	
	public StringTemplate addBlock(DashAST t) {
		nodes.push(new BlockMemoryNode());
		
		String temp = "";
		
		for (Symbol s : t.scope.getDefined()) {
			int sym_id = s.id;
			int type = s.type.getTypeIndex();
			
			if (type == SymbolTable.tINTERVAL){
				 addLocalVariable(sym_id, type);
				 StringTemplate alloc = stg.getInstanceOf("interval_alloc_local");
				 alloc.setAttribute("sym_id", sym_id);
				 temp += alloc.toString() + "\n";
			}
		}
		
		return new StringTemplate(temp);
	}
	
	public StringTemplate freeBlock() {
		String temp = "";
		int type = nodes.peek().getNodeType();
		int i = 0;
		boolean returnFound = false;
		while (type != BLOCK_MEMORY_NODE) {
			MemoryNode node = nodes.get(i);
			if(node.getNodeType() == RETURN_MEMORY_NODE) {
				returnFound = true;
				break;
			}
			type = node.getNodeType();
			i++;
		}
		
		type = nodes.peek().getNodeType();
		while (type != BLOCK_MEMORY_NODE) {
			MemoryNode node = nodes.peek();
			if (!returnFound) {
				if (type == VARIABLE_MEMORY_NODE) {
					VariableMemoryNode var = (VariableMemoryNode) node;
					temp += freeVariable(var);
				}
			}

			nodes.pop();

			type = nodes.peek().getNodeType();
		}

		nodes.pop();
		
		return new StringTemplate(temp);
	}
	
	public void addMethod()  {
		nodes.push(new MethodMemoryNode());
	}
	
	public StringTemplate freeMethodReturn() {
		String temp = "";
		int type = nodes.peek().getNodeType();
		int i = 0;
		while (type != METHOD_MEMORY_NODE) {
			MemoryNode node = nodes.get(i);
			if (node.getNodeType() == VARIABLE_MEMORY_NODE) {
				VariableMemoryNode var = (VariableMemoryNode) node;
				temp += freeVariable(var);
			}
			
			i++;
			
			type = node.getNodeType();
		}
		
		nodes.push(new ReturnMemoryNode());
		
		return new StringTemplate(temp);
	}
	
	public StringTemplate freeMethod() {
		String temp = "";
		int type = nodes.peek().getNodeType();
		while (type != METHOD_MEMORY_NODE) {
			nodes.pop();			
			type = nodes.peek().getNodeType();
		}
		
		nodes.pop();
		
		return new StringTemplate(temp);
	}
}
