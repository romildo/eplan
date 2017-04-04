package main;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import absyn.AST;
import absyn.Exp;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import error.CompilerError;
import java_cup.runtime.Symbol;
import javaslang.render.dot.DotFile;
import javaslang.render.text.Boxes;
import javaslang.render.text.PrettyPrinter;
import parse.SymbolConstants;
import parse.Lexer;
import parse.Parser;

import static error.ErrorHelper.fatal;

// command line options
class DriverOptions {
   @Parameter(description = "<source file>")
   public List<String> parameters = new ArrayList<>();

   @Parameter(names = {"--help", "-h"}, description = "Usage help", help = true)
   public boolean help = false;

   @Parameter(names = {"--lexer"}, description = "Lexical analysis")
   public boolean lexer = false;

   @Parameter(names = {"--parser"}, description = "Syntax analysis")
   public boolean parser = true;

   @Parameter(names = {"--pp-ast"}, description = "Pretty print syntax tree")
   public boolean pp_ast = false;

   @Parameter(names = {"--pp-anotated-ast"}, description = "Pretty print annotated syntax tree")
   public boolean pp_annotated_ast = false;

   @Parameter(names = {"--box-ast"}, description = "Boxed syntax tree")
   public boolean box_ast = false;

   @Parameter(names = {"--box-annotated-ast"}, description = "Boxed annotated syntax tree")
   public boolean box_annotated_ast = true;

   @Parameter(names = {"--dot-ast"}, description = "Generate dot file of syntax tree")
   public boolean dot_ast = false;

   @Parameter(names = {"--dot-annotated-ast"}, description = "Generate dot file of annotated syntax tree")
   public boolean dot_annotted_ast = true;
}

// main
public class Driver {

   public static void main(String[] args) {
      // parse command line options
      final DriverOptions options = new DriverOptions();
      final JCommander jCommander = new JCommander(options);
      jCommander.setProgramName("Driver");

      try {
         jCommander.parse(args);
      }
      catch (ParameterException e) {
         System.out.println(e.getMessage());
         jCommander.usage();
         System.exit(1);
      }

      if (options.help) {
         jCommander.usage();
         return;
      }

      Reader input = null;
      String name = null;
      try {
         // set the input (source code) to compile
         if (options.parameters.isEmpty()) {
            name = "unknown";
            input = new InputStreamReader(System.in);
         }
         else {
            name = options.parameters.get(0);
            input = new FileReader(name);
         }

         // do only lexical analyses
         if (options.lexer)
            lexicalAnalysis(name, input);

         // do only lexical analyses
         if (options.parser)
            syntaxAnalysis(options, name, input);
      }
      catch (CompilerError e) {
         System.out.println(e.getMessage());
         System.exit(3);
      }
      catch (IOException e) {
         System.out.println(e.getMessage());
         System.exit(2);
      }
      catch (Exception e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
         System.exit(3);
      }
      finally {
         // closes the input file
         if (input instanceof FileReader)
            try {
               input.close();
            }
            catch (IOException e) {
               System.out.println(e.getMessage());
               System.exit(4);
            }
      }
   }

   public static void lexicalAnalysis(String name, Reader input) throws IOException {
      final Lexer lexer = new Lexer(input, "unknown");
      Symbol tok;
      do {
         tok = lexer.next_token();
         System.out.printf("%-55s %-8s %s%n",
                           tok,
                           SymbolConstants.terminalNames[tok.sym],
                           tok.value == null ? "" : tok.value);
      } while (tok.sym != SymbolConstants.EOF);
   }

   public static void syntaxAnalysis(DriverOptions options, String name, Reader input) throws Exception {
      final Lexer lexer = new Lexer(input, "unknown");
      final Parser parser = new Parser(lexer);
      final Symbol result = parser.parse();
      //System.out.println(result);

      if (!(result.value instanceof AST))
         throw fatal("internal error: program should be an AST");

      final AST parseTree = (AST) result.value;
      if (options.pp_ast) {
         System.out.println("===Abstract syntax tree:===========");
         System.out.println();
         System.out.println(PrettyPrinter.pp(parseTree.toTree()));
         System.out.println();
      }
      if (options.box_ast) {
         System.out.println("===Abstract syntax tree:===========");
         System.out.println();
         System.out.println(Boxes.box(parseTree.toTree()));
         System.out.println();
      }
      if (options.dot_ast) {
         DotFile.write(parseTree.toTree(), name + ".dot");
      }

      if (!(parseTree instanceof Exp))
         throw fatal("internal error: program should be an expression");

      final Exp main = (Exp) parseTree;
      main.semantic(new env.Env());
      if (options.pp_annotated_ast) {
         System.out.println("===Annotated abstract syntax tree:===========");
         System.out.println();
         System.out.println(PrettyPrinter.pp(parseTree.toTree()));
         System.out.println();
      }
      if (options.box_annotated_ast) {
         System.out.println("===Annotated abstract syntax tree:===========");
         System.out.println();
         System.out.println(Boxes.box(parseTree.toTree()));
         System.out.println();
      }
      if (options.dot_annotted_ast) {
         DotFile.write(parseTree.toTree(), name + ".annotated.dot");
      }
   }

}
