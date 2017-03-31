package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.ARRAY;
import types.INT;
import types.Type;

import static semantic.SemanticHelper.typeMismatch;
import static semantic.SemanticHelper.errorArray;

public class VarSubscript extends Var {

    public final Var varBase;
    public final Exp index;

    public VarSubscript(Loc loc, Var varBase, Exp index) {
        super(loc);
        this.varBase = varBase;
        this.index = index;
    }

    @Override
    public Tree.Node<String> toTree() {
        List<Tree.Node<String>> children = List.of(varBase.toTree(), index.toTree());
        return Tree.of(annotateType("VarSubscript"), children);
    }

    @Override
    protected Type semantic_(Env env) {
        Type tArray = varBase.semantic(env);
        Type tIndex = index.semantic(env);
        if(!(tArray instanceof ARRAY))
            throw errorArray(varBase.loc, tArray);
        if(!tIndex.is(INT.T))
            throw typeMismatch(index.loc, tIndex, INT.T);
        ARRAY t = (ARRAY) tArray;
        return t.tElements;
    }

}
