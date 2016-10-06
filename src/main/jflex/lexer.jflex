package parse;

import java_cup.runtime.Symbol;

%%

%public
%cup

%%

[ \t\f\n\r]+         { /* skip */ }

[0-9]+ ("." [0-9]+)? { return new Symbol(sym.LITINT, yytext()); }

"+"                  { return new Symbol(sym.PLUS); }
"-"                  { return new Symbol(sym.MINUS); }
"*"                  { return new Symbol(sym.TIMES); }
"/"                  { return new Symbol(sym.DIV); }
"("                  { return new Symbol(sym.LPAREN); }
")"                  { return new Symbol(sym.RPAREN); }

.                    { System.out.printf("unexpected char |%s|\n", yytext()); }
