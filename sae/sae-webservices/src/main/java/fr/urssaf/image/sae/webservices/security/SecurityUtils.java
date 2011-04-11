package fr.urssaf.image.sae.webservices.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.vi.modele.VISignVerifParams;

/**
 * Classe utilitaires pour la mise en place du contexte de sécurité
 */
public final class SecurityUtils {

   
   private SecurityUtils() {

   }

   
   /**
    * Chargement d'un certificat X509 depuis un fichier de ressource
    *
    * @param fichierRessource le chemin du fichier de ressource (ex. : "src/test/resources/toto.crt")
    * 
    * @return l'objet certificat X509
    */
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   public static X509Certificate loadCertificat(
         String fichierRessource) {
      
      try {
         
         CertificateFactory certifFactory = CertificateFactory.getInstance("X.509");
         
         X509Certificate cert = (X509Certificate) certifFactory.generateCertificate(
               new FileInputStream(fichierRessource));
         return cert;
      
      } catch (CertificateException e) {
         throw new RuntimeException(e);
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      }
      
   }
   
   
   /**
    * Chargement d'une CRL depuis un fichier de ressource
    *
    * @param fichierRessource le chemin du fichier de ressource (ex. : "src/test/resources/toto.crl")
    * 
    * @return l'objet CRL
    */
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   public static X509CRL loadCRL(
         String fichierRessource) {
      
      try {
         
         CertificateFactory certifFactory = CertificateFactory.getInstance("X.509");
         
         X509CRL crl = (X509CRL) certifFactory.generateCRL(
               new FileInputStream(fichierRessource));
         return crl;
      
      } catch (CertificateException e) {
         throw new RuntimeException(e);
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      } catch (CRLException e) {
         throw new RuntimeException(e);
      }
      
   }
   
   /**
    * Définit les certificats des AC racine d'un objet VISignVerifParams
    * en les chargeant depuis les fichiers de ressources indiqués en paramètre
    * 
    * @param signVerifParams l'objet à remplir
    * @param ficRessources les fichiers de ressources à charger
    */
   public static void signVerifParamsSetCertifsAC(
         VISignVerifParams signVerifParams,
         List<String> ficRessources) {
      
      List<X509Certificate> lstCertifACRacine = new ArrayList<X509Certificate>();
      
      for(String fichierRessource:ficRessources) {
         lstCertifACRacine.add(loadCertificat(fichierRessource));
      }
      
      signVerifParams.setCertifsACRacine(lstCertifACRacine);
      
   }
   
   
   /**
    * Définit les certificats des CRL d'un objet VISignVerifParams
    * en les chargeant depuis les fichiers de ressources indiqués en paramètre
    * 
    * @param signVerifParams l'objet à remplir
    * @param ficRessources les fichiers de ressources à charger
    */
   public static void signVerifParamsSetCRL(
         VISignVerifParams signVerifParams,
         List<String> ficRessources) {
      
      List<X509CRL> crls = new ArrayList<X509CRL>();
      
      for(String fichierRessource:ficRessources) {
         crls.add(loadCRL(fichierRessource));
      }
      
      signVerifParams.setCrls(crls);
      
   }
   

}
