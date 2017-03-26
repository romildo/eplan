package absyn;

import env.Env;
import javaslang.collection.List;
import javaslang.collection.Tree;
import javaslang.control.Option;
import parse.Loc;
import semantic.SemanticHelper;
import types.RECORD;
import types.Type;

public class ExpRecord extends Exp{

    public final String typeName;
    public final List<ParameterWithExp> elements;

    public ExpRecord(Loc loc, String typeName, List<ParameterWithExp> elements) {
        super(loc);
        this.typeName = typeName;
        this.elements = elements;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of("ExpRecord",Tree.of(typeName),Tree.of("Elements", elements.map(ParameterWithExp::toTree)));
    }

    @Override
    protected Type semantic_(Env env) {
        Type t_typeName = env.tenv.get(typeName);
        if (t_typeName == null)
            throw SemanticHelper.undefined(loc,"type",typeName);
        else {
            t_typeName = t_typeName.actual();
            if (!(t_typeName instanceof RECORD))
                throw SemanticHelper.recordMismatch(loc,t_typeName);
        }

        List<Parameter> t_parameters = ((RECORD) t_typeName).elements;
        //Lista para verificar a duplicidade entre variaveis
        List<String> verify = List.empty();

        for (ParameterWithExp parameterWithExp: elements ) {
            Type t_parameterWithExp = parameterWithExp.semantic_(env);

            if (verify.contains(parameterWithExp.name))
                throw SemanticHelper.duplicatedParameter(loc,parameterWithExp.name);
            else{
                 Option<Parameter> t_p = t_parameters.find(p -> p.name == parameterWithExp.name);
                 if (t_p.isEmpty())
                     throw SemanticHelper.unknownRecordParameter(loc,parameterWithExp.name);
                 else{
                     Type t_aux = t_p.get().semantic_(env);
                     if (!t_parameterWithExp.is(t_aux))
                         throw SemanticHelper.recordTypeMismatch(loc, parameterWithExp.name, t_parameterWithExp, t_aux);
                     t_parameters = t_parameters.remove(t_p.get());
                 }
                 verify = verify.prepend(parameterWithExp.name);
            }
        }
        if (! t_parameters.isEmpty())
            throw SemanticHelper.missingParametersRecord(loc, t_parameters.map(p -> p.name));

        return t_typeName;
    }
}
