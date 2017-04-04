package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.FUNCTION;
import types.Type;

import static semantic.SemanticHelper.*;


public class ExpCall extends Exp {

   public final String function;
   public final List<Exp> args;

   public ExpCall(Loc loc, String function, List<Exp> args) {
      super(loc);
      this.function = function;
      this.args = args;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpCall"),
                     Tree.of(function.toString()),
                     Tree.of("args",
                             args.map(Exp::toTree)));
   }

   @Override
   protected Type semantic_(Env env) {
      // analyse all arguments
      List<Type> t_args = args.map(exp -> exp.semantic(env));
      // search the function name in the environment
      Type entry = env.venv.get(function);
      // check whether it was found and report error if not found
      if (entry == null)
         throw undefined(loc, "function", function);
      // check whether it really names a function
      if (!(entry instanceof FUNCTION))
         throw notAFunction(loc, function);
      // it is a function, so get a more specific version of the entry
      FUNCTION signature = (FUNCTION) entry;
      // check the arguments: walk the list of parameters and the list of
      // arguments in parallel, checking each argument
      // it finishes when the end of any of the lists are reached
      List<Type> parameters = signature.formals;
      List<Exp> arguments = args;
      List<Type> t_arguments = t_args;
      while (parameters.nonEmpty() && arguments.nonEmpty()) {
         Exp arg = arguments.head();
         Type t_arg = t_arguments.head();
         Type t_par = parameters.head();
         // are the argument of the expected type?
         if (!t_arg.is(t_par))
            throw typeMismatch(arg.loc, t_arg, t_par);
         // advances to the next argument
         parameters = parameters.tail();
         arguments = arguments.tail();
         t_arguments = t_arguments.tail();
      }
      // at the end there may be more arguments...
      if (arguments.nonEmpty())
         throw tooMuchArguments(loc, function);
      // ... or more parameters
      if (parameters.nonEmpty())
         throw tooFewArguments(loc, function);
      return signature.result;
   }

}
