package fr.urssaf.image.sae.integration.ihmweb.modele;

/**
 * Mode d'appel à l'archivage unitaire :<br>
 * <br>
 * <ul>
 * <li>Opération archivageUnitaire (avec URL ECDE)</li>
 * <li>Opération archivageUnitairePJ avec URL ECDE</li>
 * <li>Opération archivageUnitairePJ avec envoi du contenu et sans MTOM</li>
 * <li>Opération archivageUnitairePJ avec envoi du contenu et avec MTOM</li>
 * </ul>
 */
public enum ModeArchivageUnitaireEnum {

   /**
    * Opération archivageUnitaire (avec URL ECDE)
    */
   archivageUnitaire,

   /**
    * Opération archivageUnitairePJ avec URL ECDE
    */
   archivageUnitairePJUrlEcde,

   /**
    * Opération archivageUnitairePJ avec envoi du contenu et sans MTOM
    */
   archivageUnitairePJContenuSansMtom,

   /**
    * Opération archivageUnitairePJ avec envoi du contenu et avec MTOM
    */
   archivageUnitairePJContenuAvecMtom

}
