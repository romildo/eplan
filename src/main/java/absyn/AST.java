package absyn;

import javaslang.render.ToTree;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import parse.Loc;

public abstract class AST implements ToTree<String> {

   // Location where the phrase was found in the source code
   protected final Loc loc;

   public AST(Loc loc) {
      this.loc = loc;
   }

   @Override
   public String toString() {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
   }

}
