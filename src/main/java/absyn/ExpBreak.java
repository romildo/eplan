package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.BOOL;
import types.Type;
import types.UNIT;

import static semantic.SemanticHelper.typeMismatch;

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
        return Tree.of(annotateType("ExpWhile: "), test.toTree(), body.toTree());
    }

    @Override
    protected Type semantic_(Env env) {
        //Verificando a expressão teste
        final Type t_test = test.semantic(env);
        if (!t_test.is(BOOL.T))
            throw typeMismatch(test.loc, t_test, BOOL.T);

        body.semantic(env);

        return UNIT.T;
    }

}
