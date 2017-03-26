package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.ARRAY;
import types.RECORD;
import types.Type;

import static semantic.SemanticHelper.undefined;

public class TyRecord extends Ty {

    public final List<Parameter> elements;

    public TyRecord(Loc loc, List<Parameter> elements) {
        super(loc);
        this.elements = elements;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("TyRecord: ",elements.map(Parameter::toTree));
    }

    @Override
    public Type semantic(Env env) {
        List<Type> t_elements = elements.map(parameter -> parameter.semantic_(env));
        return new RECORD(elements);
    }


}
