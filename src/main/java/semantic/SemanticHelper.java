package semantic;

import error.CompilerError;

import parse.Loc;
import types.Type;

public interface SemanticHelper {

   static CompilerError error(String message) {
      return new CompilerError(message);
   }

   static CompilerError error(String format, Object... args) {
      return new CompilerError(format, args);
   }

   static CompilerError error(Loc loc, String format, Object... args) {
      return new CompilerError(loc, format, args);
   }

   static CompilerError typeMismatch(Loc loc, Type found, Type... expected) {
      final StringBuilder builder = new StringBuilder();
      final int n = expected.length;
      if (n > 0) {
         builder.append(expected[0]);
         if (n > 1) {
            for (int i = 1; i < n - 2; i++)
               builder.append(", ").append(expected[i]);
            builder.append(" or ").append(expected[n - 1]);
         }
      }
      return error(loc, "type mismatch: found %s but expected %s", found, builder);
   }

   static CompilerError undefined(Loc loc, String category, String name) {
      return new CompilerError(loc, "undefined %s '%s'", category, name);
   }

   static CompilerError notAFunction(Loc loc, String name) {
      return error(loc, "'%s' is not a function", name);
   }

   static CompilerError tooFewArguments(Loc loc, String name) {
      return error(loc, "too few arguments in call to '%s'", name);
   }

   static CompilerError tooMuchArguments(Loc loc, String name) {
      return error(loc, "too much arguments in call to '%s'", name);
   }

}
