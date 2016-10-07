package parse;

import java_cup.runtime.Symbol;

%%

%public
%final
%class Lexer
%implements SymbolConstants
%cupsym SymbolConstants
%cup

%%

[ \t\f\n\r]+         { /* skip */ }

[0-9]+ ("." [0-9]+)? { return new Symbol(LITINT, yytext()); }

"+"                  { return new Symbol(PLUS); }
"-"                  { return new Symbol(MINUS); }
"*"                  { return new Symbol(TIMES); }
"/"                  { return new Symbol(DIV); }
"("                  { return new Symbol(LPAREN); }
")"                  { return new Symbol(RPAREN); }

.                    { System.out.printf("unexpected char |%s|\n", yytext()); }
