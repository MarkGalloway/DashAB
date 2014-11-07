antlr = /opt/antlr-3.2/lib/antlr-3.2.jar

all:
	clang -c runtime.c -o runtime.o
	ar rcs libruntime.a runtime.o

	antlr3 src/*.g
	javac -d ./ src/*.java src/ab/dash/*.java src/ab/dash/ast/*.java src/ab/dash/exceptions/*.java

runtime:
	clang -c runtime.c -o runtime.o
	ar rcs libruntime.a runtime.o
	
test:
	javac -cp .:$(antlr):ab/dash/:ab/dash/ast/:ab/dash/exeptions/:junit-4.10.jar -d ./ src/ab/dash/testing/*.java
	java -cp .:$(antlr):junit-4.10.jar ab/dash/testing/FullTest

clean:
	rm runtime.o
	rm libruntime.a

	rm -f *.class
	rm -f *.tokens

	rm -fr ab/
	rm -fr bin/
	rm -fr antlr-generated/
	rm -fr ASTOutput/
	rm -fr LLVMIROutput/

	rm src/DashLexer.java
	rm src/DashParser.java
	rm src/Def.java
	rm src/Types.java
	rm src/ConstantFolding.java
	rm src/Ref.java
	rm src/SimplifyExpressions.java
	
