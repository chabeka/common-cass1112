package fr.urssaf.image.sae.verifsplitdfce;

public final class ConvertUtils {

   /**
    * Constructeur privee.
    */
   private ConvertUtils() {
      super();
   }
   
   public static Long convertByteToLong(byte[] bytes) {
      long value = 0;
      for (int i = 0; i < bytes.length; i++) {
         value = (value << 8) + (bytes[i] & 0xff);
      }
      return Long.valueOf(value);
   }
   
   public static byte[] toByteArray(long valeur) {
      int nbOctet;
      if (valeur <= Byte.MAX_VALUE) {
         nbOctet = 1;
      } else if (valeur > Byte.MAX_VALUE && valeur <= Short.MAX_VALUE) {
         nbOctet = 2;
      } else if (valeur > Short.MAX_VALUE && valeur <= Integer.MAX_VALUE) {
         nbOctet = 4;
      } else if (valeur > Integer.MAX_VALUE && valeur <= Long.MAX_VALUE) {
         nbOctet = 8;
      } else {
         throw new IllegalArgumentException("valeur trop grande");
      }
      byte[] result = new byte[nbOctet];
      for (int i = nbOctet - 1; i >= 0; i--) {
        result[i] = (byte) (valeur & 0xffL);
        valeur >>= 8;
      }
      return result;
   }
}
