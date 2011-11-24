package fr.urssaf.image.sae.webservice.client.demo.util;

import java.io.InputStream;

/**
 * Classe utilitaire de {@link ClassLoader}
 * 
 * 
 */
public final class ResourceUtils {

   private ResourceUtils() {

   }

   /**
    * retourne un fichier contenu dans le classpath<br>
    * Cette méthode permet de récupérer des fichiers dans le classpath<br>
    * <br>
    * ex : <code>loadResource("temp\fichier.xml")</code> permet de récuperer le
    * fichier dans le répertoire <code>temp</code> situé à la racine du
    * répertoire de compilation
    * 
    * @param object
    *           instance d'une classe du classpath
    * @param path
    *           chemin du fichier
    * @return fichier correspondant au path
    */
   public static InputStream loadResource(Object object, String path) {

      return object.getClass().getClassLoader().getResourceAsStream(path);
   }
}
