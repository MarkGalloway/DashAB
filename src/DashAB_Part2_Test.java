
import java.io.IOException;

import ab.dash.CompileOutput;
import ab.dash.Runner;

public class DashAB_Part2_Test {

	
    public static void main(String[] args) throws IOException, InterruptedException {
    	CompileOutput output = Runner.llvmCompile(args);
    	if (output != null)
    		System.out.println(output.llvm);
    	else
    		System.err.println("Build Failed");
    }
}
