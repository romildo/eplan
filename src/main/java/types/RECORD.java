package types;

import javaslang.Tuple2;
import javaslang.collection.List;

public class RECORD extends Type {

    public List<Tuple2<String, Type>> params;

    public RECORD(List<Tuple2<String, Type>> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Tuple2<String, Type> t : params) {
            result.append(t._1()).append(": ").append(t._2().toString()).append(", ");
        }
        return "record: " + result.toString();
    }
}
