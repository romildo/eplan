package absyn;

import env.Env;
import error.CompilerError;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.NAME;
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
      for (DecType d : decs)
         env.tenv.put(d.name, new NAME(d.name));
      for (DecType d : decs) {
         Type t = d.ty.semantic(env);
         Type tname = env.tenv.get(d.name);
         if (! (tname instanceof NAME))
            throw new CompilerError("bug!!!!!!");
         ((NAME) tname).binding = t;
      }
   }
}
