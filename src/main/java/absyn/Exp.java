package absyn;

import static org.bytedeco.javacpp.LLVM.*;

public abstract class Exp extends AST {

   public abstract LLVMValueRef codegen(LLVMModuleRef module, LLVMBuilderRef builder);

}