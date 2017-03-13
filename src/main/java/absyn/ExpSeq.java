package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;
import types.UNIT;

public class ExpSeq extends Exp {

   public final List<Exp> exps;

   public ExpSeq(Loc loc, List<Exp> exps) {
      super(loc);
      this.exps = exps;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpSeq"),
                     exps.map(Exp::toTree));
   }

   @Override
   protected Type semantic_(Env env) {
      Type t = UNIT.T;
      for (Exp e : exps)
         t = e.semantic_(env);
      return t;
   }

}
