package fr.urssaf.image.sae.anais.portail.form;

import fr.urssaf.image.sae.anais.portail.validator.NotEmpty;

/**
 * Classe de formulaire correspondant à l'authentification auprès d'ANAIS :
 * <code>connection/form.jsp </code><br>
 * <br>
 * 
 * <pre>
 * &lt;form:form method="post" modelAttribute="connectionForm" name="form_cirti">
 * 
 * ...
 * 
 * &lt;/form:form>
 * </pre>
 * 
 * 
 */
public class ConnectionForm {

   private String userLogin;

   private String userPassword;

   /**
    * Retourne le contenu de la balise &lt;form:input path='userLogin'
    * id="login"/><br>
    * <br>
    * La valeur n'est pas valide si il n'y a aucun text.<br>
    * Dans ce cas un message est renvoyé: user.login.empty
    * 
    * @return Identifiant
    */
   @NotEmpty(message = "{user.login.empty}")
   public final String getUserLogin() {
      return userLogin;
   }

   /**
    * Initialise le contenu de la balise &lt;form:input path='userLogin'
    * id="login"/>
    * 
    * @param userLogin
    *           Identifiant
    */
   public final void setUserLogin(String userLogin) {
      this.userLogin = userLogin;
   }

   /**
    * Retourne le contenu de la balise &lt;form:password path='userPassword'
    * id="password"/> <br>
    * La valeur n'est pas valide si in n'y a aucun texte. <br>
    * Dans ce cas un message est renvoyé: user.password.empty
    * 
    * @return Mot de passe
    */
   @NotEmpty(message = "{user.password.empty}")
   public final String getUserPassword() {
      return userPassword;
   }

   /**
    * Initialise le contenu de la balise &lt;form:password path='userPassword'
    * id="password"/>
    * 
    * @param userPassword
    *           Mot de passe
    */
   public final void setUserPassword(String userPassword) {
      this.userPassword = userPassword;
   }

}
