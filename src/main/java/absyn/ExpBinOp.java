package absyn;

import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
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

      if (!t_left.is(INT.T, REAL.T))
         throw typeMismatch(left.loc, t_left, INT.T, REAL.T);

      if (!t_right.is(INT.T, REAL.T))
         throw typeMismatch(right.loc, t_right, INT.T, REAL.T);

      if (t_left.is(REAL.T) || t_right.is(REAL.T))
         return REAL.T;

      return INT.T;
   }

   @Override
   public LLVMValueRef translate(LLVMModuleRef module, LLVMBuilderRef builder) {
      LLVMValueRef v_left = left.translate(module, builder);
      LLVMValueRef v_right = right.translate(module, builder);

      if (type instanceof REAL) {
         if (left.type instanceof INT)
            v_left = int2real(builder, v_left);

         if (right.type instanceof INT)
            v_right = int2real(builder, v_right);
      }

      switch (op) {
         case PLUS:
            if (type instanceof INT)
               return LLVMBuildAdd(builder, v_left, v_right, "tmpadd");
            else
               return LLVMBuildFAdd(builder, v_left, v_right, "tmpadd");
         case MINUS:
            if (type instanceof INT)
               return LLVMBuildSub(builder, v_left, v_right, "tmpsub");
            else
               return LLVMBuildFSub(builder, v_left, v_right, "tmpsub");
         case TIMES:
            if (type instanceof INT)
               return LLVMBuildMul(builder, v_left, v_right, "tmpmul");
            else
               return LLVMBuildFMul(builder, v_left, v_right, "tmpmul");
         case DIV:
            if (type instanceof INT)
               return LLVMBuildSDiv(builder, v_left, v_right, "tmpdiv");
            else
               return LLVMBuildFDiv(builder, v_left, v_right, "tmpdiv");
         default:
            throw fatal("unknown operator %s in binary operation", op);
      }
   }
}
