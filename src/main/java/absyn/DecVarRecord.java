package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import javaslang.control.Option;
import parse.Loc;
import semantic.SemanticHelper;
import types.RECORD;
import types.Type;

public class DecVarRecord extends Dec {

    public final String name;
    public final String typeName;
    public final String bodyVarName;
    public final String bodyRegisterName;

    public DecVarRecord(Loc loc, String name, String typeName, String bodyVarName, String bodyRegisterName) {
        super(loc);
        this.name = name;
        this.typeName = typeName;
        this.bodyVarName = bodyVarName;
        this.bodyRegisterName = bodyRegisterName;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("DecVarRecord",
                        (typeName != null)? Tree.of(name,
                                Tree.of(typeName)):Tree.of(name),
                        Tree.of(bodyVarName,
                                Tree.of(bodyRegisterName)));
    }

    @Override
    public Type semantic(Env env) {
        Type t_typeName;
        if (typeName != null) {
            t_typeName = env.tenv.get(typeName);

            if (t_typeName == null)
                throw SemanticHelper.undefined(loc, "type", typeName);
            else {
                Type t_bodyVar = env.venv.get(bodyVarName);
                t_bodyVar = t_bodyVar.actual();

                List<Parameter> t_bodyParams = ((RECORD) t_bodyVar).elements;
                Option<Parameter> t_p = t_bodyParams.find(p -> p.name == bodyRegisterName);
                if (t_p.isEmpty())
                    throw SemanticHelper.unknownRecordParameter(loc, bodyRegisterName);
                else {
                    Type t_aux = t_p.get().semantic_(env);
                    if (!t_aux.is(t_typeName))
                        throw SemanticHelper.typeMismatch(loc, t_typeName, t_aux);
                }
            }

        }
        else{
            Type t_bodyVar = env.venv.get(bodyVarName);
            t_bodyVar = t_bodyVar.actual();

            List<Parameter> t_bodyParams = ((RECORD) t_bodyVar).elements;
            Option<Parameter> t_p = t_bodyParams.find(p -> p.name == bodyRegisterName);
            if (t_p.isEmpty())
                throw SemanticHelper.unknownRecordParameter(loc, bodyRegisterName);

            t_typeName = t_p.get().semantic_(env);
        }
        env.venv.put(name, t_typeName);
        return t_typeName;
    }
}
