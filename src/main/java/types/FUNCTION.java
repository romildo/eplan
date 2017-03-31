package types;

import javaslang.collection.List;

public class FUNCTION extends Type {

   public Type result;
   public List<Type> formals;

   public FUNCTION(Type result, List<Type> formals) {
      this.result = result;
      this.formals = formals;
   }

   public FUNCTION(Type result, Type... formals) {
      this(result, List.of(formals));
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("(");
      formals.map(Type::toString).intersperse(",");
      builder.append("->");
      builder.append(result.toString());
      return builder.toString();
   }

}
