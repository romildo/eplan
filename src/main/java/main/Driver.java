package main;

import java.io.InputStreamReader;
import java.io.Reader;

import java_cup.runtime.Symbol;

import parse.Yylex;
import parse.sym;

public class Driver {

  public static void main(String[] args) throws Exception {
    Reader input = new InputStreamReader(System.in);
    Yylex lexer = new Yylex(input);
    Symbol tok;
    do {
      tok = lexer.next_token();
      System.out.println(tok);
    } while (tok.sym != sym.EOF);
  }

}
