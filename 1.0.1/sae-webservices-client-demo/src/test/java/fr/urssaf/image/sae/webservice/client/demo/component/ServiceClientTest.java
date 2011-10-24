package fr.urssaf.image.sae.webservice.client.demo.component;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;

public class ServiceClientTest {

   private static URL server;

   @BeforeClass
   public static void beforeClass() throws MalformedURLException {

      server = new URL("http:\\test\\");
   }

   @Test
   public void serviceClient_success() {

      new ServiceClient("test", server);
   }

   @Test
   public void serviceClient_failure_action() {

      assertServiceClient(null);
      assertServiceClient(" ");
      assertServiceClient("");
   }

   private void assertServiceClient(String action) {

      try {
         new ServiceClient(action, server);
         fail("le test doit échouer");
      } catch (IllegalArgumentException e) {

         assertEquals("'soapAction' is required", e.getMessage());
      }
   }

   @Test
   public void serviceClient_failure_server() {

      try {
         new ServiceClient("test", null);
         fail("le test doit échouer");
      } catch (IllegalArgumentException e) {

         assertEquals("'server' is required", e.getMessage());
      }
   }
}
