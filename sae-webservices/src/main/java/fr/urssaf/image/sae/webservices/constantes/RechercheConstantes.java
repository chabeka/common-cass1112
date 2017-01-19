package fr.urssaf.image.sae.webservices.constantes;

/**
 * Constantes du service de recherche
 */
public final class RechercheConstantes {

   private RechercheConstantes() {
      // Constructeur privé
   }

   /**
    * Le nombre maximal de résultats de recherche que le service de recherche
    * doit renvoyer via la couche web service
    */
   public static final int NB_MAX_RESULTATS_RECHERCHE = 200;

   /**
    * Le nombre maximal de résultats de recherche demandé à DFCE pour la recherche avec 
    * nombre de résultats. Le nombre de résultat renvoyé à la couche web service sera limité
    * à {@link RechercheConstantes#NB_MAX_RESULTATS_RECHERCHE}.
    * Nous avons choisi la limite connu de DFCE, soit 5000 documents.
    */
   public static final int NB_MAX_RESULTATS_RECH_DFCE = 5000;
}
