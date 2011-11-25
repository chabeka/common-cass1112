package fr.urssaf.image.sae.webservice.client.demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Classe utilitaires des object {@link InputStream}
 * 
 * 
 */
public final class StreamUtils {

   private StreamUtils() {

   }

   /**
    * instanciation d'uhe chaine de chaine de caractère en remplace dans le
    * contenu d'un flux les éléments de la liste 'searchList' par les valeurs de
    * 'replacementList'<br>
    * <br>
    * La méthode appelle
    * {@link StringUtils#replaceEach(String, String[], String[])}
    * 
    * @param input
    *           flux d'origine
    * @param searchList
    *           éléments à remplacer
    * @param replacementList
    *           valeurs de éléments
    * @return nouvelle chaine de caractère
    */
   public static String createObject(InputStream input, String[] searchList,
         String[] replacementList) {

      try {
         List<String> lines = IOUtils.readLines(input);

         StringBuffer assertion = new StringBuffer();

         for (String line : lines) {

            String newLine = StringUtils.replaceEach(line, searchList,
                  replacementList);

            assertion.append(newLine.concat("\n"));

         }

         return assertion.toString();

      } catch (IOException e) {
         throw new IllegalArgumentException(e);
      }
   }

}
