package absyn;

import env.Env;
import parse.Loc;

public abstract class Ty extends AST {

   public Ty(Loc loc) {
      super(loc);
   }

   // Do semantic analysis of the declaraction
   public abstract void semantic(Env env);

}
