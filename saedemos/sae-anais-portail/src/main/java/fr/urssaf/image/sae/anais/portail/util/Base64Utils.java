package fr.urssaf.image.sae.anais.portail.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;


/**
 * Méthodes utilitaires pour les conversions en Base64
 */
public class Base64Utils {

   
   /**
    * Encode une chaîne de caractères en base64
    * @param text la chaîne de caractères à encoder
    * @return la chaîne de caractères encodées en base 64
    */
   public static String encode(String text) {
      
      byte[] iso = StringUtils.getBytesIso8859_1(text);
      
      return StringUtils.newStringUtf8(Base64.encodeBase64(iso, false));
      
   }
   
   
   /**
    * Décode une chaîne de caractères encodée en base64
    * @param base64 la chaîne de caractères encodée en base 64
    * @return la chaîne de caractères décodée
    */
   public static String decode(String base64) {
      
      return StringUtils.newStringIso8859_1(Base64.decodeBase64(base64));
      
   }
   
}
