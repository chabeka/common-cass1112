package fr.urssaf.image.sae.services.document.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Résultat du parsing d'une requête de recherche pour le remplacement des codes
 * longs de métadonnées par leurs codes courts
 */
public class SAESearchQueryParserResult {

   private String requeteOrigine;

   private String requeteCodeCourts;

   private Map<String, String> metaUtilisees = new HashMap<String, String>();

   /**
    * Constructeur
    */
   public SAESearchQueryParserResult() {
      // Constructeur par défaut
   }

   /**
    * Constructeur
    * 
    * @param requeteOrigine
    *           la requête qui a été passée au parser
    */
   public SAESearchQueryParserResult(String requeteOrigine) {
      this.requeteOrigine = requeteOrigine;
   }

   /**
    * La requête qui a été passée au parser
    * 
    * @return La requête qui a été passée au parser
    */
   public final String getRequeteOrigine() {
      return requeteOrigine;
   }

   /**
    * La requête qui a été passée au parser
    * 
    * @param requeteOrigine
    *           La requête qui a été passée au parser
    */
   public final void setRequeteOrigine(String requeteOrigine) {
      this.requeteOrigine = requeteOrigine;
   }

   /**
    * La requête de recherche avec les codes courts
    * 
    * @return La requête de recherche avec les codes courts
    */
   public final String getRequeteCodeCourts() {
      return requeteCodeCourts;
   }

   /**
    * La requête de recherche avec les codes courts
    * 
    * @param requeteCodeCourts
    *           La requête de recherche avec les codes courts
    */
   public final void setRequeteCodeCourts(String requeteCodeCourts) {
      this.requeteCodeCourts = requeteCodeCourts;
   }

   /**
    * La liste des métadonnées utilisées dans la requête d'origine.<br>
    * <ul>
    * <li>Clé de la map : code long de la métadonnée</li>
    * <li>Valeur associée : code court de la métadonnée</li>
    * </ul>
    * 
    * @return La liste des métadonnées utilisées dans la requête d'origine.
    */
   public final Map<String, String> getMetaUtilisees() {
      return metaUtilisees;
   }

   /**
    * La liste des métadonnées utilisées dans la requête d'origine.<br>
    * <ul>
    * <li>Clé de la map : code long de la métadonnée</li>
    * <li>Valeur associée : code court de la métadonnée</li>
    * </ul>
    * 
    * @param metaUtilisees
    *           La liste des métadonnées utilisées dans la requête d'origine.
    */
   public final void setMetaUtilisees(Map<String, String> metaUtilisees) {
      this.metaUtilisees = metaUtilisees;
   }

}
