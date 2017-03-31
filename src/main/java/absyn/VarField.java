package absyn;

import env.Env;
import error.CompilerError;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.ARRAY;
import types.INT;
import types.RECORD;
import types.Type;

import static semantic.SemanticHelper.typeMismatch;

public class VarField extends Var {

    public final Var varBase;
    public final String fieldName;

    public VarField(Loc loc, Var varBase, String fieldName) {
        super(loc);
        this.varBase = varBase;
        this.fieldName = fieldName;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("VarField", varBase.toTree(), Tree.of(fieldName));
    }

    @Override
    protected Type semantic_(Env env) {
        Type var_type = varBase.semantic_(env);
        var_type = var_type.actual();
        if (!(var_type instanceof RECORD)) {
            throw new CompilerError("type mismatch: found " + varBase.toString() + " but expected record");
        }
        RECORD record = ((RECORD) var_type);
        List<Tuple2<String, Type>> params = record.params;
        Type fieldType = null;
        for (Tuple2<String, Type> t : params) {
            if (t._1().equals(fieldName)) {
                fieldType = t._2();
            }
        }
        if (fieldType == null) {
            throw new CompilerError("record parameter " + fieldName + " doesn't exist");
        }
        return fieldType.actual();
    }
}
