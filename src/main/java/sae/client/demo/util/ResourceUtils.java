package sae.client.demo.util;

import java.io.InputStream;

/**
 * Classe utilitaire de {@link ClassLoader}
 * 
 * 
 */
public final class ResourceUtils {

   private ResourceUtils() {

   }

   public static InputStream loadResource(Object object, String path) {

      return object.getClass().getClassLoader().getResourceAsStream(path);
   }
   
   
}
