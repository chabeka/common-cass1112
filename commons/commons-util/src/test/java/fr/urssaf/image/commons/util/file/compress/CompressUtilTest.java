package fr.urssaf.image.commons.util.file.compress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.commons.util.checksum.ChecksumFileUtil;
import fr.urssaf.image.commons.util.compress.CompressUtil;
import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * les fichiers de compression sont placés dans le repertoire archive du
 * répertoire temporaire si il n'existe pas il est créé
 * 
 * @author Bertrand BARAULT
 * 
 */
@SuppressWarnings("PMD")
public class CompressUtilTest {

   private final static String DIRECTORY;

   private final static String DIRECTORY_TEST = "src/test/resources/compress/archive";

   private final static String FILE_TEST = "src/test/resources/compress/archive/archive_1.txt";

   private final static String[] FILTRE = new String[] { "txt" };

   private static final Logger LOG = Logger.getLogger(CompressUtilTest.class);
   
   private static Boolean deleteDirectoryAfterTests;

   static {
      DIRECTORY = FilenameUtils.concat(
            SystemUtils.getJavaIoTmpDir().getAbsolutePath(),
            "archives");
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
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(CompressUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   @Test
   public void zipDirectory() throws IOException {

      String checksum = CompressUtil.zip(archiveFile("dir_zip.zip"),
            DIRECTORY_TEST, FILTRE);

      LOG.debug("zip de " + DIRECTORY_TEST + ":" + checksum);
      assertChecksum("échec du zip " + DIRECTORY_TEST, "dir_zip.zip", checksum);

   }

   @Test
   public void zipFile() throws IOException {

      String checksum = CompressUtil
            .zip(archiveFile("file_zip.zip"), FILE_TEST);

      LOG.debug("zip de " + FILE_TEST + ":" + checksum);
      assertChecksum("échec du zip " + FILE_TEST, "file_zip.zip", checksum);

   }

   @Test
   public void gzipFile() throws IOException {

      String compressFileName = FilenameUtils.getName(GzipUtils
            .getCompressedFilename(FILE_TEST));

      String checksum = CompressUtil.gzip(archiveFile(compressFileName),
            FILE_TEST);

      LOG.debug("gzip de " + FILE_TEST + ":" + checksum);
      assertChecksum("échec du gzip " + FILE_TEST, FilenameUtils
            .getName(FILE_TEST)
            + ".gz", checksum);

   }

   @Test
   public void gzipDirectory(){

      try {
         String gzFile = FilenameUtils.getName(GzipUtils
               .getCompressedFilename(DIRECTORY_TEST));

         CompressUtil.gzip(archiveFile(gzFile), DIRECTORY_TEST);
         fail("le test doit leve une exception de type "+IOException.class);
      } catch (IOException e) {
         assertEquals("l'exception attendu n'est pas correcte",
               "Gzip n'est pas fait pour compresser un répertoire", e
                     .getMessage());
      }

   }

   @Test
   public void tgz() throws IOException {

      String compressFileName = FilenameUtils.getName(DIRECTORY_TEST) + ".tgz";

      String checksum = CompressUtil.tgz(archiveFile(compressFileName),
            DIRECTORY_TEST, FILTRE);

      LOG.debug("gzip de " + DIRECTORY_TEST + ":" + checksum);
      assertChecksum("échec du gzip " + DIRECTORY_TEST, FilenameUtils
            .getBaseName(DIRECTORY_TEST)
            + ".tgz", checksum);

   }
   
   
   @Test
   public void tarFile() throws IOException {

      String checksum = CompressUtil
            .tar(archiveFile("file_tar.tar"), FILE_TEST);

      LOG.debug("tar de " + FILE_TEST + ":" + checksum);
      assertChecksum("échec du tar " + FILE_TEST, "file_tar.tar", checksum);

   }

   @Test
   public void tarDirectory() throws IOException {

      String checksum = CompressUtil.tar(archiveFile("dir_tar.tar"),
            DIRECTORY_TEST, FILTRE);

      LOG.debug("tar " + DIRECTORY_TEST + ":" + checksum);
      assertChecksum("échec du tar " + DIRECTORY_TEST, "dir_tar.tar", checksum);

   }

   private void assertChecksum(String libelle, String file, String checksum)
         throws IOException {

      assertEquals(libelle, ChecksumFileUtil.crc32(archiveFile(file)), checksum);
   }

   private String archiveFile(String filename) {
      return FilenameUtils.concat(DIRECTORY, filename);
   }

}
