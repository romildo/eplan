package main;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import absyn.Exp;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java_cup.runtime.Symbol;
import javaslang.render.dot.DotFile;
import javaslang.render.text.Boxes;
import javaslang.render.text.PrettyPrinter;
import parse.SymbolConstants;
import parse.Lexer;
import parse.parser;

import static error.ErrorManager.em;

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
}

// main
public class Driver {

   public static void main(String[] args) {
      // parse command line options
      DriverOptions options = new DriverOptions();
      JCommander jCommander = new JCommander(options);
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
      try {
         // set the input (source code) to compile
         if (options.parameters.isEmpty())
            input = new InputStreamReader(System.in);
         else
            input = new FileReader(options.parameters.get(0));

         // do only lexical analyses
         if (options.lexer)
            lexicalAnalysis(input);

         // do only lexical analyses
         if (options.parser)
            syntaxAnalysis(input);

         em.summary();
      }
      catch (IOException e) {
         System.out.println(e.getMessage());
         System.exit(2);
      }
      catch (Exception e) {
         System.out.println(e.getMessage());
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

   public static void lexicalAnalysis(Reader input) throws IOException {
      Lexer lexer = new Lexer(input);
      Symbol tok;
      do {
         tok = lexer.next_token();
         System.out.printf("%-55s %-8s %s%n",
                           tok,
                           SymbolConstants.terminalNames[tok.sym],
                           tok.value == null ? "" : tok.value);
      } while (tok.sym != SymbolConstants.EOF);
   }

   public static void syntaxAnalysis(Reader input) throws Exception {
      final Lexer lexer = new Lexer(input);
      final parser parser = new parser(lexer);
      final Symbol result = parser.parse();
      final Exp parseTree = (Exp) result.value;
      System.out.println(result.value);
      System.out.println();
      System.out.println("===Abstract syntax tree:===========");
      System.out.println();
      System.out.println(PrettyPrinter.pp(parseTree.toTree()));
      System.out.println();
      System.out.println(Boxes.box(parseTree.toTree()));
      DotFile.write(parseTree.toTree(), "test1.dot");
      System.out.println();
   }

}
