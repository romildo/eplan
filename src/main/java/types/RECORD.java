package types;

import java.util.HashMap;
import java.util.Map;

public class RECORD extends Type {
    public Map<String, Type> fields;

    public RECORD() { fields = new HashMap<>();}

    @Override
    public String toString() {
        return "RECORD";
    }
}
