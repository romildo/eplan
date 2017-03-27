package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.ARRAY;
import types.INT;
import types.Type;

public class VarSubscript extends Var {

   public final Var base;
   public final Exp indice;

   public VarSubscript(Loc loc, Var base, Exp indice) {
      super(loc);
      this.base = base;
      this.indice = indice;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("VarSubscript: "),
                     base.toTree(),
                     indice.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      Type t_init = indice.semantic(env);
      if (!t_init.is(INT.T))
         throw SemanticHelper.typeMismatch(indice.loc,t_init, INT.T);

      Type t_base = base.semantic(env);

      t_base = t_base.actual();
      if (!(t_base instanceof ARRAY))
         throw SemanticHelper.arrayMismatch(loc,t_base);

      t_base = ((ARRAY)t_base).typeName;

      System.out.println("VarSubscript: "+t_base);
      return t_base;
   }

}
