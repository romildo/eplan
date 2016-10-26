package absyn;

import parse.Loc;
import types.Type;

import static org.bytedeco.javacpp.LLVM.*;

public abstract class Exp extends AST {

   // Type of the expression, calculated by the semantic analyser
   public Type type;

   public Exp(Loc loc) {
      super(loc);
   }

   // Obtain the type of the expression as a string prefixed by the given text.
   protected String annotateType(String text) {
      final String theType = type == null ? "" : "\n<" + type + ">";
      return text + theType;
   }

   // Do semantic analysis of the expression
   public Type semantic() {
      type = semantic_();
      return type;
   }

   // Type check the expression. Should be defined in the concrete subclasses.
   protected abstract Type semantic_();

   // Generate code for the LLVM IR (intermediate representation)
   public abstract LLVMValueRef translate(LLVMModuleRef module, LLVMBuilderRef builder);

}
