package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.ARRAY;
import types.INT;
import types.Type;
import types.UNIT;

import static semantic.SemanticHelper.typeMismatch;
import static semantic.SemanticHelper.undefined;
import static semantic.SemanticHelper.errorArray;

public class ExpArray extends Exp {

    public final String nameOfType;
    public final List<Exp> content;

    public ExpArray(Loc loc, String nameOfType, List<Exp> content) {
        super(loc);
        this.nameOfType = nameOfType;
        this.content = content;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpArray: " + nameOfType), content.map(Exp::toTree));
    }

    @Override
    protected Type semantic_(Env env) {
        Type type = env.tenv.get(nameOfType);
        if(type == null)
            throw undefined(loc, "type", nameOfType);
        if(!(type.actual() instanceof ARRAY))
            throw errorArray(loc, type);
        for(Exp e: content){
            Type t = e.semantic(env);
            if(!t.is(((ARRAY) type).tElements))
                throw typeMismatch(e.loc, t, type);
        }
        return type;
    }
}
