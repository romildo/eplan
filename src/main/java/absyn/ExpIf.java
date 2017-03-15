package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.BOOL;
import types.Type;
import types.UNIT;

import static semantic.SemanticHelper.typeMismatch;


public class ExpIf extends Exp {

    public final Exp expCond, expThen, expElse;

    public ExpIf(Loc loc, Exp expCond, Exp expThen, Exp expElse) {
        super(loc);
        this.expCond = expCond;
        this.expThen = expThen;
        this.expElse = expElse;
    }

    @Override
    public Tree.Node<String> toTree() {
        List<Tree.Node<String>> children = List.of(Tree.of("Cond", expCond.toTree()));
        children = children.append(Tree.of("Then", expThen.toTree()));
        if (expElse != null)
            children = children.append(Tree.of("Else", expElse.toTree()));
        return Tree.of("ExpIf", children);
    }

    @Override
    protected Type semantic_(Env env) {
        Type t_cond = expCond.semantic_(env);

        if (!t_cond.is(BOOL.T)) {
            throw typeMismatch(expCond.loc, t_cond, BOOL.T);
        }

        Type t_then = expThen.semantic_(env);
        if (expElse == null) {
            if (!t_then.is(UNIT.T)) {
                throw typeMismatch(expThen.loc, t_then, UNIT.T);
            }
            return t_then;
        } else {
            Type t_else = expElse.semantic_(env);
            if (!t_then.is(t_else))
                if (!t_else.is(t_then))
                    throw typeMismatch(expElse.loc, t_else, t_then);
                else
                    return t_then;
            return t_else;
        }

    }

}
