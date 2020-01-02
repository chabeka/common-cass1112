/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Classe utile pour le fichier de reprise en cas de coupure lors de la migration
 */
public class RepriseFileUtils {

  // METHODE POUR LE REPRISE

  public static File getKeysFile(final String dirPath, final String fileName) throws IOException {
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
      fileHandler = new RandomAccessFile(file, "rw");
      final long fileLength = fileHandler.length() - 1;
      StringBuilder sb = new StringBuilder();

      for (long filePointer = fileLength; filePointer != -1; filePointer--) {
        fileHandler.seek(filePointer);
        final int readByte = fileHandler.readByte();

        if (readByte == 0xA) {
          if (filePointer == fileLength) {
            continue;
          }
          // on verifie que la ligne correspond à un UUID valide
          final boolean isValidUUID = valideUUID(sb.reverse().toString());
          if (isValidUUID) {
            break;
          } else {
            fileHandler.setLength(fileLength + 1);
            sb = new StringBuilder();
            continue;
          }

        } else if (readByte == 0xD) {
          if (filePointer == fileLength - 1) {
            continue;
          }
          // on verifie que la ligne correspond à un UUID valide
          final boolean isValidUUID = valideUUID(sb.reverse().toString());
          if (isValidUUID) {
            break;
          } else {
            fileHandler.setLength(fileLength + 1);
            sb = new StringBuilder();
            continue;
          }
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

  /*
   * verifie que la chaine de String correspond bien à un UUID valide
   */
  public static boolean valideUUID(final String strKey) {
    try {
      UUID.fromString(strKey);
    }
    catch (final IllegalArgumentException e) {
      return false;
    }
    return true;
  }

}
