package absyn;

import javaslang.collection.Tree;
import org.bytedeco.javacpp.LLVM;

import static org.bytedeco.javacpp.LLVM.*;


public class ExpNegate extends Exp {
    public final Exp e;

    public ExpNegate(Exp e) {
        this.e = e;
    }


    @Override


    public LLVMValueRef codegen(LLVMModuleRef module, LLVMBuilderRef builder) {
        final LLVMValueRef v_e = e.codegen(module, builder);
        final LLVMValueRef zero = LLVMConstReal(LLVMDoubleType(), 0);
        return LLVMBuildFSub(builder, zero, v_e, "subtmp");

    }


    @Override


    public Tree.Node<String> toTree() {
        return Tree.of("ExpNegate", e.toTree());
    }
}