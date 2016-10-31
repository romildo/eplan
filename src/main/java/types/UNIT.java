package types;

public class UNIT extends Type {

   public static final UNIT T = new UNIT();

   private UNIT() {
   }

   @Override
   public String toString() {
      return "unit";
   }

}
