package fr.urssaf.image.sae.storage.dfce.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Pour calculer en ligne les hash.
 * <ul>
 * <li><a href="http://www.webutils.pl/index.php?idx=md5">calcul de MD5</a></li>
 * <li><a href="http://www.webutils.pl/index.php?idx=sha1">calcul de
 * SHA-1,SHA-256,SHA-358,SHA-512</a></li>
 * <ul>
 * 
 * 
 */
@SuppressWarnings("PMD.MethodNamingConventions")
public class HashUtilsTest {

   private byte[] data;

   @Test
   public void testHash() throws IOException, NoSuchAlgorithmException {

      File file = new File("src/test/resources/PDF/doc1.PDF");
      byte[] contentByte = FileUtils.readFileToByteArray(file);
      InputStream stream = new FileInputStream(file);

      String hashByte = HashUtils.hashHex(contentByte, "SHA-1");
      String hashStream = HashUtils.hashHex(stream, "SHA-1");

      stream.close();

      System.out.println(hashByte == hashStream);

   }

   @Before
   public void before() throws IOException {

      File file = new File("src/test/resources/PDF/doc1.PDF");
      data = FileUtils.readFileToByteArray(file);

   }

   @Test
   public void hashHex_success_MD5() throws NoSuchAlgorithmException {

      String expectedHash = "b25035165d5164454cb2600cb88e0c11";
      String actualHash = HashUtils.hashHex(data, "MD5");

      Assert.assertEquals("le calcul de MD5 est incorrect", expectedHash,
            actualHash);

   }

   @Test
   public void hashHex_success_SHA1() throws NoSuchAlgorithmException {

      String expectedHash = "a2f93f1f121ebba0faef2c0596f2f126eacae77b";
      String actualHash = HashUtils.hashHex(data, "SHA-1");

      Assert.assertEquals("le calcul de SHA-1 est incorrect", expectedHash,
            actualHash);

   }

   @Test
   public void testSha1InputStream() {
      String expectedHash = "a2f93f1f121ebba0faef2c0596f2f126eacae77b";
      File file = new File("src/test/resources/PDF/doc1.PDF");
      InputStream stream = null;

      try {
         stream = new FileInputStream(file);
         String actualHash = HashUtils.hashHex(stream, "SHA-1");

         Assert.assertEquals("le hash est incoorect", expectedHash, actualHash);

      } catch (Exception e) {
         Assert.fail("aucune erreur attendue");

      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }

   }

   @Test
   public void hashHex_success_SHA256() throws NoSuchAlgorithmException {

      String expectedHash = "af1ad1fbc359c6c0490684e3ce8995f08523e92f683837569c5dc32ed0ca42da";
      String actualHash = HashUtils.hashHex(data, "SHA-256");

      Assert.assertEquals("le calcul de SHA-256 est incorrect", expectedHash,
            actualHash);

   }

   @Test
   public void hashHex_success_SHA384() throws NoSuchAlgorithmException {

      String expectedHash = "abad79e5b2848d6f724040135a929d6d4527c79172be2f3f71cb9b2f5b37b30ec731d7b79301f9d11936262c8b9f22ed";
      String actualHash = HashUtils.hashHex(data, "SHA-384");

      Assert.assertEquals("le calcul de SHA-384 est incorrect", expectedHash,
            actualHash);

   }

   @Test
   public void hashHex_success_SHA512() throws NoSuchAlgorithmException {

      String expectedHash = "e228b7bb881b656a9c2839d888aa473afa1e8dcb97d760fb5915e74f8e864ea6ea5eb6e571ec1479cac6648ce5bf5186f1a5b8e3843b50bb190e9cebb62b2073";
      String actualHash = HashUtils.hashHex(data, "SHA-512");

      Assert.assertEquals("le calcul de SHA-512 est incorrect", expectedHash,
            actualHash);

   }

   @Test(expected = NoSuchAlgorithmException.class)
   public void hashHex_failure() throws NoSuchAlgorithmException {

      HashUtils.hashHex(data, "SHA-1024");

   }
}
