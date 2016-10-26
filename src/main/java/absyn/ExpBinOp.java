package absyn;

import javaslang.collection.Tree;
import parse.Loc;
import types.REAL;
import types.Type;

import static org.bytedeco.javacpp.LLVM.*;
import static error.ErrorHelper.fatal;
import static semantic.SemanticHelper.*;

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
      return Tree.of(annotateType("ExpBinOp: " + op), left.toTree(), right.toTree());
   }

   @Override
   protected Type semantic_() {
      final Type t_left = left.semantic();
      final Type t_right = right.semantic();
      if (! t_left.is(REAL.T))
         throw typeMismatch(left.loc, t_left, REAL.T);
      if (! t_right.is(REAL.T))
         throw typeMismatch(right.loc, t_right, REAL.T);
      return REAL.T;
   }

   @Override
   public LLVMValueRef translate(LLVMModuleRef module, LLVMBuilderRef builder) {
      final LLVMValueRef v_left = left.translate(module, builder);
      final LLVMValueRef v_right = right.translate(module, builder);

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
            throw fatal("unknown operator %s in binary operation", op);
      }
   }
}
