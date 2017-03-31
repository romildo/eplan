package absyn;

import env.Env;
import error.CompilerError;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.ARRAY;
import types.RECORD;
import types.Type;

import static semantic.SemanticHelper.typeMismatch;
import static semantic.SemanticHelper.undefined;

public class ExpRecord extends Exp {

    public final String typeName;
    public final List<ParamAssign> params;

    public ExpRecord(Loc loc, String typeName, List<ParamAssign> params) {
        super(loc);
        this.typeName = typeName;
        this.params = params;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("ExpRecord", Tree.of(typeName), Tree.of("Fields", params.map(ParamAssign::toTree)));
    }

    @Override
    protected Type semantic_(Env env) {
        Type type = env.tenv.get(typeName);
        if (type == null) {
            throw SemanticHelper.undefined(loc, "type", typeName);
        }
        type = type.actual();
        if (!(type instanceof RECORD)) {
            throw new CompilerError("type mismatch: found " + type.toString() + " but expected record");
        }
        RECORD record = ((RECORD) type);
        List<Tuple2<String, Type>> params_record = record.params;
        List<String> fields_assign = List.empty();
        int cont = 0;
        for (ParamAssign p : params) {
            Type p_type = p.semantic(env);

            if (fields_assign.contains(p.name)) {
                throw new CompilerError("duplicate field encountered");
            } else {
                Type fieldType = null;
                for (Tuple2<String, Type> t : params_record) {
                    if (t._1().equals(p.name)) {
                        fieldType = t._2();
                    }
                }
                if (fieldType == null) {
                    throw new CompilerError("record parameter " + p.name + " doesn't exist");
                } else {
                    if (!p_type.is(fieldType)) {
                        throw SemanticHelper.typeMismatch(loc, p_type, fieldType);
                    }
                    cont++;
                }
                fields_assign = fields_assign.prepend(p.name);
            }
        }
        if (cont != params_record.size()) {
            throw new CompilerError("missing fields");
        }
        return type;
    }
}
