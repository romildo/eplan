package absyn;

import env.Env;
import error.CompilerError;
import javaslang.collection.List;
import javaslang.collection.Tree;
import parse.Loc;
import types.ARRAY;
import types.NAME;
import types.RECORD;
import types.Type;

import static semantic.SemanticHelper.undefined;

public class DecTypeMutual extends Dec {

   public final List<DecType> decs;

   public DecTypeMutual(Loc loc, List<DecType> decs) {
      super(loc);
      this.decs = decs;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of("DecTypeMutual", decs.map(DecType::toTree));
   }

   @Override
   public void semantic(Env env) {
      for(DecType d: decs) {
         if(d.ty instanceof TyName)
            env.tenv.put(d.name, new NAME(d.name));
         else if(d.ty instanceof TyArray)
            env.tenv.put(d.name, new ARRAY());
         else if(d.ty instanceof TyRecord)
            env.tenv.put(d.name, new RECORD());
         else
            throw new CompilerError("undefined ty, not recognized");
      }

      for(DecType d: decs) {
         Type type = env.tenv.get(d.name);
         Type t = d.ty.semantic(env);
         if(type instanceof NAME)
            ((NAME) type).binding = t;
         else if(type instanceof ARRAY) {
            ((ARRAY) type).tElements = t;
         }
         else if(type instanceof RECORD) {
            RECORD r = (RECORD) type;
            TyRecord ty = (TyRecord) d.ty;
            for(Exp e : ty.fields) {
               ExpField field = (ExpField) e;
               String name = field.name;
               Type tfield = env.tenv.get(field.type);
               if(tfield == null)
                  throw new CompilerError("FATAL BUG no semantic do record em DecTypeMutual!!!!!");
               else
                  r.fields.put(name, tfield);
            }
         }
         else
            throw new CompilerError("bug!!!!!!");
      }
   }
}
