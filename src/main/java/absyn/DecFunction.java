package absyn;

import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;

/**
 * Created by Christian on 15/03/2017.
 */
public class DecFunction extends AST {
    public final String name;
    public final String type;
    public final List<Parameter> params;
    public final Exp body;

    public DecFunction(Loc loc, String name, List<Parameter> params, String type, Exp body) {
        super(loc);
        this.name = name;
        this.type = type;
        this.params = params;
        this.body = body;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("DecFunction", Tree.of(name, params.map(Parameter::toTree).append(Tree.of(type)).append(body.toTree())));
    }
}
