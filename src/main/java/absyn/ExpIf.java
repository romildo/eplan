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

    public final Exp test;
    public final Exp e1;
    public final Exp e2;

    public ExpIf(Loc loc, Exp test, Exp e1, Exp e2) {
        super(loc);
        this.test = test;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public Tree.Node<String> toTree() {
        List<Tree.Node<String>> children = List.of(test.toTree());
        children = children.append(e1.toTree());
        if (e2 != null)
            children = children.append(e2.toTree());
        return Tree.of("ExpIf", children);
    }

    @Override
    protected Type semantic_(Env env) {
        Type t_test = test.semantic(env);
        if (!t_test.is(BOOL.T)) {
            throw typeMismatch(test.loc, t_test, BOOL.T);
        }

        Type t_e1 = e1.semantic(env);
        if (e2 != null) {
            Type t_e2 = e2.semantic(env);
            if (!t_e1.is(t_e2)) {
                if (!t_e2.is(t_e1)) {
                    throw typeMismatch(e2.loc, t_e2, t_e1);
                } else {
                    return t_e2;
                }
            }
            return t_e1;
        } else {
            if (!t_e1.is(UNIT.T)) {
                throw typeMismatch(e1.loc, t_e1, UNIT.T);
            }
            return t_e1;
        }
    }
}
