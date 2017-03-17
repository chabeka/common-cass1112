package fr.urssaf.image.sae.webservices.constantes;

/**
 * Les constantes pour la traçabilité
 */
public final class TracesConstantes {

   private TracesConstantes() {
      // Constructeur privé
   }

   /**
    * Le code de l'événement de l'échec du WS de pingSecure
    */
   public static final String CODE_EVT_WS_PINGSECURE_KO = "WS_PING_SECURE|KO";

   /**
    * Le code de l'événement de l'échec du WS d'archivage unitaire
    */
   public static final String CODE_EVT_WS_ARCHIVAGE_UNITAIRE_KO = "WS_CAPTURE_UNITAIRE|KO";

   /**
    * Le code de l'événement de l'échec du WS d'archivage de masse
    */
   public static final String CODE_EVT_WS_ARCHIVAGE_MASSE_KO = "WS_CAPTURE_MASSE|KO";
   
   /**
    * Le code de l'événement de l'échec du WS de suppression de masse
    */
   public static final String CODE_EVT_WS_SUPPRESSION_MASSE_KO = "WS_SUPPRESSION_MASSE|KO";
   
   /**
    * Le code de l'événement de l'échec du WS de suppression de masse
    */
   public static final String CODE_EVT_WS_RESTORE_MASSE_KO = "WS_RESTORE_MASSE|KO";

   /**
    * Le code de l'événement de l'échec du WS de consultation
    */
   public static final String CODE_EVT_WS_CONSULTATION_KO = "WS_CONSULTATION|KO";

   /**
    * Le code de l'événement de l'échec du WS de recherche
    */
   public static final String CODE_EVT_WS_RECHERCHE_KO = "WS_RECHERCHE|KO";

   /**
    * Le code de l'événement du chargement des certificats d'AC racine
    */
   public static final String CODE_EVT_CHARGE_CERT_ACRACINE = "WS_LOAD_CERTS_ACRACINE|OK";

   /**
    * Le code de l'événement du chargement des CRL
    */
   public static final String CODE_EVT_CHARGE_CRL = "WS_LOAD_CRLS|OK";

   /**
    * Le code de l'événement d'échec du chargement des CRL
    */
   public static final String CODE_EVT_ECHEC_CHARGE_CRL = "WS_LOAD_CRLS|KO";
   
   /**
    * Le code de l'événement échec du transfert de document
    */
   public static final String CODE_EVT_WS_TRANSFERT_KO = "WS_TRANSFERT|KO";
   
   /**
    * Le code de l'événement échec de suppression de document
    */
   public static final String CODE_EVT_WS_SUPPRESSION_KO = "WS_SUPPRESSION|KO";
   
   /**
    * Le code de l'événement échec de modification de métadonnées
    */
   public static final String CODE_EVT_WS_MODIFICATION_KO = "WS_MODIFICATION|KO";
   
   /**
    * Le code de l'événement échec de récupération de métadonnées
    */
   public static final String CODE_EVT_WS_RECUPERATION_METAS_KO = "WS_RECUPERATION_METAS|KO";
   
   /**
    * Le code de l'événement échec d'ajout de note
    */
   public static final String CODE_EVT_WS_AJOUT_NOTE_KO = "WS_AJOUT_NOTE|KO";
   
   /**
    * Le code de l'événement échec de récupération d'un document attaché
    */
   public static final String CODE_EVT_WS_GET_DOC_FORMAT_ORIGINE = "WS_GET_DOC_FORMAT_ORIGINE|KO";
   
   /**
    * Le code de l'événement échec de récupération des états des traitements de masse
    */
   public static final String CODE_EVT_WS_ETAT_TRAIT_MASSE = "WS_ETAT_TRAITEMENTS_MASSE|KO";

   /**
    * Le code de l'événement échec de modification en masse de documents
    */
   public static final String CODE_EVT_WS_MODIFICATION_MASSE_KO = "WS_MODIFICATION_MASSE|KO";
}
