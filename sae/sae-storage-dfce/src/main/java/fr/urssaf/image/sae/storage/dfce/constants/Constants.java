package fr.urssaf.image.sae.storage.dfce.constants;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Cette classe contient la liste des constantes utilisées dans l'application.
 */
public final class Constants {

   /**
    * Constructeur
    */
   private Constants() {
      assert false;
   }

   /**
    * Code du message d'erreur lors d'un problème de suppression de document
    */
   public static final String DEL_CODE_ERROR = "delete.code.message";

   /**
    * Code du message d'erreur lors d'un problème d'archivage de document
    */
   public static final String INS_CODE_ERROR = "insertion.code.message";

   /**
    * Code du message d'erreur lors d'un problème de consultation de document
    */
   public static final String RTR_CODE_ERROR = "retrieve.code.message";

   /**
    * Code du message d'erreur lors d'un problème de recherche de documents
    */
   public static final String SRH_CODE_ERROR = "search.code.message";

   /**
    * Code du message d'erreur lors d'un problème de connection à DFCE
    */
   public static final String CNT_CODE_ERROR = "connection.code.message";
   
   /**
    * Code du message d'erreur lors d'un problème de mise en corbeille de document
    */
   public static final String COR_CODE_ERROR = "recyclebin.code.message";
   
   /**
    * Code du message d'erreur lors d'un problème de restore de document
    */
   public static final String RST_CODE_ERROR = "restore.code.message";

   /**
    * Protocole HTTPS
    */
   public static final String HTTPS = "https";

   /**
    * Protocole HTTP
    */
   public static final String HTTP = "http";

   /**
    * Un formatage de date en chaîne de caractères, conforme ISO-8601
    */
   public static final String DATE_PATTERN = "yyyy-MM-dd";

   /**
    * La Local par défaut
    */
   public static final Locale DEFAULT_LOCAL = Locale.FRENCH;

   /**
    * encoding de lecture
    */
   public static final Charset ENCODING = Charset.forName("UTF-8");

   /**
    * Le message d'erreur par défaut
    */
   public static final String NO_MESSAGE_FOR_THIS_KEY = "Pas de message correspondant à cette clé";

   /**
    * Traçabilité : code de l'événément "Dépôt d'un document dans DFCE"
    */
   public static final String TRACE_CODE_EVT_DEPOT_DOC_DFCE = "DFCE_DEPOT_DOC|OK";
   
   /**
    * Traçabilité : code de l'événément "Suppression d'un document de DFCE"
    */
   public static final String TRACE_CODE_EVT_SUPPRESSION_DOC_DFCE = "DFCE_SUPPRESSION_DOC|OK";
   
   /**
    * Traçabilité : code de l'événément "Modification d'un document dans DFCE"
    */
   public static final String TRACE_CODE_EVT_MODIF_DOC_DFCE = "DFCE_MODIF_DOC|OK";
   
   /**
    * Traçabilité : code de l'événément "Transfert d'un document vers la GNS"
    */
   public static final String TRACE_CODE_EVT_TRANSFERT_DOC_DFCE = "DFCE_TRANSFERT_DOC|OK";
   
   /**
    * Traçabilité : code de l'événément "Dépot d'un document attaché dans DFCE"
    */
   public static final String TRACE_CODE_EVT_DEPOT_ATTACH_DFCE = "DFCE_DEPOT_ATTACH|OK";
   
   /**
    * Traçabilité : code de l'événément "Mise à la corbeille d'un document dans DFCE"
    */
   public static final String TRACE_CODE_EVT_CORBEILLE_DOC_DFCE = "DFCE_CORBEILLE_DOC|OK";

   /**
    * Traçabilité : code de l'événément "Restore d'un document dans DFCE"
    */
   public static final String TRACE_CODE_EVT_RESTORE_DOC_DFCE = "DFCE_RESTORE_DOC|OK";
}
