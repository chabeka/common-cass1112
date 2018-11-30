package fr.urssaf.image.sae.integration.ihmweb.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;


/**
 * Fonctions de calcul de checksum
 */
public final class ChecksumUtils {

   
   private ChecksumUtils() {
      
   }
   
   
   /**
    * Calcul le SHA-1 du flux passé en paramètre
    * 
    * @param stream le flux dont il faut calculer le SHA-1
    * @return le SHA-1 du flux, sous la forme d'une chaîne de caractères hexa
    */
   public static String sha1(InputStream stream) {
      try {
         return DigestUtils.shaHex(stream);
      } catch (IOException e) {
         throw new IntegrationRuntimeException(e);
      }
   }
   
   
   /**
    * Calcul le SHA-1 du fichier dont le chemin est passé en paramètre
    * 
    * @param cheminFichier le chemin du fichier dont il faut calculer le SHA-1
    * @return le SHA-1 du fichier, sous la forme d'une chaîne de caractères hexa
    */
   public static String sha1(String cheminFichier) {
      FileInputStream fileInputStream;
      try {
         fileInputStream = new FileInputStream(cheminFichier);
         return sha1(fileInputStream);
      } catch (FileNotFoundException e) {
         throw new IntegrationRuntimeException(e);
      }
      
   }
   
   
}
