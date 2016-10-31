package env;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

class Binder<V> {
   V value;
   String prevtop;
   Binder<V> tail;

   Binder(V v, String p, Binder<V> t) {
      value = v;
      prevtop = p;
      tail = t;
   }
}

/**
 * The Table class is similar to java.util.Dictionary, except that each key must
 * be a symbol and there is a scope mechanism.
 */

public class Table<V> {
   private Map<String, Binder<V>> dict;
   private String top;
   private Binder<V> marks;

   public Table() {
      dict = new IdentityHashMap<String, Binder<V>>();
   }

   /**
    * Gets the object associated with the specified symbol in the Table.
    */
   public V get(String key) {
      Binder<V> e = dict.get(key);
      if (e == null)
         return null;
      else
         return e.value;
   }

   /**
    * Puts the specified value into the Table, bound to the specified symbol.
    */
   public void put(String key, V value) {
      dict.put(key, new Binder<V>(value, top, dict.get(key)));
      top = key;
   }

   /**
    * Remembers the current state of the Table.
    */
   public void beginScope() {
      marks = new Binder<V>(null, top, marks);
      top = null;
   }

   /**
    * Restores the table to what it was at the most recent beginScope that has
    * not already been ended.
    */
   public void endScope() {
      while (top != null) {
         Binder<V> e = dict.get(top);
         if (e.tail != null)
            dict.put(top, e.tail);
         else
            dict.remove(top);
         top = e.prevtop;
      }
      top = marks.prevtop;
      marks = marks.tail;
   }

   /**
    * Returns a Set view of the Table's symbols.
    */
   public Set<String> keySet() {
      return dict.keySet();
   }
}
