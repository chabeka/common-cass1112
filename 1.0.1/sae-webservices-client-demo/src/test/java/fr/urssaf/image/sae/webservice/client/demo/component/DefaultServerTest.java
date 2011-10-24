package fr.urssaf.image.sae.webservice.client.demo.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class DefaultServerTest {

   @Test
   public void defaultServer_success() {

      DefaultServer server = new DefaultServer(
            "server/default_success.properties");

      assertEquals("http://test/", server.getUrl().toExternalForm());
   }

   @Test
   public void defaultServer_failure_bad_url() {

      String server = "server/default_failure_bad_url.properties";

      try {
         new DefaultServer(server);

         fail("le test doit échouer");
      } catch (IllegalArgumentException e) {

         assertEquals("'server.url' is bad URL ", e.getMessage());
      }

   }

   @Test
   public void defaultServer_failure_empty_url() {

      String server = "server/default_failure_empty_url.properties";

      try {
         new DefaultServer(server);

         fail("le test doit échouer");
      } catch (IllegalArgumentException e) {

         assertEquals("'server.url' is required in " + server, e.getMessage());
      }

   }
}
