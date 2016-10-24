package absyn;

import javaslang.collection.Tree;
import parse.Loc;

import static org.bytedeco.javacpp.LLVM.*;
import static error.ErrorManager.em;

public class ExpBinOp extends Exp {

   public enum Op {PLUS, MINUS, TIMES, DIV}

   public final Op op;
   public final Exp left;
   public final Exp right;

   public ExpBinOp(Loc loc, Op op, Exp left, Exp right) {
      super(loc);
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

   @Override
   public LLVMValueRef codegen(LLVMModuleRef module, LLVMBuilderRef builder) {
      final LLVMValueRef v_left = left.codegen(module, builder);
      final LLVMValueRef v_right = right.codegen(module, builder);

      switch (op) {
         case PLUS:
            return LLVMBuildFAdd(builder, v_left, v_right, "addtmp");
         case MINUS:
            return LLVMBuildFSub(builder, v_left, v_right, "subtmp");
         case TIMES:
            return LLVMBuildFMul(builder, v_left, v_right, "multmp");
         case DIV:
            return LLVMBuildFDiv(builder, v_left, v_right, "divtmp");
         default:
            em.fatal("unknown operator %s in binary operation", op);
            return LLVMConstReal(LLVMDoubleType(), 0);
      }
   }
}
