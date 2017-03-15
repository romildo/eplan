package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;

import static semantic.SemanticHelper.typeMismatch;

public class ExpAssign extends Exp {

   public final Var var;
   public final Exp exp;

   public ExpAssign(Loc loc, Var var, Exp exp) {
      super(loc);
      this.var = var;
      this.exp = exp;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpAssign"),
                     var.toTree(),
                     exp.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      Type t_var = var.semantic(env);
      Type t_exp = exp.semantic(env);
      if (!t_exp.is(t_var))
         throw typeMismatch(exp.loc, t_exp, t_var);
      return t_var;
   }
}
