package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf;

import com.netflix.astyanax.annotations.Component;

/**
 * Classe représentant la famille de colonne TermInfoRangeString
 * 
 * 
 */
public class DocInfoColumn {

   @Component(ordinal = 0)
   private String name;

   /**
    * @return the name
    */
   public final String getName() {
      return name;
   }

   /**
    * @param name the name to set
    */
   public final void setName(String name) {
      this.name = name;
   }

   
}
