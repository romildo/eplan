package absyn;

import com.sun.org.apache.xpath.internal.operations.Variable;
import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.BOOL;
import types.Type;
import static semantic.SemanticHelper.typeMismatch;
import types.UNIT;

/**
 * Created by aluno on 13/03/2017.
 */

public class ExpIf extends Exp{

    public final Exp exp1;
    public final Exp exp2;
    public final Exp exp3;

    public ExpIf(Loc loc, Exp exp1, Exp exp2, Exp exp3) {
        super(loc);
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    @Override
    public Tree.Node<String> toTree() {
        if (!(exp3 == null)){
            return Tree.of(annotateType("ExpAssign"),
                    exp1.toTree(),
                    exp2.toTree(),
                    exp3.toTree());
        }
        return Tree.of(annotateType("ExpAssign"),
                exp1.toTree(),
                exp2.toTree());
    }

    @Override
    protected Type semantic_(Env env) {
        Type Tipoexp1 = exp1.semantic(env);

        if (!(Tipoexp1.is(BOOL.T))){
            throw typeMismatch(exp1.loc, Tipoexp1, BOOL.T);
        }

        Type Tipoexp2 = exp2.semantic(env);

        if (exp3 == null){
            if (!(Tipoexp2.is(UNIT.T))){
                throw typeMismatch(exp2.loc, Tipoexp2, UNIT.T);
            }
            return UNIT.T;
        }
        else {
            Type Tipoexp3 = exp3.semantic(env);
            if (!(Tipoexp3.is(UNIT.T))){
                throw typeMismatch(exp3.loc, Tipoexp3, UNIT.T);
            }
            return UNIT.T;
        }
    }
}



