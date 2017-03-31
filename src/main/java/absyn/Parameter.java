package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;

public class Parameter extends AST {

   public final String name;
   public final String type;

   public Parameter(Loc loc, String name, String type) {
      super(loc);
      this.name = name;
      this.type = type;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(this.toString());
   }

   @Override
   public String toString() {
      return name + ": " + type;
   }
}
