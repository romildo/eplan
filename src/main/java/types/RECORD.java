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
    public Tree.Node<String> toTree() {
        return Tree.of("RECORD", elements.map(Parameter::toTree));
    }
}
