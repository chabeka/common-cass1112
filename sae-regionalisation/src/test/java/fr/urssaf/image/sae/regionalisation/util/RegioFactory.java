/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Classe permettant de faire des objets utilisés dans les tests
 * 
 */
public final class RegioFactory {

   /**
    * Constructeur
    */
   private RegioFactory() {
   }

   /**
    * Création du répertoire temporaire
    * 
    * @return le répertoire temporaire
    * @throws IOException
    *            erreur lors de la création du répertoire
    */
   public static File createSuiviTempFile() throws IOException {
      File tempDirectory = FileUtils.getTempDirectory();
      File treatDirectory = new File(tempDirectory, "regionalisation");
      treatDirectory.mkdir();
      return treatDirectory;
   }

}
