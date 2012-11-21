package fr.urssaf.image.sae.client.vi.securite.signature;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import fr.urssaf.image.sae.client.vi.exception.XmlSignatureException;
import fr.urssaf.image.sae.client.vi.signature.DefaultKeystore;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.client.vi.signature.XmlSignature;

public class XmlSignatureTest {

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

      assertTrue("la signature est incorrecte", XmlSignature
            .verifieSignatureXmlCrypto(IOUtils.toInputStream(xmlSign)));
   }
   
   
}
