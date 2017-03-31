package absyn;

import env.Env;
import error.CompilerError;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;
import types.UNIT;

/**
 * Created by aluno on 24/03/2017.
 */
public class ExpBreak extends Exp {

    public ExpBreak(Loc loc) {
        super(loc);
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("Break"));
    }

    @Override
    public Type semantic_(Env env) {
        if (env.isWhile) {
            return UNIT.T;
        }else{
            throw new CompilerError(loc, "unexpected: break");
        }
    }

}
