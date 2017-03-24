package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;

public class DecFunction extends AST {
    public final String name;
    public final List<Param> params;
    public final String returnType;
    public final Exp exp;

    public DecFunction(Loc loc, String name, List<Param> params, String returnType, Exp exp) {
        super(loc);
        this.name = name;
        this.params = params;
        this.returnType = returnType;
        this.exp = exp;
    }

    @Override
    public Tree.Node<String> toTree() {
        List<Tree.Node<String>> children = List.of(Tree.of(name));
        for (Param p : params) {
            children = children.append(p.toTree());
        }
        children = children.append(Tree.of(returnType));
        children = children.append(exp.toTree());
        return Tree.of("DecFunction", children);
    }
}
