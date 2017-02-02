package fr.urssaf.image.sae.integration.ihmweb.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Méthodes utilitaires pour l'encodage/décodage en base 64
 */
public final class Base64Utils {
   
   private Base64Utils() {
      
   }

   
   /**
    * Encode une chaîne de caractères
    * 
    * @param input la chaîne de caractères à "base64iser"
    * @return la représentation de la chaîne de caractères en base64
    */
   public static String encode(String input) {
      byte[] bytes = org.apache.commons.codec.binary.StringUtils.getBytesUtf8(input);
      byte[] base64 = Base64.encodeBase64(bytes, false);
      return org.apache.commons.codec.binary.StringUtils.newStringUtf8(base64);
   }
   
   
   /**
    * Décode une chaîne de caractères base64
    * 
    * @param input la chaîne à décoder
    * @return la chaîne décodée
    */
   public static String decode(String input) {
      byte[] bytes = Base64.decodeBase64(input);
      return org.apache.commons.codec.binary.StringUtils.newStringUtf8(bytes);
   }
   
}
