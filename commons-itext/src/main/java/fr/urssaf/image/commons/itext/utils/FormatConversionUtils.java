package fr.urssaf.image.commons.itext.utils;

import fr.urssaf.image.commons.itext.exception.FormatConversionParametrageException;
import fr.urssaf.image.commons.itext.model.FormatConversionParametres;

/**
 * Classe utilitaire permettant de calculer les paramètres de conversion.
 */
public final class FormatConversionUtils {

   /**
    * Constructeur privee.
    */
   private FormatConversionUtils() {
      super();
   }

   /**
    * Methode permettant d'avoir les parametres de conversion.
    * 
    * @param numeroPage
    *           numero de page demande
    * @param nombrePages
    *           nombre de pages demande
    * @param nbPagesTotal
    *           nombre de pages total
    * @return FormatConversionParametres parametres de conversion
    * @throws FormatConversionParametrageException
    *            exception de parametrage
    */
   public static FormatConversionParametres getParametresConversion(
         final Integer numeroPage, final Integer nombrePages,
         final int nbPagesTotal) throws FormatConversionParametrageException {

      int numeroPageDebut = -1;
      int numeroPageFin = -1;
      if (numeroPage == null && nombrePages == null) {
         // cas ou on demande le document complet
         numeroPageDebut = 1;
         numeroPageFin = nbPagesTotal;
      } else if (numeroPage != null && numeroPage.intValue() <= 0) {
         // cas du numero de page negatif ou null
         throw new FormatConversionParametrageException(
               "Le numéro de page doit être compris entre la 1ere et la dernière page du document.");
      } else if (numeroPage != null && numeroPage.intValue() > nbPagesTotal) {
         // cas du numero de page superieur au nombres de pages
         throw new FormatConversionParametrageException(
               "Le numéro de page doit être compris entre la 1ere et la dernière page du document.");
      } else if (nombrePages != null && nombrePages.intValue() == 0) {
         // cas du nombre de pages a zero
         throw new FormatConversionParametrageException(
               "Le nombre de pages doit être différent de 0.");
      } else if (numeroPage == null && nombrePages != null
            && nombrePages.intValue() > 0) {
         // cas ou on demande juste un nombre de pages positif
         // on initialise le numero de page de debut a la premiere page
         numeroPageDebut = 1;
         if (nombrePages.intValue() < nbPagesTotal) {
            numeroPageFin = nombrePages.intValue();
         } else {
            numeroPageFin = nbPagesTotal;
         }
      } else if (numeroPage == null && nombrePages != null
            && nombrePages.intValue() < 0) {
         // cas ou on demande juste un nombre de pages negatif
         // on initialise le numero de page de fin a la derniere page
         numeroPageFin = nbPagesTotal;
         if ((-nombrePages.intValue()) < nbPagesTotal) {
            numeroPageDebut = nbPagesTotal + nombrePages.intValue();
         } else {
            numeroPageDebut = 1;
         }
      } else if (numeroPage != null && nombrePages == null) {
         // cas ou on demande d'une page a la fin du document
         numeroPageDebut = numeroPage.intValue();
         numeroPageFin = nbPagesTotal;
      } else if (numeroPage != null && nombrePages != null
            && nombrePages.intValue() > 0) {
         // cas ou on demande d'une page avec un nombre de pages positif
         numeroPageDebut = numeroPage.intValue();
         if ((numeroPage.intValue() + nombrePages.intValue()) < nbPagesTotal) {
            numeroPageFin = numeroPage.intValue() + nombrePages.intValue() - 1;
         } else {
            numeroPageFin = nbPagesTotal;
         }
      } else if (numeroPage != null && nombrePages != null
            && nombrePages.intValue() < 0) {
         // cas ou on demande d'une page avec un nombre de pages negatif
         numeroPageFin = numeroPage.intValue();
         if (numeroPage.intValue() + nombrePages.intValue() > 1) {
            numeroPageDebut = numeroPage.intValue() + nombrePages.intValue()
                  + 1;
         } else {
            numeroPageDebut = 1;
         }
      }
      return new FormatConversionParametres(numeroPageDebut, numeroPageFin);
   }
}
