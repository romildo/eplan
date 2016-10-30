package absyn;

import javaslang.collection.Tree;
import parse.Loc;
import types.INT;
import types.Type;

import static org.bytedeco.javacpp.LLVM.*;

public class ExpInt extends Exp {

    public final String value;

    public ExpInt(Loc loc, String value) {
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
    public LLVMValueRef translate(LLVMModuleRef module, LLVMBuilderRef builder) {
        return LLVMConstIntOfString(LLVMInt32Type(), value, (byte) 10);
    }
}