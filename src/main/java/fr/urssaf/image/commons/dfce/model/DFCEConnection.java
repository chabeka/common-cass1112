package fr.urssaf.image.commons.dfce.model;

import java.net.URL;

/**
 * Paramètre de connexion à DFCE<br>
 * . Ces parmètres sont utilisés lors de l'appel de la méthode
 * {@link net.docubase.toolkit.service.ServiceProvider#connect(String, String, String)}
 * <br>
 * <br>
 * Les paramètres sont :
 * <ul>
 * <li><code>login</code>: login de connexion à DFCE</li>
 * <li><code>password</code>: mot de passe de connexion à DFCE</li>
 * <li><code>serverUrl</code>: URL de connexion à DFCE</li>
 * </ul>
 * 
 */
public class DFCEConnection {

   private String login;

   private String password;

   private URL serverUrl;

   /**
    * @return the login
    */
   public final String getLogin() {
      return login;
   }

   /**
    * @param login
    *           the login to set
    */
   public final void setLogin(String login) {
      this.login = login;
   }

   /**
    * @return the password
    */
   public final String getPassword() {
      return password;
   }

   /**
    * @param password
    *           the password to set
    */
   public final void setPassword(String password) {
      this.password = password;
   }

   /**
    * @return the serverUrl
    */
   public final URL getServerUrl() {
      return serverUrl;
   }

   /**
    * @param serverUrl
    *           the serverUrl to set
    */
   public final void setServerUrl(URL serverUrl) {
      this.serverUrl = serverUrl;
   }

}
