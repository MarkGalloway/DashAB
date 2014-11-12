
import java.io.IOException;

import ab.dash.CompileOutput;
import ab.dash.Runner;

public class DashAB_Part1_Test {

	
    public static void main(String[] args) throws IOException, InterruptedException {
    	CompileOutput output = Runner.llvmCompile(args);
    	if (output != null)
    		System.out.println(output.llvm);
    }
}
