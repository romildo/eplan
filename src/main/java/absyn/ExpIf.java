package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.BOOL;
import types.INT;
import types.Type;
import types.UNIT;

import static semantic.SemanticHelper.typeMismatch;

public class ExpIf extends Exp {

    public final Exp condition;
    public final Exp expTrue;
    public final Exp expFalse;

    public ExpIf(Loc loc, Exp condition, Exp expTrue, Exp expFalse) {
        super(loc);
        this.condition = condition;
        this.expTrue = expTrue;
        this.expFalse = expFalse;
    }

    public ExpIf(Loc loc, Exp condition, Exp expTrue) {
        super(loc);
        this.condition = condition;
        this.expTrue = expTrue;
        this.expFalse = null;
    }

    @Override
    public Tree.Node<String> toTree() {
        List<Tree.Node<String>> children = List.of(condition.toTree());
        children = children.append(expTrue.toTree());
        if (expFalse != null)
            children = children.append(expFalse.toTree());
        return Tree.of(annotateType("ExpIf"), children);
    }

    @Override
    protected Type semantic_(Env env) {
        Type tCondition = condition.semantic(env);
        if(tCondition.is(BOOL.T)){
            Type tExp1 = expTrue.semantic(env);
            if(expFalse == null){
                if(!tExp1.is(UNIT.T)){
                    throw typeMismatch(expTrue.loc, tExp1, UNIT.T);
                }
            }
            Type tExp2 = expFalse.semantic(env);
            if(tExp2.is(tExp1))
                return tExp1;
            else
                throw typeMismatch(expFalse.loc, tExp2, tExp1);
        }
        else
            throw typeMismatch(condition.loc, tCondition, BOOL.T);
    }
}
