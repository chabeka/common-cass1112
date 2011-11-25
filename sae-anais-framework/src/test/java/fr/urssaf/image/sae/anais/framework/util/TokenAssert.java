package fr.urssaf.image.sae.anais.framework.util;

// CHECKSTYLE:OFF
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

@SuppressWarnings("PMD")
public final class TokenAssert {

   private TokenAssert() {

   }

   public static void assertCTD_0_right(String xml) throws IOException {

      assertCTD("ctd_0_right.xml", xml);

   }

   public static void assertCTD_1_right(String xml) throws IOException {

      assertCTD("ctd_1_right.xml", xml);

   }

   public static void assertCTD_rights(String xml) throws IOException {

      assertCTD("ctd_rights.xml", xml);

   }

   public static void assertCTD(String key, String xml) throws IOException {

      File file = new File("src/test/resources/ctd/"+key);
      
      assertEquals(FileUtils.readFileToString(file, "UTF-8"),xml);
   }
}

// CHECKSTYLE:ON
