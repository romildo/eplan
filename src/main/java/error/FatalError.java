package error;

public class FatalError extends RuntimeException {

   public FatalError(String message) {
      super("fatal: " + message);
   }

   public FatalError(String format, Object... args) {
      this(String.format(format, args));
   }

}
