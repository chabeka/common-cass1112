/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.common;

/**
 * Classe contenant les constantes utilisées dans le reste de la capture de
 * masse
 * 
 */
public final class Constantes {

   /**
    * Identifiant du paramètre idTraitement
    */
   public static final String ID_TRAITEMENT = "capture.masse.idtraitement";

   /**
    * Identifiant du paramètre sommaire
    */
   public static final String SOMMAIRE = "capture.masse.sommaire";

   /**
    * PoolThread
    */
   public static final String THREAD_POOL = "INSERTION_POOL_THREAD";

   /**
    * Liste des Document en erreur
    */
   public static final String DOC_EXCEPTION = "DOCUMENT_EXCEPTION";

   /**
    * Liste des indexs des documents en erreur
    */
   public static final String INDEX_EXCEPTION = "INDEX_EXCEPTION";

   /**
    * Liste des codes erreur
    */
   public static final String CODE_EXCEPTION = "CODE_EXCEPTION";

   /**
    * Documents intégrés
    */
   public static final String INTEG_DOCS = "INTEGRATED_DOCUMENTS";

   /**
    * Nombre de documents intégrés
    */
   public static final String NB_INTEG_DOCS = "NB_INTEG_DOCS";

   /**
    * Chemin complet du fichier sommaire.xml
    */
   public static final String SOMMAIRE_FILE = "SOMMAIRE_FILE";

   /**
    * Index utilisé pour le contrôler le document courant
    */
   public static final String CTRL_INDEX = "CTRL_INDEX";

   /**
    * Nombre de documents total dans un fichier sommaire.xml
    */
   public static final String DOC_COUNT = "DOC_COUNT";

   /**
    * Code Erreur technique
    */
   public static final String ERR_BUL001 = "SAE-CA-BUL001";

   /**
    * Code Erreur fonctionnelle
    */
   public static final String ERR_BUL002 = "SAE-CA-BUL002";

   /**
    * Code Erreur pour la capture en masse en mode tout ou rien qui a été
    * interrompue
    */
   public static final String ERR_BUL003 = "SAE-CA-BUL003";

   /**
    * Recherche réalisée pour le rollback
    */
   public static final String SEARCH_ROLLBACK = "SEARCH_ROLLBACK";

   /**
    * Nombre total d'élément à supprimer
    */
   public static final String COUNT_ROLLBACK = "COUNT_ROLLBACK";

   /**
    * Flag indiquant qu'un trace pour le systeme a été générée
    */
   public static final String FLAG_BUL003 = "FLAG_BUL003";

   /**
    * Hash du fichier sommaire.xml
    */
   public static final String HASH = "hash";

   /**
    * Le type de hash
    */
   public static final String TYPE_HASH = "typeHash";

   /*
    * Paramétrage pour activer ou non l'écriture de la liste des documents
    * intégrés avec l'UUID associé dans le fichier resultat.xml
    */
   public static final String RESTITUTION_UUIDS = "RESTITUTION_UUIDS";

   /**
    * l'Url ECDE
    */
   public static final String ECDE_URL = "ecdeUrl";

   /**
    * Traçabilité : le code de l'événement pour l'échec d'une capture de masse
    */
   public static final String TRACE_CODE_EVT_ECHEC_CM = "CAPTURE_MASSE|KO";

   /**
    * Constructeur
    */
   private Constantes() {
   }
}
