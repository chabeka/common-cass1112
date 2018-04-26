package fr.urssaf.image.commons.util.checksum;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Classe de calcul du checksum sur des chaines de caractères.<br>
 * Les différents algorithmes supportés sont :
 * <ul>
 *    <li>MD5</li>
 *    <li>SHA_1</li>
 *    <li>SHA_256</li>
 *    <li>SHA_384</li>
 *    <li>SHA_512</li>
 * </ul>
 * Cette surcouche s'appuie sur <code>org.apache.commons.codec</code> 
 *       
 * @author Bertrand BARAULT
 *
 */
public final class ChecksumStringUtil {

   private ChecksumStringUtil() {

   }

   /**
    * Renvoie le MD5 en hexadécimal<br>
    * Utilise l'encodage UTF-8 pour lire la chaîne de caractères.
    * 
    * @param data
    *           chaine de caractère
    * @return chaine de caractère en hexa
    */
   public static String md5(String data) {
      return DigestUtils.md5Hex(data);
   }

   /**
    * Renvoie le SHA-1 en hexadécimal<br>
    * Utilise l'encodage UTF-8 pour lire la chaîne de caractères.
    * 
    * @param data
    *           chaine de caractères
    * @return chaine de caractères en hexa
    */
   public static String sha(String data) {
      return DigestUtils.shaHex(data);
   }
   

   /**
    * Renvoie le SHA-256 en hexadécimal<br>
    * Utilise l'encodage UTF-8 pour lire la chaîne de caractères.
    * 
    * @param data
    *           chaine de caractère
    * @return chaine de caractère en hexa
    */
   public static String sha256(String data) {
      return DigestUtils.sha256Hex(data);
   }

   /**
    * Renvoie le SHA-384 en hexadécimal<br>
    * Utilise l'encodage UTF-8 pour lire la chaîne de caractères.
    * 
    * @param data
    *           chaine de caractère
    * @return chaine de caractère en hexa
    */
   public static String sha384(String data) {
      return DigestUtils.sha384Hex(data);
   }

   /**
    * Renvoie le SHA-512 en hexadécimal<br>
    * Utilise l'encodage UTF-8 pour lire la chaîne de caractères.
    * 
    * @param data
    *           chaine de caractère
    * @return chaine de caractère en hexa
    */
   public static String sha512(String data) {
      return DigestUtils.sha512Hex(data);
   }

}
