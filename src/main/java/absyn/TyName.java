package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;

import static semantic.SemanticHelper.undefined;

public class TyName extends Ty {

   public final String name;

   public TyName(Loc loc, String name) {
      super(loc);
      this.name = name;
   }

   @Override
   public Type semantic(Env env) {
      Type t = env.tenv.get(name);
      if (t == null)
         throw undefined(loc, "type", name);
      return t;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of("TyName: " + name);
   }
}
