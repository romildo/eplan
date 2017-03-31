package parse;

import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.Symbol;

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

   public static Loc loc(Symbol symbol) {
      return loc(new Location(symbol.left, symbol.right));
   }

   public static Loc loc(Symbol a, Symbol b) {
      return loc(new Location(a.left, b.right));
   }

   public static Loc loc(ComplexSymbol symbol) {
      return new Loc(symbol.getLeft(), symbol.getRight());
   }

   public static Loc loc(ComplexSymbol a, ComplexSymbol b) {
      return new Loc(a.getLeft(), b.getRight());
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
