package absyn;


import javaslang.collection.Tree;
import static org.bytedeco.javacpp.LLVM.*;

/**
 * Created by christian on 10/20/16.
 */

public class ExpNegate extends Exp{

    public final Exp arg;

    public ExpNegate(Exp arg) {
        this.arg = arg;
    }

        @Override
    public LLVMValueRef codegen(LLVMModuleRef module, LLVMBuilderRef builder) {
        final LLVMValueRef v_arg = arg.codegen(module,builder);
        final LLVMValueRef zero = LLVMConstReal(LLVMDoubleType(), 0);
        return LLVMBuildFSub(builder, zero, v_arg, "subtmp");
    }

        @Override
    public Tree.Node<String> toTree() {
        return Tree.of("ExpNegate", arg.toTree());
    }
}