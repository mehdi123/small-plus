JAVA=java
JAVAC=javac
JFLEX=jflex
CUP=$(JAVA) java_cup.Main <

all:  parser Program
	$(JAVAC) ../Parser/parser.java

Program: Program.java
	$(JAVAC) Program.java

parser: parser.cup
	$(CUP) parser.cup
	$(JAVAC) sym.java

clean:
	del parser.java *.class
