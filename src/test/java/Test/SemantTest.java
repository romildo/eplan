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
   public void testExpressionOfAssign() throws IOException {
      trun("let var a : int = 10 in a := 5", INT.T);
      trun("let var a = 100.8 in a := 5 * 43.56", REAL.T);
      erun("let var a = 10 in a := 5 * 43.56", "error.CompilerError: 1/24-1/33 type mismatch: found real but expected int");
      trun("let var a = false in a := true || false", BOOL.T);
      erun("let var a = 10 in a := true", "error.CompilerError: 1/24-1/28 type mismatch: found bool but expected int");
   }

   @Test
   public void testExpressionIf() throws IOException {
      trun("if false then () else ()", UNIT.T);
      trun("if true then 57*3.14 else 10.4", REAL.T);
      trun("if true || false then true else false", BOOL.T);
      erun("if 4.234 then true else false", "error.CompilerError: 1/4-1/9 type mismatch: found real but expected bool");
      erun("if false then 57*3.14 else false", "error.CompilerError: 1/28-1/33 type mismatch: found bool but expected real");
   }
}
