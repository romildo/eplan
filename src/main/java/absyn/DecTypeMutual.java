package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.Type;

public class DecTypeMutual extends Dec {

   public final List<DecType> decs;

   public DecTypeMutual(Loc loc, List<DecType> decs) {
      super(loc);
      this.decs = decs;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of("DecTypeMutual", decs.map(DecType::toTree));
   }

   @Override
   public void semantic(Env env) {

   }
}
