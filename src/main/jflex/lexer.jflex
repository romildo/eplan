package parse;

import static error.ErrorManager.em;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.ComplexSymbolFactory;

%%

%public
%final
%class Lexer
%implements SymbolConstants
%cupsym SymbolConstants
%cup
%line
%column

%{
   private ComplexSymbolFactory complexSymbolFactory = new ComplexSymbolFactory();

   // auxiliary methods to construct terminal symbols at current location

   private Location locLeft() {
      return new Location(yyline + 1, yycolumn + 1);
   }

   private Location locRight() {
      return new Location(yyline + 1, yycolumn + 1 + yylength());
   }

   private Symbol tok(int type, String lexeme, Object value) {
      return complexSymbolFactory.newSymbol(lexeme, type, locLeft(), locRight(), value);
   }

   private Symbol tok(int type, Object value) {
      return tok(type, yytext(), value);
   }

   private Symbol tok(int type) {
      return tok(type, null);
   }
%}

%%

[ \t\f\n\r]+         { /* skip */ }

[0-9]+ ("." [0-9]+)? { return tok(LITINT, yytext()); }

"+"                  { return tok(PLUS); }
"-"                  { return tok(MINUS); }
"*"                  { return tok(TIMES); }
"/"                  { return tok(DIV); }
"("                  { return tok(LPAREN); }
")"                  { return tok(RPAREN); }

.                    { em.error(Loc.loc(locLeft()), "unexpected char '%s'", yytext()); }
