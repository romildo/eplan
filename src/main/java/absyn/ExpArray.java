package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.ARRAY;
import types.Type;

public class ExpArray  extends Exp{

    public final String typeName;
    public final List<Exp> elements;

    public ExpArray(Loc loc, String typeName, List<Exp> elements) {
        super(loc);
        this.typeName = typeName;
        this.elements = elements;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("ExpArray",Tree.of(typeName),Tree.of("Elements", elements.map(Exp::toTree)));
    }

    @Override
    protected Type semantic_(Env env) {
        Type t_typeName = env.tenv.get(typeName);
        Type t_test = t_typeName;
        if (t_typeName == null)
            throw SemanticHelper.undefined(loc,"type",typeName);
        else{
            t_typeName = t_typeName.actual();
            if (!(t_typeName instanceof ARRAY))
                throw SemanticHelper.arrayMismatch(loc,t_typeName);
        }

        Type t_elements = ((ARRAY) t_typeName).typeName;

        for (Exp e: elements) {
            Type t_aux = e.semantic(env);
            if (!t_aux.is(t_elements))
                throw SemanticHelper.typeMismatch(loc,t_aux,t_elements);
        }

        return t_test;
    }
}
