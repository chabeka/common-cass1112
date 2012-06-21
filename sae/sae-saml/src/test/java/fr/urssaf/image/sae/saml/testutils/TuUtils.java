package fr.urssaf.image.sae.saml.testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.FileUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.Signature;
import org.slf4j.Logger;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.saml.opensaml.SamlXML;
import fr.urssaf.image.sae.saml.params.SamlSignatureVerifParams;
import fr.urssaf.image.sae.saml.util.XMLUtils;


/**
 * Méthodes utilitaires pour les tests unitaires
 */
@SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
public final class TuUtils {

   
   private TuUtils() {
      
   }
   
   /**
    * Chargement d'un objet Signature depuis un fichier de ressource
    * 
    * @param fichierRessource
    *    Le chemin du fichier de ressource<br>
    *    exemple : "src/test/resources/signature/signature01.xml"
    *    
    * @return l'objet Signature
    */
   public static Signature loadSignature(String fichierRessource) {
      try {
         File file = new File(fichierRessource);
         String signatureStr = FileUtils.readFileToString(file, CharEncoding.UTF_8);
         Element element = XMLUtils.parse(signatureStr);
         return (Signature) SamlXML.unmarshaller(element);
      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (SAXException e) {
         throw new RuntimeException(e);
      }
   }
   
   
   /**
    * Chargement d'un objet Assertion depuis un fichier de ressource
    * 
    * @param fichierRessource
    *    fichierRessource Le chemin du fichier de ressource<br>
    *    exemple : "src/test/resources/assertion/assertion01.xml"
    *    
    * @return l'objet Assertion
    */
   public static Assertion getAssertion(String fichierRessource) {
      try {
         File file = new File(fichierRessource);
         String assertionSaml = FileUtils.readFileToString(file, CharEncoding.UTF_8);
         Element element = XMLUtils.parse(assertionSaml);
         return (Assertion) SamlXML.unmarshaller(element);
      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (SAXException e) {
         throw new RuntimeException(e);
      }
   }
   
   
   /**
    * Charge un fichier de ressources vers un objet Element
    * 
    * @param fichierRessource le chemin du fichier de ressource (ex. : "src/test/resources/toto.xml")
    * @return l'objet Element
    * @throws SAXException en cas d'erreur de parsing du fichier de ressource
    * @throws IOException si le fichier de ressource indiqué n'est pas trouvé
    */
   public static Element loadResourceFileToElement(
         String fichierRessource) 
      throws 
         SAXException, 
         IOException {

      File file = new File(fichierRessource);
      return XMLUtils.parse(FileUtils.readFileToString(file, CharEncoding.UTF_8));

   }
   
   
   /**
    * Fait un log.debug sur une assertion SAML typée en Element
    * 
    * @param logger le logger à utiliser
    * @param assertion l'assertion SAML
    */
   public static void debugAssertion(
         Logger logger,
         Element assertion) {
      
      Transformer transformer = XMLUtils.initTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(
            "{http://xml.apache.org/xslt}indent-amount", "4");

      logger.debug("\n" + XMLUtils.print(assertion, CharEncoding.UTF_8, transformer));
      
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
    * Renvoie un objet SamlSignatureVerifParams pour la vérification de la
    * signature XML, à partir des éléments de test standard présents
    * dans le répertoire src/test/resources/certif_test_std
    * 
    * @return l'objet permettant de vérifier la signature
    */
   public static SamlSignatureVerifParams buildSignVerifParamsStd() {
      
      // Création de l'objet
      SamlSignatureVerifParams signVerifParams = new SamlSignatureVerifParams();
      
      // Les certificats des AC racine
      signVerifParamsSetCertifsAC(
            signVerifParams,
            Arrays.asList("src/test/resources/certif_test_std/X509/AC-01_IGC_A.crt"));
      
      // Les CRL
      signVerifParamsSetCRL(
            signVerifParams,
            Arrays.asList(
                  "src/test/resources/certif_test_std/CRL/CRL_AC-01_Pseudo_IGC_A.crl",
                  "src/test/resources/certif_test_std/CRL/CRL_AC-02_Pseudo_ACOSS.crl",
                  "src/test/resources/certif_test_std/CRL/CRL_AC-03_Pseudo_Appli.crl"));
      
      // Renvoie du résultat
      return signVerifParams;
      
   }
   
   
   /**
    * Définit les certificats des AC racine d'un objet SamlSignatureVerifParams
    * en les chargeant depuis les fichiers de ressources indiqués en paramètre
    * 
    * @param signVerifParams l'objet à remplir
    * @param ficRessources les fichiers de ressources à charger
    */
   public static void signVerifParamsSetCertifsAC(
         SamlSignatureVerifParams signVerifParams,
         List<String> ficRessources) {
      
      List<X509Certificate> lstCertifACRacine = new ArrayList<X509Certificate>();
      
      for(String fichierRessource:ficRessources) {
         lstCertifACRacine.add(TuUtils.loadCertificat(fichierRessource));
      }
      
      signVerifParams.setCertifsACRacine(lstCertifACRacine);
      
   }
   
   
   /**
    * Définit les certificats des CRL d'un objet SamlSignatureVerifParams
    * en les chargeant depuis les fichiers de ressources indiqués en paramètre
    * 
    * @param signVerifParams l'objet à remplir
    * @param ficRessources les fichiers de ressources à charger
    */
   public static void signVerifParamsSetCRL(
         SamlSignatureVerifParams signVerifParams,
         List<String> ficRessources) {
      
      List<X509CRL> crls = new ArrayList<X509CRL>();
      
      for(String fichierRessource:ficRessources) {
         crls.add(TuUtils.loadCRL(fichierRessource));
      }
      
      signVerifParams.setCrls(crls);
      
   }
   
}
