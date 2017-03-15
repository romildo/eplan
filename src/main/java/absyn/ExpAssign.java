package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.Type;

import static error.ErrorHelper.*;
import static semantic.SemanticHelper.*;
import static semantic.SemanticHelper.typeMismatch;

public class ExpAssign extends Exp {

   public final Var var;
   public final Exp value;

   public ExpAssign(Loc loc, Var var, Exp value) {
      super(loc);
      this.var = var;
      this.value = value;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpAssign"), var.toTree(), value.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      Type t1 = var.semantic(env);
      Type t2 = value.semantic(env);
      if(t1.is(t2))
         return t1;
      else
         throw typeMismatch(value.loc, t2, t1);
   }
}
