package fr.urssaf.image.sae.integration.ihmweb.modele;



/**
 * Mode d'appel à la consultation :<br>
 * <br>
 * <ul>
 *    <li>ancien service : sans MTOM</li>
 *    <li>nouveau service : avec MTOM</li>
 * </ul>
 */
public enum ModeConsultationEnum {

   
   /**
    * Ancien service de consultation (sans MTOM)
    */
   AncienServiceSansMtom {
      
//      @Override
//      public String toString() {
//         return "Ancien service - sans MTOM";
//      }
      
   },
   
   
   /**
    * Nouveau service de consultation (avec MTOM)
    */
   NouveauServiceAvecMtom {
      
//      @Override
//      public String toString() {
//         return "Nouveau service - avec MTOM";
//      }
      
   }
   
   
}
