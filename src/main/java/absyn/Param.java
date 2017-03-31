package absyn;

/*
  Created: brenokeller 
  Date: 3/24/17
  Project: eplan
 */

import env.Env;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;

import static semantic.SemanticHelper.undefined;

public class Param extends AST {

    public final String name;
    public final String nameType;

    public Param(Loc loc, String name, String nameType) {
        super(loc);
        this.name = name;
        this.nameType = nameType;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("Param", Tree.of(name), Tree.of(nameType));
    }

    public Type semantic(Env env) {
        Type t = env.tenv.get(nameType);
        if (t == null) {
            throw undefined(loc, "type", nameType);
        }
        return t;
    }
}
