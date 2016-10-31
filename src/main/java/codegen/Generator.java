package codegen;

import absyn.Exp;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import types.INT;
import types.REAL;
import types.Type;

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

      // default library
      addRuntime(mod, builder);

      // main function generation
      LLVMTypeRef main_type = LLVMFunctionType(LLVMInt32Type(), new PointerPointer((Pointer) null), 0, 0);
      LLVMValueRef main = LLVMAddFunction(mod, "main", main_type);
      LLVMBasicBlockRef entry = LLVMAppendBasicBlock(main, "entry");
      LLVMPositionBuilderAtEnd(builder, entry);
      LLVMValueRef exp_value = exp.codegen(mod, builder);
      LLVMValueRef print_result = addPrintResult(mod, builder, exp.type, exp_value);
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

      // LLVMDumpModule(mod);

      if (LLVMPrintModuleToFile(mod, name + ".ll", error) != 0) {
         em.fatal(error.getString());
         LLVMDisposeMessage(error);
      }
      else
         System.out.printf("target code written to %s.ll%n", name);

      LLVMDisposeBuilder(builder);
      LLVMDisposeExecutionEngine(engine);
   }

   public static LLVMValueRef addCall(LLVMModuleRef module,
                                      LLVMBuilderRef builder,
                                      String function,
                                      LLVMValueRef... args) {
      LLVMValueRef f = LLVMGetNamedFunction(module, function);
      return LLVMBuildCall(builder, f, new PointerPointer(args), args.length, "");
   }

   public static void addPrototype(LLVMModuleRef module,
                                   LLVMBuilderRef builder,
                                   String function,
                                   LLVMTypeRef result,
                                   LLVMTypeRef... args) {
      LLVMTypeRef funcType = LLVMFunctionType(result, new PointerPointer(args), args.length, 0);
      LLVMValueRef f = LLVMAddFunction(module, function, funcType);
      LLVMSetLinkage(f, LLVMExternalLinkage);
   }

   public static void addRuntime(LLVMModuleRef module,
                                 LLVMBuilderRef builder) {
      addPrototype(module, builder, "__eplan_print_double", LLVMVoidType(), LLVMDoubleType());
      addPrototype(module, builder, "llvm.pow.f64", LLVMDoubleType(), LLVMDoubleType(), LLVMDoubleType());
   }

   public static LLVMValueRef addPrintResult(LLVMModuleRef module,
                                             LLVMBuilderRef builder,
                                             Type t_exp,
                                             LLVMValueRef v_exp) {
      if (t_exp instanceof REAL) {
         return addCall(module, builder, "__eplan_print_double", v_exp);
      }
      if (t_exp instanceof INT){
         return addCall(module, builder, "__eplan_print_int", v_exp);
      }

      return LLVMConstReal(LLVMInt32Type(), 0);
   }

}
