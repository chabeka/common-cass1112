/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.util;

/**
 * Classe contenant l'ensemble des constantes de la régionalisation
 * 
 */
public final class Constants {

   /**
    * Constructeur
    */
   private Constants() {
   }

   /**
    * liste des métadonnées modifiables
    */
   public static final String[] METADATAS = new String[] { "nne", "npe", "den",
         "cv2", "scv", "nci", "nce", "srt", "psi", "nst", "nre", "nic", "dre",
         "apr", "atr", "cop", "cog", "sac", "nbp" };

   /** identifiant du document */
   public static final String TRACE_ID_DOCUMENT = "TRACE_ID_DOCUMENT";

   /** numéro de ligne */
   public static final String TRACE_LIGNE = "TRACE_LIGNE";

   /** nom de la métadonnée */
   public static final String TRACE_META_NAME = "TRACE_META_NAME";

   /** ancienne valeur */
   public static final String TRACE_OLD_VALUE = "TRACE_OLD_VALUE";

   /** nouvelle valeur */
   public static final String TRACE_NEW_VALUE = "TRACE_NEW_VALUE";

   /** requête lucène */
   public static final String TRACE_REQUETE_LUCENE = "TRACE_REQUETE_LUCENE";

   /** Nombre de documents impactés */
   public static final String TRACE_DOC_COUNT = "TRACE_DOC_COUNT";

   /** indicateur de mise à jour des documents */
   public static final String TRACE_INDIC_MAJ = "TRACE_INDIC_MAJ";

   /** indicateur de mise à jour des métadonnées */
   public static final String TRACE_UPDATE_TRUE = "oui";

   /** indicateur de non mise à jour des métadonnées */
   public static final String TRACE_UPDATE_FALSE = "non";

   /** trace écrite lors d'une mise à jour de métadonnée */
   public static final String TRACE_OUT_MAJ = "ligne ${" + TRACE_LIGNE
         + "} - document ${" + TRACE_ID_DOCUMENT + "} : " + "métadonnée ${"
         + TRACE_META_NAME + "} mise à jour. " + "Ancienne valeur = ${"
         + TRACE_OLD_VALUE + "} / Nouvelle valeur = ${" + TRACE_NEW_VALUE
         + "}.";

   /** trace écrite lors d'une recherche de documents */
   public static final String TRACE_OUT_REC = "ligne ${" + TRACE_LIGNE
         + "} - la requête lucene ${" + TRACE_REQUETE_LUCENE + "} concerne ${"
         + TRACE_DOC_COUNT + "} documents. Mise à jour des métadonnées : ${"
         + TRACE_INDIC_MAJ + "}.";

}
