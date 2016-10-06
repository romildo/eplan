package main;

import java.io.InputStreamReader;
import java.io.Reader;

public class LexerDriver {

  public static void main(String[] args) throws Exception {
    Reader input = new InputStreamReader(System.in);
    Yylex lexer = new Yylex(input);
    int tok;
    do {
      tok = lexer.yylex();
      System.out.println(tok);
    } while (tok != lexer.YYEOF);
  }

}
