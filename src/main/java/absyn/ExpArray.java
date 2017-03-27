package absyn;

import env.Env;
import error.CompilerError;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.ARRAY;
import types.Type;

import static semantic.SemanticHelper.typeMismatch;
import static semantic.SemanticHelper.undefined;

public class ExpArray extends Exp {

    public final String typeName;
    public final List<Exp> exp;

    public ExpArray(Loc loc, String typeName, List<Exp> exp) {
        super(loc);
        this.typeName = typeName;
        this.exp = exp;
    }

    @Override
    public Tree.Node<String> toTree() {
        List<Tree.Node<String>> children = List.of();
        children = children.append(Tree.of(typeName));
        for (Exp e : exp) {
            children = children.append(e.toTree());
        }
        return Tree.of("ExpArray", children);
    }

    @Override
    protected Type semantic_(Env env) {
        Type type = env.tenv.get(typeName);
        if (type == null) {
            throw undefined(loc, "type", typeName);
        }
        if (!(type.actual() instanceof ARRAY)) {
            throw new CompilerError("type mismatch: found " + type.toString() + " but expected array");
        }
        ARRAY array = (ARRAY) type.actual();
        for (Exp e : exp) {
            Type t = e.semantic(env);
            if (!(t.is(array.type.actual()))) {
                throw typeMismatch(loc, t, array.type);
            }
        }
        return type;
    }
}
