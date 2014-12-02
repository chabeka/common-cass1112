package fr.urssaf.image.sae.anais.portail.configuration;

/**
 * Configuration de l'application sur laquelle est branchée le portail
 */
public class AppliSaeConfig {

   private String codeAppli;

   private String urlPost;

   private String relayState;

   /**
    * Code de l'application sur laquelle est branchée le portail
    * 
    * @return Code de l'application sur laquelle est branchée le portail
    */
   public final String getCodeAppli() {
      return codeAppli;
   }

   /**
    * Code de l'application sur laquelle est branchée le portail
    * 
    * @param codeAppli
    *           Code de l'application sur laquelle est branchée le portail
    */
   public final void setCodeAppli(String codeAppli) {
      this.codeAppli = codeAppli;
   }

   /**
    * URL sur laquelle faire le POST du VI
    * 
    * @return URL sur laquelle faire le POST du VI
    */
   public final String getUrlPost() {
      return urlPost;
   }

   /**
    * URL sur laquelle faire le POST du VI
    * 
    * @param urlPost
    *           URL sur laquelle faire le POST du VI
    */
   public final void setUrlPost(String urlPost) {
      this.urlPost = urlPost;
   }

   /**
    * Paramètre du POST RelayState dans lequel on indique la ressource vers
    * laquelle l'application sur laquelle le portail est branchée doit rediriger
    * après une authentification réussie
    * 
    * @return Paramètre du POST RelayState dans lequel on indique la ressource
    *         vers laquelle l'application sur laquelle le portail est branchée
    *         doit rediriger après une authentification réussie
    */
   public final String getRelayState() {
      return relayState;
   }

   /**
    * Paramètre du POST RelayState dans lequel on indique la ressource vers
    * laquelle l'application sur laquelle le portail est branchée doit rediriger
    * après une authentification réussie
    * 
    * @param relayState
    *           Paramètre du POST RelayState dans lequel on indique la ressource
    *           vers laquelle l'application sur laquelle le portail est branchée
    *           doit rediriger après une authentification réussie
    */
   public final void setRelayState(String relayState) {
      this.relayState = relayState;
   }

}
