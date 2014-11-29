package ab.dash.testing.mike;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.Def;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;


public class TestLLVMCode {

	private static String SlurpFile(String f)
			throws FileNotFoundException,
			IOException
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String l;

		while ((l = br.readLine()) != null)
			sb.append(l + System.getProperty("line.separator"));

		br.close();

		return sb.toString();
	}

	public static void main(String[] args)
			throws RecognitionException,
			FileNotFoundException,
			IOException
	{
        StringBuilder sb;
		sb = new StringBuilder();

		String[] STGFiles = new String[] {
				"StringTemplate/LLVM.stg",
				"StringTemplate/LLVM_Bool.stg",
				"StringTemplate/LLVM_Int.stg"
				};
		
		for (String s : STGFiles)
			sb.append(SlurpFile(s));
		
		StringTemplateGroup stg = new StringTemplateGroup(
				new StringReader(sb.toString()));

		List<StringTemplate> defs = new ArrayList<StringTemplate>();

		defs.add(stg.getInstanceOf("bool_init_global"));
		defs.add(stg.getInstanceOf("int_init_global"));
		defs.get(0).setAttribute("sym_id", "foo");
		defs.get(1).setAttribute("sym_id", "bar");
		
		StringTemplate constant1 = stg.getInstanceOf("int_literal");
		constant1.setAttribute("id", "10");
		constant1.setAttribute("val", "33");

		StringTemplate constant2 = stg.getInstanceOf("int_literal");
		constant2.setAttribute("id", "11");
		constant2.setAttribute("val", "77");

		List<StringTemplate> fbody = new ArrayList<StringTemplate>();

		StringTemplate addition = stg.getInstanceOf("int_add");
		addition.setAttribute("id", "12");
		addition.setAttribute("lhs", constant1);
		addition.setAttribute("lhs_id", constant1.getAttribute("id"));
		addition.setAttribute("rhs", constant2);
		addition.setAttribute("rhs_id", constant2.getAttribute("id"));
		
		StringTemplate printnum = stg.getInstanceOf("int_print");
		printnum.setAttribute("id", "15");
		printnum.setAttribute("expr", addition);
		printnum.setAttribute("expr_id", addition.getAttribute("id"));

		StringTemplate constant3 = stg.getInstanceOf("int_literal");
		constant3.setAttribute("id", "13");
		constant3.setAttribute("val", "0");

		StringTemplate constant4 = stg.getInstanceOf("bool_literal");
		constant4.setAttribute("id", "7953");
		constant4.setAttribute("val", "1");

		fbody.add(printnum);

		fbody.add(stg.getInstanceOf("bool_print"));
		fbody.get(1).setAttribute("id", "5453");
		fbody.get(1).setAttribute("expr", constant4);
		fbody.get(1).setAttribute("expr_id", constant4.getAttribute("id"));

		fbody.add(stg.getInstanceOf("return"));
		fbody.get(2).setAttribute("id", "444");
		fbody.get(2).setAttribute("expr", constant3);
		fbody.get(2).setAttribute("expr_id", constant3.getAttribute("id"));
		fbody.get(2).setAttribute("type", stg.getInstanceOf("int_type"));

		StringTemplate main = stg.getInstanceOf("function_main");
		main.setAttribute("code", fbody);

		StringTemplate prog = stg.getInstanceOf("program");
		prog.setAttribute("type_defs", defs);
		prog.setAttribute("globals", "");
		prog.setAttribute("code", main);

		System.out.println(prog.toString());
	}
}
