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

   /** numéro de compte externe */
   public static final String NUM_CPTE_EXT = "nce";

   /** code organisme gestionnaire */
   public static final String CODE_ORG_GEST = "cog";

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
   public static final String TRACE_OUT_MAJ = "MAJ;${" + TRACE_LIGNE + "}%${"
         + TRACE_ID_DOCUMENT + "}%${" + TRACE_META_NAME + "}%${"
         + TRACE_OLD_VALUE + "}%${" + TRACE_NEW_VALUE + "}";

   /** trace écrite lors d'une recherche de documents */
   public static final String TRACE_OUT_REC = "REC;${" + TRACE_LIGNE + "}%${"
         + TRACE_REQUETE_LUCENE + "}%${" + TRACE_DOC_COUNT + "}%${"
         + TRACE_INDIC_MAJ + "}";

   /** entete recherche */
   public static final String ENTETE_OUT_REC = "#REC;numero de ligne%requete lucene"
         + "%nombre de doc impactes%mise a jour des donnees";

   /** entete mise a jour */
   public static final String ENTETE_OUT_MAJ = "#MAJ;ligne%document"
         + "%metadonnee%ancienne valeur%nouvelle valeur";

}
