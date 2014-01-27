package fr.urssaf.image.sae.lotinstallmaj.modele;

/**
 * Configuration d'acces au toolkit DFCE.
 * 
 * 
 */
public final class DfceConfig {

   private String urlToolkit;
   private String login;
   private String password;
   private String basename;
   private int timeOut;

   /**
    * @return l'url du toolkit DFCE
    */
   public String getUrlToolkit() {
      return urlToolkit;
   }

   /**
    * @param urlToolkit
    *           l'url du toolkit DFCE
    */
   public void setUrlToolkit(String urlToolkit) {
      this.urlToolkit = urlToolkit;
   }

   /**
    * @return login de connexion au toolkit DFCE
    */
   public String getLogin() {
      return login;
   }

   /**
    * @param login
    *           de connexion au toolkit DFCE
    */
   public void setLogin(String login) {
      this.login = login;
   }

   /**
    * @return password de connexion au toolkit DFCE
    */
   public String getPassword() {
      return password;
   }

   /**
    * @param password
    *           de connexion au toolkit DFCE
    */
   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * @return nom de la base DFCE a utiliser.
    */
   public String getBasename() {
      return basename;
   }

   /**
    * @param basename
    *           nom de la base DFCE a utiliser.
    */
   public void setBasename(String basename) {
      this.basename = basename;
   }

   /**
    * @return le timeout de connexion à DFCE
    */
   public final int getTimeOut() {
      return timeOut;
   }

   /**
    * @param timeOut
    *           le timeout de connexion à DFCE
    */
   public final void setTimeOut(int timeOut) {
      this.timeOut = timeOut;
   }

}
