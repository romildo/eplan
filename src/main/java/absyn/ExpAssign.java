package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.FUNCTION;
import types.INT;
import types.REAL;
import types.Type;

import static semantic.SemanticHelper.*;


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
      return Tree.of(annotateType("ExpAssign"),var.toTree(),exp.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      Type t_var = var.semantic_(env);
      Type t_exp = exp.semantic_(env);
      if (!t_var.is(t_exp)){
          throw typeMismatch(var.loc, t_var, t_exp);
      }
      return t_var;
   }

}
