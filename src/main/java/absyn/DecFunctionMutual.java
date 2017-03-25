package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import semantic.SemanticHelper;
import types.FUNCTION;
import types.Type;
import types.UNIT;


public class DecFunctionMutual extends Dec {

    public final List<DecFunction> decs;

    public DecFunctionMutual(Loc loc, List<DecFunction> decs) {
        super(loc);
        this.decs = decs;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("DecFunctionMutual", decs.map(DecFunction::toTree));
    }

    @Override
    public Type semantic(Env env) {
        for (DecFunction d : decs) {
            List<Type> t_params = d.parameters.map(p -> p.semantic(env));

            Type t_result = UNIT.T;
            if (d.typeName != null) {
                t_result = env.tenv.get(d.typeName);
                if (t_result == null) {
                    throw SemanticHelper.undefined(d.loc, "type", d.name);
                }
            }

            env.venv.put(d.name, new FUNCTION(t_result, t_params));
        }
        for (DecFunction d : decs) {
            env.venv.beginScope();

            FUNCTION func = (FUNCTION) env.venv.get(d.name);
            List<Type> t_params = func.formals;
            List<Parameter> parameters = d.parameters;

            while (!parameters.isEmpty()) {
                env.venv.put(parameters.head().name, t_params.head());
                parameters = parameters.tail();
                t_params = t_params.tail();
            }

            Type t_body = d.body.semantic(env);
            if (!t_body.is(func.result))
                throw SemanticHelper.functionTypeMismatch(d.loc, func.result, t_body);
            env.venv.endScope();
        }
        return null;
    }
}
