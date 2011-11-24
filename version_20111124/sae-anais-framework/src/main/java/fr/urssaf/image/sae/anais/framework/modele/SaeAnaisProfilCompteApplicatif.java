package fr.urssaf.image.sae.anais.framework.modele;

/**
 * Paramètres décrivant le compte applicatif à utiliser pour que l'application
 * s'authentifie à ANAIS avant de demander les droits de l'utilisateur<br>
 * <br>
 * L'instanciation est uniquement possible avec
 * {@link ObjectFactory#createSaeAnaisProfilCompteApplicatif()}
 * 
 * @see ObjectFactory
 */
public class SaeAnaisProfilCompteApplicatif {

   @SuppressWarnings("PMD.ShortVariable")
   private String dn;

   private String password;

   private String codeApplication;

   @SuppressWarnings("PMD.UncommentedEmptyConstructor")
   protected SaeAnaisProfilCompteApplicatif() {

   }

   /**
    * 
    * @return Le DN du compte applicatif
    */

   public final String getDn() {
      return dn;
   }

   /**
    * 
    * @param dn
    *           Le DN du compte applicatif
    */
   @SuppressWarnings("PMD.ShortVariable")
   public final void setDn(String dn) {
      this.dn = dn;
   }

   /**
    * 
    * @return Le mot de passe du compte applicatif
    */
   public final String getPassword() {
      return password;
   }

   /**
    * 
    * @param password
    *           Le mot de passe du compte applicatif
    */
   public final void setPassword(String password) {
      this.password = password;
   }

   /**
    * 
    * @return Le code de l’application
    */
   public final String getCodeApplication() {
      return codeApplication;
   }

   /**
    * 
    * @param codeApplication
    *           Le code de l’application
    */
   public final void setCodeApplication(String codeApplication) {
      this.codeApplication = codeApplication;
   }

}
