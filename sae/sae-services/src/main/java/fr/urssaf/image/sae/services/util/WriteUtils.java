/**
 * 
 */
package fr.urssaf.image.sae.services.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitaire d'écriture de fichier
 * 
 */
public final class WriteUtils {

   private static final int ESSAIS_MAX = 3;
   private static final int WAITING_TIME = 10000;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(WriteUtils.class);

   private WriteUtils() {
   }

   /**
    * Création et écriture du fichier. 3 essais de création de fichier.
    * 
    * @param file
    *           fichier à créer
    * @param lines
    *           les lignes à insérer. Peut être null ou vide
    * @param endLineChar
    *           caractères de fin de ligne
    * @throws IOException
    *            exception levée lors de l'écriture du fichier
    */
   public static void writeFile(File file, List<String> lines,
         String endLineChar) throws IOException {

      int index = 0;
      String prefixeTrc = "writeFile()";

      while (index < ESSAIS_MAX && !file.exists()) {
         try {
            file.createNewFile();

            if (CollectionUtils.isNotEmpty(lines)) {
               FileUtils.writeLines(file, lines, endLineChar);
            }

         } catch (IOException e) {

            if (index < ESSAIS_MAX - 1) {
               LOGGER.warn("{} - {}ème tentative d'écriture du ficher {} ",
                     new Object[] { prefixeTrc, index + 1,
                           file.getAbsoluteFile().getName() });

               try {
                  Thread.sleep(WAITING_TIME);
               } catch (InterruptedException exception) {
                  LOGGER.info("impossible d'endormir le process");
               }

            }

            if (index == ESSAIS_MAX - 1) {
               throw e;
            }

            index++;
         }
      }
   }
}
