package fr.urssaf.image.sae.integration.ihmweb.config;


/**
 * Classe pour stocker la configuration de ce programme d'intégration. 
 */
public class TestConfig {

   private String urlSaeService;
   private String dnsEcde;
   private String versionRND;
   private String cmCompareRepRef;
   private String cmCompareRepPasse;
   
   private String dfceServeurUrl;
   private String dfceLogin;
   private String dfcePassword;
   private String dfceBase;
   
   
   /**
    * L'URL du service web SaeService
    * 
    * @return L'URL du service web SaeService
    */
   public final String getUrlSaeService() {
      return urlSaeService;
   }

   
   /**
    * L'URL du service web SaeService
    * 
    * @param urlSaeService L'URL du service web SaeService
    */
   public final void setUrlSaeService(String urlSaeService) {
      this.urlSaeService = urlSaeService;
   }


   /**
    * Le DNS utilisé dans les URL ECDE
    * @return Le DNS utilisé dans les URL ECDE
    */
   public final String getDnsEcde() {
      return dnsEcde;
   }


   /**
    * Le DNS utilisé dans les URL ECDE
    * @param dnsEcde Le DNS utilisé dans les URL ECDE
    */
   public final void setDnsEcde(String dnsEcde) {
      this.dnsEcde = dnsEcde;
   }


   /**
    * La version du RND en cours
    * @return the versionRND
    */
   public final String getVersionRND() {
      return versionRND;
   }


   /**
    * La version du RND en cours
    * @param versionRND the versionRND to set
    */
   public final void setVersionRND(String versionRND) {
      this.versionRND = versionRND;
   }


   /**
    * Comparateur de resultats.xml : Répertoire de la passe de référence 
    * @return Comparateur de resultats.xml : Répertoire de la passe de référence
    */
   public final String getCmCompareRepRef() {
      return cmCompareRepRef;
   }


   /**
    * Comparateur de resultats.xml : Répertoire de la passe de référence
    * @param cmCompareRepRef Comparateur de resultats.xml : Répertoire de la passe de référence
    */
   public final void setCmCompareRepRef(String cmCompareRepRef) {
      this.cmCompareRepRef = cmCompareRepRef;
   }


   /**
    * Comparateur de resultats.xml : Répertoire de la passe à comparer à la référence
    * @return Comparateur de resultats.xml : Répertoire de la passe à comparer à la référence
    */
   public final String getCmCompareRepPasse() {
      return cmCompareRepPasse;
   }


   /**
    * Comparateur de resultats.xml : Répertoire de la passe à comparer à la référence
    * @param cmCompareRepPasse Comparateur de resultats.xml : Répertoire de la passe à comparer à la référence
    */
   public final void setCmCompareRepPasse(String cmCompareRepPasse) {
      this.cmCompareRepPasse = cmCompareRepPasse;
   }


   /**
    * DFCE : URL du Toolkit
    * @return DFCE : URL du Toolkit
    */
   public final String getDfceServeurUrl() {
      return dfceServeurUrl;
   }


   /**
    * DFCE : URL du Toolkit
    * @param dfceServeurUrl DFCE : URL du Toolkit
    */
   public final void setDfceServeurUrl(String dfceServeurUrl) {
      this.dfceServeurUrl = dfceServeurUrl;
   }


   /**
    * DFCE : Login de connexion au Toolkit
    * @return DFCE : Login de connexion au Toolkit
    */
   public final String getDfceLogin() {
      return dfceLogin;
   }


   /**
    * DFCE : Login de connexion au Toolkit
    * @param dfceLogin DFCE : Login de connexion au Toolkit
    */
   public final void setDfceLogin(String dfceLogin) {
      this.dfceLogin = dfceLogin;
   }


   /**
    * DFCE : Password de connexion au Toolkit
    * @return DFCE : Password de connexion au Toolkit
    */
   public final String getDfcePassword() {
      return dfcePassword;
   }


   /**
    * DFCE : Password de connexion au Toolkit
    * @param dfcePassword DFCE : Password de connexion au Toolkit
    */
   public final void setDfcePassword(String dfcePassword) {
      this.dfcePassword = dfcePassword;
   }


   /**
    * DFCE : Nom de la base
    * @return DFCE : Nom de la base
    */
   public final String getDfceBase() {
      return dfceBase;
   }


   /**
    * DFCE : Nom de la base
    * @param dfceBase DFCE : Nom de la base
    */
   public final void setDfceBase(String dfceBase) {
      this.dfceBase = dfceBase;
   }
   
   
}
