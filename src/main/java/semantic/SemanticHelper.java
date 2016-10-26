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

}