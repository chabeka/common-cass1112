package fr.urssaf.hectotest;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;

/**
 * Utilitaires de conversion
 *
 */
public class ConvertHelper 
{
    /**
     * Convert the byte array to an int starting from the given offset.
     *
     * @param b The byte array
     * @param offset The array offset
     * @return The integer
     */
    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            if (b.length > (i + offset)) value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
    
    public static long byteArrayToLong(byte[] b) {
    	long value = 0;
    	for (int i = 0; i < b.length; i++)
    	{
    	   value = (value << 8) + (b[i] & 0xff);
    	}
      return value;
    }

    public static UUID byteArrayToUUID(byte[] b) {
       ByteBuffer buffer = ByteBuffer.wrap(b);
       long mostSigBit = buffer.getLong(0);
       long leastSigBit = buffer.getLong(8);
       return new UUID(mostSigBit,leastSigBit);
    }

    /**
     * Convertit une chaîne hexadécimale (ex : "1a2e65") en tableau de bytes
     *
     * @param s La chaîne hexadécimale
     * @return Les bytes
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    
    /**
     * Construit une chaine de caractères plus ou moins lisible par un humain moyen, à partir d'un
     * tableau de bytes.
     * Les bytes correspondant aux caractères UTF8 sont convertis en caractères, et les autres
     * sont affichés sous la forme \xHH (HH étant le caractère hexadécimal du byte)
     * @param bytes : les bytes à afficher
     * @return
     * @throws Exception
     */
	public static String getReadableUTF8String(byte[] bytes) throws Exception {
		String result = "";
		// Cf http://fr.wikipedia.org/wiki/UTF-8
		
		int i = 0;
		while (i < bytes.length) {
			byte b = bytes[i];
			// 128 = 10000000
			if ((b & 128) == 0) {				
				// On est dans le cas d'un caractère à 7 bits : 0xxxxxxx
				if (b < 32) {
					// Caractère non imprimable
					result += "\\x" + getHexString(b);
				}
				else {
					result += (char) b;
				}
				i++;
				continue;
			}
			else {
				// Est-on dans cette forme là ? 110xxxxx 10xxxxxx
				// 192 = 11000000
				// 32 = 00100000
				if (((b & 192) == 192) && ((b & 32) == 0)) {
					if (i < bytes.length - 1) {
						byte b2 = bytes[i+1];
						// 64 = 01000000
						if (((b2 & 128) == 128) && ((b2 & 64) == 0)) {
							byte[] myBytes = new byte[2];
							myBytes[0] = b;
							myBytes[1] = b2;
							result += new String(myBytes, "UTF-8");
							i+=2;
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
	 * Essaye d'effectuer l'opération inverse de getReadableUTF8String, c'est à dire
	 * convertit une "chaîne lisible" en tableau de bytes
	 * @param s : chaîne lisible. Exemple : \x00\x00\x00\x00\x03nre\x00\x00\x03289\x00
	 * @return : tableau de bytes
	 * @throws Exception
	 */
	public static byte[] getBytesFromReadableUTF8String(String s) throws Exception {
	
		ArrayList<Byte> list = new ArrayList<Byte>(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (s.charAt(i) == '\\' && s.charAt(i+1) == 'x') {
				char c1 = Character.toLowerCase(s.charAt(i + 2)); 
				char c2 = Character.toLowerCase(s.charAt(i + 3));
				if ((c1 >= '0' && c1 <= '9')||(c1 >= 'a' && c1 <= 'f')) {
					if ((c2 >= '0' && c2 <= '9')||(c2 >= 'a' && c2 <= 'f')) {
						byte b = (byte) ((Character.digit(c1, 16) << 4)
                                + Character.digit(c2, 16));
						list.add(b);
						i += 3;
						continue;
					}
				}
			}
			String myChar = c + "";
			byte[] bytes = myChar.getBytes("UTF-8");
			for (byte b : bytes) {
				list.add(b);
			}
		}
		// Transforme la liste en tableau
		byte[] result = new byte[list.size()];
		int i = 0;
		for (Byte b : list) {
			result[i++] = b;
		}
		return result;
	}
	
	
	public static String getHexString(byte b) throws Exception {
		byte[] bytes = new byte[1];
		bytes[0] = b;
		return getHexString(bytes);
	}

	/**
	 * Renvoie la représentation hexadécimale d'un tableau de bytes
	 * @param bytes tableau de bytes
	 * @return
	 * @throws Exception
	 */
	public static String getHexString(byte[] bytes) throws Exception {
		  String result = "";
		  for (int i=0; i < bytes.length; i++) {
		    result +=
		          Integer.toString( ( bytes[i] & 0xff ) + 0x100, 16).substring( 1 );
		  }
		  return result;
	}
	
	/**
	 * Encode une chaîne de caractères en hexadécimal
	 * @param str chaîne à encoder
	 * @return chaîne encodée
	 */
	static String stringToHex(String str) {
		char[] chars = str.toCharArray();
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			strBuffer.append(Integer.toHexString((int) chars[i]));
		}
		return strBuffer.toString();
	}
	
	public static byte[] stringToBytes (String str) throws UnsupportedEncodingException {
		return str.getBytes("ISO-8859-1");
	}
	
	/**
	 *   
	 * @param str Exemple : "DOCUBASE|||sm_d"

	 * @return
	 */
	public static byte[] stringToBytesWithDocubaseDelimiter(String str) throws UnsupportedEncodingException {
		byte[] bytes = str.getBytes("ISO-8859-1"); 
		int delimiterIndex = str.indexOf("|||", 0);
		while (delimiterIndex >=0) {
			bytes[delimiterIndex] = (byte) 0xef;
			bytes[delimiterIndex+1] = (byte) 0xbf;
			bytes[delimiterIndex+2] = (byte) 0xbf;
			delimiterIndex = str.indexOf("|||", delimiterIndex + 3);
		}
		return bytes;
	}
	
}
