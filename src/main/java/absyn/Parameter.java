package absyn;

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.Type;

public class Parameter extends Exp {

    public final String name;
    public final String typeName;

    public Parameter(Loc loc, String name, String typeName) {
        super(loc);
        this.name = name;
        this.typeName = typeName;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("Parameter", Tree.of(name), Tree.of(typeName));
    }

    @Override
    protected Type semantic_(Env env) {
        Type t_type = env.tenv.get(typeName);
        if (t_type == null)
            throw SemanticHelper.undefined(loc, "type", typeName);
        return t_type;
    }

    @Override
    public String toString() {
        return  name + " : " + typeName;
    }
}
