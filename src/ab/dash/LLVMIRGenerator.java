package ab.dash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	    AND, OR, XOR, ADD, SUB, MULT, DIV, MOD, POWER, EQ, NE, LT, LE, GT, GE
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
							TupleTypeSymbol tuple = (TupleTypeSymbol)s.type;
							
							String init = "";
							
							for (int i = 0; i < tuple.fields.size(); i++) {
								VariableSymbol member = (VariableSymbol) tuple.fields.get(i);
								int member_type = member.type.getTypeIndex();
								
								if (member_type == SymbolTable.tBOOLEAN)
									init += "i1 0";
								else if (member_type == SymbolTable.tCHARACTER)
									init += "i8 0";
								else if (member_type == SymbolTable.tINTEGER)
									init += "i32 0";
								else if (member_type == SymbolTable.tREAL)
									init += "float 0.0";
								
								if (i < tuple.fields.size() - 1)
									init += ", ";
							}
							
							template.setAttribute("init", init);
							template.setAttribute("type_id", tuple.tupleTypeIndex);
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

			List<StringTemplate> args = new ArrayList<StringTemplate>();
			for (int i = 1; i < t.getChildCount() - 1; i++) {
				DashAST argument_node = ((DashAST) t.getChild(i).getChild(0));
				VariableSymbol arg_var = (VariableSymbol)argument_node.symbol;
				Type arg_type = arg_var.type;
				StringTemplate type_template = getType(arg_type);

				String argTemplate = "declare_argument";
				StringTemplate arg = stg.getInstanceOf(argTemplate);
				arg.setAttribute("arg_id", arg_var.id);
				arg.setAttribute("arg_type", type_template);
				arg.setAttribute("id", argument_node.llvmResultID);

				args.add(arg);
			}

			StringTemplate code = exec((DashAST)t.getChild(t.getChildCount() - 1));
			String code_s = code.toString();
			if (type.getTypeIndex() == SymbolTable.tVOID) {
				code_s += "\nret void\n";
			}
			
			StringTemplate template = null;
			if (type.getTypeIndex() == SymbolTable.tTUPLE) {
				template = stg.getInstanceOf("function_returning_tuple");
				template.setAttribute("type_id", ((TupleTypeSymbol)type).tupleTypeIndex);
			}
			else {
				template = stg.getInstanceOf("function");
				template.setAttribute("return_type", getType(type));
			}
			template.setAttribute("code", code_s);
			template.setAttribute("args", args);
			template.setAttribute("sym_id", sym_id);
			template.setAttribute("id", t.llvmResultID);
			return template;
		}
		
		case DashLexer.CALL:
		{
			DashAST method_node = (DashAST) t.getChild(0);
			MethodSymbol method = (MethodSymbol)method_node.symbol;
			Type method_type = method.type;

			// Arguments
			List<StringTemplate> code = new ArrayList<StringTemplate>();
			List<StringTemplate> args = new ArrayList<StringTemplate>();
			DashAST argument_list = (DashAST) t.getChild(1);
			StringTemplate stackSave = null;

			for (int i = 0; i < argument_list.getChildCount(); i++) {
				DashAST arg = (DashAST)argument_list.getChild(i);
				DashAST arg_child = (DashAST)arg.getChild(0);
				Type arg_type = arg.evalType;
				
				if (((DashAST)arg.getChild(0)).promoteToType != null) {
					arg_type = ((DashAST)arg.getChild(0)).promoteToType;
				}
				
				StringTemplate llvm_arg_type = getType(arg_type);

				StringTemplate arg_template = null;
				if (arg_child.symbol instanceof VariableSymbol) {
					VariableSymbol var_sym = (VariableSymbol)arg_child.symbol;
					arg_template = stg.getInstanceOf("pass_variable_by_reference");
					arg_template.setAttribute("var_id", var_sym.id);
					arg_template.setAttribute("arg_type", llvm_arg_type);
				} else if (arg_type.getTypeIndex() == SymbolTable.tTUPLE) {
					code.add(exec(arg));

					arg_template = stg.getInstanceOf("pass_tuple_expr_by_reference");
					arg_template.setAttribute("tuple_expr_id", arg_child.llvmResultID);
					arg_template.setAttribute("type_id", ((TupleTypeSymbol)arg_type).tupleTypeIndex);
				} else {
					if (stackSave == null) {
						stackSave = stg.getInstanceOf("save_stack");
						stackSave.setAttribute("call_id", method.id);
						code.add(stackSave);
					}

					code.add(exec(arg));

					StringTemplate toStack = stg.getInstanceOf("expr_result_to_stack");
					toStack.setAttribute("expr_id", arg_child.llvmResultID);
					toStack.setAttribute("expr_type", llvm_arg_type);
					code.add(toStack);

					arg_template = stg.getInstanceOf("pass_expr_by_reference");
					arg_template.setAttribute("arg_expr_id", arg_child.llvmResultID);
					arg_template.setAttribute("arg_type", llvm_arg_type);
				}

				arg_template.setAttribute("id", arg_child.llvmResultID);

				args.add(arg_template);
			}

			StringTemplate template = null;
			if (method_type.getTypeIndex() == SymbolTable.tVOID) {
				template = stg.getInstanceOf("call_void");
			}
			else if (method_type.getTypeIndex() == SymbolTable.tTUPLE) {
				template = stg.getInstanceOf("call_returning_tuple");
				template.setAttribute("type_id", ((TupleTypeSymbol)method_type).tupleTypeIndex);
			} else {
				template = stg.getInstanceOf("call");
				template.setAttribute("return_type", getType(method_type));
			}

			if (stackSave != null) {
				StringTemplate stackRestore = stg.getInstanceOf("restore_stack");
				stackRestore.setAttribute("call_id", method.id);
				template.setAttribute("postcode", stackRestore);
			}

			template.setAttribute("code", code);
			template.setAttribute("args", args);
			template.setAttribute("function_id", method.id);
			template.setAttribute("id", t.llvmResultID);
			return template;
		}
		
		case DashLexer.Return:
		{
			int id = t.llvmResultID;
			
			DashAST expr = (DashAST)t.getChild(0);
			int expr_id = ((DashAST)t.getChild(0).getChild(0)).llvmResultID;

			StringTemplate expr_template = exec(expr);
			StringTemplate template = null;

			if (expr.evalType.getTypeIndex() == SymbolTable.tTUPLE) {
				TupleTypeSymbol tuple_type = (TupleTypeSymbol)expr.evalType;
				template = stg.getInstanceOf("return_tuple");
				template.setAttribute("id", id);
				template.setAttribute("type_id", tuple_type.tupleTypeIndex);

				StringTemplate assignmentTemplate = stg.getInstanceOf("tuple_assign");

				List<StringTemplate> element_assigns = new ArrayList<StringTemplate>();

				List<Symbol> fields = tuple_type.fields;
				for (int i = 0; i <	fields.size(); i++) {
					StringTemplate memberAssign = null;
					StringTemplate getMember = null;

					int field_type = fields.get(i).type.getTypeIndex();
					if (field_type == SymbolTable.tINTEGER) {
						getMember = stg.getInstanceOf("int_get_tuple_member");
						memberAssign = stg.getInstanceOf("int_tuple_assign");
					}
					else if (field_type == SymbolTable.tREAL) {
						getMember = stg.getInstanceOf("real_get_tuple_member");
						memberAssign = stg.getInstanceOf("real_tuple_assign");
					}
					else if (field_type == SymbolTable.tCHARACTER) {
						getMember = stg.getInstanceOf("char_get_tuple_member");
						memberAssign = stg.getInstanceOf("char_tuple_assign");
					}
					else if (field_type == SymbolTable.tBOOLEAN) {
						getMember = stg.getInstanceOf("bool_get_tuple_member");
						memberAssign = stg.getInstanceOf("bool_tuple_assign");
					}

					getMember.setAttribute("id", DashAST.getUniqueId());
					getMember.setAttribute("tuple_expr_id", expr_id);
					getMember.setAttribute("tuple_type", tuple_type.tupleTypeIndex);
					getMember.setAttribute("index", i);

					memberAssign.setAttribute("id", DashAST.getUniqueId());
					memberAssign.setAttribute("tuple_expr_id", template.getAttribute("id"));
					memberAssign.setAttribute("tuple_type", tuple_type.tupleTypeIndex);
					memberAssign.setAttribute("index", i);
					memberAssign.setAttribute("expr", getMember);
					memberAssign.setAttribute("expr_id", getMember.getAttribute("id"));

					element_assigns.add(memberAssign);
				}

				assignmentTemplate.setAttribute("rhs_expr", expr_template);
				assignmentTemplate.setAttribute("element_assigns", element_assigns);

				template.setAttribute("assign_code", assignmentTemplate);
			}
			else {
				template = stg.getInstanceOf("return");
				StringTemplate type_template = getType(((DashAST) t.getChild(0)).evalType);

				template.setAttribute("expr_id", expr_id);
				template.setAttribute("expr", expr_template);
				template.setAttribute("type", type_template);
				template.setAttribute("id", id);
			}
			return template;
		}
		
		case DashLexer.TUPLE_LIST:
		{
			TupleTypeSymbol tuple = (TupleTypeSymbol)t.evalType;

			StringTemplate template = stg.getInstanceOf("tuple_init_literal");
			template.setAttribute("id", t.llvmResultID);
			template.setAttribute("type_id", tuple.tupleTypeIndex);

			List<StringTemplate> element_exprs = new ArrayList<StringTemplate>();

			for (int i = 0; i < t.getChildCount(); i++) {
				DashAST element_node = (DashAST)t.getChild(i);
				int element_expr_id = ((DashAST)element_node.getChild(0)).llvmResultID;

				StringTemplate memberAssign = null;

				int type = element_node.evalType.getTypeIndex();
				if (type == SymbolTable.tINTEGER)
					memberAssign = stg.getInstanceOf("int_tuple_assign");
				else if (type == SymbolTable.tREAL)
					memberAssign = stg.getInstanceOf("real_tuple_assign");
				else if (type == SymbolTable.tCHARACTER)
					memberAssign = stg.getInstanceOf("char_tuple_assign");
				else if (type == SymbolTable.tBOOLEAN)
					memberAssign = stg.getInstanceOf("bool_tuple_assign");

				memberAssign.setAttribute("id", DashAST.getUniqueId());
				memberAssign.setAttribute("tuple_expr_id", t.llvmResultID);
				memberAssign.setAttribute("tuple_type", tuple.tupleTypeIndex);
				memberAssign.setAttribute("index", i);
				memberAssign.setAttribute("expr", exec(element_node));
				memberAssign.setAttribute("expr_id", element_expr_id);

				element_exprs.add(memberAssign);
			}
			
			template.setAttribute("element_exprs", element_exprs);

			return template;
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
		
		case DashLexer.INPUT:
		{
			int id = ((DashAST)t).llvmResultID;
			DashAST node = (DashAST)t.getChild(0).getChild(0);
			
			if (node.getType() == DashLexer.DOT) {
				DashAST tupleNode = (DashAST) node.getChild(0);
				DashAST memberNode = (DashAST) node.getChild(1);
				
				VariableSymbol tuple = (VariableSymbol) tupleNode.symbol;
				TupleTypeSymbol tuple_type = (TupleTypeSymbol)tuple.type;
				int index = tuple_type.getMemberIndex(memberNode.getText());
				
				
				int type = node.evalType.getTypeIndex();
				
				StringTemplate template = null;
				if (type == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("int_input_tuple");
				}  else if (type == SymbolTable.tREAL) {
					template = stg.getInstanceOf("real_input_tuple");
				} else if (type == SymbolTable.tCHARACTER) {
					template = stg.getInstanceOf("char_input_tuple");
				} else if (type == SymbolTable.tBOOLEAN) {
					template = stg.getInstanceOf("bool_input_tuple");
				}
				
				template.setAttribute("index", index);
				template.setAttribute("tuple_type", tuple_type.tupleTypeIndex);
				template.setAttribute("tuple_id", tuple.id);
				template.setAttribute("id", id);
				return template;
			}
			
			Symbol sym = node.symbol;
			int sym_id = sym.id;
			
			StringTemplate template = null;
			if (sym.type.getTypeIndex() == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_input");
			} else if (sym.type.getTypeIndex() == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_input");
			} else if (sym.type.getTypeIndex() == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_input");
			} else if (sym.type.getTypeIndex() == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_input");
			}
			
			template.setAttribute("sym_id", sym_id);
			template.setAttribute("id", id);

			return template;
		}
		
		case DashLexer.UNPACK:
		{
			int id = ((DashAST)t).llvmResultID;
			int last_child_index = t.getChildCount() - 1;
			TupleTypeSymbol tuple = (TupleTypeSymbol) ((DashAST)t.getChild(last_child_index)).evalType;
			
			StringTemplate tuple_expr = exec((DashAST)t.getChild(last_child_index));
			int tuple_expr_id = ((DashAST)t.getChild(last_child_index).getChild(0)).llvmResultID;
			
			List<StringTemplate> element_assigns = new ArrayList<StringTemplate>();
			
			for (int i = 0; i < tuple.fields.size(); i++) {
				VariableSymbol member = (VariableSymbol) tuple.fields.get(i);
				VariableSymbol sym = (VariableSymbol) ((DashAST)t.getChild(i).getChild(0)).symbol;
				
				StringTemplate memberAssign = null;
				StringTemplate getMember = null;

				int field_type = member.type.getTypeIndex();
				if (field_type == SymbolTable.tINTEGER) {
					getMember = stg.getInstanceOf("int_get_tuple_member");
					memberAssign = stg.getInstanceOf("int_local_assign");
				}
				else if (field_type == SymbolTable.tREAL) {
					getMember = stg.getInstanceOf("real_get_tuple_member");
					memberAssign = stg.getInstanceOf("real_local_assign");
				}
				else if (field_type == SymbolTable.tCHARACTER) {
					getMember = stg.getInstanceOf("char_get_tuple_member");
					memberAssign = stg.getInstanceOf("char_local_assign");
				}
				else if (field_type == SymbolTable.tBOOLEAN) {
					getMember = stg.getInstanceOf("bool_get_tuple_member");
					memberAssign = stg.getInstanceOf("bool_local_assign");
				}
				
				int uid = DashAST.getUniqueId();
				getMember.setAttribute("id", uid);
				getMember.setAttribute("tuple_expr_id", tuple_expr_id);
				getMember.setAttribute("tuple_type", tuple.tupleTypeIndex);
				getMember.setAttribute("index", i);
				
				memberAssign.setAttribute("expr_id", uid);
				memberAssign.setAttribute("expr", getMember);
				memberAssign.setAttribute("sym_id", sym.id);
				memberAssign.setAttribute("id", DashAST.getUniqueId());
				
				element_assigns.add(memberAssign);
			}
			
			StringTemplate template = stg.getInstanceOf("tuple_unpack");;
			template.setAttribute("tuple_expr", tuple_expr);
			template.setAttribute("element_assigns", element_assigns);
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
				StringTemplate expr = exec(child);
				int expr_id = ((DashAST)child.getChild(0)).llvmResultID;
				
				StringTemplate template = null;
				if (type == SymbolTable.tTUPLE) {
					TupleTypeSymbol tuple_type = (TupleTypeSymbol) sym.type;
					template = stg.getInstanceOf("tuple_assign");
                    List<StringTemplate> element_assigns = new ArrayList<StringTemplate>();
                    
                    StringTemplate getLocalTuple = null;
                    if (scope.getScopeIndex() == SymbolTable.scGLOBAL) {
                    	getLocalTuple = stg.getInstanceOf("tuple_get_global");
                    } else {
                    	getLocalTuple = stg.getInstanceOf("tuple_get_local");
                    }
                    
                    getLocalTuple.setAttribute("id", DashAST.getUniqueId());
                    getLocalTuple.setAttribute("type_id", tuple_type.tupleTypeIndex);
                    getLocalTuple.setAttribute("sym_id", sym.id);

                    List<Symbol> fields = tuple_type.fields;
					for (int i = 0; i <	fields.size(); i++) {
						StringTemplate memberAssign = null;
						StringTemplate getMember = null;

						int field_type = fields.get(i).type.getTypeIndex();
						if (field_type == SymbolTable.tINTEGER) {
							getMember = stg.getInstanceOf("int_get_tuple_member");
							memberAssign = stg.getInstanceOf("int_tuple_assign");
						}
						else if (field_type == SymbolTable.tREAL) {
							getMember = stg.getInstanceOf("real_get_tuple_member");
							memberAssign = stg.getInstanceOf("real_tuple_assign");
						}
						else if (field_type == SymbolTable.tCHARACTER) {
							getMember = stg.getInstanceOf("char_get_tuple_member");
							memberAssign = stg.getInstanceOf("char_tuple_assign");
						}
						else if (field_type == SymbolTable.tBOOLEAN) {
							getMember = stg.getInstanceOf("bool_get_tuple_member");
							memberAssign = stg.getInstanceOf("bool_tuple_assign");
						}

						getMember.setAttribute("id", DashAST.getUniqueId());
						getMember.setAttribute("tuple_expr_id", expr_id);
						getMember.setAttribute("tuple_type", tuple_type.tupleTypeIndex);
						getMember.setAttribute("index", i);

						memberAssign.setAttribute("id", DashAST.getUniqueId());
						memberAssign.setAttribute("tuple_expr_id", getLocalTuple.getAttribute("id"));
						memberAssign.setAttribute("tuple_type", tuple_type.tupleTypeIndex);
						memberAssign.setAttribute("index", i);
						memberAssign.setAttribute("expr", getMember);
						memberAssign.setAttribute("expr_id", getMember.getAttribute("id"));

						element_assigns.add(memberAssign);
					}
					
					template.setAttribute("lhs_expr", getLocalTuple);
					template.setAttribute("rhs_expr", expr);
					template.setAttribute("element_assigns", element_assigns);
					return template;
				}
				
				if (scope.getScopeIndex() == SymbolTable.scGLOBAL) {
					if (type == SymbolTable.tINTEGER) {
						template = stg.getInstanceOf("int_global_assign");
					} else if (type == SymbolTable.tREAL) {
						template = stg.getInstanceOf("real_global_assign");
					} else if (type == SymbolTable.tCHARACTER) {
						template = stg.getInstanceOf("char_global_assign");
					} else if (type == SymbolTable.tBOOLEAN) {
						template = stg.getInstanceOf("bool_global_assign");
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
					}
				}
				
				template.setAttribute("expr_id", expr_id);
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
					template = stg.getInstanceOf("int_tuple_assign");
				}  else if (type == SymbolTable.tREAL) {
					template = stg.getInstanceOf("real_tuple_assign");
				} else if (type == SymbolTable.tCHARACTER) {
					template = stg.getInstanceOf("char_tuple_assign");
				} else if (type == SymbolTable.tBOOLEAN) {
					template = stg.getInstanceOf("bool_tuple_assign");
				}
				
				StringTemplate getLocalTuple = stg.getInstanceOf("tuple_get_local");
				getLocalTuple.setAttribute("id", DashAST.getUniqueId());
				getLocalTuple.setAttribute("sym_id", tuple.id);
				getLocalTuple.setAttribute("type_id", tuple_type.tupleTypeIndex);
				
				template.setAttribute("expr_id", arg_id);
				template.setAttribute("expr", expr);
				template.setAttribute("index", index);
				template.setAttribute("tuple_type", tuple_type.tupleTypeIndex);
				template.setAttribute("tuple_expr", getLocalTuple);
				template.setAttribute("tuple_expr_id", getLocalTuple.getAttribute("id"));
				template.setAttribute("id", id);
				return template;
			}
			
			Symbol sym = node.symbol;
			int sym_id = sym.id;
			
			int type = ((DashAST)t.getChild(1)).evalType.getTypeIndex();
			
			StringTemplate expr = exec((DashAST)t.getChild(1));
			int arg_id = ((DashAST)t.getChild(1).getChild(0)).llvmResultID;
			
			StringTemplate template = null;
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
		{
			Type type = ((DashAST)t.getChild(0)).evalType;
			
			if (type.getTypeIndex() == SymbolTable.tTUPLE) {
				return tupleOperation(t, LLVMOps.EQ);
			}
			
			return operation(t, LLVMOps.EQ);
		}
			

		case DashLexer.INEQUALITY:
		{
			Type type = ((DashAST)t.getChild(0)).evalType;
			
			if (type.getTypeIndex() == SymbolTable.tTUPLE) {
				return tupleOperation(t, LLVMOps.NE);
			}
			
			return operation(t, LLVMOps.NE);
		}

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
			
		case DashLexer.MODULAR:
			return operation(t, LLVMOps.MOD);
			
		case DashLexer.POWER:
			return operation(t, LLVMOps.POWER);
			
		case DashLexer.DOT:
		{
			int id = ((DashAST)t).llvmResultID;
			DashAST tupleNode = (DashAST) t.getChild(0);
			DashAST memberNode = (DashAST) t.getChild(1);
			
			StringTemplate tuple_expr = exec(tupleNode);
			int tuple_expr_id = tupleNode.llvmResultID;
			
			VariableSymbol tuple = (VariableSymbol) tupleNode.symbol;
			TupleTypeSymbol tuple_type = null;
			if (tuple != null) {
				tuple_type = (TupleTypeSymbol)tuple.type;
			} else {
				tuple_type = (TupleTypeSymbol)tupleNode.evalType;
			}
			
			int index = tuple_type.getMemberIndex(memberNode.getText());
			
			int type = t.evalType.getTypeIndex();
			
			StringTemplate template = null;
			if (type == SymbolTable.tINTEGER) {
				//*_get_tuple_member(id, tuple_expr, tuple_expr_id, tuple_type, index)
				template = stg.getInstanceOf("int_get_tuple_member");
			}  else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_get_tuple_member");
			} else if (type == SymbolTable.tCHARACTER) {
				template = stg.getInstanceOf("char_get_tuple_member");
			} else if (type == SymbolTable.tBOOLEAN) {
				template = stg.getInstanceOf("bool_get_tuple_member");
			}
			
			template.setAttribute("index", index);
			template.setAttribute("tuple_type", tuple_type.tupleTypeIndex);
			template.setAttribute("tuple_expr_id", tuple_expr_id);
			template.setAttribute("tuple_expr", tuple_expr);
			template.setAttribute("id", id);
			return template;
		}
		
		case DashLexer.TYPECAST:
		{
			int id = ((DashAST)t).llvmResultID;
			DashAST child = (DashAST)t.getChild(0);
			
			Type castTo = t.evalType;
			Type castFrom = child.evalType;
			
			StringTemplate expr = exec(child);
			int expr_id = ((DashAST)child.getChild(0)).llvmResultID;
			
			StringTemplate template = null;
			if (castFrom.getTypeIndex() == SymbolTable.tBOOLEAN) {
				if (castTo.getTypeIndex() == SymbolTable.tBOOLEAN) {
					template = stg.getInstanceOf("bool_to_bool");
				} else if (castTo.getTypeIndex() == SymbolTable.tCHARACTER) {
					template = stg.getInstanceOf("bool_to_char");
				} else if (castTo.getTypeIndex() == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("bool_to_int");
				} else if (castTo.getTypeIndex() == SymbolTable.tREAL) {
					template = stg.getInstanceOf("bool_to_real");
				}
			} else if (castFrom.getTypeIndex() == SymbolTable.tCHARACTER) {
				if (castTo.getTypeIndex() == SymbolTable.tBOOLEAN) {
					template = stg.getInstanceOf("char_to_bool");
				} else if (castTo.getTypeIndex() == SymbolTable.tCHARACTER) {
					template = stg.getInstanceOf("char_to_char");
				} else if (castTo.getTypeIndex() == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("char_to_int");
				} else if (castTo.getTypeIndex() == SymbolTable.tREAL) {
					template = stg.getInstanceOf("char_to_real");
				}
			} else if (castFrom.getTypeIndex() == SymbolTable.tINTEGER) {
				if (castTo.getTypeIndex() == SymbolTable.tBOOLEAN) {
					template = stg.getInstanceOf("int_to_bool");
				} else if (castTo.getTypeIndex() == SymbolTable.tCHARACTER) {
					template = stg.getInstanceOf("int_to_char");
				} else if (castTo.getTypeIndex() == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("int_to_int");
				} else if (castTo.getTypeIndex() == SymbolTable.tREAL) {
					template = stg.getInstanceOf("int_to_real");
				}
			} else if (castFrom.getTypeIndex() == SymbolTable.tREAL) {
				if (castTo.getTypeIndex() == SymbolTable.tINTEGER) {
					template = stg.getInstanceOf("real_to_int");
				} else if (castTo.getTypeIndex() == SymbolTable.tREAL) {
					template = stg.getInstanceOf("real_to_real");
				}
			}
			
			template.setAttribute("expr_id", expr_id);
			template.setAttribute("expr", expr);
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
			int id = ((DashAST) t).llvmResultID;
			StringTemplate template = null;

			int val = Integer.parseInt(t.getText().replaceAll("_", ""));

			template = stg.getInstanceOf("int_literal");
			template.setAttribute("val", val);

			if (t.promoteToType != null && t.promoteToType.getTypeIndex() == SymbolTable.tREAL) {
				template.setAttribute("id", id + "_temp");
				
				StringTemplate promote = stg.getInstanceOf("int_to_real");
				promote.setAttribute("expr_id", id + "_temp");
				promote.setAttribute("expr", template);
				promote.setAttribute("id", id);
				
				return promote;
			} else {
				template.setAttribute("id", id);
			}
			
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
	
	private StringTemplate tupleOperation(DashAST t, LLVMOps op) {
		String id = Integer.toString(((DashAST)t).llvmResultID);
		Type type = ((DashAST)t.getChild(0)).evalType;
		
		StringTemplate lhs = exec((DashAST)t.getChild(0));
		String lhs_id = Integer.toString(((DashAST)t.getChild(0)).llvmResultID);
		
		StringTemplate rhs = exec((DashAST)t.getChild(1));
		String rhs_id = Integer.toString(((DashAST)t.getChild(1)).llvmResultID);
		
		String code = "";
		
		StringTemplate template_init = null;
		template_init = stg.getInstanceOf("tuple_cmp_init");
		template_init.setAttribute("lhs_expr", lhs);
		template_init.setAttribute("rhs_expr", rhs);
		template_init.setAttribute("id", id);
		
		code += template_init.toString() + "\n";
		
		TupleTypeSymbol tuple = (TupleTypeSymbol) type;
		
		for (int i = 0; i < tuple.fields.size(); i++) {
			VariableSymbol s = (VariableSymbol) tuple.fields.get(i);
			int type_index = s.type.getTypeIndex();
			
			StringTemplate template_cmp = null;
			if (type_index == SymbolTable.tBOOLEAN)
				template_cmp = stg.getInstanceOf("bool_tuple_eq_member");
			else if (type_index == SymbolTable.tCHARACTER)
				template_cmp = stg.getInstanceOf("char_tuple_eq_member");
			else if (type_index == SymbolTable.tINTEGER)
				template_cmp = stg.getInstanceOf("int_tuple_eq_member");
			else if (type_index == SymbolTable.tREAL)
				template_cmp = stg.getInstanceOf("real_tuple_eq_member");
			
			template_cmp.setAttribute("lhs_expr_id", lhs_id);
			template_cmp.setAttribute("rhs_expr_id", rhs_id);
			template_cmp.setAttribute("index", i);
			template_cmp.setAttribute("tuple_type", tuple.tupleTypeIndex);
			template_cmp.setAttribute("id", id);
			
			code += template_cmp.toString() + "\n";
		}
		
		StringTemplate template_result = null;
		if (op == LLVMOps.EQ) {
			template_result = stg.getInstanceOf("tuple_cmp_eq_result");
		} else if (op == LLVMOps.NE) {
			template_result = stg.getInstanceOf("tuple_cmp_ne_result");
		}
		
		template_result.setAttribute("id", id);
		
		code += template_result.toString() + "\n";
		
		return new StringTemplate(code);
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
			} else if (type == SymbolTable.tTUPLE) {
				template = stg.getInstanceOf("tuple_get_global");
				template.setAttribute("type_id", ((TupleTypeSymbol)sym.type).tupleTypeIndex);
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
			} else if (type == SymbolTable.tTUPLE) {
				template = stg.getInstanceOf("tuple_get_local");
				template.setAttribute("type_id", ((TupleTypeSymbol)sym.type).tupleTypeIndex);
			}
		}
		template.setAttribute("sym_id", sym_id);
		
		if (t.promoteToType != null && t.promoteToType.getTypeIndex() == SymbolTable.tREAL) {
			template.setAttribute("id", id + "_temp");
			
			StringTemplate promote = stg.getInstanceOf("int_to_real");
			promote.setAttribute("expr_id", id + "_temp");
			promote.setAttribute("expr", template);
			promote.setAttribute("id", id);
			
			return promote;
		} else {
			template.setAttribute("id", id);
		}
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
		
		if (((DashAST)t.getChild(0)).promoteToType != null) {
			if (((DashAST)t.getChild(0)).promoteToType.getTypeIndex() == SymbolTable.tREAL) {
				type = ((DashAST)t.getChild(0)).promoteToType.getTypeIndex();
			}
		}
		
		StringTemplate lhs = exec((DashAST)t.getChild(0));
		String lhs_id = Integer.toString(((DashAST)t.getChild(0)).llvmResultID);
		
		StringTemplate rhs = exec((DashAST)t.getChild(1));
		String rhs_id = Integer.toString(((DashAST)t.getChild(1)).llvmResultID);
		
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
			}
			break;
		case SUB:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_sub");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_sub");
			}
			break;
		case MULT:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_mul");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_mul");
			}
			break;
		case DIV:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_div");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_div");
			}
			break;
		case MOD:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_mod");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_mod");
			}
			break;
		case POWER:
			if (type == SymbolTable.tINTEGER) {
				template = stg.getInstanceOf("int_pow");
			} else if (type == SymbolTable.tREAL) {
				template = stg.getInstanceOf("real_pow");
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
