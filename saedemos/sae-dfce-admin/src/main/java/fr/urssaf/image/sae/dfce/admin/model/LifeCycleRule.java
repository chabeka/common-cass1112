package fr.urssaf.image.sae.dfce.admin.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Classe représentant le référentiel des codes RND.
 * 
 * @author Rhofir
 */
@XStreamAlias("LifeCycleRule")
public class LifeCycleRule {
   @XStreamImplicit(itemFieldName = "Rule")
   private List<Rule> rules;

   /**
    * @return Liste de type {@link Rule}
    */
   public final List<Rule> getRules() {
      return rules;
   }

   /**
    * @param rules
    *           : Liste de type {@link Rule}.
    */
   public final void setTypeDocuments(final List<Rule> rules) {
      this.rules = rules;
   }
}
