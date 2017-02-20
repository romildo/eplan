package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.REAL;
import types.Type;

import static error.ErrorHelper.fatal;
import static semantic.SemanticHelper.*;

public class ExpBinOp extends Exp {

   public enum Op {PLUS, MINUS, TIMES, DIV}

   public final Op op;
   public final Exp left;
   public final Exp right;

   public ExpBinOp(Loc loc, Op op, Exp left, Exp right) {
      super(loc);
      this.op = op;
      this.left = left;
      this.right = right;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpBinOp: " + op), left.toTree(), right.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      final Type t_left = left.semantic(env);
      final Type t_right = right.semantic(env);

      if (!t_left.is(INT.T, REAL.T))
         throw typeMismatch(left.loc, t_left, INT.T, REAL.T);

      if (!t_right.is(INT.T, REAL.T))
         throw typeMismatch(right.loc, t_right, INT.T, REAL.T);

      if (t_left.is(REAL.T) || t_right.is(REAL.T))
         return REAL.T;

      return INT.T;
   }

}
