package fr.urssaf.image.dictao.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class Utils {

   
   public URL buildPkcs12urlFromFichierRessource(String nomFicRessource) {
      
      try {
         
         return this.getClass().getResource(nomFicRessource).toURI().toURL();
         
      } catch (URISyntaxException e) {
         throw new RuntimeException(e);
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      }
      
   }
   
   
   public String getContentFichierSignature(String cheminCompletFichier) {
      
      File file = new File(cheminCompletFichier);
      FileInputStream fis;
      try {
         fis = new FileInputStream(file);
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      }
      try {
         return IOUtils.toString(fis);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      
   }
   
}
