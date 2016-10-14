package codegen;

import absyn.Exp;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;

import static org.bytedeco.javacpp.LLVM.*;
import static error.ErrorManager.em;

// http://llvm.org/releases/3.8.0/docs/index.html
// http://llvm.org/releases/3.8.0/docs/tutorial/index.html
// http://llvm.org/releases/3.8.0/docs/ProgrammersManual.html
// http://llvm.org/docs/doxygen/html/group__LLVMC.html
// https://github.com/paulsmith/getting-started-llvm-c-api
// http://bytedeco.org/javacpp-presets/llvm/apidocs/org/bytedeco/javacpp/LLVM.html
// https://github.com/wickedchicken/llvm-c-example

public class Generator {

   public static void codegen(String name, Exp exp) {
      BytePointer error = new BytePointer((Pointer) null); // Used to retrieve messages from functions
      LLVMLinkInMCJIT();
      LLVMInitializeNativeAsmPrinter();
      LLVMInitializeNativeAsmParser();
      LLVMInitializeNativeDisassembler();
      LLVMInitializeNativeTarget();
      LLVMModuleRef mod = LLVMModuleCreateWithName("__eplan_module");
      LLVMBuilderRef builder = LLVMCreateBuilder();

      // print_double prototype
      LLVMTypeRef[] params = {LLVMDoubleType()};
      LLVMTypeRef funcType = LLVMFunctionType(LLVMVoidType(), new PointerPointer(params), 1, 0);
      LLVMValueRef func = LLVMAddFunction(mod, "__eplan_print_double", funcType);
      LLVMSetLinkage(func, LLVMExternalLinkage);

      // main function generation
      LLVMTypeRef main_type = LLVMFunctionType(LLVMInt32Type(), new PointerPointer((Pointer) null), 0, 0);
      LLVMValueRef main = LLVMAddFunction(mod, "main", main_type);
      LLVMBasicBlockRef entry = LLVMAppendBasicBlock(main, "entry");
      LLVMPositionBuilderAtEnd(builder, entry);
      LLVMValueRef exp_value = exp.codegen(mod, builder);
      LLVMValueRef[] print_double_args = {exp_value};
      LLVMValueRef print_double = LLVMGetNamedFunction(mod, "__eplan_print_double");
      LLVMValueRef print_double_result = LLVMBuildCall(builder, print_double, new PointerPointer(print_double_args), 1, "");
      LLVMValueRef result = LLVMConstInt(LLVMInt32Type(), 0, 0);
      LLVMBuildRet(builder, result);

      // analysis and execution
      LLVMVerifyModule(mod, LLVMAbortProcessAction, error);
      LLVMDisposeMessage(error); // Handler == LLVMAbortProcessAction -> No need to check errors

      LLVMExecutionEngineRef engine = new LLVMExecutionEngineRef();
      if (LLVMCreateJITCompilerForModule(engine, mod, 2, error) != 0) {
         em.fatal(error.getString());
         LLVMDisposeMessage(error);
      }

      LLVMDumpModule(mod);

      if (LLVMPrintModuleToFile(mod, name + ".ll", error) != 0) {
         em.fatal(error.getString());
         LLVMDisposeMessage(error);
      }

      LLVMDisposeBuilder(builder);
      LLVMDisposeExecutionEngine(engine);
   }

}
