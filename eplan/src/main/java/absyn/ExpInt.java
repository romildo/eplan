package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.Type;

public class ExpInt extends Exp {

   public final String value;

   public ExpInt(Loc loc, String value) {
      super(loc);
      this.value = value;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpInt: " + value));
   }

   @Override
   protected Type semantic_(Env env) {
      return INT.T;
   }
}
