package absyn;

import env.Env;
import error.CompilerError;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.*;

import static semantic.SemanticHelper.typeMismatch;
import static semantic.SemanticHelper.undefined;

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
    public void semantic(Env env) {
        for (DecFunction f : decs) {
            List<Type> t_params = List.of();
            for (Param p : f.params) {
                Type t = env.tenv.get(p.nameType);
                if (t == null) {
                    throw undefined(p.loc, "type", p.nameType);
                }
                t_params = t_params.append(t);
            }

            Type t_result = UNIT.T;
            if (f.returnType != null) {
                t_result = env.tenv.get(f.returnType);
                if (t_result == null) {
                    throw undefined(f.loc, "type", f.returnType);
                }
            }
            env.venv.put(f.name, new FUNCTION(t_result, t_params));
        }
        for (DecFunction f : decs) {
            FUNCTION function = (FUNCTION) env.venv.get(f.name);
            List<Type> t_params = function.formals;

            env.venv.beginScope();
            List<Param> ps = f.params;
            while (!ps.isEmpty()) {
                env.venv.put(ps.head().name, t_params.head());
                ps = ps.tail();
                t_params = t_params.tail();
            }

            Type t_body = f.exp.semantic(env);
            if (!t_body.is(function.result)) {
                typeMismatch(f.exp.loc, t_body, function.result);
            }
            env.venv.endScope();
        }
    }
}
