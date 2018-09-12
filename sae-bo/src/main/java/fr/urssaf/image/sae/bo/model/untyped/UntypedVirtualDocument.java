/**
 * 
 */
package fr.urssaf.image.sae.bo.model.untyped;

import java.util.List;

import fr.urssaf.image.sae.bo.model.AbstractVirtualDocument;

/**
 * Classe représentant un document virtuel. C'est-à-dire la référence d'un
 * fichier, ainsi que les métadonnées non typées associées au document virtuel
 * 
 */
public class UntypedVirtualDocument extends AbstractVirtualDocument {

   private List<UntypedMetadata> uMetadatas;

   /**
    * @return la liste des métadonnées métier
    */
   public final List<UntypedMetadata> getuMetadatas() {
      return uMetadatas;
   }

   /**
    * @param uMetadatas
    *           la liste des métadonnées métier
    */
   public final void setuMetadatas(List<UntypedMetadata> uMetadatas) {
      this.uMetadatas = uMetadatas;
   }

}
