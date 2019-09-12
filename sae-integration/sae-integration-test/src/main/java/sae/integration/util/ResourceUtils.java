package sae.integration.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * Classe utilitaire de {@link ClassLoader}
 */
public final class ResourceUtils {

   private ResourceUtils() {

   }

   /**
    * Chargement d'un fichier depuis les ressources du jar
    * 
    * @param object
    *           l'objet duquel la demande de chargement du fichier provient
    * @param path
    *           le chemin du fichier au sein des ressources du projet
    * @return le flux pointant sur le fichier de ressource
    */
   public static InputStream loadResource(final Object object, final String path) {

      return object.getClass().getClassLoader().getResourceAsStream(path);
   }

   public static String loadResourceAsString(final Object object, final String path) {
      final InputStream stream = loadResource(object, path);
      try {
         return IOUtils.toString(stream);
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }
   }
}
