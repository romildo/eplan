package types;

public class ARRAY extends Type {
    public Type tElements;

    public ARRAY() {}
    public ARRAY(Type tElem) {
        this.tElements = tElem;
    }

    @Override
    public String toString() {
        return "ARRAY" + tElements.toString();
    }
}
