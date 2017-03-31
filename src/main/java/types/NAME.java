package types;

import javaslang.collection.Tree;

/**
 * Created by Christian on 20/03/2017.
 */
public class NAME extends Type {
    public final String name;
    public Type binding;

    public NAME(String name) {
        this.name = name;
    }

    @Override
    public Type actual() {
        return binding.actual();
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("NAME: " + name);
    }

    @Override
    public boolean is(Type type) {
        return binding.is(type);
    }
}
