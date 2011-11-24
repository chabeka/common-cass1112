package fr.urssaf.image.sae.anais.framework.modele;

/**
 * classe de paramétrage d'adressage du serveur ANAIS<br>
 * <br>
 * L'instanciation est uniquement possible avec
 * {@link ObjectFactory#createSaeAnaisAdresseServeur()}
 * 
 * @see ObjectFactory
 */
public class SaeAnaisAdresseServeur {

   private String hote;

   private Integer port;

   private boolean tls;

   private int timeout;

   @SuppressWarnings("PMD.UncommentedEmptyConstructor")
   protected SaeAnaisAdresseServeur() {

   }

   /**
    * 
    * @return L’adresse IP ou le nom DNS
    */
   public final String getHote() {
      return hote;
   }

   /**
    * 
    * @param hote
    *           L’adresse IP ou le nom DNS
    */
   public final void setHote(String hote) {
      this.hote = hote;
   }

   /**
    * 
    * @return port Le numéro de port TCP sur lequel se serveur écoute
    */
   public final Integer getPort() {
      return port;
   }

   /**
    * 
    * @param port
    *           Le numéro de port TCP sur lequel se serveur écoute
    */
   public final void setPort(Integer port) {
      this.port = port;
   }

   /**
    * 
    * @return Flag indiquant si TLS doit être activé pour la communication avec
    *         le serveur
    */
   public final boolean isTls() {
      return tls;
   }

   /**
    * 
    * @param tls
    *           Flag indiquant si TLS doit être activé pour la communication
    *           avec le serveur
    */
   public final void setTls(boolean tls) {
      this.tls = tls;
   }

   /**
    * 
    * @return Le timeout de connexion au serveur, en millisecondes
    */
   public final int getTimeout() {
      return timeout;
   }

   /**
    * 
    * @param timeout
    *           Le timeout de connexion au serveur, en millisecondes
    */
   public final void setTimeout(int timeout) {
      this.timeout = timeout;
   }

}
