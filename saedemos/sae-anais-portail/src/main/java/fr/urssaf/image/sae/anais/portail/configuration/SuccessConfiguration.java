package fr.urssaf.image.sae.anais.portail.configuration;

/**
 * Classe de configuration de l'application web du SAE <br>
 * <br>
 * La configuration s'effectue dans le fichier
 * <code>applicationContext.xml</code><br>
 * <br>
 * exemple:
 * 
 * <pre>
 *  &lt;bean id="configurationSuccess"
 *       class="fr.urssaf.image.sae.anais.portail.configuration.SuccessConfiguration">
 *       &lt;property name="url" value="" />
 *      &lt;property name="service" value="/success.html" />
 *   &lt;/bean>
 * </pre>
 * 
 */
public class SuccessConfiguration {

   private String url;

   private String service;

   /**
    * 
    * @return URL de l'application web du SAE
    */
   public final String getUrl() {
      return url;
   }

   /**
    * initialisation de l'URL de redirection une fois l'utilisateur authentifié
    * auprès de ANAIS<br>
    * 
    * @param url
    *           URL de l'application web du SAE
    */
   public final void setUrl(String url) {
      this.url = url;
   }

   /**
    * 
    * @return service de l'application web du SAE
    */
   public final String getService() {
      return service;
   }

   /**
    * initialisation du service de l'application web du SAE
    * 
    * @param service
    *           nom du service de l'application web du SAE
    */
   public final void setService(String service) {
      this.service = service;
   }

}
