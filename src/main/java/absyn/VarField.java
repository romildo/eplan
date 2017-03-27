package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import javaslang.control.Option;
import parse.Loc;
import semantic.SemanticHelper;
import types.INT;
import types.RECORD;
import types.Type;

public class VarField extends Var {

    public final Var base;
    public final String field;

    public VarField(Loc loc, Var base, String field) {
        super(loc);
        this.base = base;
        this.field = field;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("VarSubscript: "),
                base.toTree(),
                Tree.of(field));
    }

    @Override
    protected Type semantic_(Env env) {
        Type t_base = base.semantic(env);

        t_base = t_base.actual();
        if (!(t_base instanceof RECORD))
            throw SemanticHelper.recordMismatch(loc, t_base);

        List<Parameter> t_bodyParams = ((RECORD) t_base).elements;
        Option<Parameter> t_p = t_bodyParams.find(p -> p.name == field);
        if (t_p.isEmpty())
            throw SemanticHelper.unknownRecordParameter(loc, field);

        Type t_aux = t_p.get().semantic_(env);
        //t_aux = t_aux.actual();
        return t_aux;
    }

}
