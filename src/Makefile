JAVA=d:\Prolang\j2sdk1.4.1_02\bin\java
JAVAC=javac
JFLEX=jflex
CUP=$(JAVA) java_cup.Main <

Main: Main.java Lexer.java Parser\parser.java Main.class

Main.class: Main.java
	$(JAVAC) $*.java
	$(JAVAC) $Parser/*.java

Lexer.java: Lexer.flex
	$(JFLEX) Lexer.flex

parser.java: parser.cup
	$(CUP) parser.cup

clean:
	del parser.java Lexer.java sym.java *.class *~
