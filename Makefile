antlr = /usr/local/lib/antlr-3.3-complete.jar
C_FILES := $(wildcard runtime/*.c)
OBJ_FILES := $(addprefix runtime/,$(notdir $(C_FILES:.c=.o)))

all: runtime
	antlr3 src/*.g
	javac -d ./ src/*.java src/ab/dash/*.java src/ab/dash/ast/*.java src/ab/dash/exceptions/*.java src/ab/dash/opt/*.java

runtime: $(OBJ_FILES)
	ar rcs libruntime.a $^

runtime/%.o: runtime/%.c
	clang -c -o $@ $<

clean_runtime:
	rm -f runtime/*.o
	rm libruntime.a
	
test:
	javac -cp .:$(antlr):ab/dash/:ab/dash/ast/:ab/dash/exeptions/:junit-4.10.jar -d ./ src/ab/dash/testing/*.java
	java -cp .:$(antlr):junit-4.10.jar ab/dash/testing/FullTest

clean:
	rm -f runtime/*.o
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
	
