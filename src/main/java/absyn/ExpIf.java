package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.*;

import static semantic.SemanticHelper.typeMismatch;

public class ExpIf extends Exp {

   public final Exp test;
   public final Exp alt1;
   public final Exp alt2;

   public ExpIf(Loc loc, Exp test, Exp alt1, Exp alt2) {
      super(loc);
      this.test = test;
      this.alt1 = alt1;
      this.alt2 = alt2;
   }

   @Override
   public Tree.Node<String> toTree() {
      List<Tree.Node<String>> children = List.of(test.toTree(), alt1.toTree());
      if (alt2 != null)
         children = children.append(alt2.toTree());
      return Tree.of(annotateType("ExpIf: "), children);
   }

   @Override
   protected Type semantic_(Env env) {
      final Type t_test = test.semantic(env);
      if (!t_test.is(BOOL.T))
         throw typeMismatch(test.loc, t_test, BOOL.T);
      final Type t_alt1 = alt1.semantic(env);
      if (alt2 == null) {
         if (!t_alt1.is(UNIT.T))
            throw typeMismatch(alt1.loc, t_alt1, UNIT.T);
         return UNIT.T;
      }
      final Type t_alt2 = alt2.semantic(env);
      if (t_alt2.is(t_alt1))
         return t_alt1;
      if (t_alt1.is(t_alt2))
         return t_alt2;
      throw typeMismatch(alt2.loc, t_alt2, t_alt1);
   }

}
