package error;

import parse.Loc;

public class CompilerError extends RuntimeException {

   public CompilerError(String message) {
      super(message);
   }

   public CompilerError(String format, Object... args) {
      this(String.format(format, args));
   }

   public CompilerError(Loc loc, String format, Object... args) {
      this(loc + " " + String.format(format, args));
   }
}
