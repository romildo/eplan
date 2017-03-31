package types;

public class BOOL extends Type {

   public static final BOOL T = new BOOL();

   private BOOL() {
   }

   @Override
   public String toString() {
      return "bool";
   }
}
