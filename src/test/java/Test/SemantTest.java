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
        } catch (Exception e) {
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
    public void testAssign() throws Exception {
        trun("let var x = 2 in x := 3",
                INT.T);
        trun("let var x = 2 var y = 1 in y := x := 3",
                INT.T);
        trun("let var x = true in x := false",
                BOOL.T);
        erun("let var x = 2 in x := 3.5",
                "error.CompilerError: 1/18-1/19 type mismatch: found int but expected real");

    }

    @Test
    public void testExpIf() throws Exception {
        //trun("if true then print_int(4)", UNIT.T);
        trun("if false then 4/4 else 3/2", INT.T);
        trun("if not(true && false) then print_int(5) else print_real(4.3)", UNIT.T);
        erun("if false then true else 3/2",
                "error.CompilerError: 1/25-1/28 type mismatch: found int but expected bool");
        erun("if true then 4*5",
                "error.CompilerError: 1/14-1/17 type mismatch: found int but expected unit");
        erun("if 2+2 then print_int(4)",
                "error.CompilerError: 1/4-1/7 type mismatch: found int but expected bool");
    }

}
