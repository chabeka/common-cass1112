package fr.urssaf.image.sae.extraitdonnees.bean;

/**
 * Les compteurs pour le service d'extraction des UUID
 */
public final class Compteurs {

   private int nbDocsSortis = 0;
   private int nbDocsDansBase = 0;
   private int nbDocsTraites = 0;

   /**
    * Le nombre de documents écrits dans le fichier de sortie
    * 
    * @return Le nombre de documents écrits dans le fichier de sortie
    */
   public int getNbDocsSortis() {
      return nbDocsSortis;
   }

   /**
    * Le nombre de documents écrits dans le fichier de sortie
    * 
    * @param nbDocsSortis
    *           Le nombre de documents écrits dans le fichier de sortie
    */
   public void setNbDocsSortis(int nbDocsSortis) {
      this.nbDocsSortis = nbDocsSortis;
   }

   /**
    * Le nombre de documents analysés dans la base GED
    * 
    * @return Le nombre de documents analysés dans la base GED
    */
   public int getNbDocsDansBase() {
      return nbDocsDansBase;
   }

   /**
    * Le nombre de documents analysés dans la base GED
    * 
    * @param nbDocsDansBase
    *           Le nombre de documents analysés dans la base GED
    */
   public void setNbDocsDansBase(int nbDocsDansBase) {
      this.nbDocsDansBase = nbDocsDansBase;
   }

   /**
    * Le nombre total de documents analysés
    * 
    * @return Le nombre total de documents analysés
    */
   public int getNbDocsTraites() {
      return nbDocsTraites;
   }

   /**
    * Le nombre total de documents analysés
    * 
    * @param nbDocsTraites
    *           Le nombre total de documents analysés
    */
   public void setNbDocsTraites(int nbDocsTraites) {
      this.nbDocsTraites = nbDocsTraites;
   }

   /**
    * Incrémente de 1 le nombre de documents écrits dans le fichier de sortie
    */
   public void incrementeNbDocsSortis() {
      nbDocsSortis++;
   }

   /**
    * Incrémente de 1 le nombre de documents analysés dans la base GED
    */
   public void incrementeNbDocsDansBase() {
      nbDocsDansBase++;
   }

   /**
    * Incrémente de 1 le nombre total de documents analysés
    */
   public void incrementeNbDocsTraites() {
      nbDocsTraites++;
   }

}
