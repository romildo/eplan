package absyn;

import env.Env;
import error.CompilerError;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;
import types.UNIT;
import types.BOOL;

/**
 * Created by aluno on 24/03/2017.
 */
public class ExpWhile extends Exp {

    public final Exp test;
    public final Exp body;

    public ExpWhile(Loc loc, Exp test, Exp body) {
        super(loc);
        this.test = test;
        this.body = body;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpWhile"),test.toTree(),body.toTree());
    }

    @Override
    protected Type semantic_(Env env) {
        Type t_test = test.semantic_(env);
        if (!t_test.is(BOOL.T))
            throw new CompilerError(loc, "type mismatch: found %s but expected %s", t_test, BOOL.T);
        env.isWhile = true;
        Type t_body = body.semantic_(env);
        env.isWhile = false;
        return UNIT.T;
    }
}
