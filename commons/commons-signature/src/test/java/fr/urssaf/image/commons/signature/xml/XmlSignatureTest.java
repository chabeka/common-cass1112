package fr.urssaf.image.commons.signature.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.log4j.Logger;
import org.junit.Test;

import fr.urssaf.image.commons.signature.exceptions.XmlSignatureException;
import fr.urssaf.image.commons.signature.utils.KeyStoreUtils;
import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

@SuppressWarnings({"PMD.LongVariable","PMD.JUnitTestsShouldIncludeAssert"})
public class XmlSignatureTest {

   private static final Logger LOG = Logger.getLogger(XmlSignatureTest.class);
   
   
   @Test
   public void testConstructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(XmlSignature.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   @Test
   public void testSignature()
   throws 
      KeyStoreException, 
      NoSuchAlgorithmException, 
      CertificateException, 
      IOException, 
      XmlSignatureException {
      
      // Données d'entrées
      String cheminPkcs12 = "src/test/resources/pkcs12/Portail_Image.p12";
      String p12password = "hiUnk6O3QnRN";
      String fichierXmlAsigner = "src/test/resources/xml_a_signer/xml_a_signer_01.xml";
      // String fichierXmlResultatAttendu = "src/test/resources/xml_a_signer/xml_a_signer_01_resultat_attendu.xml";
      
      // Préparation du KeyStore
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      FileInputStream keyStoreInputStream = new FileInputStream(cheminPkcs12);
      try {
         keyStore.load(keyStoreInputStream, p12password.toCharArray());
      } finally {
         keyStoreInputStream.close();
      }
      String aliasClePrivee = keyStore.aliases().nextElement();
      KeyStoreUtils.logContenuKeyStore(LOG,keyStore);
      
      // Chargement du XML à signer
      FileInputStream xmlASignerInputStream = new FileInputStream(fichierXmlAsigner);
      
      // Appel de la méthode de signature
      String xmlSigne;
      try {
      
         // Appel de la méthode de signature
         String passwordClePrivee = p12password;
         xmlSigne = XmlSignature.signeXml(
               xmlASignerInputStream,keyStore,aliasClePrivee,passwordClePrivee);
         }
      finally {
         xmlASignerInputStream.close();
      }
      
      // Affichage du XML signé dans la console
      LOG.debug(xmlSigne);
      
      // Vérification du résultat
      // TODO : réactiver test
      /*
      String resultatAttendu = FileReaderUtil.read(fichierXmlResultatAttendu,"UTF-8");
      assertEquals(
            "La génération de la signature XML est incorrecte",
            resultatAttendu,
            xmlSigne);
      /* */
      
   }
   
   
   @Test
   public void testVerificationSignature()
   throws 
      FileNotFoundException, 
      XmlSignatureException {
      
      // Chargement du XML à signer
      String xmlVerif = "src/test/resources/xml_a_verifier/xml_signe_01.xml";
      FileInputStream inputStream = new FileInputStream(xmlVerif);
      
      // Appel de la méthode attendu 
      Boolean bSignatureOk = XmlSignature.verifieSignatureXmlCrypto(inputStream);
      
      // Vérification du résultat attendu
      Boolean bResultatAttendu = true;
      assertEquals(
            "La vérification de la signature XML a échoué",
            bResultatAttendu,
            bSignatureOk);
      
   }
   
   
   
}
