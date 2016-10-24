package error;

import parse.Loc;
import types.Type;

import static error.ErrorManager.em;

public interface CommonErrors {

   default void typeMismatch(Loc loc, Type found, Type... expected) {
      StringBuilder builder = new StringBuilder();
      int n = expected.length;
      if (n > 0) {
         builder.append(expected[0]);
         if (n > 1) {
            for (int i = 1; i < n - 2; i++)
               builder.append(", ").append(expected[i]);
            builder.append(" or ").append(expected[n - 1]);
         }
      }
      em.error(loc, "type mismatch: found %s but expected %s", found, builder);
   }

}
