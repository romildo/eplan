package absyn;

import com.sun.org.apache.xpath.internal.operations.Variable;
import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;
import static semantic.SemanticHelper.typeMismatch;
import types.UNIT;

/**
 * Created by aluno on 13/03/2017.
 */

public class ExpAssign extends Exp{

    public final Var var;
    public final Exp exp;

    public ExpAssign(Loc loc, Var var, Exp exp) {
        super(loc);
        this.var = var;
        this.exp = exp;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpAssign"),
                var.toTree(),
                exp.toTree());
    }

    @Override
    protected Type semantic_(Env env) {
        Type Tipovar = var.semantic(env);
        Type Tipoexp = exp.semantic(env);

        if (!Tipoexp.is(Tipovar))
            throw typeMismatch(exp.loc, Tipoexp, Tipovar);

        return Tipovar;
    }


}



