package ab.dash;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.20.2.4305 modeling language!*/

import java.util.*;
import java.io.*;
import org.junit.*;

/**
 * @umplesource SampleFileWriter_Code.ump 14
 */
// line 14 "../../../../src/SampleFileWriter_Code.ump"
public class SampleFileWriter
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SampleFileWriter()
  {}

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {}

  // line 23 "../../../../src/SampleFileWriter_Code.ump"
   public static  File[] getAllFiles(File inputDirectory){
    File[] allFiles = inputDirectory.listFiles();
    Arrays.sort(allFiles, new Comparator<File>(){
      public int compare(File f1, File f2)
      {
        return f1.getName().compareTo(f2.getName());
      } 
    });
    return allFiles;
  }

  // line 35 "../../../../src/SampleFileWriter_Code.ump"
   public static  String readContent(File filename){
    BufferedReader reader = null;
    StringBuffer answer = new StringBuffer();
    
    try
    {
      reader = new BufferedReader(new FileReader(filename));
      String nextLine = null;
      while (( nextLine = reader.readLine()) != null)
      {
        answer.append(nextLine);
        answer.append("\n");
      } 
      
    }
    catch (Exception e)
    {
      throw new RuntimeException("Unable to read content",e);
    }
    finally
    {
      closeAsRequired(reader);
    }    
    return answer.toString();
  }

  // line 63 "../../../../src/SampleFileWriter_Code.ump"
   public static  void assertEitherFileContent(File expected1, File expected2, String actual){
    try
    {
      assertFileContent(expected1,actual);
    }
    catch (AssertionError e)
    {
      assertFileContent(expected2,actual);
    }
  }

  // line 75 "../../../../src/SampleFileWriter_Code.ump"
   public static  void assertPartialFileContent(File partial, String actual){
    BufferedReader expectedReader = null;
    BufferedReader actualReader = null;
    boolean foundMatch = false;
    
    try
    {
      expectedReader = new BufferedReader(new FileReader(partial));
      actualReader = new BufferedReader(new StringReader(actual));
      
      String expectedLine = null;
      String actualLine = null;
      
      int line = 0;
      do 
      {
        if (expectedLine == null || foundMatch)
        {
          expectedLine = expectedReader.readLine();
        }
        actualLine = actualReader.readLine();
        // HACK: To deal with // line # comments
        while (actualLine != null && actualLine.indexOf("// line") != -1)
        { //Ignore the line, go to next
          actualLine = actualReader.readLine();
        }
        
        line++;
        
        if (expectedLine == null) 
        {
          break; 
        }
        else if (foundMatch)
        {
          Assert.assertEquals("Failed at:" + line,expectedLine,actualLine);
        }
        else if (expectedLine.equals(actualLine))
        {
          foundMatch = true;
        }
      } 
      while (expectedLine != null);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      closeAsRequired(expectedReader);
      closeAsRequired(actualReader);
    }
    if (!foundMatch) { Assert.fail("Did not find expected lines"); }
  }

  // line 132 "../../../../src/SampleFileWriter_Code.ump"
   public static  void assertFileContent(File expected, String actual){
    assertFileContent(expected, actual, true);
  }

  // line 136 "../../../../src/SampleFileWriter_Code.ump"
   public static  void assertFileContent(File expected, String actual, boolean ignoreLineComments){
    try
    {
      assertFileContent(new BufferedReader(new FileReader(expected)),new BufferedReader(new StringReader(actual)),ignoreLineComments);
    }
    catch (FileNotFoundException e)
    {
      Assert.fail(e.getMessage());
    }
  }

  // line 147 "../../../../src/SampleFileWriter_Code.ump"
   public static  void assertFileContent(File expected, File actual, boolean ignoreLineComments){
    try
    {
      assertFileContent(new BufferedReader(new FileReader(expected)),new BufferedReader(new FileReader(actual)),ignoreLineComments);
    }
    catch (FileNotFoundException e)
    {
      Assert.fail(e.getMessage());
    }
  }

  // line 159 "../../../../src/SampleFileWriter_Code.ump"
   public static  void assertFileContent(BufferedReader expectedReader, BufferedReader actualReader, boolean ignoreLineComments){
    try
    {
      String expectedLine = null;
      String actualLine = null;
      
      int line = 0;
      do 
      {
        actualLine = actualReader.readLine();
        if (ignoreLineComments)
        {
          // HACK: To deal with // line # comments
          while (actualLine != null && actualLine.indexOf("// line") != -1)
          { //Ignore the line, go to next
            actualLine = actualReader.readLine();
          }
        }
        expectedLine = expectedReader.readLine();
        
        line++;
        
        // HACK: To deal with umple version issues
        if (expectedLine != null && expectedLine.indexOf("This code was generated using the UMPLE") == -1)
        {
          Assert.assertEquals("Failed at:" + line,expectedLine,actualLine);  
        }
      } 
      while (expectedLine != null && actualLine != null);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      closeAsRequired(expectedReader);
      closeAsRequired(actualReader);
    }
  }

  // line 201 "../../../../src/SampleFileWriter_Code.ump"
   public static  void closeAsRequired(BufferedReader reader){
    if (reader != null)
    {
      try
      {
        reader.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  // line 216 "../../../../src/SampleFileWriter_Code.ump"
   public static  void closeAsRequired(InputStream reader){
    if (reader != null)
    {
      try
      {
        reader.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  // line 231 "../../../../src/SampleFileWriter_Code.ump"
   public static  String rationalize(String rawPath){
    String path = rawPath;
    File f = new File(path);
    if (f.exists())
    {
      return path;
    }
    
    path = "../cruise.umple/" + rawPath;
    f = new File(path);
    if (f.exists())
    {
      return path;
    }
    
    path = "/h/ralph/umple/trunk/cruise.umple/" + rawPath;
    return path;
  }

  // line 251 "../../../../src/SampleFileWriter_Code.ump"
   public static  void createFile(String filename, String input){
    File f = new File(filename);
    f.getAbsoluteFile().getParentFile().mkdirs();
    
    try
    {
      BufferedWriter out = new BufferedWriter(new FileWriter(filename));
      out.write(input);
      out.close();
    }
    catch (IOException e)
    {
      throw new RuntimeException("Unable to create file [" + filename + "]", e);
    }
  }

  // line 269 "../../../../src/SampleFileWriter_Code.ump"
   public static  void destroy(String filename){
    File file = new File(filename);
    
    if (file.isDirectory())
    {
      destroy(file);
    }
    else
    {
      file.delete();
    }
  }

  // line 283 "../../../../src/SampleFileWriter_Code.ump"
   private static  void destroy(File path){
    if( path.exists() ) {
      File[] files = path.listFiles();
      for(int i=0; i<files.length; i++) {
        if(files[i].isDirectory()) {
          destroy(files[i]);
        }
        else {
          files[i].delete();
        }
      }
    }
    path.delete();
  }

}