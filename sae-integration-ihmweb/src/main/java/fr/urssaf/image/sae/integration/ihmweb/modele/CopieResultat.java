package fr.urssaf.image.sae.integration.ihmweb.modele;

import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.UuidType;

/**
 * Résultat d'un appel au service de copie (ou copieMTOM)
 */
public class CopieResultat {

   private UuidType idGed;

   /**
    * Constructeur
    */
   public CopieResultat() {

   }

   /**
    * Constructeur
    * 
    * @param idGed
    *           Identifiant de l'archive GED
    */
   public CopieResultat(final UuidType idGed) {
      this.idGed = idGed;
   }

   /**
    * Getter pour idGed
    * 
    * @return the idGed
    */
   public UuidType getIdGed() {
      return idGed;
   }

   /**
    * Setter pour idGed
    * 
    * @param idGed
    *           the idGed to set
    */
   public void setIdGed(UuidType idGed) {
      this.idGed = idGed;
   }


}
