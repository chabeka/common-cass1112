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
 * Tests unitaires de la classe Base64Decode
 */
@SuppressWarnings("PMD")
public class Base64DecodeTest {

   private final static String DIRECTORY;
   
   private static Boolean deleteDirectoryAfterTests;

   static {
      DIRECTORY = FilenameUtils.concat(
            SystemUtils.getJavaIoTmpDir().getAbsolutePath(),
            "base64");
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
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(Base64Decode.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test du décodage sans préciser de charset.<br>
    * ISO 8859-1 doit être utilisé.
    */
   @Test
   public void decodeAvecCharsetParDefaut() {
      String actual = Base64Decode.decode("6WE=");
      String expected = "éa";
      assertEquals("Décodage d'une chaîne base64 en chaîne ISO 8859-1 (défaut)", expected, actual);
   }
   
   /**
    * Test du décodage en précisant une sortie au format UTF-8
    */
   @Test
   public void decodeUTF8() {

      String actual = Base64Decode.decodeUTF8("w6lh");
      String expected = "éa";
      assertEquals("Décodage d'une chaîne base64 en chaîne UTF-8", expected, actual);
   }
   
   /**
    * Test du décodage en précisant le charset de la sortie
    */
   @SuppressWarnings("PMD.MethodNamingConventions")
   @Test
   public void decodeAvecCharset() {
      String actual = Base64Decode.decode("6WE=",CharEncoding.ISO_8859_1);
      String expected = "éa";
      assertEquals("Décodage d'une chaîne base64 en chaîne ISO 8859-1", expected, actual);
   }

         
   @Test
   public void decodeFile() throws IOException {

      // Décode
      String fichierBase64 = "src/test/resources/base64/lorem_ipsum_base64.txt";
      String fichierDest = FilenameUtils.concat(DIRECTORY, "decodeFileTest1.txt");
      Base64Decode.decodeFile(fichierBase64,fichierDest);
      
      // Lecture du fichier de sortie
      String actual = FileUtils.readFileToString(new File(fichierDest),CharEncoding.UTF_8);
      actual = StringUtils.trim(actual);
      
      // Vérification
      String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut ut urna id risus ullamcorper varius sit amet ultrices lectus. Nullam malesuada, massa id eleifend tincidunt, justo ante molestie magna, vitae eleifend dolor dolor sed tortor.";
      assertEquals("Décodage d'un fichier en base 64 dans un autre fichier",expected,actual);

   }
   
   
   /**
    * Test de décodage d'une chaîne base64 stockée dans un fichier<br>
    * Cas de test :<br>
    * - la base 64 représente une chaîne de caractères UTF-8<br>
    * - le fichier contenant la base 64 est écrit en UTF-8<br>
    * - le fichier contenant la base 64 est découpé en lignes
    * 
    * @throws IOException
    */
   @Test
   public void decodeFileToString() throws IOException {

      // Décode
      String fichierBase64 = "src/test/resources/base64/lorem_ipsum_base64.txt";
      int longueurLigne = 76;
      byte[] separateurLigne = "\r\n".getBytes();
      String actual = Base64Decode.decodeFileToString(
            fichierBase64, 
            longueurLigne,
            separateurLigne,
            CharEncoding.UTF_8);
      
      // Vérification
      String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut ut urna id risus ullamcorper varius sit amet ultrices lectus. Nullam malesuada, massa id eleifend tincidunt, justo ante molestie magna, vitae eleifend dolor dolor sed tortor.";
      assertEquals("Décodage d'un fichier en base 64 vers une chaîne de caractères",expected,actual);

   }
   
   
   /**
    * Test de décodage d'une chaîne base64 stockée dans un fichier<br>
    * Cas de test :<br>
    *  - la base 64 représente une chaîne de caractères UTF-8<br>
    *  - le fichier contenant la base 64 est écrit en UTF-16<br>
    * 
    * @throws IOException
    */
   @Test
   public void decodeFileToStringTest2() throws IOException {
      
      // ---------------------------------------------------------
      // Conditions initiales
      //  - Encodage d'une chaîne de caractères UTF_8 en base 64
      //  - Ecriture de cette chaîne dans un fichier en UTF_16
      // ---------------------------------------------------------
      
      // pour le caractère "é" :
      //  - UTF_8  => 2 octets
      //  - UTF_16 => 4 octets
      String chaineTest = "é";
      
      // Récupération de la base64
      //  - UTF_8  : w6k=
      //  - UTF_16 : /v8A6Q==
      // La base 64 est sur 4 octets
      String base64 = Base64Encode.encode(chaineTest, CharEncoding.UTF_8);
      assertEquals("Contrôle intermédiaire de la base64 générée","w6k=",base64);
      
      // Ecriture d'un fichier en UTF_16 contenant la base 64
      // Le fichier fera 10 octets
      //  Pour le savoir :
      //  System.out.println("w6k=".getBytes(CharEncoding.UTF_16).length);
      String fichierBase64 = FilenameUtils.concat(DIRECTORY, "decodeFileToStringTest2.txt");
      FileUtils.writeStringToFile(new File(fichierBase64), base64, CharEncoding.UTF_16);
      
      
      // ---------------------------------------------------------
      // Test
      // ---------------------------------------------------------
      
      String actual = Base64Decode.decodeFileToString(fichierBase64,-1,null,CharEncoding.UTF_8);
      
      
      // ---------------------------------------------------------
      // Vérifications
      // ---------------------------------------------------------
      
      String expected = "é";
      assertEquals("Décodage d'une chaîne base64 stockée dans un fichier",expected,actual);
      
   }
   
   
   /**
    * Test de décodage d'une chaîne base64 stockée dans un fichier<br>
    * Cas de test :<br>
    *  - la base 64 représente une chaîne de caractères UTF-16<br>
    *  - le fichier contenant la base 64 est écrit en UTF-8<br>
    * 
    * @throws IOException
    */
   @Test
   public void decodeFileToStringTest3() throws IOException {
      
      // ---------------------------------------------------------
      // Conditions initiales
      //  - Encodage d'une chaîne de caractères UTF_8 en base 64
      //  - Ecriture de cette chaîne dans un fichier en UTF_16
      // ---------------------------------------------------------
      
      // pour le caractère "é" :
      //  - UTF_8  => 2 octets
      //  - UTF_16 => 4 octets
      String chaineTest = "é";
      
      // Récupération de la base64
      //  - UTF_8  : w6k=
      //  - UTF_16 : /v8A6Q==
      // La base 64 est sur 8 octets
      String base64 = Base64Encode.encode(chaineTest, CharEncoding.UTF_16);
      assertEquals("Contrôle intermédiaire de la base64 générée","/v8A6Q==",base64);
      
      // Ecriture d'un fichier en UTF_8 contenant la base 64
      // Le fichier fera 8 octets
      //  Pour le savoir :
      // System.out.println("/v8A6Q==".getBytes(CharEncoding.UTF_8).length);
      String fichierBase64 = FilenameUtils.concat(DIRECTORY, "decodeFileToStringTest3.txt");
      FileUtils.writeStringToFile(new File(fichierBase64), base64, CharEncoding.UTF_8);
      
      
      // ---------------------------------------------------------
      // Test
      // ---------------------------------------------------------
      
      String actual = Base64Decode.decodeFileToString(fichierBase64,-1,null,CharEncoding.UTF_16);
      
      
      // ---------------------------------------------------------
      // Vérifications
      // ---------------------------------------------------------
      
      String expected = "é";
      assertEquals("Décodage d'une chaîne base64 stockée dans un fichier",expected,actual);
      
   }
   

}
