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
    * Constructeur par défaut
    */
   public EtapeEtResultat() {
      // Constructeur par défaut
   }

   /**
    * Constructeur
    * 
    * @param etape
    *           Le libellé de l'étape
    * @param resultat
    *           Des informations sur le résultat de l'étape
    */
   public EtapeEtResultat(String etape, String resultat) {
      this.etape = etape;
      this.resultat = resultat;
   }

   /**
    * Des informations sur le résultat de l'étape
    * 
    * @return Des informations sur le résultat de l'étape
    */
   public final String getResultat() {
      return resultat;
   }

   /**
    * Des informations sur le résultat de l'étape
    * 
    * @param resultat
    *           Des informations sur le résultat de l'étape
    */
   public final void setResultat(String resultat) {
      this.resultat = resultat;
   }

   /**
    * Le libellé de l'étape
    * 
    * @return Le libellé de l'étape
    */
   public final String getEtape() {
      return etape;
   }

   /**
    * Le libellé de l'étape
    * 
    * @param etape
    *           Le libellé de l'étape
    */
   public final void setEtape(String etape) {
      this.etape = etape;
   }

}
