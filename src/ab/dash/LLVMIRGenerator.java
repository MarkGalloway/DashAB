package ab.dash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import ab.dash.DashLexer;
import ab.dash.ast.DashAST;
import ab.dash.ast.Symbol;
import ab.dash.ast.SymbolTable;
import ab.dash.ast.Type;


public class LLVMIRGenerator {
	private StringTemplateGroup stg;
	private StringTemplate template;
	private Map<Integer, Symbol> global;
	
	public enum LLVMOps {
	    ADD, SUB, MULT, DIV, EQ, NE, LT, GT
	}
	
	public LLVMIRGenerator(StringTemplateGroup stg) {		
		this.stg = stg;
	}
	
	public String toString() {
		return this.template.toString();
	}
	
	public void build(DashAST tree) {
		this.global = new HashMap<Integer, Symbol>();
		this.template = exec(tree);
	}
	
	protected StringTemplate exec(DashAST t) {
		System.out.println("Parsing: " + t + ", ");
		
		switch(t.getToken().getType()) {
		case DashLexer.PROGRAM:
		{	
			String code = "";
			for(int i = 0; i < t.getChildCount(); i++)
				code += exec((DashAST)t.getChild(i)).toString() + "\n";
			
			String global_vars = "";
			for (Map.Entry<Integer, Symbol> entry : this.global.entrySet()) {
				int type = entry.getValue().type.getTypeIndex();
				StringTemplate template = null;
				if (type == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("int_global_variable");
				}
				
				template.setAttribute("id", entry.getKey());
				
				global_vars += template.toString() + "\n";
			}
			
			StringTemplate template = stg.getInstanceOf("program");
			template.setAttribute("type_defs", null);
			template.setAttribute("globals", global_vars);
			template.setAttribute("code", code);
			return template;
		}
		
		case DashLexer.PROCEDURE_DECL:
		{
			
		}
			
		case DashLexer.EXPR:
			return exec((DashAST)t.getChild(0));
			
		case DashLexer.BLOCK:
		{
			String temp = "";
			for(int i = 0; i < t.getChildCount(); i++)
				temp += exec((DashAST)t.getChild(i)).toString() + "\n";
			return new StringTemplate(temp);
		}
			
//		case DashLexer.COND:
//		{
//			int id = ((DashAST)t).llvmResultID;
//			
//			StringTemplate expr = exec((DashAST)t.getChild(0));
//			int expr_id = ((DashAST)t.getChild(0).getChild(0)).llvmResultID;
//			
//			StringTemplate block = exec((DashAST)t.getChild(1));
//			
//			StringTemplate template = stg.getInstanceOf("cond");
//			
//			template.setAttribute("block", block);
//			template.setAttribute("expr_id", expr_id);
//			template.setAttribute("expr", expr);
//			template.setAttribute("id", id);
//			return template;
//		}
//
//		case DashLexer.LOOP:
//		{
//			int id = ((DashAST)t).llvmResultID;
//			
//			StringTemplate expr = exec((DashAST)t.getChild(0));
//			int expr_id = ((DashAST)t.getChild(0).getChild(0)).llvmResultID;
//			
//			StringTemplate block = exec((DashAST)t.getChild(1));
//			
//			StringTemplate template = stg.getInstanceOf("loop");
//			
//			template.setAttribute("block", block);
//			template.setAttribute("expr_id", expr_id);
//			template.setAttribute("expr", expr);
//			template.setAttribute("id", id);
//			return template;
//		}
//
//		case DashLexer.PRINT:
//		{
//			StringTemplate expr = exec((DashAST)t.getChild(0));
//			
//			int type = ((DashAST)t.getChild(0)).evalType.getTypeIndex();
//			int arg_id = ((DashAST)t.getChild(0).getChild(0)).llvmResultID;
//			int id = ((DashAST)t).llvmResultID;
//			
//			StringTemplate template = null;
//			if (type == SymbolTable.tINTEGER) {
//				// print_int(arg, arg_id, id)
//				template = stg.getInstanceOf("print_int");
//			} else if (type == SymbolTable.tVECTOR) {
//				// print_vector(arg, arg_id, id)
//				template = stg.getInstanceOf("print_vector");
//			}
//			
//			template.setAttribute("arg", expr);
//			template.setAttribute("arg_id", arg_id);
//			template.setAttribute("id", id);
//			
//			return template;
//		}
			
		case DashLexer.VAR_DECL:
		{
			Symbol sym = ((DashAST)t.getChild(1)).symbol;
			int sym_id = sym.id;
			
			this.global.put(sym_id, sym);
			
			int id = ((DashAST)t).llvmResultID;
			int type = ((DashAST)t.getChild(2)).evalType.getTypeIndex();
			
			StringTemplate expr = exec((DashAST)t.getChild(2));
			int arg_id = ((DashAST)t.getChild(2).getChild(0)).llvmResultID;
			
			StringTemplate template = null;
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("assign_int");
			}
			
			template.setAttribute("expr_id", arg_id);
			template.setAttribute("expr", expr);
			template.setAttribute("name", sym_id);
			template.setAttribute("id", id);
			return template;
		}

		case DashLexer.ASSIGN:
		{
			Symbol sym = ((DashAST)t.getChild(0)).symbol;
			int sym_id = sym.id;
			
			int id = ((DashAST)t).llvmResultID;
			int type = ((DashAST)t.getChild(1)).evalType.getTypeIndex();
			
			StringTemplate expr = exec((DashAST)t.getChild(1));
			int arg_id = ((DashAST)t.getChild(1).getChild(0)).llvmResultID;
			
			StringTemplate template = null;
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("assign_int");
			}
			
			template.setAttribute("expr_id", arg_id);
			template.setAttribute("expr", expr);
			template.setAttribute("name", sym_id);
			template.setAttribute("id", id);
			return template;
		}

		case DashLexer.EQUALITY:
			return operation(t, LLVMOps.EQ);

		case DashLexer.INEQUALITY:
			return operation(t, LLVMOps.NE);

		case DashLexer.LESS:
			return operation(t, LLVMOps.LT);

		case DashLexer.GREATER:
			return operation(t, LLVMOps.GT);

		case DashLexer.ADD:
			return operation(t, LLVMOps.ADD);

		case DashLexer.SUBTRACT:
			return operation(t, LLVMOps.SUB);

		case DashLexer.MULTIPLY:
			return operation(t, LLVMOps.MULT);

		case DashLexer.DIVIDE:
			return operation(t, LLVMOps.DIV);

		case DashLexer.ID:
		{
			int id = ((DashAST)t).llvmResultID;
			int sym_id = ((DashAST)t).symbol.id;
			
			int type = ((DashAST)t).evalType.getTypeIndex();
			
			StringTemplate template = null;
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("get_int");
			}
			
			template.setAttribute("name", sym_id);
			template.setAttribute("id", id);
			return template;
		}

		case DashLexer.INTEGER:
		{
			int id = ((DashAST)t).llvmResultID;
			int val = Integer.parseInt(t.getText());
			
			StringTemplate template = stg.getInstanceOf("literal_int");
			template.setAttribute("val", val);
			template.setAttribute("id", id);
			return template;
		}

		default:
			/*
			 * Should never get here.
			 */
			throw new RuntimeException("Unrecognized token: " + t.getText());
		}

	}

	private StringTemplate operation(DashAST t, LLVMOps op)
	{
		String id = Integer.toString(((DashAST)t).llvmResultID);
		int type = ((DashAST)t.getChild(0)).evalType.getTypeIndex();
		
		StringTemplate lhs = exec((DashAST)t.getChild(0));
		String lhs_id = Integer.toString(((DashAST)t.getChild(0)).llvmResultID);
		
		StringTemplate rhs = exec((DashAST)t.getChild(1));
		String rhs_id = Integer.toString(((DashAST)t.getChild(1)).llvmResultID);
		
		Type lhs_promotion_type = ((DashAST)t.getChild(0)).promoteToType;
		Type rhs_promotion_type = ((DashAST)t.getChild(1)).promoteToType;
		if (lhs_promotion_type != null) {
			
		} else if (rhs_promotion_type != null) {
			
		}
		
		StringTemplate template = null;
		switch (op) {
		case EQ:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("eq_int");
			}
			break;
		case NE:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("ne_int");
			}
			break;
		case LT:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("lt_int");
			}
			break;
		case GT:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("gt_int");
			}
			break;
		case ADD:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("add_int");
			}
			break;
		case SUB:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("sub_int");
			}
			break;
		case MULT:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("mul_int");
			}
			break;
		case DIV:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("div_int");
			}
			break;
		}
		
		template.setAttribute("rhs_id", rhs_id);
		template.setAttribute("rhs", rhs);
		template.setAttribute("lhs_id", lhs_id);
		template.setAttribute("lhs", lhs);
		template.setAttribute("id", id);
		return template;
	}
}
