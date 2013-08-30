package fr.urssaf.image.sae.integration.ihmweb.constantes;

/**
 * Constantes
 */
public final class SaeIntegrationConstantes {

   
   private SaeIntegrationConstantes() {
      
   }
   
   
   /**
    * Nom du fichier flag dont la présence dans le répertoire adéquat de l'ECDE
    * indique que le traitement de masse est terminé
    */
   public static final String NOM_FIC_FLAG_TDM = "fin_traitement.flag";
   
   /**
    * Nom du fichier flag dont la présence dans le répertoire adéquat de l'ECDE
    * indique que le traitement de masse a commencé
    */
   public static final String NOM_FIC_DEB_FLAG_TDM = "debut_traitement.flag";
 
   
   /**
    * Nom du fichier resultats.xml contenant les résultats d'un traitement de masse
    */
   public static final String NOM_FIC_RESULTATS = "resultats.xml";
   
   
   /**
    * Le code long de la métadonnée "NomFichier"
    */
   public static final String META_NOM_FICHIER = "NomFichier";
   
   
   /**
    * Le code long de la métadonnée "Hash"
    */
   public static final String META_HASH = "Hash";
      
   /**
    * Pour les VI : recipient par défaut
    */
   public static final String VI_DEFAULT_RECIPIENT = "urn:URSSAF";
   
   /**
    * Pour les VI : audience par défaut
    */
   public static final String VI_DEFAULT_AUDIENCE = "http://sae.urssaf.fr";
   
   /**
    * Pour les VI : issuer par défaut
    */
   public static final String VI_DEFAULT_ISSUER = "SaeIntegration";
   
   /**
    * Pour les VI : PAGM par défaut
    */
   public static final String VI_DEFAULT_PAGM = "ROLE_TOUS;FULL";
   

   /**
    * Le code long de la métadonnée CodeOrganismeProprietaire
    */
   public static final String META_CODE_ORG_PROPRIETAIRE = "CodeOrganismeProprietaire";

   
}
