package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.Type;

public class DecVar extends Dec {

   public final String name;
   public final String typeName;
   public final Exp init;

   public DecVar(Loc loc, String name, String typeName, Exp init) {
      super(loc);
      this.name = name;
      this.typeName = typeName;
      this.init = init;
   }

   @Override
   public Tree.Node<String> toTree() {
      List<Tree.Node<String>> children = List.of(Tree.of(name));
      if (typeName != null)
         children = children.append(Tree.of(typeName));
      children = children.append(init.toTree());
      return Tree.of("DecVar", children);
   }

   @Override
   public void semantic(Env env) {
      Type t_init = init.semantic(env);
      Type t_var = t_init;
      if (typeName != null) {
         Type t_typeName = env.tenv.get(typeName);
         if (t_typeName == null)
            throw SemanticHelper.undefined(loc, "type", typeName);
         if (!t_init.is(t_typeName))
            throw SemanticHelper.typeMismatch(init.loc, t_init, t_typeName);
         t_var = t_typeName;
      }
      env.venv.put(name, t_var);
   }
}
