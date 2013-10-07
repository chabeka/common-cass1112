package fr.urssaf.image.sae.storage.dfce.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.util.Assert;

/**
 * Classe utilitaire pour le calcul du hash
 * 
 * 
 */
public final class HashUtils {

   private HashUtils() {

   }

   /**
    * Calcul le hash d'un tableau de byte.<br>
    * <br>
    * Les algorithmes du hash prises en compte
    * <ul>
    * <li>MD5</li>
    * <li>SHA-1</li>
    * <li>SHA-256</li>
    * <li>SHA-384</li>
    * <li>SHA-512</li>
    * </ul>
    * 
    * @param data
    *           un tableau de byte
    * @param digestAlgo
    *           algorithme de hachage, doit être renseigné
    *           
    * @return string          
    * @throws NoSuchAlgorithmException
    *            l'algorithme de hachage n'est pas prise en compte
    */
   public static String hashHex(byte[] data, String digestAlgo)
         throws NoSuchAlgorithmException {

      Assert.notNull(data, "'data' is required");
      Assert.hasText(digestAlgo, "'digestAlgo' is required");

      MessageDigest messageDigest = MessageDigest.getInstance(digestAlgo);

      String hash = Hex.encodeHexString(messageDigest.digest(data));

      return hash;

   }
}
