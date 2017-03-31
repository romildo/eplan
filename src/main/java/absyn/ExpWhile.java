package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.BOOL;
import types.Type;
import types.UNIT;

import static semantic.SemanticHelper.typeMismatch;

public class ExpWhile extends Exp {
   public final Exp condition;
   public final Exp exp;

   public ExpWhile(Loc loc, Exp condition, Exp exp) {
      super(loc);
      this.condition = condition;
      this.exp = exp;
   }

   @Override
   public Tree.Node<String> toTree() {
      List<Tree.Node<String>> children = List.of(condition.toTree(), exp.toTree());
      return Tree.of(annotateType("ExpWhile"), children);
   }

   @Override
   protected Type semantic_(Env env) {
      boolean control = env.controlBreak;
      env.controlBreak = true;

      Type tCondition = condition.semantic(env);
      if(!tCondition.is(BOOL.T))
         throw typeMismatch(condition.loc, tCondition, BOOL.T);
      exp.semantic(env);   // só verifica se não contém erros

      env.controlBreak = control;
      return UNIT.T;
   }

}
