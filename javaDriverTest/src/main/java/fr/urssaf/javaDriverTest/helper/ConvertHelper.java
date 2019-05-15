package fr.urssaf.javaDriverTest.helper;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Utilitaires de conversion
 *
 */
public class ConvertHelper {
  /**
   * Convert the byte array to an int starting from the given offset.
   *
   * @param b
   *           The byte array
   * @param offset
   *           The array offset
   * @return The integer
   */
  public static int byteArrayToInt(final byte[] b, final int offset) {
    int value = 0;
    for (int i = 0; i < 4; i++) {
      final int shift = (4 - 1 - i) * 8;
      if (b.length > (i + offset)) {
        value += (b[i + offset] & 0x000000FF) << shift;
      }
    }
    return value;
  }

  public static long byteArrayToLong(final byte[] b) {
    long value = 0;
    for (final byte element : b) {
      value = (value << 8) + (element & 0xff);
    }
    return value;
  }

  public static UUID byteArrayToUUID(final byte[] b) {
    final ByteBuffer buffer = ByteBuffer.wrap(b);
    final long mostSigBit = buffer.getLong(0);
    final long leastSigBit = buffer.getLong(8);
    return new UUID(mostSigBit, leastSigBit);
  }

  /**
   * Convertit une chaîne hexadécimale (ex : "1a2e65") en tableau de bytes
   *
   * @param s
   *           La chaîne hexadécimale
   * @return Les bytes
   */
  public static byte[] hexStringToByteArray(final String s) {
    final int len = s.length();
    final byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
          .digit(s.charAt(i + 1), 16));
    }
    return data;
  }

  /**
   * Construit une chaine de caractères plus ou moins lisible par un humain
   * moyen, à partir d'un tableau de bytes. Les bytes correspondant aux
   * caractères UTF8 sont convertis en caractères, et les autres sont affichés
   * sous la forme \xHH (HH étant le caractère hexadécimal du byte)
   *
   * @param bytes
   *           : les bytes à afficher
   * @return
   * @throws Exception
   */
  public static String getReadableUTF8String(final byte[] bytes) throws Exception {
    String result = "";
    // Cf http://fr.wikipedia.org/wiki/UTF-8

    int i = 0;
    while (i < bytes.length) {
      final byte b = bytes[i];
      // 128 = 10000000
      if ((b & 128) == 0) {
        // On est dans le cas d'un caractère à 7 bits : 0xxxxxxx
        if (b < 32) {
          // Caractère non imprimable
          result += "\\x" + getHexString(b);
        } else {
          result += (char) b;
        }
        i++;
        continue;
      } else {
        // Est-on dans cette forme là ? 110xxxxx 10xxxxxx
        // 192 = 11000000
        // 32 = 00100000
        if (((b & 192) == 192) && ((b & 32) == 0)) {
          if (i < bytes.length - 1) {
            final byte b2 = bytes[i + 1];
            // 64 = 01000000
            if (((b2 & 128) == 128) && ((b2 & 64) == 0)) {
              final byte[] myBytes = new byte[2];
              myBytes[0] = b;
              myBytes[1] = b2;
              result += new String(myBytes, "UTF-8");
              i += 2;
              continue;
            }
          }
        }
      }
      // Ce n'est pas un caractère UTF8
      result += "\\x" + getHexString(b);
      i++;
    }
    return result;
  }

  /**
   * Essaye d'effectuer l'opération inverse de getReadableUTF8String, c'est à
   * dire convertit une "chaîne lisible" en tableau de bytes
   *
   * @param s
   *           : chaîne lisible. Exemple :
   *           \x00\x00\x00\x00\x03nre\x00\x00\x03289\x00
   * @return : tableau de bytes
   * @throws Exception
   */
  public static byte[] getBytesFromReadableUTF8String(final String s)
      throws Exception {

    final ArrayList<Byte> list = new ArrayList<Byte>(s.length());
    for (int i = 0; i < s.length(); i++) {
      final char c = s.charAt(i);
      if (s.charAt(i) == '\\' && s.charAt(i + 1) == 'x') {
        final char c1 = Character.toLowerCase(s.charAt(i + 2));
        final char c2 = Character.toLowerCase(s.charAt(i + 3));
        if ((c1 >= '0' && c1 <= '9') || (c1 >= 'a' && c1 <= 'f')) {
          if ((c2 >= '0' && c2 <= '9') || (c2 >= 'a' && c2 <= 'f')) {
            final byte b = (byte) ((Character.digit(c1, 16) << 4) + Character
                .digit(c2, 16));
            list.add(b);
            i += 3;
            continue;
          }
        }
      }
      final String myChar = c + "";
      final byte[] bytes = myChar.getBytes("UTF-8");
      for (final byte b : bytes) {
        list.add(b);
      }
    }
    // Transforme la liste en tableau
    final byte[] result = new byte[list.size()];
    int i = 0;
    for (final Byte b : list) {
      result[i++] = b;
    }
    return result;
  }

  public static String getHexString(final byte b) throws Exception {
    final byte[] bytes = new byte[1];
    bytes[0] = b;
    return getHexString(bytes);
  }

  /**
   * Renvoie la représentation hexadécimale d'un tableau de bytes
   *
   * @param bytes
   *           tableau de bytes
   * @return
   * @throws Exception
   */
  public static String getHexString(final byte[] bytes) throws Exception {
    String result = "";
    for (final byte b : bytes) {
      result += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
    }
    return result;
  }

  /**
   * Encode une chaîne de caractères en hexadécimal
   *
   * @param str
   *           chaîne à encoder
   * @return chaîne encodée
   */
  static String stringToHex(final String str) {
    final char[] chars = str.toCharArray();
    final StringBuffer strBuffer = new StringBuffer();
    for (final char c : chars) {
      strBuffer.append(Integer.toHexString(c));
    }
    return strBuffer.toString();
  }

  public static byte[] stringToBytes(final String str)
      throws UnsupportedEncodingException {
    return str.getBytes("ISO-8859-1");
  }

  /**
   *
   * @param str
   *           Exemple : "DOCUBASE|||sm_d"
   *
   * @return
   */
  public static byte[] stringToBytesWithDocubaseDelimiter(final String str)
      throws UnsupportedEncodingException {
    final byte[] bytes = str.getBytes("ISO-8859-1");
    int delimiterIndex = str.indexOf("|||", 0);
    while (delimiterIndex >= 0) {
      bytes[delimiterIndex] = (byte) 0xef;
      bytes[delimiterIndex + 1] = (byte) 0xbf;
      bytes[delimiterIndex + 2] = (byte) 0xbf;
      delimiterIndex = str.indexOf("|||", delimiterIndex + 3);
    }
    return bytes;
  }

}
