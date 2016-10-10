package absyn;

import javaslang.collection.Tree;

public class ExpBinOp extends Exp {

   public enum Op {PLUS, MINUS, TIMES, DIV}

   public final Op op;
   public final Exp left;
   public final Exp right;

   public ExpBinOp(Op op, Exp left, Exp right) {
      this.op = op;
      this.left = left;
      this.right = right;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of("ExpBinOp: " + op,
                     left.toTree(),
                     right.toTree());
   }
}
