package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.Type;

import static semantic.SemanticHelper.*;
import static semantic.SemanticHelper.typeMismatch;

public class ExpAssign extends Exp {

    public final Var var;
    public final Exp exp;


    public ExpAssign(Loc loc, Var var, Exp exp) {
        super(loc);
        this.exp = exp;
        this.var = var;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpAssign"), var.toTree(), exp.toTree());
    }

    @Override
    protected Type semantic_(Env env) {
        Type t1 = var.semantic(env);
        Type t2 = exp.semantic(env);

        if (t1.is(t2))
            return t1;
        else
            throw typeMismatch(exp.loc, t2, t1);
    }
}