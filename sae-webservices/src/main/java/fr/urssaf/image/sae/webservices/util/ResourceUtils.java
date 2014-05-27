package fr.urssaf.image.sae.webservices.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Class utilitaire de {@link Resource}
 * 
 * 
 */
public final class ResourceUtils {

   private ResourceUtils() {

   }

   /**
    * Renvoie la liste des fichiers contenues dans une
    * {@link FileSystemResource}<br>
    * <ul>
    * <li>si la resource est un répertoire alors on renvoie l'ensemble des
    * fichiers contenus dans ce répertoire et ses sous répertoires</li>
    * <li>sinon on renvoie le fichier lui même</li>
    * </ul>
    * 
    * @param resource
    *           ressource
    * @param extensions
    *           extensions de filtrage, liste d'extension {ex : .java,.xml} si
    *           vide alors tous les fichiers sont renvoyés, si null aucun
    * @return liste de ressources
    */
   public static List<Resource> loadResources(FileSystemResource resource,
         String[] extensions) {

      List<Resource> resources = new ArrayList<Resource>();

      if (resource.getFile().isDirectory()) {
         for (File acFile : FileUtils.listFiles(resource.getFile(), extensions,
               true)) {
            resources.add(createFileSystemResource(acFile));
         }
      } else {

         resources.add(resource);
      }

      return resources;
   }

   /**
    * 
    * @param file
    *           a File handle
    * @return instance de {@link FileSystemResource}
    */
   public static FileSystemResource createFileSystemResource(File file) {
      return new FileSystemResource(file);
   }

}
