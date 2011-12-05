package fr.urssaf.image.sae.services.enrichment.xml.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Classe représentant le référentiel des codes RND.
 * 
 * @author Rhofir
 */
@XStreamAlias("RCND")
public class RndReference {
   @XStreamImplicit(itemFieldName = "TypeDocument")
   private List<TypeDocument> typeDocuments;

   /**
    * @return Liste de type {@link TypeDocument}
    */
   public final List<TypeDocument> getTypeDocuments() {
      return typeDocuments;
   }

   /**
    * @param typeDocuments
    *           : Liste de type {@link TypeDocument}.
    */
   public final void setTypeDocuments(List<TypeDocument> typeDocuments) {
      this.typeDocuments = typeDocuments;
   }
}
