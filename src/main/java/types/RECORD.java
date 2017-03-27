package types;

import absyn.Parameter;
import javaslang.collection.List;
import javaslang.collection.Tree;

public class RECORD extends Type {

    public final List<Parameter> elements;

    public RECORD(List<Parameter> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        final int n = elements.size();
        if (n > 0) {
            builder.append(elements.get());
            if (n > 1) {
                for (int i = 1; i < n; i++)
                    builder.append(", ").append(elements.get(i));
            }
        }
        return "RECORD  {" + builder + "}";
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("RECORD", elements.map(Parameter::toTree));
    }
}
