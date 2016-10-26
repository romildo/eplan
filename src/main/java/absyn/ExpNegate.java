package absyn;

import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.REAL;
import types.Type;

import static org.bytedeco.javacpp.LLVM.*;
import static error.ErrorHelper.*;
import static semantic.SemanticHelper.*;

public class ExpNegate extends Exp {

   public final Exp arg;

   public ExpNegate(Loc loc, Exp arg) {
      super(loc);
      this.arg = arg;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpNegate"), arg.toTree());
   }

   @Override
   protected Type semantic_() {
      final Type t_arg = arg.semantic();

      if (t_arg instanceof INT || t_arg instanceof REAL)
         return t_arg;

      throw typeMismatch(arg.loc, t_arg, INT.T, REAL.T);
   }

   @Override
   public LLVMValueRef translate(LLVMModuleRef module, LLVMBuilderRef builder) {
      final LLVMValueRef v_arg = arg.translate(module, builder);

      if (type instanceof INT)
         return LLVMBuildNeg(builder, v_arg, "tmpneg");

      if (type instanceof REAL)
         return LLVMBuildFNeg(builder, v_arg, "tmpneg");

      throw fatal("unexpected type '%s'", type);
   }

}
