package env;

import types.*;

public class Env {

  public Table<Type> tenv;
  public Table<Type> venv;

  public Env() {
    tenv = new Table<Type>();
    put(tenv, "unit", UNIT.T);
    put(tenv, "int",  INT.T);
    put(tenv, "real", REAL.T);

    venv = new Table<Type>();
    put(venv, "print_int",  new FUNCTION(UNIT.T, INT.T));
    put(venv, "print_real", new FUNCTION(UNIT.T, REAL.T));
    put(venv, "round",      new FUNCTION(INT.T, REAL.T));
    put(venv, "ceil",       new FUNCTION(INT.T, REAL.T));
    put(venv, "floor",      new FUNCTION(INT.T, REAL.T));
    put(venv, "real",       new FUNCTION(REAL.T, INT.T));
  }

  @Override
  public String toString() {
    return "Env{" +
           "tenv=" + tenv +
           ", venv=" + venv +
           '}';
  }

  private static <E> void put(Table<E> table, String name, E value) {
    table.put(name.intern(), value);
  }

}
