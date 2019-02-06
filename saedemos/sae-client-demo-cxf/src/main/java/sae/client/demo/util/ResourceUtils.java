package sae.client.demo.util;

import java.io.InputStream;

/**
 * Classe utilitaire de {@link ClassLoader}
 * 
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
   public static InputStream loadResource(Object object, String path) {

      return object.getClass().getClassLoader().getResourceAsStream(path);
   }

}
