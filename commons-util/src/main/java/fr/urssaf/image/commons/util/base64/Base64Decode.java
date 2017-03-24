package fr.urssaf.image.commons.util.base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * Décodage de base64
 *
 */
public final class Base64Decode {

   private Base64Decode() {

   }

   /**
    * Décode une chaîne base64 en une chaîne ISO8859_1
    * 
    * @param base64
    *           texte en base 64
    * @return la chaîne décodée
    */
   public static String decode(String base64) {
      return StringUtils.newStringIso8859_1(Base64.decodeBase64(base64));
   }
   

   /**
    * Décode une chaîne base64 en une chaîne UTF-8
    * 
    * @param text
    *           texte en base 64
    * @return la chaîne décodée
    */
   public static String decodeUTF8(String text) {
      return StringUtils.newStringUtf8(Base64.decodeBase64(text));
   }

   
   /**
    * Décode une chaîne base64
    * 
    * @param text
    *           texte en base 64
    * @param charsetName
    *           charset à utiliser pour restituer la chaîne décodée
    * @return la chaîne décodée
    */
   public static String decode(String text, String charsetName) {

      return StringUtils.newString(Base64.decodeBase64(text), charsetName);
   }
   

   /**
    * Décode un fichier texte dont le contenu est en base 64, et écrit le résultat
    * dans un nouveau fichier.
    * 
    * @param fichierBase64
    *           chemin du fichier encodé
    * @param fichierDeSortie
    *           chemin de sortie du fichier décodé
    * @throws IOException
    *            exception sur les fichiers
    */
   public static void decodeFile(
         String fichierBase64, 
         String fichierDeSortie)
         throws IOException {

      FileInputStream fis = new FileInputStream(fichierBase64);
      try {

         Base64InputStream input = new Base64InputStream(fis, false);
         try {

            FileOutputStream output = new FileOutputStream(fichierDeSortie);
            try {
               IOUtils.copy(input, output);
            } finally {
               if (output!=null) {
                  output.close();
               }
            }
         }
         finally {
            if (input!=null) {
               input.close();
            }
         }
      }
      finally {
         if (fis!=null) {
            fis.close();
         }
      }
      
   }

   /**
    * Décode un fichier texte dont le contenu est en base 64, et renvoie le
    * résultat sous la forme d'une chaîne de caractères
    * 
    * @param fichierBase64
    *           chemin du fichier texte contenant la base64
    * @param longueurLigne
    *           longueur d'une ligne dans <i>fichierBase64</i>.  
    *           Si <i>longueurLigne</i><0, on considère que le fichier n'est pas découpé en lignes.
    * @param separateurLigne
    *            le séparateur de lignes si <i>fichierBase64</i> est découpé en lignes.
    *            Ce paramètre est ignoré si <i>longueurLigne</i><0
    * @param encoding
    *           le charset à utiliser pour construire la chaîne de caractères de sortie
    * @return chaine décodée
    * @throws IOException
    *            exception sur les fichiers
    */
   public static String decodeFileToString(
         String fichierBase64,
         int longueurLigne,
         byte[] separateurLigne,
         String encoding) throws IOException {
      
      FileInputStream input = new FileInputStream(fichierBase64);
      try {
      
         Base64InputStream inputBase64 = new Base64InputStream(input,false,longueurLigne,separateurLigne);
         try {
         
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream() ;
            
            IOUtils.copy(inputBase64,byteBuffer);
            
            return new String(byteBuffer.toByteArray(), encoding);
         
         }
         finally {
            if (inputBase64!=null) {
               inputBase64.close();
            }
         }
      
      }
      finally {
         if (input!=null) {
            input.close();
         }
      }
      
   }
   
}
