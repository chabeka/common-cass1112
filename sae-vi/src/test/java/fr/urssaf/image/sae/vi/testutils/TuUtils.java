package fr.urssaf.image.sae.vi.testutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.urssaf.image.sae.vi.modele.VISignVerifParams;


/**
 * Méthodes utilitaires pour les tests unitaires
 */
@SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
public final class TuUtils {
   
   
   private TuUtils() {
      
   }

   
   /**
    * Renvoie un objet VISignVerifParams qui contient tous les éléments nécessaires
    * pour vérifier un VI signé avec Portail_Image.p12
    * 
    * @return l'objet VISignVerifParams
    */
   public static VISignVerifParams buildSignVerifParamsOK() {
      
      
      VISignVerifParams signVerifParams = new VISignVerifParams();
      
      signVerifParamsSetCertifsAC(
            signVerifParams, 
            Arrays.asList("src/test/resources/AC/AC-01_IGC_A.crt"));
      
      signVerifParamsSetCRL(
            signVerifParams, 
            Arrays.asList(
                  "src/test/resources/CRL/CRL_AC-01_Pseudo_IGC_A.crl",
                  "src/test/resources/CRL/CRL_AC-02_Pseudo_ACOSS.crl",
                  "src/test/resources/CRL/CRL_AC-03_Pseudo_Appli.crl"));
      
      return signVerifParams;
      
   }
   
   
   /**
    * Chargement d'un certificat X509 depuis un fichier de ressource
    *
    * @param fichierRessource le chemin du fichier de ressource (ex. : "src/test/resources/toto.crt")
    * 
    * @return l'objet certificat X509
    */
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
         lstCertifACRacine.add(TuUtils.loadCertificat(fichierRessource));
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
         crls.add(TuUtils.loadCRL(fichierRessource));
      }
      
      signVerifParams.setCrls(crls);
      
   }
   
}
