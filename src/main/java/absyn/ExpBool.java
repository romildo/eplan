package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.BOOL;
import types.Type;

public class ExpBool extends Exp {

   public final String value;

   public ExpBool(Loc loc, String value) {
      super(loc);
      this.value = value;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpBool: " + value));
   }

   @Override
   protected Type semantic_(Env env) {
      return BOOL.T;
   }
}
