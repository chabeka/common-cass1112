package fr.urssaf.image.commons.util.checksum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

public class ChecksumStringUtilTest {

   private final static String DECODE = "Ã©a";

   private static final Logger LOG = Logger
         .getLogger(ChecksumStringUtilTest.class);

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(ChecksumStringUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   @Test
   public void md5() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumStringUtil.md5(DECODE);

      LOG.debug(checksum);
      assertEquals("erreur de calcul du MD5 de " + DECODE,
            "FC0CD26AF94F81D281C42D7EBAE20846".toLowerCase(), checksum);
   }

   @Test
   public void sha1() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumStringUtil.sha(DECODE);

      LOG.debug(checksum);
      assertEquals("erreur de calcul du SHA_1 de " + DECODE,
            "a3c236302907861c7504a2205ef11181d125b449", checksum);
   }

   @Test
   public void sha256() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumStringUtil.sha256(DECODE);

      LOG.debug(checksum);
      assertEquals("erreur de calcul du SHA_256 de " + DECODE,
            "a59e9561d61c2ae4a969a31e376e5ce7d21272021b73c59631acfb459242de4b",
            checksum);
   }

   @Test
   public void sha384() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumStringUtil.sha384(DECODE);

      LOG.debug(checksum);
      assertEquals(
            "erreur de calcul du SHA_384 de " + DECODE,
            "c92dd688c353b4aa9aab3eb7b1448550e0662d1576b787864babfbcdac1cb0a5ee7429711f879de9f6104adbc6573f0d",
            checksum);
   }

   @Test
   public void sha512() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumStringUtil.sha512(DECODE);

      LOG.debug(checksum);
      assertEquals(
            "erreur de calcul du SHA_512 de " + DECODE,
            "231d27eb65ddcf0a7919f5b666c67bd242fc52e72d65271b6556878d88dbb1c576da0c5a6d2686e6c2788da22b10ccced83d17b0d82f79e25988383f4af0fad6",
            checksum);
   }
}
