package fr.urssaf.image.sae.webservices.component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;

/**
 * Classe utilitaire pour les tests
 * 
 * 
 */
public final class IgcConfigUtils {

   private IgcConfigUtils() {

   }

   /**
    * 
    * @param repertory
    *           nom du répertoire temporaire
    * @return instance d'un repertoire temporaire
    */
   public static File createTempRepertory(String repertory) {

      File tempRepertory = new File(FilenameUtils.concat(SystemUtils
            .getJavaIoTmpDir().getAbsolutePath(), repertory));

      createRepertory(tempRepertory);

      return tempRepertory;

   }

   /**
    * création d'un répertoire
    * 
    * @param repertory
    *           nom du répertoire
    */
   public static void createRepertory(File repertory) {

      try {
         FileUtils.forceMkdir(repertory);
         cleanDirectory(repertory);
      } catch (IOException e) {
         throw new IllegalArgumentException(e);
      }
   }

   /**
    * nettoyage du répertoire
    * 
    * @param directory
    *           nom du repertoire
    */
   public static void cleanDirectory(File directory) {

      try {
         FileUtils.cleanDirectory(directory);

      } catch (IOException e) {
         throw new IllegalArgumentException(e);
      }
   }

   /**
    * 
    * @param url
    *           adresse de l'url
    * @return instance de {@link URL}
    */
   public static URL createURL(String url) {

      try {
         return new URL(url);
      } catch (MalformedURLException e) {
         throw new IllegalArgumentException(e);
      }
   }

   /**
    * Téléchargement d'un fichier
    * 
    * @param url
    *           fichier à télécharger
    * @param destination
    *           fichier téléchargé
    */
   public static void download(URL url, File destination) {

      try {
         FileUtils.copyURLToFile(url, destination);
      } catch (IOException e) {
         throw new IllegalArgumentException(e);
      }

   }

}
