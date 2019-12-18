/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * TODO (AC75095028) Description du type
 *
 */
public class RepriseFileUtils {

  // METHODE POUR LE REPRISE

  public static File getKeysFile(final String dirName, final String fileName) throws IOException {
    final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    final File dir = new File(classLoader.getResource(dirName).getFile());
    final Path dirPath = dir.toPath();
    final File file = new File(dirPath.toString() + "/" + fileName);

    if (!file.exists()) {
      file.createNewFile();
    }
    return file;
  }
  /**
   * Recuperer la dernière ligne du fichier
   * https://stackoverflow.com/questions/686231/quickly-read-the-last-line-of-a-text-file
   * 
   * @param file
   * @return
   */
  public static String getLastLine(final File file) {

    RandomAccessFile fileHandler = null;
    try {
      fileHandler = new RandomAccessFile(file, "r");
      final long fileLength = fileHandler.length() - 1;
      final StringBuilder sb = new StringBuilder();

      for (long filePointer = fileLength; filePointer != -1; filePointer--) {
        fileHandler.seek(filePointer);
        final int readByte = fileHandler.readByte();

        if (readByte == 0xA) {
          if (filePointer == fileLength) {
            continue;
          }
          break;

        } else if (readByte == 0xD) {
          if (filePointer == fileLength - 1) {
            continue;
          }
          break;
        }

        sb.append((char) readByte);
      }

      final String lastLine = sb.reverse().toString();
      return lastLine;
    }
    catch (final IOException e) {
      e.printStackTrace();
      return null;
    }
    finally {
      if (fileHandler != null) {
        try {
          fileHandler.close();
        }
        catch (final IOException e) {
          /* ignore */
        }
      }
    }

  }
}
