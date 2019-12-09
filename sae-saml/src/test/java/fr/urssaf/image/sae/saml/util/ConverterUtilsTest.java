package fr.urssaf.image.sae.saml.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URISyntaxException;

import org.joda.time.DateTime;
import org.junit.Test;

@SuppressWarnings({"PMD.MethodNamingConventions","PMD.JUnitAssertionsShouldIncludeMessage"})
public class ConverterUtilsTest {

   private static final String FAIL_MESSAGE = "le test doit échouer";

   @Test
   public void date() {

      DateTime date = new DateTime(1999, 12, 31, 23, 59, 59, 999);

      assertEquals("Fri Dec 31 23:59:59 CET 1999", ConverterUtils.date(date)
            .toString());

   }

   @Test
   public void date_null() {

      assertNull(ConverterUtils.date(null));

   }

   @Test
   public void uri_success() {

      String host = "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent";

      assertEquals(host, ConverterUtils.uri(host).toASCIIString());
   }

   @Test
   public void uri_failure_null() {

      assertNull(ConverterUtils.uri(null));
   }

   @Test
   public void uri_failure_bad() {

      try {
         ConverterUtils.uri("bad uri");
         fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {

         assertEquals(URISyntaxException.class, e.getCause().getClass());
      }
   }

   @Test
   public void uuid_success() {

      String uuid = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6";

      assertEquals(uuid, ConverterUtils.uuid(uuid).toString());
   }

   @Test(expected = IllegalArgumentException.class)
   public void uuid_failure() {

      ConverterUtils.uuid("bad uuid");

   }
}
