package absyn;

import env.Env;
import error.ErrorHelper;
import javaslang.collection.Tree;
import parse.Loc;
import types.BOOL;
import types.INT;
import types.REAL;
import types.Type;

import static error.ErrorHelper.fatal;
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
        return Tree.of(annotateType("ExpAssign"), var.toTree(), exp.toTree());
    }

    @Override
    protected Type semantic_(Env env) {
        final Type t_var = var.semantic(env);
        final Type t_exp = exp.semantic(env);

        if (!t_var.is(t_exp)) {
            throw typeMismatch(exp.loc, t_exp, t_var);
        }
        return t_var;
    }

}
