package absyn;

import javaslang.collection.Tree;
import parse.Loc;

public class DecType extends AST {
   public final String name;
   public final Ty ty;

   public DecType(Loc loc, String name, Ty ty) {
      super(loc);
      this.name = name;
      this.ty = ty;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of("DecType", Tree.of(name), ty.toTree());
   }
}
