package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.Type;
import types.UNIT;

import static semantic.SemanticHelper.errorBreak;

public class ExpBreak extends Exp {
   public ExpBreak(Loc loc) {
      super(loc);
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpBreak"));
   }

   @Override
   protected Type semantic_(Env env) {
      if(!env.controlBreak)
          throw errorBreak(loc);
       return UNIT.T;
   }
}
