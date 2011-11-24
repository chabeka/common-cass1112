package fr.urssaf.image.sae.anais.framework.modele;

/**
 * Profil du compte applicatif à utiliser pour que l'application s'authentifie
 * auprès d'ANAIS<br>
 * <br>
 * Comptes applicatifs
 * <ul>
 * <li><code>Sae</code> : <code>SAE</code></li>
 * <li><code>Autre</code> : <code>AUTRE</code></li>
 * </ul>
 * 
 */
public enum SaeAnaisEnumCompteApplicatif {

   Sae("SAE"), Autre("AUTRE");

   private String appli;

   SaeAnaisEnumCompteApplicatif(String appli) {
      this.appli = appli;
   }

   /**
    * @return Code du compte applicatif
    */
   public String appli() {
      return this.appli;
   }
}
