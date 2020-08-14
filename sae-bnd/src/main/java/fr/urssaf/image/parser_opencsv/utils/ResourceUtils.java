package fr.urssaf.image.parser_opencsv.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;

/**
 * Classe utilitaire
 */
public final class ResourceUtils {

   private final static String SOMMAIRE_SUFFIX_DIR_PATH = "/BND_SCRIPT/";

   private final static String DOCUMENTS_SUFFIX_DIR_PATH = "/documents";

   private ResourceUtils() {

   }

   public static void copyResourceToFile(final String resourcePath, final String outFilePath) {
      InputStream stream = null;
      try {
         final File file = new File(resourcePath);
         System.out.println(resourcePath);
         System.out.println(outFilePath);
         stream = new FileInputStream(file);
         java.nio.file.Files.copy(
                                  stream,
                                  new File(outFilePath).toPath(),
                                  StandardCopyOption.REPLACE_EXISTING);
      }
      catch (final IOException e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
         throw new RuntimeException(e);
      } finally {
         if (stream != null) {
            try {
               stream.close();
            }
            catch (final IOException e) {
               throw new RuntimeException("Erreur lors de la fermeture du flux. Details : " + e.getMessage());
            }
         }
      }
   }

   /**
    * Copy the file <code>inputFile</code> to the file <code>outputFile</code>.
    *
    * @param inputFile
    *           File to be copied
    * @param outputFile
    *           Copy
    * @param force
    *           Force the writting if outputFile already exists
    * @throws IOException
    */
   @SuppressWarnings("resource")
   public static void copyFile(final String resourcePath, final String outFilePath, final boolean force) throws IOException {
      final File inputFile = new File(resourcePath);
      final File outputFile = new File(outFilePath);

      if (!force && outputFile.exists()) {
         throw new IOException("File " + outputFile + " already exists.");
      } else if (outputFile.exists()) {
         outputFile.delete();
      }

      try (FileChannel in = new FileInputStream(inputFile).getChannel();
            FileChannel out = new FileOutputStream(outputFile).getChannel();) {

         in.transferTo(0, in.size(), out);
      }
      catch (final IOException e) {
         // copyFileIO(inputFile, outputFile, true);
      }
   }

   /**
    * Copy the file <code>inputFile</code> to the file <code>outputFile</code>.
    * <p>
    * This is the non-nio method.
    * </p>
    *
    * @param inputFile
    *           File to be copied
    * @param outputFile
    *           Copy
    * @param force
    *           Force the writting if outputFile already exists
    * @throws IOException
    */
   public static void copyFileIO(final String resourcePath, final String outFilePath, final boolean force) throws IOException {
      final File inputFile = new File(resourcePath);
      final File outputFile = new File(outFilePath);
      if (!force && outputFile.exists()) {
         throw new IOException("File " + outputFile + " already exists.");
      } else if (outputFile.exists()) {
         outputFile.delete();
      }
      FileInputStream sourceFile = null;
      FileOutputStream destinationFile = null;
      try {
         outputFile.createNewFile();
         sourceFile = new FileInputStream(inputFile);
         destinationFile = new FileOutputStream(outputFile);
         // Read by 0.5Mo parts
         final byte[] buffer = new byte[512 * 1024];
         int nbLecture;
         while ((nbLecture = sourceFile.read(buffer)) != -1) {
            destinationFile.write(buffer, 0, nbLecture);
         }
      }
      finally {
         if (sourceFile != null) {
            try {
               sourceFile.close();
            }
            catch (final Exception e) {
            }
         }
         if (destinationFile != null) {
            try {
               destinationFile.close();
            }
            catch (final Exception e) {
            }
         }
      }
   }

   /**
    * Permet de créer l'emplacement où sera stocker le fichier sommaire.xml
    * et les documents associés
    * 
    * @param ecdeConfig
    *           configuration de l'ECDE
    * @return
    * @throws IOException
    */
   public static synchronized String createEcdeDir(final EcdeSource ecdeConfig) throws IOException {
      final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
      final String dir = ecdeConfig.getBasePath().getPath() + SOMMAIRE_SUFFIX_DIR_PATH + LocalDateTime.now().format(formatter);
      new File(dir).mkdirs();
      int counter = 1;
      while (true) {
         String subDirAsString = dir + "/" + counter + "/";
         subDirAsString = FilenameUtils.separatorsToSystem(subDirAsString);
         final File subDir = new File(subDirAsString);
         if (!subDir.exists()) {
            subDir.mkdir();
            setFilePermissions(subDir);
            final Path path = Paths.get(subDirAsString + DOCUMENTS_SUFFIX_DIR_PATH);
            Files.createDirectories(path);
            return subDirAsString;
         }
         counter++;
      }
   }
   
   /**
    * Ajout des permissions pour les creation des dossiers sur le serveur linux
    * @param ecdeFile
    * @throws IOException
    */
   public static void setFilePermissions(final File ecdeFile) throws IOException {
     final Set<PosixFilePermission> perms = new HashSet<>();
    if (!System.getProperty("os.name").contains("Windows")) {
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        Files.setPosixFilePermissions(ecdeFile.toPath(), perms);
      }
   }
}
