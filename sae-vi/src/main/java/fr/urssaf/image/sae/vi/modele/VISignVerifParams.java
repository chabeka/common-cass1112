package fr.urssaf.image.sae.vi.modele;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

/**
 * Eléments permettant de vérifier la signature d'un VI
 */
public class VISignVerifParams {

   private Map<X509Certificate, String> certifsACRacine;

   private List<X509CRL> crls;

   private Map<String, List<String>> patternsIssuer;

   /**
    * Les certificats des AC racine
    * 
    * @return Les certificats des AC racine
    */
   public final Map<X509Certificate, String> getCertifsACRacine() {
      return certifsACRacine;
   }

   /**
    * Les certificats des AC racine
    * 
    * @param certifsACRacine
    *           Les certificats des AC racine
    */
   public final void setCertifsACRacine(
         Map<X509Certificate, String> certifsACRacine) {
      this.certifsACRacine = certifsACRacine;
   }

   /**
    * Les CRL de toutes les AC impliquées dans la délivrance des certificats de
    * signature de VI
    * 
    * @return Les CRL de toutes les AC impliquées dans la délivrance des
    *         certificats de signature de VI
    */
   public final List<X509CRL> getCrls() {
      return crls;
   }

   /**
    * Les CRL de toutes les AC impliquées dans la délivrance des certificats de
    * signature de VI
    * 
    * @param crls
    *           Les CRL de toutes les AC impliquées dans la délivrance des
    *           certificats de signature de VI
    */
   public final void setCrls(List<X509CRL> crls) {
      this.crls = crls;
   }

   /**
    * Les patterns de vérification de l'IssuerDN du certificat contenant la clé
    * publique associée à la clé privée de la signature du VI.<br>
    * Ce sont des expressions régulières.
    * 
    * @return les patterns
    */
   public final Map<String, List<String>> getPatternsIssuer() {
      return patternsIssuer;
   }

   /**
    * Les patterns de vérification de l'IssuerDN du certificat contenant la clé
    * publique associée à la clé privée de la signature du VI.<br>
    * Ce sont des expressions régulières.
    * 
    * @param patternsIssuer
    *           les patterns
    */
   public final void setPatternsIssuer(Map<String, List<String>> patternsIssuer) {
      this.patternsIssuer = patternsIssuer;
   }

}
