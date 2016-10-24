package absyn;

import parse.Loc;

import static org.bytedeco.javacpp.LLVM.*;

public abstract class Exp extends AST {

   public Exp(Loc loc) {
      super(loc);
   }

   // Generate code for the LLVM IR (intermediate representation)
   public abstract LLVMValueRef codegen(LLVMModuleRef module, LLVMBuilderRef builder);

}