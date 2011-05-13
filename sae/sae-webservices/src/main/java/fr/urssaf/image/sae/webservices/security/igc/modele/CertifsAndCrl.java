package fr.urssaf.image.sae.webservices.security.igc.modele;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Classe permettant de stocker en mémoire les certificats des AC racine et les
 * CRL
 * 
 * 
 */
public class CertifsAndCrl {

   private List<X509Certificate> certsAcRacine;

   private List<X509CRL> crl;

   private DateTime dateMajCrl;

   /**
    * 
    * @return Les certificats des AC racines en lesquelles le SAE a confiance
    */
   public final List<X509Certificate> getCertsAcRacine() {
      return certsAcRacine;
   }

   /**
    * 
    * @param certsAcRacine
    *           Les certificats des AC racines en lesquelles le SAE a confiance
    */
   public final void setCertsAcRacine(List<X509Certificate> certsAcRacine) {
      this.certsAcRacine = certsAcRacine;
   }

   /**
    * 
    * @return Les CRL des différentes AC
    */
   public final List<X509CRL> getCrl() {
      return crl;
   }

   /**
    * 
    * @param crl
    *           Les CRL des différentes AC
    */
   public final void setCrl(List<X509CRL> crl) {
      this.crl = crl;
   }

   /**
    * 
    * @return Date de la dernière mise à jour de l’attribut crl
    */
   public final DateTime getDateMajCrl() {
      return dateMajCrl;
   }

   /**
    * 
    * @param dateMajCrl
    *           Date de la dernière mise à jour de l’attribut crl
    */
   public final void setDateMajCrl(DateTime dateMajCrl) {
      this.dateMajCrl = dateMajCrl;
   }

}
