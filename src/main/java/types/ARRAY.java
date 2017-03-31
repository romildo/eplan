package types;

public class ARRAY extends Type {

    public Type type;

    public ARRAY(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "array " + type.toString();
    }
}
