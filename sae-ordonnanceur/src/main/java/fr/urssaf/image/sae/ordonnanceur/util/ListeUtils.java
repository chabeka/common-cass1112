package fr.urssaf.image.sae.ordonnanceur.util;

import java.net.URI;
import java.util.List;

/**
 * Méthodes utilitaires pour les listes
 */
public final class ListeUtils {

   private ListeUtils() {
      // Constructeur privé
   }

   /**
    * Maintien la liste passée en paramètre en dessous d'un certain nombre
    * d'éléments<br>
    * Si le nombre maximal d'éléments est atteint, vide la liste de moitié en
    * partant des éléments les plus anciens
    * 
    * @param liste
    *           la liste
    * @param nbMaxElements
    *           le nombre maximal d'éléments de la liste
    */
   public static void nettoieListeSiBesoin(List<URI> liste, int nbMaxElements) {

      if (liste.size() > nbMaxElements) {

         // vide la moitié de la liste
         while (liste.size()>(nbMaxElements / 2)) {
            liste.remove(0);
         }
         
      }

   }

}
