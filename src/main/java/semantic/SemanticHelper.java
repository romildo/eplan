package semantic;

import error.CompilerError;

import parse.Loc;
import types.Type;

public interface SemanticHelper {

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
      return new CompilerError(loc, "type mismatch: found %s but expected %s", found, builder);
   }

   static CompilerError errorArray(Loc loc, Type found) {
      return new CompilerError(loc, "type mismatch: found %s but expected ARRAY", found);
   }

   static CompilerError undefined(Loc loc, String category, String name) {
      return new CompilerError(loc, "undefined %s '%s'", category, name);
   }

   static CompilerError errorBreak(Loc loc) {
      return new CompilerError(loc, "unexpected 'break'");
   }

   static CompilerError syntaxError(Loc loc) {
      return new CompilerError(loc, "syntax-error: Only declare record fields here");
   }

   static CompilerError fieldAlreadyExists(Loc loc) {
      return new CompilerError(loc, "syntax-error: A field with this name already exists");
   }

   static CompilerError functionAlreadyExist(Loc loc) {
      return new CompilerError(loc, "syntax-error: A function with this name already exists");
   }

   static CompilerError notAFunction(Loc loc, String name) {
      return new CompilerError(loc, "'%s' is not a function", name);
   }

   static CompilerError tooFewArguments(Loc loc, String name) {
      return new CompilerError(loc, "too few arguments in call to '%s'", name);
   }

   static CompilerError tooMuchArguments(Loc loc, String name) {
      return new CompilerError(loc, "too much arguments in call to '%s'", name);
   }

}
