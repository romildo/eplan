package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.Type;

public class ExpVar extends Exp {

   public final Var variable;

   public ExpVar(Loc loc, Var variable) {
      super(loc);
      this.variable = variable;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpVar"),
                     variable.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      return variable.semantic(env);
   }
}
