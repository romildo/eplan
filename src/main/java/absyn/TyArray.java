package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;

import static semantic.SemanticHelper.undefined;

/**
 * Created by Christian on 26/03/2017.
 */
public class TyArray extends Ty {
    public final String type;

    public TyArray(Loc loc, String type) {
        super(loc);
        this.type = type;
    }

    @Override
    public Type semantic(Env env) {
        Type t = env.tenv.get(type);
        if(t == null)
            throw undefined(loc, "type", type);
        return t;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("TyArray: " + type);
    }
}

// let type t1 = [bool] var a = @t1[true, false, 3 <= 3, 2 != 4] in (2+3)