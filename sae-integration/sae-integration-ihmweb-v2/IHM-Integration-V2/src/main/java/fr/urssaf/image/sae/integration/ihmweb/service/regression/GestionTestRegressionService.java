package fr.urssaf.image.sae.integration.ihmweb.service.regression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Couche service permettant la sauvegarde des differents tests sur le serveur
 * 
 */
@Service
public class GestionTestRegressionService {

   /**
    * Fonction de sauvegarde des fichiers de test
    * 
    * @param listeFile
    * @param cheminSauvegarde
    * @return
    * @throws IOException
    */
   public boolean sauvegarderTest(List<File> listeFile, String cheminSauvegarde)
         throws IOException {

      // pour chaques fichiers contenus dans la liste
      for (File file : listeFile) {
         OutputStream out = null;
         InputStream filecontent = new FileInputStream(file);

         try {
            // ouvre inputStream dans le dossier ou la sauvegarde doit etre fait
            out = new FileOutputStream(new File(cheminSauvegarde
                  + file.getName()));

            int read = 0;
            final byte[] bytes = new byte[2048];

            // on copie le contenu du fichier dans le nouveau
            while ((read = filecontent.read(bytes)) != -1) {
               out.write(bytes, 0, read);
            }

         } catch (FileNotFoundException fne) {
            fne.printStackTrace();
            return false;
         } finally {
            if (out != null) {
               out.close();
            }
            if (filecontent != null) {
               filecontent.close();
            }
         }
      }
      return true;
   }
}
