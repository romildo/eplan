package absyn;

import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.Type;

import static org.bytedeco.javacpp.LLVM.*;

public class ExpInt extends Exp {

   public final Long value;

   public ExpInt(Loc loc, Long value) {
      super(loc);
      this.value = value;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpInt: " + value));
   }

   @Override
   protected Type semantic_() {
      return INT.T;
   }

   @Override
   public LLVMValueRef codegen(LLVMModuleRef module, LLVMBuilderRef builder) {
      return LLVMConstInt(LLVMInt32Type(), value, 1); //Pesquisar sobre o terceiro parâmetro desta função
   }
}

