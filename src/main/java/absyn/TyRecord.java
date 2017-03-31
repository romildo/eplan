package absyn;

import env.Env;
import error.CompilerError;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.ARRAY;
import types.RECORD;
import types.Type;

import static semantic.SemanticHelper.undefined;

public class TyRecord extends Ty {

    public final List<Param> params;

    public TyRecord(Loc loc, List<Param> params) {
        super(loc);
        this.params = params;
    }

    @Override
    public Type semantic(Env env) {
        List<Tuple2<String, Type>> t_params = params.map(p -> new Tuple2<>(p.name, p.semantic(env)));
        for (Tuple2<String, Type> t : t_params) {
            int cont = 0;
            for (Tuple2<String, Type> t2 : t_params) {
                if (t._1().equals(t2._1())) {
                    cont++;
                }
            }
            if (cont > 1) {
                throw new CompilerError("error: two fields with same name");
            }
        }
        return new RECORD(t_params);
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("TyRecord", params.map(Param::toTree));
    }
}
