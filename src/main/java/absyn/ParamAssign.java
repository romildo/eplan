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

public class ParamAssign extends AST {

    public final String name;
    public final Exp exp;

    public ParamAssign(Loc loc, String name, Exp exp) {
        super(loc);
        this.name = name;
        this.exp = exp;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("Param", Tree.of(name), exp.toTree());
    }

    public Type semantic(Env env) {
        return exp.semantic(env);
    }
}
