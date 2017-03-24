package fr.urssaf.image.commons.util.net;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.junit.Test;

public class MySSLContextFactoryTest {

   @Test
   public void getSSLContext() throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

      MySSLContextFactory ctx = new MySSLContextFactory(
            "src/test/resources/net/cert1.p12", "toto");

      assertNotNull("ContextSSL doit être non null", ctx.getSSLContext());
   }
}
