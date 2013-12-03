package fr.urssaf.image.sae.format.model;

/**
 * Objet permettant de manipuler le traitement de la méthode identifyFile de
 * l'interface Identifier.
 * 
 */
public class EtapeEtResultat {

   private String etape;
   private String resultat;

   /**
    * @return the resultat
    */
   public final String getResultat() {
      return resultat;
   }

   /**
    * @param resultat
    *           the resultat to set
    */
   public final void setResultat(String resultat) {
      this.resultat = resultat;
   }

   /**
    * @return the etape
    */
   public final String getEtape() {
      return etape;
   }

   /**
    * @param etape
    *           the etape to set
    */
   public final void setEtape(String etape) {
      this.etape = etape;
   }

}
