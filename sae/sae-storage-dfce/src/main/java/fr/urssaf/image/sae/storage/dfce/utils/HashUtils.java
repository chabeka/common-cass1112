package fr.urssaf.image.sae.storage.dfce.utils;

import java.io.IOException;
import java.io.InputStream;
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

   /**
    * taille du buffer de lecture
    */
   private static final int BUFFER_SIZE = 1024;

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

   /**
    * * Calcul le hash d'un stream.<br>
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
    * @param stream
    *           le stream dont il faut calculer le hash
    * @param digestAlgo
    *           algo de hashage
    * @return le hashalgorithme de hachage, doit être renseigné
    * 
    * @throws NoSuchAlgorithmException
    *            l'algorithme de hachage n'est pas prise en compte
    * @throws IOException
    *            erreur lors de la lecture du flux
    */
   public static String hashHex(InputStream stream, String digestAlgo)
         throws NoSuchAlgorithmException, IOException {

      // BufferedInputStream bufferedInputStream = null;

      MessageDigest digest;
      // bufferedInputStream = new BufferedInputStream(stream);
      digest = MessageDigest.getInstance(digestAlgo);
      byte[] buffer = new byte[1024];
      int read = stream.read(buffer, 0, BUFFER_SIZE);

      while (read > -1) {
         digest.update(buffer, 0, read);
         read = stream.read(buffer, 0, BUFFER_SIZE);
      }

      return Hex.encodeHexString(digest.digest());

      // bufferedInputStream.close();
   }
}
