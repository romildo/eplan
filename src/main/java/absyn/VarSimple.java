package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.FUNCTION;
import types.Type;

import static semantic.SemanticHelper.undefined;
import static semantic.SemanticHelper.error;

public class VarSimple extends Var {

   public final String name;

   public VarSimple(Loc loc, String name) {
      super(loc);
      this.name = name;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("VarSimple: " + name));
   }

   @Override
   protected Type semantic_(Env env) {
      Type t = env.venv.get(name);

      if (t == null)
         throw undefined(loc, "variable", name);

      if (t instanceof FUNCTION)
         throw error(loc, "function '%s' used as variable", name);

      return t;
   }

}
