package absyn;

import env.Env;
import parse.Loc;
import types.Type;

public abstract class Dec extends AST {

   public Dec(Loc loc) {
      super(loc);
   }

   // Do semantic analysis of the declaraction
   public abstract void semantic(Env env);

}
