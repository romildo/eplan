package parse;

import static error.ErrorHelper.error;

import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
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

%eofval{
    return tok(EOF);
%eofval}

%ctorarg String unitName

%init{
   this.unit = unitName;
%init}

%{
   private String unit;

   private ComplexSymbolFactory complexSymbolFactory = new ComplexSymbolFactory();

   public SymbolFactory getSymbolFactory() {
      return complexSymbolFactory;
   }

   // auxiliary methods to construct terminal symbols at current location

   private Location locLeft() {
      return new Location(unit, yyline + 1, yycolumn + 1);
   }

   private Location locRight() {
      return new Location(unit, yyline + 1, yycolumn + 1 + yylength());
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

litint    = [0-9]+
litfloat1 = [0-9]+ "." [0-9]*
litfloat2 = [0-9]* "." [0-9]+
litfloat3 = ({litint} | {litfloat1} | {litfloat2}) [eE] [+-]? {litint}
litreal   = {litint} | {litfloat1} | {litfloat2} | {litfloat3}
litbool   = true | false

id        = [a-zA-Z][a-zA-Z0-9_]*

%%

[ \t\f\n\r]+         { /* skip */ }

{litint}             { return tok(LITINT, yytext()); }
{litreal}            { return tok(LITREAL, yytext()); }
{litbool}            { return tok(LITBOOL, yytext()); }

var                  { return tok(VAR); }
type                 { return tok(TYPE); }
let                  { return tok(LET); }
in                   { return tok(IN); }
if                   { return tok(IF); }
then                 { return tok(THEN); }
else                 { return tok(ELSE); }
break                { return tok(BREAK); }
while                { return tok(WHILE); }
do                   { return tok(DO); }
function             { return tok(FUNCTION); }

{id}                 { return tok(ID, yytext().intern()); }

"+"                  { return tok(PLUS); }
"-"                  { return tok(MINUS); }
"*"                  { return tok(TIMES); }
"/"                  { return tok(DIV); }
"&&"                 { return tok(AND); }
"||"                 { return tok(OR); }
"<"                  { return tok(LESS); }
"<="                 { return tok(LESSEG); }
">"                  { return tok(MORE); }
">="                 { return tok(MOREEG); }
"=="                 { return tok(EQUALS); }
"!="                 { return tok(DIF); }
"("                  { return tok(LPAREN); }
")"                  { return tok(RPAREN); }
","                  { return tok(COMMA); }
";"                  { return tok(SEMICOLON); }
":"                  { return tok(COLON); }
"="                  { return tok(EQ); }
":="                 { return tok(ASSIGN); }
"["                  { return tok(LBRACKET); }
"]"                  { return tok(RBRACKET); }
"@"                  { return tok(AT); }
"{"                  { return tok(LBRACE); }
"}"                  { return tok(RBRACE); }
"."                  { return tok(DOT); }

.                    { throw error(Loc.loc(locLeft()), "unexpected char '%s'", yytext()); }
