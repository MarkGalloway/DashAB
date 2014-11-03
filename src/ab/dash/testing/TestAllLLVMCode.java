package ab.dash.testing;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.antlr.runtime.RecognitionException;

public class TestAllLLVMCode {
	public static void showFiles(File[] files) throws RecognitionException {
		Arrays.sort(files, new Comparator<File>() {
		    public int compare(File f1, File f2) {
		        return f1.getName().compareTo(f2.getName());
		    }
		});
		
		for (File file : files) {
			if (file.isDirectory()) {
				System.out.println("Directory: " + file.getName());
				showFiles(file.listFiles()); // Calls same method again.
			} else {
				int i = file.getName().lastIndexOf('.');
	        	if (i > 0) {
	        	    String extension = file.getName().substring(i+1);
	        	    
	        	    if (extension.equals("ds")) {
		        	    System.out.println("File: " + file.getName());
			            String out = TestLLVMCodeGen.parseFile(file.getName(), file.getPath());
			            System.out.println("Out:");
			            System.out.println(out);
	        	    }
	        	}
			}
		}
	}

	public static void main(String[] args) throws RecognitionException {
//		parseFile("simpleMain.db", "TestPrograms/05SimpleMain/simpleMain.db");
//		parseFile("variableDeclarationInMain.db", "TestPrograms/07VariableDeclarationInMain/variableDeclarationInMain.db");
		File[] files = new File("TestPrograms/").listFiles();
		showFiles(files);

		File[] invalid_files = new File("TestInvalidTypePrograms/").listFiles();
		showFiles(invalid_files);
	}
}
