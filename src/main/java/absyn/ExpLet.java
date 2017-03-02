package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;

public class ExpLet extends Exp {

   public final List<Dec> decs;
   public final Exp body;

   public ExpLet(Loc loc, List<Dec> decs, Exp body) {
      super(loc);
      this.decs = decs;
      this.body = body;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpLet"),
                     Tree.of("Decs",
                             decs.map(Dec::toTree)),
                     body.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      env.tenv.beginScope();;
      env.venv.beginScope();
      decs.forEach(d -> d.semantic(env));
      Type t_body = body.semantic(env);
      env.tenv.endScope();
      env.venv.endScope();
      return t_body;
   }
}
