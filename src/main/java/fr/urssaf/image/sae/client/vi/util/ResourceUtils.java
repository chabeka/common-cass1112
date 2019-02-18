package fr.urssaf.image.sae.client.vi.util;

import java.io.InputStream;

/**
 * Classe utilitaire de {@link ClassLoader}
 */
public final class ResourceUtils {

   private ResourceUtils() {

   }

   /**
    * Récupération du stream associée à la ressource avec le chemin donné
    * 
    * @param object
    *           objet de référence pour le loader
    * @param path
    *           chemin de la resource
    * @return le stream associé à la resource
    */
   public static InputStream loadResource(Object object, String path) {

      return object.getClass().getClassLoader().getResourceAsStream(path);
   }

}
