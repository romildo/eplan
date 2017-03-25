package types;

public class ARRAY extends Type {

   public final Type typeName;

   public ARRAY(Type typeName) {
      this.typeName = typeName;
   }

   @Override
   public String toString() {
      return "Array of "+ typeName;
   }
}
