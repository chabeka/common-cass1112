package fr.urssaf.image.dictao.client.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class Utils {

   public static URL ressourcePathToURL(String nomFicRessource) {
      
      try {
         return Utils.class.getResource(nomFicRessource).toURI().toURL();
      } catch (URISyntaxException e) {
         throw new RuntimeException(e);
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      }
      
   }

	public static String ressourcePathToFilePath(String nomFicRessource) {

		try {
			URL url = Utils.class.getResource(nomFicRessource);
			File file = new File(url.toURI());
			return file.getAbsoluteFile().toString();

		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

	}

   
   public static String getContentFichierSignature(String cheminCompletFichier) {
      
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
   
	public static String getFileContent(URL url) {
		try {
			return IOUtils.toString(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
   
}
