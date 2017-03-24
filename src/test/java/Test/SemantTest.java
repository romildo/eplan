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
               .matches(t -> t.is(type));
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
   public void testTypeDeclaration() throws Exception {
      trun("let\n" +
           "  type t1 = int\n" +
           "  var x : t1 = 3\n" +
           "in\n" +
           "  x + 1\n" +
           "",
           INT.T);

      erun("let\n" +
           "  type t1 = inteiro\n" +
           "  var x : t1 = 3\n" +
           "in\n" +
           "  print_int(x)\n" +
           "",
           "error.CompilerError: 2/13-2/20 undefined type 'inteiro'");

      trun("let\n" +
           "  type t1 = int\n" +
           "  type t2 = real\n" +
           "  var x : t1 = 3\n" +
           "  var y : t2 = 2.7\n" +
           "in\n" +
           "  round(y) + x\n" +
           "",
           INT.T);

      trun("let\n" +
           "  type t1 = t2\n" +
           "  type t2 = real\n" +
           "  var x : t1 = 3.5\n" +
           "in\n" +
           "  x\n" +
           "",
           REAL.T);

      erun("let\n" +
           "  type t1 = t2\n" +
           "  var x : t1 = 3.5\n" +
           "  type t2 = real\n" +
           "in\n" +
           "  x\n" +
           "",
           "error.CompilerError: 2/13-2/15 undefined type 't2'");

   }

}
