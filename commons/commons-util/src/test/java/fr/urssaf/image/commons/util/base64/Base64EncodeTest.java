package fr.urssaf.image.commons.util.base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;


/**
 * Tests unitaires de la classe {@link Base64Encode}
 */
@SuppressWarnings("PMD")
public class Base64EncodeTest {

   private final static String DIRECTORY;
   
   private static Boolean deleteDirectoryAfterTests;

   static {
      DIRECTORY = FilenameUtils.concat(SystemUtils.getJavaIoTmpDir()
            .getAbsolutePath(), "base64");
   }

   @BeforeClass
   public static void init() throws IOException {
      
      // Création d'un répertoire temporaire
      File directory = new File(DIRECTORY);
      deleteDirectoryAfterTests = ! directory.exists();
      FileUtils.forceMkdir(directory);
      FileUtils.cleanDirectory(directory);
      
   }
   
   
   @AfterClass
   public static void nettoyage() throws IOException {
      
      // Nettoyage des fichiers créés
      File directory = new File(DIRECTORY);
      FileUtils.cleanDirectory(directory);
      if (deleteDirectoryAfterTests) {
         FileUtils.deleteDirectory(directory);
      }
      
   }


   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(Base64Encode.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test d'encodage en base 64 d'une chaîne de caractères ISO8859_1
    */
   @SuppressWarnings("PMD.MethodNamingConventions")
   @Test
   public void encodeISO8859_1() {
      String actual = Base64Encode.encode("éa");
      String expected = "6WE=";
      assertEquals("Encodage en base 64 d'une chaîne de caractères ISO8859_1", expected, actual);
   }
   
   
   /**
    * Test d'encodage en base 64 d'une chaîne de caractères UTF-8
    */
   @Test
   public void encodeUTF8() {
      String actual = Base64Encode.encodeUTF8("éa");
      String expected = "w6lh";
      assertEquals("Encodage en base 64 d'une chaîne de caractères UTF-8", expected, actual);
   }
   
   
   /**
    * Test d'encodage en base 64 d'une chaîne de caractères dont on spécifie le Charset
    */
   @Test
   public void encode() {
      String actual = Base64Encode.encode("à@çù", CharEncoding.ISO_8859_1);
      String expected = "4EDn+Q==";
      assertEquals("Encodage en base 64 d'une chaîne de caractères dont on spécifie le Charset", expected, actual);
   }
   
   
   /**
    * Test d'encodage en base 64 d'un fichier dans un autre fichier.
    * 
    * @throws IOException
    */
   @Test
   public void encodeFileTest1() throws IOException {

      // Encodage du fichier source
      String fichierSource = "src/test/resources/base64/decode.txt";
      String fichierDest = FilenameUtils.concat(DIRECTORY, "encodeFileTest1.base64.txt");
      Base64Encode.encodeFile(fichierSource, fichierDest);
      
      // Lecture du fichier résultat
      String actual = FileUtils.readFileToString(new File(fichierDest));
      actual = StringUtils.trim(actual);
            
      // Vérification
      String expected = "w4PCqWENCmF6DQoNCg==";
      assertEquals("Encodage en base 64 d'un fichier dans un autre fichier",expected,actual);
      
   }
   
   
   /**
    * Test d'encodage en base 64 d'un fichier dans un autre fichier.<br><br>
    * 
    * Cas de test : fichier suffisamment gros pour provoquer la césure des données
    * encodées en lignes de 76 caractères
    * 
    * @throws IOException
    */
   @Test
   public void encodeFileTest2() throws IOException {

      // Encodage du fichier source
      String fichierSource = "src/test/resources/base64/lorem_ipsum.txt";
      String fichierDest = FilenameUtils.concat(DIRECTORY, "encodeFileTest2.base64.txt");
      Base64Encode.encodeFile(fichierSource, fichierDest);
      
      // Lecture du fichier résultat
      String actual = FileUtils.readFileToString(new File(fichierDest));
      actual = StringUtils.trim(actual);
      
      // Vérification
      StringBuffer expected = new StringBuffer(320);
      expected.append("TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4g");
      expected.append(SystemUtils.LINE_SEPARATOR);
      expected.append("VXQgdXQgdXJuYSBpZCByaXN1cyB1bGxhbWNvcnBlciB2YXJpdXMgc2l0IGFtZXQgdWx0cmljZXMg");
      expected.append(SystemUtils.LINE_SEPARATOR);
      expected.append("bGVjdHVzLiBOdWxsYW0gbWFsZXN1YWRhLCBtYXNzYSBpZCBlbGVpZmVuZCB0aW5jaWR1bnQsIGp1");
      expected.append(SystemUtils.LINE_SEPARATOR);
      expected.append("c3RvIGFudGUgbW9sZXN0aWUgbWFnbmEsIHZpdGFlIGVsZWlmZW5kIGRvbG9yIGRvbG9yIHNlZCB0");
      expected.append(SystemUtils.LINE_SEPARATOR);
      expected.append("b3J0b3Iu");
      assertEquals("Encodage en base 64 d'un fichier dans un autre fichier",expected.toString(),actual);
      
   }
   
   
   /**
    * Test d'encodage en base 64 d'un fichier dans un autre fichier.<br><br>
    * 
    * Cas de test : spécifier la largeur des lignes, et le séparateur de lignes
    * 
    * @throws IOException
    */
   @Test
   public void encodeFileTest3() throws IOException {

      // Encodage du fichier source
      String fichierSource = "src/test/resources/base64/fichier_zip.zip";
      String fichierDest = FilenameUtils.concat(DIRECTORY, "encodeFileTest3.base64.txt");
      Base64Encode.encodeFile(fichierSource, fichierDest, 152, "[]".getBytes());
      
      // Lecture du fichier résultat
      String actual = FileUtils.readFileToString(new File(fichierDest));
      actual = StringUtils.trim(actual);
      
      // Vérification
      StringBuffer expected = new StringBuffer(210);
      expected.append("UEsDBAoAAAAAAE+B9TwWdPdQEQAAABEAAAARAAAAZmljaGllcl90ZXh0ZS50eHRibGENCmJsYQ0K6ehA5yYNClBLAQIUAAoAAAAAAE+B9TwWdPdQEQAAABEAAAARAAAAAAAAAAAAIAAAAAAAAABmaWNo");
      expected.append("[]");
      expected.append("aWVyX3RleHRlLnR4dFBLBQYAAAAAAQABAD8AAABAAAAAAAA=");
      expected.append("[]");
      assertEquals("Encodage en base 64 d'un fichier dans un autre fichier",expected.toString(),actual);
      
   }
   
   
   /**
    * Test d'encodage en base64 d'un fichier.<br><br>
    * 
    * Cas de test : fichier vide
    * 
    * @throws IOException
    */
   @Test
   public void encodeFileToStringTest1() throws IOException {

      String cheminFichier = "src/test/resources/base64/fichier_vide";
      
      String base64 = Base64Encode.encodeFileToString(cheminFichier);
      
      StringBuffer expected = new StringBuffer();
      
      assertEquals(
            "echec encodage en base64 du fichier:" + cheminFichier + ":",
            expected.toString(),
            base64);
   }
   
   
   /**
    * Test d'encodage en base64 d'un fichier.<br><br>
    * 
    * Cas de test : petit fichier texte, avec caractères accentués
    * (fichier utilisé pour les tests de la 1ère implémentation base64)
    * 
    * @throws IOException
    */
   @Test
   public void encodeFileToStringTest2() throws IOException {

      String cheminFichier = "src/test/resources/base64/decode.txt";
      
      String base64 = Base64Encode.encodeFileToString(cheminFichier);
      
      StringBuffer expected = new StringBuffer(20);
      expected.append("w4PCqWENCmF6DQoNCg==");
      
      assertEquals(
            "echec encodage en base64 du fichier:" + cheminFichier + ":",
            expected.toString(),
            base64);
   }
   
   
   /**
    * Test d'encodage en base64 d'un fichier.<br><br>
    * 
    * Cas de test : petit fichier texte
    * 
    * @throws IOException
    */
   @Test
   public void encodeFileToStringTest3() throws IOException {

      // Cas de test : petit fichier texte
      
      String cheminFichier = "src/test/resources/base64/fichier_texte_60.txt";
      
      String base64 = Base64Encode.encodeFileToString(cheminFichier);
      
      StringBuffer expected = new StringBuffer(80);
      expected.append("MDEyMzQ1Njc4OQ0KMDEyMzQ1Njc4OQ0KMDEyMzQ1Njc4OQ0KMDEyMzQ1Njc4OQ0K");
      expected.append("MDEyMzQ1Njc4");
      
      assertEquals(
            "echec encodage en base64 du fichier:" + cheminFichier + ":",
            expected.toString(),
            base64);
   }
   
   
   /**
    * Test d'encodage en base64 d'un fichier.<br><br>
    * 
    * Cas de test : fichier texte
    * (qui a généré un échec du test de la 1ère implémentation base64)
    * 
    * @throws IOException
    */
   @Test
   public void encodeFileToStringTest4() throws IOException {

      String cheminFichier = "src/test/resources/base64/fichier_texte_65.txt";
      
      String base64 = Base64Encode.encodeFileToString(cheminFichier);
      
      StringBuffer expected = new StringBuffer(100);
      expected.append("MDEyMzQ1Njc4OQ0KMDEyMzQ1Njc4OQ0KMDEyMzQ1Njc4OQ0KMDEyMzQ1Njc4OQ0K");
      expected.append("MDEyMzQ1Njc4OQ0KMDEy");
      
      assertEquals(
            "echec encodage en base64 du fichier:" + cheminFichier + ":",
            expected.toString(),
            base64);
   }
   
   
   /**
    * Test d'encodage en base64 d'un fichier.<br><br>
    * 
    * Cas de test : fichier binaire
    * 
    * @throws IOException
    */
   @Test
   public void encodeFileToStringTest5() throws IOException {

      String cheminFichier = "src/test/resources/base64/fichier_zip.zip";
      
      String base64 = Base64Encode.encodeFileToString(cheminFichier);
      
      StringBuffer expected = new StringBuffer(200);
      expected.append("UEsDBAoAAAAAAE+B9TwWdPdQEQAAABEAAAARAAAAZmljaGllcl90ZXh0ZS50eHRi");
      expected.append("bGENCmJsYQ0K6ehA5yYNClBLAQIUAAoAAAAAAE+B9TwWdPdQEQAAABEAAAARAAAA");
      expected.append("AAAAAAAAIAAAAAAAAABmaWNoaWVyX3RleHRlLnR4dFBLBQYAAAAAAQABAD8AAABA");
      expected.append("AAAAAAA=");
      
      assertEquals(
            "echec encodage en base64 du fichier:" + cheminFichier + ":",
            expected.toString(),
            base64);
   }
   

}
