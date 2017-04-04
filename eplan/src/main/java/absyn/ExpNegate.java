package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.REAL;
import types.Type;

import static error.ErrorHelper.*;
import static semantic.SemanticHelper.*;

public class ExpNegate extends Exp {

   public final Exp arg;

   public ExpNegate(Loc loc, Exp arg) {
      super(loc);
      this.arg = arg;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpNegate"), arg.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      final Type t_arg = arg.semantic(env);

      if (t_arg instanceof INT || t_arg instanceof REAL)
         return t_arg;

      throw typeMismatch(arg.loc, t_arg, INT.T, REAL.T);
   }

}
