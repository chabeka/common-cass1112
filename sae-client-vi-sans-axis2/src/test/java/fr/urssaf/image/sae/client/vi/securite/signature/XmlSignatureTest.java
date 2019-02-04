package fr.urssaf.image.sae.client.vi.securite.signature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;

import fr.urssaf.image.sae.client.vi.exception.SaeClientViRuntimeException;
import fr.urssaf.image.sae.client.vi.exception.XmlSignatureException;
import fr.urssaf.image.sae.client.vi.signature.DefaultKeystore;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.client.vi.signature.XmlSignature;

public class XmlSignatureTest {

   @After
   public void after() {
      System.clearProperty("jsr105Provider");
   }

   @Test
   public void signeXml() throws FileNotFoundException, XmlSignatureException {

      KeyStoreInterface defaultKs = DefaultKeystore.getInstance();

      InputStream xmlAsigner = new FileInputStream(
            "src/test/resources/request/pingSecure.xml");

      String aliasClePrivee = defaultKs.getAlias();
      String passwordClePrivee = defaultKs.getPassword();
      KeyStore keystore = defaultKs.getKeystore();

      String xmlSign = XmlSignature.signeXml(xmlAsigner, keystore,
            aliasClePrivee, passwordClePrivee);

      Assert.assertTrue("la signature est incorrecte", XmlSignature
            .verifieSignatureXmlCrypto(IOUtils.toInputStream(xmlSign)));
   }

   @Test
   public void signeXmlAvecProviderSpecifique() throws FileNotFoundException,
         XmlSignatureException {

      KeyStoreInterface defaultKs = DefaultKeystore.getInstance();

      InputStream xmlAsigner = new FileInputStream(
            "src/test/resources/request/pingSecure.xml");

      String aliasClePrivee = defaultKs.getAlias();
      String passwordClePrivee = defaultKs.getPassword();
      KeyStore keystore = defaultKs.getKeystore();

      System.setProperty("jsr105Provider", "toto");

      try {

         XmlSignature.signeXml(xmlAsigner, keystore, aliasClePrivee,
               passwordClePrivee);

         Assert.fail("Une exception était attendue");

      } catch (SaeClientViRuntimeException ex) {

         Assert.assertEquals("java.lang.ClassNotFoundException: toto", ex
               .getMessage());

      }

   }

}
