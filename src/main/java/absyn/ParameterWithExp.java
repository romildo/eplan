package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.Type;

public class ParameterWithExp extends Exp {

    public final String name;
    public final Exp body;

    public ParameterWithExp(Loc loc, String name, Exp body) {
        super(loc);
        this.name = name;
        this.body = body;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("Parameter", Tree.of(name), body.toTree());
    }

    @Override
    protected Type semantic_(Env env) {
        return body.semantic(env);
    }
}
