package fr.urssaf.image.sae.test.divers.base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(BlockJUnit4ClassRunner.class)
public class ConversionTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ConversionTest.class);
   
   @Test
   public void convertirFichiers() throws FileNotFoundException, IOException {
      String repertoire = "C:/tmp/tif";
      String extensionOrigine = "base64";
      String extensionFinal = "pdf";
      
      File monRepertoire = new File(repertoire);
      LOGGER.debug("Traitement du répertoire : {}", repertoire);
      if (!monRepertoire.exists()) {
         LOGGER.error("Le répertoire {} n'existe pas", repertoire);
      } else if (!monRepertoire.isDirectory()) {
         LOGGER.error("Le répertoire {} n'est pas un répertoire", repertoire);
      } else {
         File[] files=monRepertoire.listFiles();
         for (File fichier : files) {
            if (fichier.isFile() && fichier.getName().endsWith("." + extensionOrigine)) {
               StringBuffer contenuFichier = new StringBuffer();
               // lecture du fichier 
               BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(fichier)));
               String ligne;
               while ((ligne = buff.readLine())!=null){
                  contenuFichier.append(ligne);
                  contenuFichier.append('\n');
               }
               buff.close(); 
               
               // converti le fichier
               LOGGER.debug("Conversion du fichier : {}", fichier.getName());
               String nomFichierSortie = fichier.getName().replace("." + extensionOrigine, "." + extensionFinal);
               String cheminNouveauFichier = repertoire + "/" + nomFichierSortie;
               byte[] fichierConverti = Base64.decodeBase64(contenuFichier.toString());
               FileOutputStream outputStream = new FileOutputStream(cheminNouveauFichier);
               outputStream.write(fichierConverti);
               outputStream.close();
            }
         }
      }
   }
}
