package fr.urssaf.image.sae.saml.params;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Eléments permettant de vérifier la signature d'une assertion SAML
 */
public class SamlSignatureVerifParams {

   private Map<X509Certificate, String> certifsACRacine;

   private List<X509CRL> crls;

   private Map<String, List<String>> patternsIssuer;

   /**
    * Faux pour désactiver la vérification des certificats
    */
   private boolean validateCertificates;

   /**
    * Les certificats des AC racine
    * 
    * @return Les certificats des AC racine
    */
   public final Map<X509Certificate, String> getCertifsACRacine() {
      return certifsACRacine;
   }

   /**
    * La liste des certificats des AC racine
    * 
    * @return la liste des certificats des AC racine
    */
   public final List<X509Certificate> getListeCertifsACRacine() {
      List<X509Certificate> certificates = null;
      if (certifsACRacine != null) {
         certificates = new ArrayList<>(certifsACRacine.keySet());
      }

      return certificates;
   }

   /**
    * Les certificats des AC racine
    * 
    * @param certifsACRacine
    *           Les certificats des AC racine
    */
   public final void setCertifsACRacine(
         final Map<X509Certificate, String> certifsACRacine) {
      this.certifsACRacine = certifsACRacine;
   }

   /**
    * Les CRL de toutes les AC impliquées dans la délivrance des certificats de
    * signature d'assertion SAML
    * 
    * @return Les CRL de toutes les AC impliquées dans la délivrance des
    *         certificats de signature d'assertion SAML
    */
   public final List<X509CRL> getCrls() {
      return crls;
   }

   /**
    * Les CRL de toutes les AC impliquées dans la délivrance des certificats de
    * signature d'assertion SAML
    * 
    * @param crls
    *           Les CRL de toutes les AC impliquées dans la délivrance des
    *           certificats de signature d'assertion SAML
    */
   public final void setCrls(final List<X509CRL> crls) {
      this.crls = crls;
   }

   /**
    * Les patterns de vérification de l'IssuerDN du certificat contenant la clé
    * publique associée à la clé privée de la signature de l'assertion SAML.<br>
    * Ce sont des expressions régulières.
    * 
    * @return les patterns
    */
   public final Map<String, List<String>> getPatternsIssuer() {
      return patternsIssuer;
   }

   /**
    * Les patterns de vérification de l'IssuerDN du certificat contenant la clé
    * publique associée à la clé privée de la signature de l'assertion SAML.<br>
    * Ce sont des expressions régulières.
    * 
    * @param patternsIssuer
    *           les patterns
    */
   public final void setPatternsIssuer(final Map<String, List<String>> patternsIssuer) {
      this.patternsIssuer = patternsIssuer;
   }

   /**
    * Renvoie vrai s'il faut valider la validité des certificats, et faux dans le cas contraire
    * 
    * @return
    */
   public final boolean shouldValidateCerticates() {
      return validateCertificates;
   }

   /**
    * Indique s'il faut valider la validité des certificats.
    * Permet de désactiver la vérification de la validité des certificats dans le cas où un client utilise encore des certificats périmés
    */
   public final void setValidateCerticates(final boolean shouldValidateCertificates) {
      validateCertificates = shouldValidateCertificates;
   }

}
