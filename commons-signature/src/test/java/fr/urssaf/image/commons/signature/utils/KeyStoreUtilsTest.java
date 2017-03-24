package fr.urssaf.image.commons.signature.utils;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.log4j.Logger;
import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

@SuppressWarnings({
   "PMD.JUnitTestsShouldIncludeAssert",
   "PMD.LongVariable",
   "PMD.MethodNamingConventions"})
public class KeyStoreUtilsTest {

   private static final Logger LOG = Logger.getLogger(KeyStoreUtilsTest.class);
   
   
   @Test
   public void testConstructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(KeyStoreUtils.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   @Test(expected = IllegalArgumentException.class)
   public void test_logContenuKeyStore_ArgumentNull() throws KeyStoreException {
      KeyStoreUtils.logContenuKeyStore(null, null);
   }
   
   
   @Test
   public void test_logContenuKeyStore()
   throws 
      KeyStoreException, 
      NoSuchAlgorithmException, 
      CertificateException, 
      IOException {
      
      // Chargement d'un PKCS#12 de test dans un KeyStore
      String cheminPkcs12 = "src/test/resources/pkcs12/Portail_Image.p12";
      String p12password = "hiUnk6O3QnRN";
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      FileInputStream keyStoreInputStream = new FileInputStream(cheminPkcs12);
      try {
         keyStore.load(keyStoreInputStream, p12password.toCharArray());
      } finally {
         keyStoreInputStream.close();
      }
      
      // Appel de la méthode à tester
      KeyStoreUtils.logContenuKeyStore(LOG,keyStore);
      
   }
      
   
   @Test
   public void test_logContenuKeyStore_keystoreNull() throws KeyStoreException {
      KeyStoreUtils.logContenuKeyStore(LOG,null);
   }
   
}
