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

}
