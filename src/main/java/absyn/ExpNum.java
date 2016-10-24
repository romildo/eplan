package absyn;

import javaslang.collection.Tree;
import parse.Loc;

import static org.bytedeco.javacpp.LLVM.*;

public class ExpNum extends Exp {

   public final Double value;

   public ExpNum(Loc loc, Double value) {
      super(loc);
      this.value = value;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of("ExpNum: " + value);
   }

   @Override
   public LLVMValueRef codegen(LLVMModuleRef module, LLVMBuilderRef builder) {
      return LLVMConstReal(LLVMDoubleType(), value);
   }
}
