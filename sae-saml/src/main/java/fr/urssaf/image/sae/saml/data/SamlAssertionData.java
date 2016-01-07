package fr.urssaf.image.sae.saml.data;

import java.security.cert.X509Certificate;

import fr.urssaf.image.sae.saml.params.SamlAssertionParams;

/**
 * Données que l'on peut extraire d'une assertion SAML 2.0
 * 
 * 
 */
public class SamlAssertionData {

   private SamlAssertionParams assertionParams;

   private X509Certificate clePublique;

   /**
    * 
    * @return Les données qui ont permis de créer l'assertion
    */
   public final SamlAssertionParams getAssertionParams() {
      return assertionParams;
   }

   /***
    * 
    * @param assertionParams
    *           Les données qui ont permis de créer l'assertion
    */
   public final void setAssertionParams(SamlAssertionParams assertionParams) {
      this.assertionParams = assertionParams;
   }

   /**
    * 
    * @return La clé publique qui a signé l'assertion, extraite de la signature
    */
   public final X509Certificate getClePublique() {
      return clePublique;
   }

   /**
    * 
    * @param clePublique
    *           La clé publique qui a signé l'assertion, extraite de la
    *           signature
    */
   public final void setClePublique(X509Certificate clePublique) {
      this.clePublique = clePublique;
   }

}
