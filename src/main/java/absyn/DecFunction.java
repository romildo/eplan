package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;

public class DecFunction extends AST {

    public final String name;
    public final List<Parameter> parameters;
    public final String typeName;
    public final Exp body;

    public DecFunction(Loc loc, String name, List parameters, String typeName, Exp body) {
        super(loc);
        this.name = name;
        this.parameters = parameters;
        this.typeName = typeName;
        this.body = body;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("DecFunction: " + name,
                Tree.of("Parameters", parameters.map(Parameter::toTree)),
                Tree.of(typeName == null ? "" : typeName),
                body.toTree()
        );
    }

}
