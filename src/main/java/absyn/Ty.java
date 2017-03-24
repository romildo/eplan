package absyn;

import env.Env;
import parse.Loc;
import types.Type;

public abstract class Ty extends AST {

   public Ty(Loc loc) {
      super(loc);
   }

   // Do semantic analysis of the declaraction
   public abstract Type semantic(Env env);

}
