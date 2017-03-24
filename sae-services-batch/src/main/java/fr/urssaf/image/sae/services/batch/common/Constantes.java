/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common;

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
    * Liste des exceptions survenues sur un rollback
    */
   public static final String ROLLBACK_EXCEPTION = "ROLLBACK_EXCEPTION";

   /**
    * Liste des indexs des documents en erreur
    */
   public static final String INDEX_EXCEPTION = "INDEX_EXCEPTION";

   /**
    * Liste des indexs des fichiers de référence
    */
   public static final String INDEX_REF_EXCEPTION = "INDEX_REF_EXCEPTION";

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
    * Index utilisé pour le contrôler le fichier de référence
    */
   public static final String CTRL_REF_INDEX = "CTRL_REF_INDEX";

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
    * Code Erreur pour la capture en masse en mode partiel qui a été interrompue
    */
   public static final String ERR_BUL004 = "SAE-CA-BUL004";

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
    * Mode de traitement du batch pour la redirection.
    */
   public static final String BATCH_MODE_NOM_REDIRECT = "BATCH_MODE_NOM_REDIRECT";

   /**
    * Mode de traitement du batch.
    */
   public static final String BATCH_MODE_NOM = "BATCH_MODE_NOM";

   /**
    * l'Url ECDE
    */
   public static final String ECDE_URL = "ecdeUrl";

   /**
    * Traçabilité : le code de l'événement pour l'échec d'une capture de masse
    */
   public static final String TRACE_CODE_EVT_ECHEC_CM = "CAPTURE_MASSE|KO";

   /**
    * La redirection a prendre pour le countElement
    */
   public static final String COUNT_DIRECTION = "capture.masse.count.element.redirection";

   /**
    * Traçabilité : contexte pour la capture de masse
    */
   public static final String CONTEXTE_CAPTURE_MASSE = "captureMasse";

   /**
    * Liste des erreurs de suppression
    */
   public static final String SUPPRESSION_EXCEPTION = "SUPPRESSION_EXCEPTION";

   /**
    * Traçabilité : le code de l'événement pour l'échec d'une suppression de masse
    */
   public static final String TRACE_CODE_EVT_ECHEC_SM = "SUPPRESSION_MASSE|KO";

   /**
    * Traçabilité : contexte pour la suppression de masse
    */
   public static final String CONTEXTE_SUPPRESSION_MASSE = "suppressionMasse";

   /**
    * Requête utilisée par le service de suppression de masse
    */
   public static final String REQ_LUCENE_SUPPRESSION = "requeteSuppression";

   /**
    * Requête finale de suppression de masse  ou de restore de masse
    */
   public static final String REQ_FINALE_TRT_MASSE = "requeteFinale";

   /**
    * UUID du traitement de suppression de masse.
    */
   public static final String ID_TRAITEMENT_SUPPRESSION = "idTraitementSuppression";

   /**
    * Code court de la métadonnée 'identifiant de suppression de masse'
    */
   public static final String CODE_COURT_META_ID_SUPPRESSION = "isi";

   /**
    * Code court de la métadonnée 'date de mise en corbeille'
    */
   public static final String CODE_COURT_META_DATE_CORBEILLE = "dmc";

   /**
    * UUID du traitement de suppression de masse à restorer par le service de
    * restore de masse
    */
   public static final String ID_TRAITEMENT_A_RESTORER = "idTraitementARestorer";

   /**
    * UUID du traitement de restore de masse.
    */
   public static final String ID_TRAITEMENT_RESTORE = "idTraitementRestore";

   /**
    * Nombre de docs supprimés.
    */
   public static final String NB_DOCS_SUPPRIMES = "nbDocsSupprimes";

   /**
    * Liste des erreurs de restore
    */
   public static final String RESTORE_EXCEPTION = "RESTORE_EXCEPTION";

   /**
    * Code court de la métadonnée 'identifiant de restore de masse'
    */
   public static final String CODE_COURT_META_ID_RESTORE = "iri";

   /**
    * Nombre de docs restorés.
    */
   public static final String NB_DOCS_RESTORES = "nbDocsRestores";

   /**
    * Traçabilité : le code de l'événement pour l'échec d'une restore de masse
    */
   public static final String TRACE_CODE_EVT_ECHEC_RM = "RESTORE_MASSE|KO";

   /**
    * Traçabilité : contexte pour la restore de masse
    */
   public static final String CONTEXTE_RESTORE_MASSE = "restoreMasse";
   
   /**
    * Nombre de docs modifiés.
    */
   public static final String NB_DOCS_MODIFIES = "nbDocsRestores";
   
   /**
    * Traçabilité : le code de l'événement pour l'échec d'une modification de masse
    */
   public static final String TRACE_CODE_EVT_ECHEC_MM = "MODIFICATION_MASSE|KO";
   
   /**
    * Traçabilité : contexte pour la modification de masse
    */
   public static final String CONTEXTE_MODIFICATION_MASSE = "modificationMasse";
 
   /**
    * Seuil de compression par défaut : 2 Mo
    */
   public static final Integer SEUIL_COMPRESSION_DEFAUT = 2097152; 

   /**
    * Le code du traitement
    */
   public static final String CODE_TRAITEMENT = "codeTraitement";

   /**
    * Nom balise dans sommaire/résultat pour le batch mode.
    */
   public static final String BATCH_MODE_ELEMENT_NAME = "batchMode";
   
   /**
    * Nombre de docs transférés.
    */
   public static final String NB_DOCS_TRANSFERES = "nbDocsTransferes";
   
   /**
    * Traçabilité : le code de l'événement pour l'échec d'un transfert de masse
    */
   public static final String TRACE_CODE_EVT_ECHEC_TM = "TRANSFERT_MASSE|KO";
   
   /**
    * Traçabilité : contexte pour la modification de masse
    */
   public static final String CONTEXTE_TRANSFERT_MASSE = "transfertMasse";

   /**
    * Constructeur
    */
   private Constantes() {
   }

   /**
    * Types de traitements
    */
   public static enum TYPES_JOB {
      capture_masse, suppression_masse, restore_masse, modification_masse, transfert_masse;
   }

   /**
    * Enumération pour le mode du batch.
    */
   public static enum BATCH_MODE {
      /**
       * Tout ou rien
       */
      TOUT_OU_RIEN("TOUT_OU_RIEN", "TOR"),
      /**
       * Partiel
       */
      PARTIEL("PARTIEL", "PAR");

      /**
       * Mode du batch
       */
      private final String modeNom;

      /**
       * Mode du batch
       */
      private final String modeNomCourt;

      /**
       * Constructeur
       *
       * @param mode
       *           Mode du batch
       */
      private BATCH_MODE(String modeNom, String modeNomCourt) {
         this.modeNom = modeNom;
         this.modeNomCourt = modeNomCourt;
      }

      /**
       * Getter pour modeNom
       * 
       * @return the modeNom
       */
      public String getModeNom() {
         return modeNom;
      }

      /**
       * Getter pour modeNomCourt
       * 
       * @return the modeNomCourt
       */
      public String getModeNomCourt() {
         return modeNomCourt;
      }

   }

   /**
    * Vérifiez si le nom d'un type de travail spécifique existe
    * 
    * @param name
    * @return true/false
    */
   public static Boolean typeJobExist(final String name) {
      for (TYPES_JOB elm : TYPES_JOB.values()) {
         if (elm.name().equals(name)) {
            return true;
         }
      }
      return false;
   }

}
