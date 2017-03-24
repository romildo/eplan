package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.BOOL;
import types.Type;
import types.UNIT;

import static semantic.SemanticHelper.breakOutWhile;
import static semantic.SemanticHelper.typeMismatch;

public class ExpBreak extends Exp {

    public ExpBreak(Loc loc) {
        super(loc);
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpBreak"));
    }

    @Override
    protected Type semantic_(Env env) {
        if (env.isWhile.size() == 0)
            throw breakOutWhile(loc);
        return UNIT.T;
    }

}
