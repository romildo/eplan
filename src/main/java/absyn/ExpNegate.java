package absyn;

import javaslang.collection.Tree;
import parse.Loc;
import types.REAL;
import types.Type;

import static org.bytedeco.javacpp.LLVM.*;
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
      if (! t_arg.is(REAL.T))
         throw typeMismatch(arg.loc, t_arg, REAL.T);
      return REAL.T;
   }

   @Override
   public LLVMValueRef translate(LLVMModuleRef module, LLVMBuilderRef builder) {
      final LLVMValueRef v_arg = arg.translate(module, builder);
      return LLVMBuildFNeg(builder, v_arg, "negtmp");
   }

}
