package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import javaslang.control.Option;
import parse.Loc;
import semantic.SemanticHelper;
import types.ARRAY;
import types.INT;
import types.RECORD;
import types.Type;

public class DecVarArray extends Dec {

    public final String name;
    public final String typeName;
    public final String arrayName;
    public final Exp init;

    public DecVarArray(Loc loc, String name, String typeName, String arrayName, Exp init) {
        super(loc);
        this.name = name;
        this.typeName = typeName;
        this.arrayName = arrayName;
        this.init = init;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("DecVarArray",
                        (typeName != null)? Tree.of(name,
                            Tree.of(typeName)):Tree.of(name),
                        Tree.of(arrayName),
                        init.toTree());
    }

    @Override
    public Type semantic(Env env) {
        Type t_init = init.semantic(env);
        if (!t_init.is(INT.T))
            throw SemanticHelper.typeMismatch(init.loc,t_init, INT.T);

        Type t_typeName;
        if (typeName != null){
            t_typeName = env.tenv.get(typeName);
            if (t_typeName == null)
                throw SemanticHelper.undefined(loc, "type", typeName);
            else{
                Type t_array = env.venv.get(arrayName);
                t_array = t_array.actual();
                t_array = ((ARRAY)t_array).typeName;
                if (!t_array.is(t_typeName))
                    throw SemanticHelper.typeMismatch(loc, t_typeName, t_array);
            }
        }
        else{
            Type t_array = env.venv.get(arrayName);
            t_array = t_array.actual();
            t_array = ((ARRAY)t_array).typeName;
            t_typeName = t_array;
        }
        env.venv.put(name, t_typeName);
        return t_typeName;
    }
}
