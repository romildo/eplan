package absyn;

import env.Env;
import error.CompilerError;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.ARRAY;
import types.INT;
import types.Type;

import static semantic.SemanticHelper.typeMismatch;

public class VarSubscript extends Var {

    public final Var varBase;
    public final Exp indice;

    public VarSubscript(Loc loc, Var varBase, Exp indice) {
        super(loc);
        this.varBase = varBase;
        this.indice = indice;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("VarSubscript", varBase.toTree(), indice.toTree());
    }

    @Override
    protected Type semantic_(Env env) {
        Type var_type = varBase.semantic(env);
        if (!(var_type.actual() instanceof ARRAY)) {
            throw new CompilerError("type mismatch: found " + type.toString() + " but expected array");
        }
        Type exp_type = indice.semantic(env);
        if (!exp_type.is(INT.T)) {
            throw typeMismatch(loc, exp_type, INT.T);
        }
        return ((ARRAY) var_type.actual()).type;
    }
}
