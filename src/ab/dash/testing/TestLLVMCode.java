package ab.dash.testing;

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
			sb.append(l + System.lineSeparator());

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

		defs.add(stg.getInstanceOf("bool_global_variable"));
		defs.add(stg.getInstanceOf("int_global_variable"));
		defs.get(0).setAttribute("id", "foo");
		defs.get(1).setAttribute("id", "bar");
		
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

		fbody.add(stg.getInstanceOf("return"));
		fbody.get(0).setAttribute("id", "2");
		fbody.get(0).setAttribute("expr", addition);
		fbody.get(0).setAttribute("expr_id", addition.getAttribute("id"));
		fbody.get(0).setAttribute("type", stg.getInstanceOf("int_type"));

		StringTemplate main = stg.getInstanceOf("function_main");
		main.setAttribute("code", fbody);

		StringTemplate prog = stg.getInstanceOf("program");
		prog.setAttribute("type_defs", defs);
		prog.setAttribute("globals", "");
		prog.setAttribute("functions", main);

		System.out.println(prog.toString());
	}
}
