package fr.urssaf.image.commons.util.base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;


/**
 * Encodage en base 64<br><br>
 * 
 * Rappels sur la base 64 :<br>
 * <ul>
 * <li>base64 est un codage de l'information utilisant 64 caractères, choisis pour être disponibles
 * sur la majorité des systèmes</li>
 * <li>un 65ème caractère (signe '=') est utilisé comme complément final</li> 
 * <li>on transforme 6 bits en 1 caractère simple de l'alphabet base 64 (donc 1 octet)</li>
 * <li>de ce fait, il faut au minimum écrire 4 octets en base 64 pour coder un message. 
 * (1er multiple commun entre 6 bits et une représentation en octets (24 bits)</li>
 * </ul>
 *
 */
public final class Base64Encode {

   private static final Logger LOGGER = Logger.getLogger(Base64Encode.class);
   
   
   /**
    * Longueur par défaut d'une ligne de Base64 écrite dans un fichier.<br>
    * <br>
    * Si la longueur de la chaîne base64 est supérieure à cette valeur, la
    * chaîne sera découpée en plusieurs lignes.<br>
    * <br>
    * La valeur de 76 caractères est issue du standard MIME qui fixe aux lignes
    * une longueur de maximale de 76 caractères (cf. RFC 2045) 
    *  
    */
   private static final int LGR_LGN_DANS_FIC = 76;
   
   
   /**
    * Taille d'un buffer pour la lecture d'un fichier (en octets)
    */
   private static final int BUFFER_READ_SIZE = 1024;
   
   private Base64Encode() {

   }

   /**
    * Encode une chaîne de caractères en base 64.<br>
    * Utilise le charset ISO 8859-1 pour traiter la chaîne.
    * 
    * @param text
    *           texte à encoder
    * @return chaine de caractères en base 64
    */
   public static String encode(String text) {

      byte[] iso = StringUtils.getBytesIso8859_1(text);
      return encode(iso);
   }

   /**
    * Encode une chaîne de caractères en base 64.<br>
    * Utilise le charset UTF-8 pour traiter la chaîne
    * 
    * @param text
    *           texte à encoder
    * @return chaine de caractères en base 64
    */
   public static String encodeUTF8(String text) {

      byte[] utf8 = StringUtils.getBytesUtf8(text);
      return encode(utf8);
   }

   /**
    * Encode une chaîne de caractères en base 64.
    * 
    * @param text
    *           texte à encoder
    * @param charsetName
    *           Charset à utiliser pour traiter la chaîne de caractères
    * @return chaine de caractères en base 64
    *
    */
   public static String encode(String text, String charsetName) {

      byte[] encodage = StringUtils.getBytesUnchecked(text, charsetName);
      LOGGER.debug("Nombre d'octets représentant la String : " + encodage.length);
      return encode(encodage);
   }

   
   private static String encode(byte[] text) {
      return StringUtils.newStringUtf8(Base64.encodeBase64(text, false));
   }
   

   /**
    * Encodage d'un fichier en base64 et écriture du résultat dans un autre fichier.<br>
    * Dans le fichier résultat, les données encodées seront séparées sur des lignes
    * de 76 caractères.<br>
    * Les lignes se terminent par le séparateur de ligne de l'OS.
    * 
    * @param fichierSource
    *           chemin du fichier à encoder en base 64
    * @param fichierDest
    *           chemin du fichier à générer
    * @throws IOException
    *            exception sur les fichiers
    */
   public static void encodeFile(String fichierSource, String fichierDest)
         throws IOException {
      
      byte[] separateurLigne = SystemUtils.LINE_SEPARATOR.getBytes();
      encodeFile(fichierSource,fichierDest,LGR_LGN_DANS_FIC,separateurLigne);
      
   }
   
   
   /**
    * Encodage d'un fichier en base64 et écriture du résultat dans un autre fichier.<br>
    * 
    * @param fichierSource
    *           chemin du fichier à encoder en base 64
    * @param fichierDest
    *           chemin du fichier à générer
    * @param longueurLigne
    *           chaque ligne de donnée encodée en base 64 contiendra <i>longueurLigne</i> caractères 
    *           (arrondi au plus proche multiple de 4). 
    *           Si longueurLigne<0, les données encodées ne seront pas divisées en lignes.
    * @param separateurLigne
    *            chaque ligne de donnée encodée en base 64 se terminera par ces caractères (ex. : \r\n).
    *            Paramètre ignoré si <i>longueurLigne</i><0
    * @throws IOException
    *            exception sur les fichiers
    */
   public static void encodeFile(
         String fichierSource,
         String fichierDest,
         int longueurLigne,
         byte[] separateurLigne)
         throws IOException {

      FileInputStream input = new FileInputStream(fichierSource);
      try {

         FileOutputStream fos = new FileOutputStream(fichierDest);
         try {
         
            Base64OutputStream output = new Base64OutputStream(
                  fos, true, longueurLigne, separateurLigne);
            
            try {
               IOUtils.copy(input, output);
            } finally {
               if (output!=null) {
                  output.close();
               }
            }
         }
         finally {
            if (fos!=null) {
               fos.close();
            }
         }
      }
      finally {
         if (input!=null) {
            input.close();
         }
      }

   }

 
  /**
    * Renvoie le contenu d'un fichier, en l'encodant en base 64
    * 
    * @param cheminFichier Le chemin du fichier
    * @return contenu du fichier en base64
    * @throws IOException en cas d'erreur d'E/S
    *  
    */
   public static String encodeFileToString(String cheminFichier) throws IOException {
      FileInputStream fis = new FileInputStream(cheminFichier);
      try {
         Base64InputStream base64inputStream = new Base64InputStream(fis,true,-1,null);
         try {
            StringBuffer base64 = new StringBuffer();
            byte[] buffer = new byte[BUFFER_READ_SIZE];
            int lus;
            do
            {
               lus = base64inputStream.read(buffer);
               if (lus>0)
               {
                  for(int i=0;i<lus;i++)
                  {
                     base64.append((char)buffer[i]);
                  }
               }
            }
            while (lus>0);
            return base64.toString();
         }
         finally {
            if (base64inputStream!=null) {
               base64inputStream.close();
            }
         }
      }
      finally {
         if (fis!=null) {
            fis.close();
         }
      }
   }
   
   
}
