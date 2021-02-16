/* --------------------------Usercode Section------------------------ */
   
import java_cup.runtime.*;
import Parser.*;
      
%%
   
/* -----------------Options and Declarations Section----------------- */
   
%class Lexer

%line
%column
    
%cup
   
/*
  Declarations
   
  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.  
*/
%{   
    /* To create a new java_cup.runtime.Symbol with information about
       the current token, the token will have no value in this
       case. */
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    /* Also creates a new java_cup.runtime.Symbol with information
       about the current token, but this object has a value. */
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
   

/*
  Macro Declarations
  
  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.  
*/
   
/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n
   
/* White space is a line terminator, space, tab, or line feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]

Comment = {TraditionalComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"

/* A literal integer is is a number beginning with a number between
   one and nine followed by zero or more numbers between zero and nine
   or just a zero.  */

DecIntegerLiteral = 0 | [1-9][0-9]*   

/* A identifier integer is a word beginning a letter between A and
   Z, a and z, or an underscore followed by zero or more letters
   between A and Z, a and z, zero and nine, or an underscore. */

Identifier = [:jletter:][:jletterdigit:]*

%%
<YYINITIAL> {
   
   
    /* Print the token found that was declared in the class sym and then
       return it. */
    "+"                { return symbol(Parser.sym.PLUS); }
    "-"                { return symbol(Parser.sym.MINUS); }
    "*"                { return symbol(Parser.sym.TIMES); }
    "/"                { return symbol(Parser.sym.DIVIDE); }
    "("                { return symbol(Parser.sym.LPAREN); }
    ")"                { return symbol(Parser.sym.RPAREN); }
    "="                            { return symbol(Parser.sym.EQ); }
  ">"                            { return symbol(Parser.sym.GT); }
  "<"                            { return symbol(Parser.sym.LT); }
  "=="                           { return symbol(Parser.sym.EQEQ); }
  "<="                           { return symbol(Parser.sym.LTEQ); }
  ">="                           { return symbol(Parser.sym.GTEQ); }
  "!="                           { return symbol(Parser.sym.NOTEQ); }
  "{"                            { return symbol(Parser.sym.LBRACE); }
  "}"                            { return symbol(Parser.sym.RBRACE); }
  "["                            { return symbol(Parser.sym.LBRACK); }
  "]"                            { return symbol(Parser.sym.RBRACK); }
  ";"                            { return symbol(Parser.sym.SEMI); }
  ","                            { return symbol(Parser.sym.COMMA); }
  "else"                         { return symbol(Parser.sym.ELSE); }
  "int"                          { return symbol(Parser.sym.INT); }
  "if"                           { return symbol(Parser.sym.IF); }
  "return"                       { return symbol(Parser.sym.RETURN); }
  "void"                         { return symbol(Parser.sym.VOID); }
  "while"                        { return symbol(Parser.sym.WHILE); }

    /* If an integer is found print it out, return the token NUMBER
       that represents an integer and the value of the integer that is
       held in the string yytext which will get turned into an integer
       before returning */
    {DecIntegerLiteral}      { System.out.print(yytext());
                         return symbol(Parser.sym.NUMBER, new Integer(yytext())); }
   
    /* If an identifier is found print it out, return the token ID
       that represents an identifier and the default value one that is
       given to all identifiers. */
    {Identifier}       { System.out.print(yytext());
                         return symbol(Parser.sym.ID, new String(yytext()));}
    /* Don't do anything if whitespace is found */
    {WhiteSpace}       { /* just skip what was found, do nothing */ }   
    {Comment}		{}
}


/* No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
