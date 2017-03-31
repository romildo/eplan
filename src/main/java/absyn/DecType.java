package absyn;

import javaslang.collection.Tree;
import parse.Loc;

/**
 * Created by Christian on 15/03/2017.
 */
public class DecType extends AST {
    public final String name;
    public final Ty ty;


    public DecType(Loc loc, String name, Ty type) {
        super(loc);
        this.name = name;
        this.ty = type;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("DecType", Tree.of(name), ty.toTree());
    }
}
