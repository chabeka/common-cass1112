/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.model;

import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.ComposantDocumentVirtuelType;

/**
 * objet permettant de connaitre l'index fichier de référence dans lequel se
 * trouve le document virtuel actuel
 * 
 */
public class SaeComposantVirtuelType extends ComposantDocumentVirtuelType {

   private int index;

   /**
    * @return l'index du fichier de référence
    */
   public final int getIndex() {
      return index;
   }

   /**
    * @param index
    *           l'index du fichier de référence
    */
   public final void setIndex(int index) {
      this.index = index;
   }

}
