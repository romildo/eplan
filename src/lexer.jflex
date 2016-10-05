
%%

%public
%integer

%%

[ \t\f\n\r]+         { /* skip */ }

[0-9]+ ("." [0-9]+)? { return 10; }

"+"                  { return 21; }
"-"                  { return 22; }
"*"                  { return 23; }
"/"                  { return 24; }

"("                  { return 31; }
")"                  { return 32; }

.                    { System.out.printf("unexpected char |%s|\n", yytext()); }
