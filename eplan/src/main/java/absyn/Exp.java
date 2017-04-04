package absyn;

import env.Env;
import parse.Loc;
import types.Type;

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
   public Type semantic(Env env) {
      type = semantic_(env);
      return type;
   }

   // Type check the expression. Should be defined in the concrete subclasses.
   protected abstract Type semantic_(Env env);
}
