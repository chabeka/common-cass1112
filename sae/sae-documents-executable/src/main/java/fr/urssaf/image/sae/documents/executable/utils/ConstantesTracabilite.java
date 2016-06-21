package fr.urssaf.image.sae.documents.executable.utils;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Cette classe contient la liste des constantes utilisées dans l'application.
 */
public final class ConstantesTracabilite {

   /**
    * Constructeur
    */
   private ConstantesTracabilite() {
      assert false;
   }


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
