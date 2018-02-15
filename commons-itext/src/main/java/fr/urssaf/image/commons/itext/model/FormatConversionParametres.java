package fr.urssaf.image.commons.itext.model;

/**
 * Objet du modele permettant de savoir de quel page a quel page ont converti le
 * tiff en pdf.
 */
public class FormatConversionParametres {

   /**
    * Numero de page de debut.
    */
   private int numeroPageDebut;

   /**
    * Numero de page de fin.
    */
   private int numeroPageFin;

   /**
    * Constructeur avec parametres.
    * 
    * @param numeroPageDebut
    *           numero de page de debut
    * @param numeroPageFin
    *           numero de page de fin
    */
   public FormatConversionParametres(int numeroPageDebut, int numeroPageFin) {
      super();
      this.numeroPageDebut = numeroPageDebut;
      this.numeroPageFin = numeroPageFin;
   }

   /**
    * Getter sur le numero de page de debut.
    * 
    * @return int numero de page de debut
    */
   public final int getNumeroPageDebut() {
      return numeroPageDebut;
   }

   /**
    * Setter sur le numero de page de debut.
    * 
    * @param numeroPageDebut
    *           numero de page de debut
    */
   public final void setNumeroPageDebut(final int numeroPageDebut) {
      this.numeroPageDebut = numeroPageDebut;
   }

   /**
    * Getter sur le numero de page de fin.
    * 
    * @return int numero de page de fin
    */
   public final int getNumeroPageFin() {
      return numeroPageFin;
   }

   /**
    * Setter sur le numero de page de fin.
    * 
    * @param numeroPageFin
    *           numero de page de fin
    */
   public final void setNumeroPageFin(final int numeroPageFin) {
      this.numeroPageFin = numeroPageFin;
   }

}
