package fr.urssaf.image.commons.util.checksum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

public class ChecksumFileUtilTest {
   
   private final static String DECODE = "src/test/resources/checksum/decode.txt";

   private static final Logger LOG = Logger.getLogger(ChecksumFileUtilTest.class);

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(ChecksumFileUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   @Test
   public void md5() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumFileUtil.md5(DECODE);

      LOG.debug(checksum);
      assertEquals("erreur de calcul du MD5 de " + DECODE,
            "C87F2FF08A530DB3DE998EF7CFA701FB".toLowerCase(), checksum);
   }

   @Test
   public void sha1() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumFileUtil.sha(DECODE);

      LOG.debug(checksum);
      assertEquals("erreur de calcul du SHA_1 de " + DECODE,
            "8d10b20ec805d694bdb913727f442c960ca9436e", checksum);
   }

   @Test
   public void sha256() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumFileUtil.sha256(DECODE);

      LOG.debug(checksum);
      assertEquals("erreur de calcul du SHA_256 de " + DECODE,
            "51a968ac27d9d6b8699621f57535547d79dd2d86b8319bf9647c62a20094a288",
            checksum);
   }

   @Test
   public void sha384() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumFileUtil.sha384(DECODE);

      LOG.debug(checksum);
      assertEquals(
            "erreur de calcul du SHA_384 de " + DECODE,
            "df97e3458649421eafa37c84d38a4c455ac7e3e7524c7e75886a8134abf26fe6238ff8ec214e329f7b6e4843b007a8ad",
            checksum);
   }

   @Test
   public void sha512() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumFileUtil.sha512(DECODE);

      LOG.debug(checksum);
      assertEquals(
            "erreur de calcul du SHA_512 de " + DECODE,
            "b2ae360db7f5554dcac5199fce61f16121e43ac64cdbbbe21199ea4342968f835b4be50e8dae9d8e4717795f3edac5813a9543aa2296043ec293a236b46e64fe",
            checksum);
   }

   @Test
   public void crc32() throws NoSuchAlgorithmException, IOException {

      String checksum = ChecksumFileUtil.crc32(DECODE);

      LOG.debug(checksum);
      assertEquals("erreur de calcul du CRC32 de " + DECODE,"BAA9A6FB".toLowerCase(),checksum);
   }
}
