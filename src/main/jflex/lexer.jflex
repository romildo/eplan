package parse;

import java_cup.runtime.Symbol;

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
   // auxiliary methods to construct terminal symbols at current location
   
   private Symbol tok(int type, Object value) {
      return new Symbol(type, yyline, yycolumn, value);
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

.                    { System.out.printf("unexpected char |%s|\n", yytext()); }
