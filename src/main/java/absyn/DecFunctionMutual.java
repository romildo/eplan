package absyn;

import env.Env;
import error.CompilerError;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.*;

import static semantic.SemanticHelper.*;

public class DecFunctionMutual extends Dec {

   public final List<DecFunction> decs;

   public DecFunctionMutual(Loc loc, List<DecFunction> decs) {
      super(loc);
      this.decs = decs;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of("DecFunctionMutual", decs.map(DecFunction::toTree));
   }

   @Override
   public void semantic(Env env) {
      for(DecFunction d: decs) {
         List<String> names = List.empty();
         List<Type> types = List.empty();
         for(Parameter p: d.params){
            if(names.isEmpty())
               names = names.append(p.name);
            else {
               for (String name : names) {
                  if (!name.equals(p.name))
                     names = names.append(p.name);
                  else
                     throw fieldAlreadyExists(p.loc);
               }
            }
            Type t = env.tenv.get(p.type);
            if(t == null)
               throw undefined(p.loc, "type", p.type);
            else
               types = types.append(t);
         }
         Type t = env.tenv.get(d.type);
         if(t == null)
            throw undefined(loc, "type", d.type);
         else {
            Type nameOfFunction = env.venv.get(d.name);
            if(nameOfFunction == null)
               env.venv.put(d.name, new FUNCTION(t, List.of()));
            else
               throw functionAlreadyExist(d.loc);
         }
      }

      for(DecFunction d: decs) {
         env.venv.beginScope();
         for(Parameter p: d.params){
            Type t = env.tenv.get(p.type);
            env.venv.put(p.name, t);
         }
         Type expected = env.tenv.get(d.type);
         Type response = d.body.semantic(env);
         if(!response.is(expected))
            throw typeMismatch(d.body.loc, response, expected);
         env.venv.endScope();
      }
   }
}
