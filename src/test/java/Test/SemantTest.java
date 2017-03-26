package Test;

import absyn.Exp;
import env.Env;
import error.CompilerError;
import java_cup.runtime.Symbol;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import parse.Lexer;
import parse.Parser;
import types.*;

import java.io.IOException;
import java.io.StringReader;

public class SemantTest {

   private Type runSemantic(String input) throws Exception {
      Lexer lexer = new Lexer(new StringReader(input), "unknown");
      Parser parser = new Parser(lexer);
      Symbol program = parser.parse();
      Exp parseTree = (Exp) program.value;
      return parseTree.semantic(new Env());
   }

   private void trun(String input, Type type) {
      try {
         softly.assertThat(runSemantic(input))
               .as("%s", input)
               .isEqualTo(type);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void erun(String input, String message) throws IOException {
      softly.assertThatThrownBy(() -> runSemantic(input))
            .as("%s", input)
            .isInstanceOf(CompilerError.class)
            .hasToString(message);
   }

   @Rule
   public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

   @Test
   public void testLiterals() throws Exception {
      trun("true", BOOL.T);
      trun("123", INT.T);
      trun("12.34", REAL.T);
   }

   @Test
   public void testFunctionCall() throws Exception {
      erun("fat(9)",
           "error.CompilerError: 1/1-1/7 undefined function 'fat'");
      erun("fat(g(), h())",
           "error.CompilerError: 1/5-1/8 undefined function 'g'");
      trun("print_int(123)",
           UNIT.T);
      erun("print_int(true)",
           "error.CompilerError: 1/11-1/15 type mismatch: found bool but expected int");
      erun("print_int(123, true, f())",
           "error.CompilerError: 1/22-1/25 undefined function 'f'");
      erun("print_int()",
           "error.CompilerError: 1/1-1/12 too few arguments in call to 'print_int'");
   }

   @Test
   public void testSequence() throws Exception {
      trun("()", UNIT.T);
      trun("(true)", BOOL.T);
      trun("(print_int(23); 2.3)", REAL.T);
   }

   @Test
   public void testSimpleVariableAndLet() throws Exception {
      erun("x",
           "error.CompilerError: 1/1-1/2 undefined variable 'x'");
      trun("let var x: int = 10 in x",
           INT.T);
      trun("let var x = 0.56 in x",
           REAL.T);
      erun("let var x: int = 3.4 in x",
           "error.CompilerError: 1/18-1/21 type mismatch: found real but expected int");
      erun("(let var x = 5 in print_int(x); x)",
           "error.CompilerError: 1/33-1/34 undefined variable 'x'");
   }

   @Test
   public void testAssignment() throws Exception {
      trun("let var x: int = 10 in x := 2*x + 1",
           INT.T);
      erun("let var x: int = 10 in x := true",
           "error.CompilerError: 1/29-1/33 type mismatch: found bool but expected int");
   }

   @Test
   public void testIf() throws Exception {
      trun("if true then print_int(2)",
           UNIT.T);
      trun("if true then 2 else 3",
           INT.T);
      trun("if true then if false then print_real(1.1) else print_real(1.2)",
           UNIT.T);
      erun("if 5 then 2 else 3",
           "error.CompilerError: 1/4-1/5 type mismatch: found int but expected bool");
      erun("if true then 2 else false",
           "error.CompilerError: 1/21-1/26 type mismatch: found bool but expected int");
      erun("if true then 3.5",
           "error.CompilerError: 1/14-1/17 type mismatch: found real but expected unit");
   }

   @Test
   public void testBooleanArguments() throws Exception {
      trun("2 == 2", BOOL.T);
      trun("2 > 2", BOOL.T);
      trun("2 < 2", BOOL.T);
      trun("2 >= 2", BOOL.T);
      trun("2 <= 2", BOOL.T);
      trun("2 != 2", BOOL.T);
      erun("2 => 2", "error.CompilerError: 1/3-1/4 Syntax error at '='");
      erun("2 =< 2", "error.CompilerError: 1/3-1/4 Syntax error at '='");
      erun("2 =! 2", "error.CompilerError: 1/3-1/4 Syntax error at '='");
      erun("2 == test", "error.CompilerError: 1/6-1/10 undefined variable 'test'");
   }

   @Test
   public void testWhile() throws Exception {
      trun("while true do 1", UNIT.T);
      trun("while false do 1", UNIT.T);
      trun("while 2 == 2 do 1", UNIT.T);
      trun("while 2 > 2 do print_int(2)", UNIT.T);
      trun("while 2 > 2 do let var x = 2 in x", UNIT.T);
      trun("let var x = 2 var y = 2 in while x > y do print_int(x)", UNIT.T);
      erun("while 2 do 1", "error.CompilerError: 1/7-1/8 type mismatch: found int but expected bool");
      erun("while print_int(2) do print_int(2)", "error.CompilerError: 1/7-1/19 type mismatch: found unit but expected bool");
   }

   @Test
   public void testBreak() throws Exception {
      trun("while true do break", UNIT.T);
      trun("while true do let var x = 2 in break", UNIT.T);
      erun("let var x = 2 in break", "error.CompilerError: 1/18-1/23 break ins't in loop.");
      erun("break", "error.CompilerError: 1/1-1/6 break ins't in loop.");
      erun("while true do let var x = 2 break in break", "error.CompilerError: 1/29-1/34 Syntax error at 'break'");
   }

   @Test
   public void testFunction() throws Exception {
      trun("let function teste(x:int):int = x in teste(2)", INT.T);
      trun("let function teste(x:real):real = x in teste(2.2)", REAL.T);
      trun("let function teste(x:bool):bool = x in teste(true)", BOOL.T);
      trun("let function teste(x:unit):unit = x in teste(())", UNIT.T);
      trun("let function teste(x:unit, y:unit):unit = x in teste((),())", UNIT.T);
      trun("let function teste(x:int, y:int):int = x in teste(2 , 2)", INT.T);
      trun("let function teste(x:real, y:real):real = x in teste(2.2 , 2.2)", REAL.T);
      erun("let function teste(x:int):int = x in teste(2.2)", "error.CompilerError: 1/44-1/47 type mismatch: found real but expected int");
      erun("let function teste(x:int):real = x in teste(2)", "error.CompilerError: 1/5-1/35 function type mismatch: found int in body but expected real");
      erun("let function teste(x:real):int = x in teste(2)", "error.CompilerError: 1/5-1/35 function type mismatch: found real in body but expected int");
      erun("let function teste(x:int):int = x in teste(())", "error.CompilerError: 1/44-1/46 type mismatch: found unit but expected int");
      erun("let function teste(x:unit):unit = x in teste()", "error.CompilerError: 1/40-1/47 too few arguments in call to 'teste'");
      erun("let function teste(x:unit):unit = x in teste(2)", "error.CompilerError: 1/46-1/47 type mismatch: found int but expected unit");
      erun("let function teste(x:unit):int = x in teste(2)", "error.CompilerError: 1/5-1/35 function type mismatch: found unit in body but expected int");
      erun("let function teste(x:unit):int = x in teste(2.2)", "error.CompilerError: 1/5-1/35 function type mismatch: found unit in body but expected int");
      erun("let function teste(x:real):real = x in teste(2)", "error.CompilerError: 1/46-1/47 type mismatch: found int but expected real");
      erun("let function teste(x:real):real = x in teste(())", "error.CompilerError: 1/46-1/48 type mismatch: found unit but expected real");
      erun("let function teste(x:real):unit = x in teste(())", "error.CompilerError: 1/5-1/36 function type mismatch: found real in body but expected unit");
      erun("let function teste(x:unit):real = x in teste(())", "error.CompilerError: 1/5-1/36 function type mismatch: found unit in body but expected real");
      erun("let function teste(x:unit):real = x in teste(2.2)", "error.CompilerError: 1/5-1/36 function type mismatch: found unit in body but expected real");
      erun("let function teste(x:real, y:real):int = x in teste(2.2, 2.2)", "error.CompilerError: 1/5-1/43 function type mismatch: found real in body but expected int");
      erun("let function teste(x:real, y:real):real = x in teste(2.2, 2)", "error.CompilerError: 1/59-1/60 type mismatch: found int but expected real");
      erun("let function teste(x:real, y:real):real = x in teste(2, 2)", "error.CompilerError: 1/54-1/55 type mismatch: found int but expected real");
      erun("let function teste(x:unit, y:real):real = x in teste((), 2)", "error.CompilerError: 1/5-1/44 function type mismatch: found unit in body but expected real");
   }

   @Test
   public void testArray() throws Exception {
      trun("let type t = [int] var v:t = @t[10,20,30] var x:int = v[1] var y:int = v[x] in x", INT.T);
      trun("let type t = [int] var v:t = @t[10,20,30] var x :int= v[2+3] in 2", INT.T);
      trun("let type t = [int] var v:t = @t[10,20,30] var x :int= v[2+3] in x", INT.T);
      trun("let type t = [int] var v:t = @t[10,20] var x:int = v[2] in x", INT.T);
      trun("let type t = [real] var v:t = @t[10.0,20.0] var x:real = v[2] in x", REAL.T);
      trun("let type t = [unit] var v:t = @t[()] var x:unit = v[2] in x", UNIT.T);
      erun("let type t = [unit] var v:t = @t[(),1] var x:unit = v[2] in x", "error.CompilerError: 1/31-1/39 type mismatch: found int but expected unit");
      erun("let type t = [real] var v:t = @t[10.0,20.0] var x:int = v[2] in x", "error.CompilerError: 1/45-1/61 type mismatch: found int but expected real");
      erun("let type t = [real] var v:t = @t[10.0,20.0] var x:real = v[2.0] in x", "error.CompilerError: 1/60-1/63 type mismatch: found real but expected int");
      erun("let type t = [int] var v:t = @t[10,20,()] var x :int= v[2+3] in 2", "error.CompilerError: 1/30-1/42 type mismatch: found unit but expected int");
      erun("let type t = [int] var v:t = @t[10,20,30] var x:int = v[y] var y:int = v[1] in x", "error.CompilerError: 1/57-1/58 undefined variable 'y'");
   }

   @Test
   public void testRegisters() throws Exception {
      trun("let type ponto = {x:real, y:int} var p:ponto = @ponto{x=2.3,y=8} var x : int = p.y in x", INT.T);
      trun("let type ponto = {x:real, y:real} var p:ponto = @ponto{x=2.3,y=0.8} var x : real = p.x in 2", INT.T);
      trun("let type ponto = {x:real, y:real} var p:ponto = @ponto{x=2.3,y=0.8} var x : real = p.x in x", REAL.T);
      trun("let type ponto = {x:int, y:int} var p:ponto = @ponto{x=2,y=0} var x : int = p.x in 2", INT.T);
      trun("let type ponto = {x:unit, y:unit} var p:ponto = @ponto{x=(),y=()} var x : unit = p.x in 2", INT.T);
      trun("let type ponto = {x:unit, y:unit} var p:ponto = @ponto{x=(),y=()} var x : unit = p.x in ()", UNIT.T);
      trun("let type ponto = {x:unit, y:unit} var p:ponto = @ponto{x=(),y=()} var x : unit = p.x in x", UNIT.T);
      erun("let type ponto = {x:int, y:int} var p:ponto = @ponto{x=2,y=0} var x : real = p.x in 2", "error.CompilerError: 1/63-1/81 type mismatch: found real but expected int");
      erun("let type ponto = {x:real, y:real} var p:ponto = @ponto{x=2.3,y=0.8} var x : int = p.x in ()", "error.CompilerError: 1/69-1/86 type mismatch: found int but expected real");
      erun("let type ponto = {x:real, y:int} var p:ponto = @ponto{x=2.3,y=8} var x : real = p.y in x", "error.CompilerError: 1/66-1/84 type mismatch: found real but expected int");
   }
}
