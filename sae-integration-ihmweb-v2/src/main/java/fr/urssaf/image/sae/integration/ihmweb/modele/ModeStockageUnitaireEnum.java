package fr.urssaf.image.sae.integration.ihmweb.modele;

/**
 * Mode d'appel au stockage unitaire :<br>
 * <br>
 * <ul>
 * <li>Opération stockageUnitaireAvecUrlEcde (avec URL ECDE)</li>
 * <li>Opération stockageUnitaireAvecContenuAvecMTOM  avec envoi du contenu et sans MTOM</li>
 * <li>Opération stockageUnitaireAvecContenuSansMTOM  avec envoi du contenu et sans MTOM</li>
 * </ul>
 */
public enum ModeStockageUnitaireEnum {

   /**
    * Opération stockageUnitaire (avec URL ECDE)
    */
   stockageUnitaireAvecUrlEcde,

   /**
    * Opération stockageUnitairePJ avec envoi du contenu et sans MTOM
    */
   stockageUnitaireAvecContenuAvecMTOM,

   /**
    * Opération stockageUnitairePJ avec envoi du contenu et avec MTOM
    */
   stockageUnitaireAvecContenuSansMTOM

}
