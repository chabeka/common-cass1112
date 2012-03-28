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


   public final String getCmCompareRepRef() {
      return cmCompareRepRef;
   }


   public final void setCmCompareRepRef(String cmCompareRepRef) {
      this.cmCompareRepRef = cmCompareRepRef;
   }


   public final String getCmCompareRepPasse() {
      return cmCompareRepPasse;
   }


   public final void setCmCompareRepPasse(String cmCompareRepPasse) {
      this.cmCompareRepPasse = cmCompareRepPasse;
   }
   
   
   
   
}
