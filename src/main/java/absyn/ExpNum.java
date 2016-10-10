package absyn;

import javaslang.collection.Tree;

public class ExpNum extends Exp {

   public final Double value;

   public ExpNum(Double value) {
      this.value = value;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of("ExpNum: " + value);
   }
}
