package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.Type;
import types.UNIT;

import static semantic.SemanticHelper.syntaxError;
import static semantic.SemanticHelper.fieldAlreadyExists;

/**
 * Created by Christian on 29/03/2017.
 */
public class TyRecord extends Ty {
    public final List<Exp> fields;

    public TyRecord(Loc loc, List<Exp> fields) {
        super(loc);
        this.fields = fields;
    }

    @Override
    public Type semantic(Env env) {
        List<String> names = List.empty();
        for(Exp e : fields) {
            if(!(e instanceof ExpField))
                throw syntaxError(e.loc);
            else {
                e.semantic(env);
                ExpField ef = (ExpField) e;
                if(names.isEmpty())
                    names = names.append(ef.name);
                else {
                    for (String name : names) {
                        if (!name.equals(ef.name))
                            names = names.append(ef.name);
                        else
                            throw fieldAlreadyExists(ef.loc);
                    }
                }
            }
        }
        return UNIT.T;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("TyRecord", fields.map(Exp::toTree));
    }
}

// let type t1 = [bool] var a = @t1[true, false, 3 <= 3, 2 != 4] in (2+3)