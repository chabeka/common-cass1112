package fr.urssaf.image.commons.util.file.compress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.commons.util.checksum.ChecksumFileUtil;
import fr.urssaf.image.commons.util.compress.UnCompressUtil;
import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * Tests unitaires de la classe {@link UnCompressUtil}<br>
 * <br>
 * Les fichiers de tests sont placés dans un sous-répertoire "archive" du
 * répertoire temporaire de l'OS.
 * 
 */
@SuppressWarnings("PMD")
public class UnCompressUtilTest {

   private final static String DIRECTORY;

   private final static String COMPRESSION = "src\\test\\resources\\compress\\compression";

   private final static String ARCHIVE = "src\\test\\resources\\compress\\archive";
   
   private static Boolean deleteDirectoryAfterTests;

   static {
      DIRECTORY = FilenameUtils.concat(SystemUtils.getJavaIoTmpDir()
            .getAbsolutePath(), "archives_decompression");
   }

   @BeforeClass
   public static void initClass() throws IOException {

      // Création d'un répertoire temporaire
      File directory = new File(DIRECTORY);
      deleteDirectoryAfterTests = ! directory.exists();
      FileUtils.forceMkdir(directory);
      FileUtils.cleanDirectory(directory);

   }

   @Before
   public void initTest() throws IOException {
      File repertory = new File(DIRECTORY);
      FileUtils.cleanDirectory(repertory);
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
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(UnCompressUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   @Test
   public void unzipDirectory() throws IOException {

      UnCompressUtil.unzip(archiveFile("dir_zip.zip"), DIRECTORY);
      assertExistsArchive();

      assertChecksum("échec sur misérables.txt", "victor_hugo//misérables.txt");
   }

   @Test
   public void untgzDirectory() throws IOException {

      UnCompressUtil.untgz(archiveFile("archive.tgz"), DIRECTORY);
      assertExistsArchive();
      assertUnicode();
   }

   @Test
   public void untarDirectory() throws IOException {

      UnCompressUtil.untar(archiveFile("dir_tar.tar"), DIRECTORY);
      assertExistsArchive();
      assertUnicode();
   }

   // TODO mettre à jour le test pour la version 1.1 de commons-compress
   // pour l'instant les noms des fichiers ne prennent pas en compte les caractères accentués
   // cela sera revu dans la version 1.1 de commons-compress (Cf. https://issues.apache.org/jira/browse/COMPRESS-114)
   private void assertUnicode() throws IOException {

      String checksum = ChecksumFileUtil.crc32(
            FilenameUtils.concat(
                  DIRECTORY,
                  "archive//victor_hugo//mis￩rables.txt"));

      assertEquals("échec sur misérables.txt",
            ChecksumFileUtil.crc32(FilenameUtils.concat(ARCHIVE,
                  "victor_hugo//misérables.txt")), checksum);
   }

   private void assertExistsArchive() throws IOException {
      assertChecksum("échec sur archive_1.txt", "archive_1.txt");
      assertChecksum("échec sur archive_2.txt", "archive_2.txt");
      assertChecksum("échec sur memoires_outre_tombe.txt",
            "chateaubriand//memoires_outre_tombe.txt");
      assertChecksum("échec sur contemplations.txt",
            "victor_hugo//contemplations.txt");

   }

   private void assertChecksum(String libelle, String file) throws IOException {

      String checksum = ChecksumFileUtil.crc32(
            FilenameUtils.concat(
                  DIRECTORY,
                  "archive//" + file));

      assertEquals(
            libelle, 
            ChecksumFileUtil.crc32(FilenameUtils.concat(ARCHIVE, file)), 
            checksum);
   }

   private String archiveFile(String filename) {
      return FilenameUtils.concat(COMPRESSION, filename);
   }

}
