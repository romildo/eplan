package error;

import parse.Loc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ErrorManager {

   public static final ErrorManager em = new ErrorManager();

   private int errorsCounter;
   private int warningsCounter;
   private int fatalsCounter;

   private List<String> errors;

   private Consumer<String> show;

   private ErrorManager() {
      reset();
   }

   public void reset() {
      reset(System.out::println);
   }

   public void reset(Consumer<String> show) {
      errorsCounter = 0;
      warningsCounter = 0;
      fatalsCounter = 0;
      errors = new ArrayList<>();
      this.show = show;
   }

   @Override
   public String toString() {
      return "ErrorManager{" +
             "errorsCounter=" + errorsCounter +
             ", warningsCounter=" + warningsCounter +
             ", fatalsCounter=" + fatalsCounter +
             ", errors=" + errors +
             ", show=" + show +
             '}';
   }

   private void message(String text) {
      errors.add(text);
      show.accept(text);
   }

   private void message(String format, Object... args) {
      message(String.format(format, args));
   }

   public void error(String format, Object... args) {
      errorsCounter++;
      message("ERROR: " + format, args);
   }

   public void error(Loc loc, String format, Object... args) {
      error(loc + " " + format, args);
   }

   public void warning(String format, Object... args) {
      warningsCounter++;
      message("WARNING: " + format, args);
   }

   public void warning(Loc loc, String format, Object... args) {
      warning(loc + " " + format, args);
   }

   public void fatal(String format, Object... args) {
      fatalsCounter++;
      message("FATAL: " + format, args);
   }

   public void fatal(Loc loc, String format, Object... args) {
      fatal(loc + " " + format, args);
   }

   public String getSummary() {
      return "\nerrors: " + errorsCounter +
             "\nwarnings: " + warningsCounter +
             "\nfatal errors: " + fatalsCounter +
             "\n";
   }

   public void summary() {
      show.accept(getSummary());
   }

}
