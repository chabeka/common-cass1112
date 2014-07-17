package fr.urssaf.image.sae.regionalisation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

public class ConversionFileTest {

   @Test
   //@Ignore
   public void generateFile() throws IOException {

      File parentDirectory = new File(
            "S:/REFERENTIEL/03- SI/SAE/PROJETS TECHNIQUES OU FONCTIONNELS/Régionalisation/Vague 4 (mi 2014)/Fichiers de fusion à blanc (FAB)/FAB3");

      regroupFiles(parentDirectory);

   }

   private File regroupFiles(File parentDirectory) {

      File[] directories = parentDirectory.listFiles();
      Reader reader = null;
      BufferedReader bReader = null;
      Writer fileWriter = null;
      File tempDirectory = new File(parentDirectory, "temp");

      try {
         if (tempDirectory.exists()) {
            FileUtils.forceDelete(tempDirectory);
         }

         tempDirectory.mkdir();

      } catch (IOException e1) {
         throw new RuntimeException(
               "erreur de suppression du répertoire temporaire");
      }

      File coti = new File(tempDirectory, "regionalisation_coti.csv");
      File pers = new File(tempDirectory, "regionalisation_pers.csv");
      File cpte = new File(tempDirectory, "regionalisation_cpte.csv");
      String line;
      for (File directory : directories) {

         System.out.println("debut de traitement du répertoire "
               + directory.getName());

         for (File file : directory.listFiles()) {

            try {

               reader = new FileReader(file);
               bReader = new BufferedReader(reader);

               if (file.getName().contains("COTI")) {
                  fileWriter = new FileWriter(coti, true);
               } else if (file.getName().contains("PERS")) {
                  fileWriter = new FileWriter(pers, true);
               } else if (file.getName().contains("CPTE")) {
                  fileWriter = new FileWriter(cpte, true);
               } else {
                  fileWriter = null;
               }

               if (fileWriter != null) {
                  while ((line = bReader.readLine()) != null) {
                     fileWriter.write(line.trim());
                     fileWriter.write("\n");
                  }
               }

            } catch (IOException e) {
               e.printStackTrace();

            } finally {

               if (reader != null) {

                  try {
                     reader.close();
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
               }

               if (fileWriter != null) {
                  try {
                     fileWriter.close();
                  } catch (IOException ex) {
                     ex.printStackTrace();
                  }
               }
            }
         }

         System.out.println("fin de traitement du répertoire "
               + directory.getName());
      }

      return tempDirectory;

   }

}
