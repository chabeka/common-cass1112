/**
 * 
 */
package fr.urssaf.image.sae.bo.model.bo;

import java.util.List;

import fr.urssaf.image.sae.bo.model.AbstractVirtualDocument;

/**
 * Classe représentant un document virtuel. C'est-à-dire la référence d'un
 * fichier, ainsi que les métadonnées typées associées au document virtuel
 * 
 */
public class SAEVirtualDocument extends AbstractVirtualDocument {

   private List<SAEMetadata> metadatas;

   /**
    * @return la liste des métadonnées métier
    */
   public final List<SAEMetadata> getMetadatas() {
      return metadatas;
   }

   /**
    * @param metadatas
    *           la liste des métadonnées métier
    */
   public final void setMetadatas(List<SAEMetadata> metadatas) {
      this.metadatas = metadatas;
   }

}
