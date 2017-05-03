package fr.urssaf.image.sae.webservice.client.demo.security.signature;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.webservice.client.demo.component.DefaultKeystore;
import fr.urssaf.image.sae.webservice.client.demo.security.signature.exception.XmlSignatureException;

public class XmlSignatureTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(XmlSignatureTest.class);

   @Test
   public void signeXml() throws FileNotFoundException, XmlSignatureException {

      DefaultKeystore defaultKs = DefaultKeystore.getInstance();

      InputStream xmlAsigner = new FileInputStream(
            "src/test/resources/request/pingSecure.xml");

      String aliasClePrivee = defaultKs.getAlias();
      String passwordClePrivee = defaultKs.getPassword();
      KeyStore keystore = defaultKs.getKeystore();

      String xmlSign = XmlSignature.signeXml(xmlAsigner, keystore,
            aliasClePrivee, passwordClePrivee);

      LOG.debug("\nmessage soap signé:\n" + xmlSign);

      assertTrue("la signature est incorrecte", XmlSignature
            .verifieSignatureXmlCrypto(IOUtils.toInputStream(xmlSign)));
   }
}
