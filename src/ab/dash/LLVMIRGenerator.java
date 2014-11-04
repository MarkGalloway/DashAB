package ab.dash;

import java.util.ArrayList;
import java.util.Stack;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import ab.dash.DashLexer;
import ab.dash.ast.DashAST;
import ab.dash.ast.MethodSymbol;
import ab.dash.ast.Scope;
import ab.dash.ast.Symbol;
import ab.dash.ast.SymbolTable;
import ab.dash.ast.TupleTypeSymbol;
import ab.dash.ast.Type;
import ab.dash.ast.VariableSymbol;


public class LLVMIRGenerator {
	private StringTemplateGroup stg;
	private StringTemplate template;
	private SymbolTable symtab;
	private Stack<Integer> loop_stack;
	
	private boolean debug_mode = false;
	
	public enum LLVMOps {
	    AND, OR, XOR, ADD, SUB, MULT, DIV, EQ, NE, LT, LE, GT, GE
	}
	
	public LLVMIRGenerator(StringTemplateGroup stg, SymbolTable symtab) {		
		this.stg = stg;
		this.symtab = symtab;
		this.loop_stack = new Stack<Integer>();
	}
	
	public String toString() {
		return this.template.toString();
	}
	
	public void debug_on() {
    	debug_mode = true;
    }
    
    public void debug_off() {
    	debug_mode = false;
    }
    
    private void debug(Object msg) {
    	if (debug_mode)
    		System.out.println(msg);
    }
	
	public void build(DashAST tree) {
		this.template = exec(tree);
	}
	
	protected StringTemplate exec(DashAST t) {
		debug("Parsing: " + t + ", ");
		
		switch(t.getToken().getType()) {
		case DashLexer.PROGRAM:
		{	
			// Generate Code
			String code = "";
			String global_code = "";
			for(int i = 0; i < t.getChildCount(); i++) {
				if (!(t.getChild(i).hasAncestor(DashLexer.PROCEDURE_DECL) ||
						t.getChild(i).hasAncestor(DashLexer.FUNCTION_DECL)) && 
						t.getChild(i).getType() == DashLexer.VAR_DECL) {
					global_code += exec((DashAST)t.getChild(i)).toString() + "\n";
				} else {
					code += exec((DashAST)t.getChild(i)).toString() + "\n";
					
				}
			}
			
			// Generate Globals
			debug("\n\nCreated Globals:");
			
			String global_vars = "";
			for (Symbol s : symtab.globals.getDefined()) {
				if (!(s instanceof MethodSymbol)) {
					if (s.type != null) {	
						
						debug(s);
						
						int type = s.type.getTypeIndex();
						StringTemplate template = null;
						if (type == SymbolTable.tINTEGER) {
							template = stg.getInstanceOf("int_init_global");
						} else if (type == SymbolTable.tREAL) {
							template = stg.getInstanceOf("real_init_global");
						} else if (type == SymbolTable.tCHARACTER) {
							template = stg.getInstanceOf("char_init_global");
						} else if (type == SymbolTable.tBOOLEAN) {
							template = stg.getInstanceOf("bool_init_global");
						} else if (type == SymbolTable.tTUPLE) {
							template = stg.getInstanceOf("tuple_init_global");
						}
						
						if (template != null) {
							template.setAttribute("sym_id", s.id);
							global_vars += template.toString() + "\n";
						}
					}
				}
			}
			
			debug("\n\nGlobals in Symbol Table:");
			debug(symtab.globals);
			
			// Generate Types
			debug("\n\nCreated Types:");

			String type_vars = "";
			for (int id = 0; id < symtab.tuples.size(); id++) {
				ArrayList<Type> fields = symtab.tuples.get(id);
				String types = "";
				for (int i = 0; i < fields.size(); i++) {
					int type = fields.get(i).getTypeIndex();
					
					StringTemplate template = null;
					if (type == SymbolTable.tINTEGER) {
						template = stg.getInstanceOf("int_type");
					} else if (type == SymbolTable.tREAL) {
						template = stg.getInstanceOf("real_type");
					} else if (type == SymbolTable.tCHARACTER) {
						template = stg.getInstanceOf("char_type");
					} else if (type == SymbolTable.tBOOLEAN) {
						template = stg.getInstanceOf("bool_type");
					}
					
					types += template.toString();
					if (i < fields.size() - 1) {
						types += ",\n";
					}
				}
				
				StringTemplate template = stg.getInstanceOf("tuple");
				template.setAttribute("types", types);
				template.setAttribute("id", id);
				
				type_vars += template.toString() + "\n\n";
			}
			
			StringTemplate template = stg.getInstanceOf("program");
			template.setAttribute("type_defs", type_vars);
			template.setAttribute("globals", global_vars);
			template.setAttribute("global_code", global_code);
			template.setAttribute("code", code);
			
			return template;
		}
		
		case DashLexer.FUNCTION_DECL:
		case DashLexer.PROCEDURE_DECL:
		{
			Symbol sym = ((DashAST)t.getChild(0)).symbol;
			int sym_id = sym.id;
			
			Type type = sym.type;
			
			if (sym.name.equals("main")) {
				StringTemplate code = exec((DashAST)t.getChild(1));
				StringTemplate template = stg.getInstanceOf("function_main");
				template.setAttribute("code", code);
				template.setAttribute("id", t.llvmResultID);
				return template;
			}
			
			String args = "";
			for (int i = 1; i < t.getChildCount() - 1; i++) {
				DashAST argument_node = ((DashAST) t.getChild(i).getChild(0));
				VariableSymbol arg_var = (VariableSymbol)argument_node.symbol;
				Type arg_type = arg_var.type;
				StringTemplate type_template = getType(arg_type);
				
				StringTemplate arg = stg.getInstanceOf("args");
				arg.setAttribute("arg_id", arg_var.id);
				arg.setAttribute("arg_type", type_template);
				arg.setAttribute("id", argument_node.llvmResultID);
				
				args += arg.toString();
				if (i < t.getChildCount() - 2) {
					args += ", ";
				}
			}
			
			String arg_init = "";
			for (int i = 1; i < t.getChildCount() - 1; i++) {
				DashAST argument_node = ((DashAST) t.getChild(i).getChild(0));
				
				VariableSymbol arg_var = (VariableSymbol)argument_node.symbol;
				Type arg_type = arg_var.type;
				StringTemplate type_template = getType(arg_type);
				
				StringTemplate arg = stg.getInstanceOf("arg_init");
				arg.setAttribute("arg_type", type_template);
				arg.setAttribute("arg_id", arg_var.id);
				arg.setAttribute("id", argument_node.llvmResultID);
				
				arg_init += arg.toString();
			}

			StringTemplate code = exec((DashAST)t.getChild(t.getChildCount() - 1));
			String code_s = code.toString();
			if (type.getTypeIndex() == SymbolTable.tVOID) {
				code_s += "\nret void\n";
			}
			
			StringTemplate template = stg.getInstanceOf("function");
			template.setAttribute("code", code_s);
			template.setAttribute("return_type", getType(type));
			template.setAttribute("args", args);
			template.setAttribute("arg_init", arg_init);
			template.setAttribute("sym_id", sym_id);
			template.setAttribute("id", t.llvmResultID);
			return template;
		}
		
		case DashLexer.CALL:
		{
			// Arguments
			String code = "";
			String args = "";
			DashAST argument_list = (DashAST) t.getChild(1);
			for (int i = 0; i < argument_list.getChildCount(); i++) {
				DashAST arg = (DashAST)argument_list.getChild(i);
				
				StringTemplate arg_exec = exec(arg);
				code += arg_exec + "\n";
				
				Type arg_type = arg.evalType;
				StringTemplate type_template = getType(arg_type);
				
				StringTemplate arg_template = stg.getInstanceOf("args_call");
				arg_template.setAttribute("arg_id", arg.llvmResultID);
				arg_template.setAttribute("arg_type", type_template);
				arg_template.setAttribute("id", arg.llvmResultID);
				
				args += arg_template;
				if (i < argument_list.getChildCount() - 1) {
					args += ", ";
				}
			}
			DashAST method_node = (DashAST) t.getChild(0);
			MethodSymbol method = (MethodSymbol)method_node.symbol;
			Type method_type = method.type;
			
			StringTemplate template = null;
			if (method_type.getTypeIndex() == SymbolTable.tVOID) {
				template = stg.getInstanceOf("call_void");
				template.setAttribute("code", code);
				template.setAttribute("args", args);
				template.setAttribute("function_id", method.id);
				template.setAttribute("id", t.llvmResultID);
				
				return template;
			} else {
				template = stg.getInstanceOf("call");
				template.setAttribute("code", code);
				template.setAttribute("args", args);
				template.setAttribute("return_type", getType(method_type));
				template.setAttribute("function_id", method.id);
				template.setAttribute("id", t.llvmResultID);
				
				return template;
			}
		}
		
		case DashLexer.Return:
		{
			int id = t.llvmResultID;
			
			StringTemplate return_expression = exec((DashAST)t.getChild(0));
			int expr_id = ((DashAST) t.getChild(0).getChild(0)).llvmResultID;

			StringTemplate template = stg.getInstanceOf("return");
			StringTemplate type_template = getType(((DashAST) t.getChild(0)).evalType);

			template.setAttribute("expr_id", expr_id);
			template.setAttribute("expr", return_expression);
			template.setAttribute("type", type_template);
			template.setAttribute("id", id);
			
			return template;
		}
		
		case DashLexer.TUPLE_LIST:
		{
			return new StringTemplate();
		}
			
		case DashLexer.EXPR:
			return exec((DashAST)t.getChild(0));
			
		case DashLexer.BLOCK:
		{
			String temp = "";
			
			debug("locals: "+ t.scope);	
			
			// Alloca Local Variables
			for (Symbol s : t.scope.getDefined()) {
				int sym_id = s.id;
				int type = s.type.getTypeIndex();
				boolean valid_symbol = false;
				StringTemplate template = null;
				if (type == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("int_init_local");
					valid_symbol = true;
				} else if (type == SymbolTable.tREAL) {
					template = stg.getInstanceOf("real_init_local");
					valid_symbol = true;
				} else if (type == SymbolTable.tCHARACTER) {
					template = stg.getInstanceOf("char_init_local");
					valid_symbol = true;
				} else if (type == SymbolTable.tBOOLEAN) {
					template = stg.getInstanceOf("bool_init_local");
					valid_symbol = true;
				} else if (type == SymbolTable.tTUPLE) {
					template = stg.getInstanceOf("tuple_init_local");
					template.setAttribute("type_id", ((TupleTypeSymbol)s.type).tupleTypeIndex);
					valid_symbol = true;
				}
				
				if (valid_symbol) {
					template.setAttribute("sym_id", sym_id);
					temp += template.toString() + "\n";
				}
			}
			
			for(int i = 0; i < t.getChildCount(); i++)
				temp += exec((DashAST)t.getChild(i)).toString() + "\n";
			return new StringTemplate(temp);
		}
			
		case DashLexer.If:
		{
			int id = ((DashAST)t).llvmResultID;
			
			StringTemplate expr = exec((DashAST)t.getChild(0));
			int expr_id = ((DashAST)t.getChild(0).getChild(0)).llvmResultID;
			
			StringTemplate block = exec((DashAST)t.getChild(1));
			StringTemplate template = null;
			if (t.getChildCount() > 2) {
				StringTemplate block2 = exec((DashAST)t.getChild(2));
				
				template = stg.getInstanceOf("if_else");
				template.setAttribute("block2", block2);
			} else {
				template = stg.getInstanceOf("if");
			}
						
			template.setAttribute("block", block);
			template.setAttribute("expr_id", expr_id);
			template.setAttribute("expr", expr);
			template.setAttribute("id", id);
			
			return template;
		}

		case DashLexer.WHILE:
		{
			int id = ((DashAST)t).llvmResultID;
			
			StringTemplate expr = exec((DashAST)t.getChild(0));
			int expr_id = ((DashAST)t.getChild(0).getChild(0)).llvmResultID;
			
			loop_stack.push(new Integer(id));
			
			StringTemplate block = exec((DashAST)t.getChild(1));
			
			loop_stack.pop();
			
			StringTemplate template = stg.getInstanceOf("while");
			
			template.setAttribute("block", block);
			template.setAttribute("expr_id", expr_id);
			template.setAttribute("expr", expr);
			template.setAttribute("id", id);
			return template;
		}
		
		case DashLexer.DOWHILE:
		{
			int id = ((DashAST)t).llvmResultID;
			
			StringTemplate expr = exec((DashAST)t.getChild(0));
			int expr_id = ((DashAST)t.getChild(0).getChild(0)).llvmResultID;
			
			loop_stack.push(new Integer(id));
			
			StringTemplate block = exec((DashAST)t.getChild(1));
			
			loop_stack.pop();
			
			StringTemplate template = stg.getInstanceOf("dowhile");
			
			template.setAttribute("block", block);
			template.setAttribute("expr_id", expr_id);
			template.setAttribute("expr", expr);
			template.setAttribute("id", id);
			return template;
		}
		
		case DashLexer.Loop:
		{
			int id = ((DashAST)t).llvmResultID;
			
			
			loop_stack.push(new Integer(id));
			
			StringTemplate block = exec((DashAST)t.getChild(0));
			
			loop_stack.pop();
			
			StringTemplate template = stg.getInstanceOf("loop");
			
			template.setAttribute("block", block);
			template.setAttribute("id", id);
			return template;
		}
		
		case DashLexer.Break:
		{
			int id = ((DashAST)t).llvmResultID;
			
			Integer loop_id = loop_stack.peek();
			
			StringTemplate template = stg.getInstanceOf("break");
			
			template.setAttribute("loop_id", loop_id.intValue());
			template.setAttribute("id", id);
			return template;
		}

		case DashLexer.Continue:
		{
			int id = ((DashAST)t).llvmResultID;
			
			Integer loop_id = loop_stack.peek();
			
			StringTemplate template = stg.getInstanceOf("continue");
			
			template.setAttribute("loop_id", loop_id.intValue());
			template.setAttribute("id", id);
			return template;
		}
		
		case DashLexer.PRINT:
		{
			StringTemplate expr = exec((DashAST)t.getChild(0));
			
			int type = ((DashAST)t.getChild(0)).evalType.getTypeIndex();
			int arg_id = ((DashAST)t.getChild(0).getChild(0)).llvmResultID;
			int id = ((DashAST)t).llvmResultID;
			
			StringTemplate template = null;
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_print");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_print");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_print");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_print");
			}
			
			template.setAttribute("expr", expr);
			template.setAttribute("expr_id", arg_id);
			template.setAttribute("id", id);
			
			return template;
		}
			
		case DashLexer.VAR_DECL:
		{
			Symbol sym = ((DashAST)t.getChild(0)).symbol;
			Scope scope = sym.scope;
			int sym_id = sym.id;
			
			//this.global.put(sym_id, sym);
			
			int id = ((DashAST)t).llvmResultID;
			int type = sym.type.getTypeIndex();
			DashAST child = (DashAST)t.getChild(1);
			if (child != null) {
				// Tuple
				if (child.getType() == DashLexer.TUPLE_LIST) {
					TupleTypeSymbol tuple = (TupleTypeSymbol) sym.type;
					String temp = "";
					for(int i = 0; i < t.getChildCount(); i++) {
						String expr = exec((DashAST) child.getChild(i)).toString() + "\n";
						int arg_id = ((DashAST)child.getChild(i).getChild(0)).llvmResultID;
						int type_l = ((DashAST)child.getChild(i)).evalType.getTypeIndex();
								
						StringTemplate template = null;
						if (type_l == SymbolTable.tINTEGER) {
							template = stg.getInstanceOf("int_local_tuple_assign");
						}  else if (type_l == SymbolTable.tREAL) {
							template = stg.getInstanceOf("real_local_tuple_assign");
						} else if (type_l == SymbolTable.tCHARACTER) {
							template = stg.getInstanceOf("char_local_tuple_assign");
						} else if (type_l == SymbolTable.tBOOLEAN) {
							template = stg.getInstanceOf("bool_local_tuple_assign");
						}
						
						template.setAttribute("expr_id", arg_id);
						template.setAttribute("expr", expr);
						template.setAttribute("index", i);
						template.setAttribute("tuple_type", tuple.tupleTypeIndex);
						template.setAttribute("tuple_id", tuple.id);
						template.setAttribute("id", id);
						
						temp += template.toString() + "\n";
					}
					return new StringTemplate(temp);
				}
				
				StringTemplate expr = exec(child);
				int arg_id = ((DashAST)child.getChild(0)).llvmResultID;
				
				StringTemplate template = null;
				if (scope.getScopeIndex() == SymbolTable.scGLOBAL) {
					if (type == SymbolTable.tINTEGER) {
						template = stg.getInstanceOf("int_global_assign");
					} else if (type == SymbolTable.tREAL) {
						template = stg.getInstanceOf("real_global_assign");
					} else if (type == SymbolTable.tCHARACTER) {
						template = stg.getInstanceOf("char_global_assign");
					} else if (type == SymbolTable.tBOOLEAN) {
						template = stg.getInstanceOf("bool_global_assign");
					} else if (type == SymbolTable.tTUPLE) {
						//template = stg.getInstanceOf("tuple_global_assign");
						template = new StringTemplate();
					}
				} else {
					if (type == SymbolTable.tINTEGER) {
						template = stg.getInstanceOf("int_local_assign");
					} else if (type == SymbolTable.tREAL) {
						template = stg.getInstanceOf("real_local_assign");
					} else if (type == SymbolTable.tCHARACTER) {
						template = stg.getInstanceOf("char_local_assign");
					} else if (type == SymbolTable.tBOOLEAN) {
						template = stg.getInstanceOf("bool_local_assign");
					} else if (type == SymbolTable.tTUPLE) {
						//template = stg.getInstanceOf("tuple_local_assign");
						template = new StringTemplate();
					}
				}
				
				template.setAttribute("expr_id", arg_id);
				template.setAttribute("expr", expr);
				template.setAttribute("sym_id", sym_id);
				template.setAttribute("id", id);
				return template;
			}
			
			return new StringTemplate("");
		}

		case DashLexer.ASSIGN:
		{
			int id = ((DashAST)t).llvmResultID;
			DashAST node = (DashAST)t.getChild(0).getChild(0);
			
			if (node.getType() == DashLexer.DOT) {
				DashAST tupleNode = (DashAST) node.getChild(0);
				DashAST memberNode = (DashAST) node.getChild(1);
				
				VariableSymbol tuple = (VariableSymbol) tupleNode.symbol;
				TupleTypeSymbol tuple_type = (TupleTypeSymbol)tuple.type;
				int index = tuple_type.getMemberIndex(memberNode.getText());
				
				StringTemplate expr = exec((DashAST)t.getChild(1));
				int arg_id = ((DashAST)t.getChild(1).getChild(0)).llvmResultID;
				
				int type = node.evalType.getTypeIndex();
				
				StringTemplate template = null;
				if (type == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("int_local_tuple_assign");
				}  else if (type == SymbolTable.tREAL) {
					template = stg.getInstanceOf("real_local_tuple_assign");
				} else if (type == SymbolTable.tCHARACTER) {
					template = stg.getInstanceOf("char_local_tuple_assign");
				} else if (type == SymbolTable.tBOOLEAN) {
					template = stg.getInstanceOf("bool_local_tuple_assign");
				}
				
				template.setAttribute("expr_id", arg_id);
				template.setAttribute("expr", expr);
				template.setAttribute("index", index);
				template.setAttribute("tuple_type", tuple_type.tupleTypeIndex);
				template.setAttribute("tuple_id", tuple.id);
				template.setAttribute("id", id);
				return template;
			}
			
			Symbol sym = node.symbol;
			Scope scope = sym.scope;
			int sym_id = sym.id;
			
			int type = ((DashAST)t.getChild(1)).evalType.getTypeIndex();
			
			StringTemplate expr = exec((DashAST)t.getChild(1));
			int arg_id = ((DashAST)t.getChild(1).getChild(0)).llvmResultID;
			
			StringTemplate template = null;
			if (scope.getScopeIndex() == SymbolTable.scGLOBAL) {
				if (type == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("int_global_assign");
				} else if (type == SymbolTable.tREAL) {
					template = stg.getInstanceOf("real_global_assign");
				} else if (type == SymbolTable.tCHARACTER) {
					template = stg.getInstanceOf("char_global_assign");
				} else if (type == SymbolTable.tBOOLEAN) {
					template = stg.getInstanceOf("bool_global_assign");
				} else if (type == SymbolTable.tTUPLE) {
					template = stg.getInstanceOf("tuple_global_assign");
				}
			} else {
				if (type == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("int_local_assign");
				} else if (type == SymbolTable.tREAL) {
					template = stg.getInstanceOf("real_local_assign");
				} else if (type == SymbolTable.tCHARACTER) {
					template = stg.getInstanceOf("char_local_assign");
				} else if (type == SymbolTable.tBOOLEAN) {
					template = stg.getInstanceOf("bool_local_assign");
				} else if (type == SymbolTable.tTUPLE) {
					template = stg.getInstanceOf("tuple_local_assign");
				}
			}
			
			template.setAttribute("expr_id", arg_id);
			template.setAttribute("expr", expr);
			template.setAttribute("sym_id", sym_id);
			template.setAttribute("id", id);
			return template;
		}
		
		case DashLexer.UNARY_MINUS:
		{
			String id = Integer.toString(((DashAST)t).llvmResultID);
			int type = ((DashAST)t.getChild(0)).evalType.getTypeIndex();
			
			StringTemplate expr = exec((DashAST)t.getChild(0));
			String expr_id = Integer.toString(((DashAST)t.getChild(0)).llvmResultID);
			
			StringTemplate template = null;
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_minus");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_minus");
			}
			
			template.setAttribute("expr_id", expr_id);
			template.setAttribute("expr", expr);
			template.setAttribute("id", id);
			
			return template;
		}
		
		case DashLexer.Not:
		{
			String id = Integer.toString(((DashAST)t).llvmResultID);
			int type = ((DashAST)t.getChild(0)).evalType.getTypeIndex();
			
			StringTemplate expr = exec((DashAST)t.getChild(0));
			String expr_id = Integer.toString(((DashAST)t.getChild(0)).llvmResultID);
			
			StringTemplate template = null;
			if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_not");
			}
			
			template.setAttribute("expr_id", expr_id);
			template.setAttribute("expr", expr);
			template.setAttribute("id", id);
			
			return template;
		}
		
		case DashLexer.And:
			return operation(t, LLVMOps.AND);
			
		case DashLexer.Or:
			return operation(t, LLVMOps.OR);
			
		case DashLexer.Xor:
			return operation(t, LLVMOps.XOR);

		case DashLexer.EQUALITY:
			return operation(t, LLVMOps.EQ);

		case DashLexer.INEQUALITY:
			return operation(t, LLVMOps.NE);

		case DashLexer.LESS:
			return operation(t, LLVMOps.LT);
			
		case DashLexer.LESS_EQUAL:
			return operation(t, LLVMOps.LE);

		case DashLexer.GREATER:
			return operation(t, LLVMOps.GT);
			
		case DashLexer.GREATER_EQUAL:
			return operation(t, LLVMOps.GE);

		case DashLexer.ADD:
			return operation(t, LLVMOps.ADD);

		case DashLexer.SUBTRACT:
			return operation(t, LLVMOps.SUB);

		case DashLexer.MULTIPLY:
			return operation(t, LLVMOps.MULT);

		case DashLexer.DIVIDE:
			return operation(t, LLVMOps.DIV);
			
		case DashLexer.DOT:
		{
			int id = ((DashAST)t).llvmResultID;
			DashAST tupleNode = (DashAST) t.getChild(0);
			DashAST memberNode = (DashAST) t.getChild(1);
			
			VariableSymbol tuple = (VariableSymbol) tupleNode.symbol;
			TupleTypeSymbol tuple_type = null;
			int tuple_id = 0;
			if (tuple != null) {
				tuple_type = (TupleTypeSymbol)tuple.type;
				tuple_id = tuple.id;
			} else {
				tuple_type = (TupleTypeSymbol)tupleNode.evalType;
				tuple_id = tupleNode.llvmResultID;
			}
			
			int index = tuple_type.getMemberIndex(memberNode.getText());
			
			int type = t.evalType.getTypeIndex();
			
			StringTemplate template = null;
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_get_local_tuple_member");
			}  else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_get_local_tuple_member");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_get_local_tuple_member");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_get_local_tuple_member");
			}
			
			template.setAttribute("index", index);
			template.setAttribute("tuple_type", tuple_type.tupleTypeIndex);
			template.setAttribute("tuple_id", tuple_id);
			template.setAttribute("id", id);
			return template;
		}

		case DashLexer.ID:
		{
			return getSymbol(t);
		}
		
		case DashLexer.CHARACTER:
		{
			int id = ((DashAST)t).llvmResultID;
			String character = t.getText();
			character = character.replaceAll("'", "");
			
			/*
			 * Bell 	\a
			 * Backspace 	\b
			 * Line Feed 	\n
			 * Carriage Return 	\r
			 * Tab 	\t
			 * Backslash 	\\
			 * Apostrophe 	\'
			 * Quotation Mark 	\"
			 * Null 	\0
			 */
			char val = 0;
			if(character.equals("\\a")) {
				val = 7;
			} else if(character.equals("\\b")) {
				val = '\b';
			} else if(character.equals("\\n")) {
				val = '\n';
			} else if(character.equals("\\r")) {
				val = '\r';
			} else if(character.equals("\\t")) {
				val = '\t';
			} else if(character.equals("\\\\")) {
				val = '\\';
			} else if(character.equals("\\'")) {
				val = '\'';
			} else if(character.equals("\\\"")) {
				val = '\"';
			} else if(character.equals("\\0")) {
				val = '\0';
			} else {
				val = character.charAt(0);
			}
			
			StringTemplate template = stg.getInstanceOf("char_literal");
			template.setAttribute("val", val);
			template.setAttribute("id", id);
			return template;
		}

		case DashLexer.INTEGER:
		{
			int id = ((DashAST)t).llvmResultID;
			int val = Integer.parseInt(t.getText().replaceAll("_", ""));
			
			StringTemplate template = stg.getInstanceOf("int_literal");
			template.setAttribute("val", val);
			template.setAttribute("id", id);
			return template;
		}
		
		case DashLexer.REAL:
		{
			int id = ((DashAST)t).llvmResultID;
			float val = Float.parseFloat(t.getText().replaceAll("_", ""));
			String hex_val = Long.toHexString(Double.doubleToLongBits(val));
			hex_val = "0x" + hex_val.toUpperCase();
			
			StringTemplate template = stg.getInstanceOf("real_literal");
			template.setAttribute("val", hex_val);
			template.setAttribute("id", id);
			return template;
		}
		
		case DashLexer.True:
		{
			int id = ((DashAST)t).llvmResultID;
			
			StringTemplate template = stg.getInstanceOf("bool_literal");
			template.setAttribute("val", 1);
			template.setAttribute("id", id);
			return template;
		}
		
		case DashLexer.False:
		{
			int id = ((DashAST)t).llvmResultID;
			
			StringTemplate template = stg.getInstanceOf("bool_literal");
			template.setAttribute("val", 0);
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
	
	private StringTemplate getSymbol(DashAST t) {
		int id = ((DashAST)t).llvmResultID;
		
		Symbol sym = ((DashAST)t).symbol;
		int sym_id = sym.id;
		Scope scope = sym.scope;
		int type = sym.type.getTypeIndex();
		
		StringTemplate template = null;
		if (scope.getScopeIndex() == SymbolTable.scGLOBAL) {
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_get_global");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_get_global");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_get_global");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_get_global");
			}
		} else {
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_get_local");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_get_local");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_get_local");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_get_local");
			}
		}
		
		template.setAttribute("sym_id", sym_id);
		template.setAttribute("id", id);
		return template;
	}
	
	private StringTemplate getType(Type type) {
		StringTemplate type_template = null;
		if (type.getTypeIndex() == SymbolTable.tINTEGER) {
			type_template = stg.getInstanceOf("int_type");
		} else if (type.getTypeIndex() == SymbolTable.tREAL) {
			type_template = stg.getInstanceOf("real_type");
		} else if (type.getTypeIndex() == SymbolTable.tCHARACTER) {
			type_template = stg.getInstanceOf("char_type");
		} else if (type.getTypeIndex() == SymbolTable.tBOOLEAN) {
			type_template = stg.getInstanceOf("bool_type");
		} else if (type.getTypeIndex() == SymbolTable.tTUPLE) {
			type_template = stg.getInstanceOf("tuple_type");
			type_template.setAttribute("type_id", ((TupleTypeSymbol)type).tupleTypeIndex);
		}  else if (type.getTypeIndex() == SymbolTable.tVOID) {
			type_template = stg.getInstanceOf("void_type");
		}
		
		return type_template;
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
		case AND: {
			if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_and");
			}
			break;
		}
		case OR: {
			if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_or");
			}
			break;
		}
		case XOR: {
			if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_xor");
			}
			break;
		}
		case EQ:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_eq");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_eq");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_eq");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_eq");
			} else if (type == SymbolTable.tTUPLE) {
				template = stg.getInstanceOf("tuple_eq");
			}
			break;
		case NE:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_ne");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_ne");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_ne");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_ne");
			} else if (type == SymbolTable.tTUPLE) {
				template = stg.getInstanceOf("tuple_ne");
			}
			break;
		case LT:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_lt");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_lt");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_lt");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_lt");
			}
			break;
		case LE:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_le");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_le");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_le");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_le");
			}
			break;
		case GT:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_gt");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_gt");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_gt");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_gt");
			}
			break;
		case GE:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_ge");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_ge");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_ge");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_ge");
			}
			break;
		case ADD:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_add");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_add");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_add");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_add");
			}
			break;
		case SUB:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_sub");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_sub");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_sub");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_sub");
			}
			break;
		case MULT:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_mul");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_mul");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_mul");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_mul");
			}
			break;
		case DIV:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_div");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_div");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_div");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_div");
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
