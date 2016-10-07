package main;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java_cup.runtime.Symbol;
import parse.Lexer;
import parse.SymbolConstants;

// command line options
class DriverOptions {
   @Parameter(description = "<source file>")
   public List<String> parameters = new ArrayList<>();

   @Parameter(names = {"--help", "-h"}, description = "Usage help", help = true)
   public boolean help = false;

   @Parameter(names = {"--lexer"}, description = "Lexical analysis")
   public boolean lexer = true;
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
      }
      catch (IOException e) {
         System.out.println(e.getMessage());
         System.exit(2);
      }
      finally {
         // closes the input file
         if (input instanceof FileReader)
            try {
               input.close();
            }
            catch (IOException e) {
               System.out.println(e.getMessage());
               System.exit(3);
            }
      }
   }

   public static void lexicalAnalysis(Reader input) throws IOException {
      Lexer lexer = new Lexer(input);
      Symbol tok;
      do {
         tok = lexer.next_token();
         System.out.println(tok);
      } while (tok.sym != SymbolConstants.EOF);
   }

}
