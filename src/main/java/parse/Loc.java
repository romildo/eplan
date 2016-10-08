package parse;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class Loc {

   public final Location left;
   public final Location right;

   private Loc(Location left, Location right) {
      this.left = left;
      this.right = right;
   }

   public static Loc loc() {
      return loc(new Location(-1, -1));
   }

   public static Loc loc(Location left) {
      return loc(left, left);
   }

   public static Loc loc(Location left, Location right) {
      return new Loc(left, right);
   }

   @Override
   public String toString() {
      if (left.getUnit().equals("unknown") && right.getUnit().equals("unknown"))
         return String.format("%d/%d-%d/%d",
                              left.getLine(), left.getColumn(),
                              right.getLine(), right.getColumn());
      else if (left.getUnit().equals(right.getUnit()))
         return String.format("%s:%d/%d-%d/%d",
                              left.getUnit(),
                              left.getLine(), left.getColumn(),
                              right.getLine(), right.getColumn());
      else
         return String.format("%s:%d/%d-%s:%d/%d",
                              left.getUnit(),
                              left.getLine(), left.getColumn(),
                              right.getUnit(),
                              right.getLine(), right.getColumn());
   }

}
