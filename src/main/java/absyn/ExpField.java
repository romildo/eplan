package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.Type;

import static semantic.SemanticHelper.undefined;

public class ExpField extends Exp {

   public final String name;
   public final String type;

   public ExpField(Loc loc, String name, String type) {
      super(loc);
      this.name = name;
      this.type = type;
   }

   @Override
   public Tree.Node<String> toTree() {
   return Tree.of(annotateType("ExpField - " + name + ":" + type));
   }

   @Override
   protected Type semantic_(Env env) {
      Type t = env.tenv.get(type);
      if(t == null)
         throw undefined(loc, "type", type);
      return t;
   }
}
