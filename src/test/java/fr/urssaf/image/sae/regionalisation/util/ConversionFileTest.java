/**
 * 
 */
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

import au.com.bytecode.opencsv.CSVReader;

public class ConversionFileTest {

   private static final String COTI_CLE = "COTI_CLE";
   private static final String CPTE_CLE = "CPTE_CLE";
   private static final String PERS_CLE = "PERS_CLE";

   @Test
    @Ignore
   public void generateFile() {
      File parentDirectory = new File("c:/datas");

      regroupFiles(parentDirectory);
      // sortFiles(tempDirectory);
      // createDatasFile(parentDirectory, tempDirectory);

   }

   /**
    * @param parentDirectory
    * @param tempDirectory
    */
   @Test
   @Ignore
   public void createDatasFile() {
      
      File parentDirectory = new File("c:/datas");
      File tempDirectory = new File(parentDirectory, "temp");
      Reader reader = null;
      CSVReader csvReader = null;
      Writer fileWriter = null;
      File destFile;

      for (File file : tempDirectory.listFiles()) {
         try {
            destFile = new File(parentDirectory, file.getName());
            fileWriter = new FileWriter(destFile);
            reader = new FileReader(file);
            csvReader = new CSVReader(reader, '$');
            String[] tabLine;
            String lucene = null;

            while ((tabLine = csvReader.readNext()) != null) {
               if (COTI_CLE.equals(tabLine[0].trim())) {
                  lucene = "nce:" + tabLine[2].trim();
                  fileWriter.write(lucene);
                  fileWriter.write(";nce;");
                  fileWriter.write(tabLine[2].trim());
                  fileWriter.write(">");
                  fileWriter.write(tabLine[3].trim());
                  fileWriter.write(";cog;UR");
                  fileWriter.write(tabLine[1].trim());
                  fileWriter.write(">UR");
                  fileWriter.write(tabLine[4].trim());
                  fileWriter.write(";cop;UR");
                  fileWriter.write(tabLine[1].trim());
                  fileWriter.write(">UR");
                  fileWriter.write(tabLine[4].trim());
                  fileWriter.write("\n");

               } else if (CPTE_CLE.equals(tabLine[0])) {
                  lucene = "nci:" + tabLine[2].trim();
                  fileWriter.write(lucene);
                  fileWriter.write(";nci;");
                  fileWriter.write(tabLine[2].trim());
                  fileWriter.write(">");
                  fileWriter.write(tabLine[3].trim());
                  fileWriter.write(";cog;UR");
                  fileWriter.write(tabLine[1].trim());
                  fileWriter.write(">UR");
                  fileWriter.write(tabLine[4].trim());
                  fileWriter.write(";cop;UR");
                  fileWriter.write(tabLine[1].trim());
                  fileWriter.write(">UR");
                  fileWriter.write(tabLine[4].trim());
                  fileWriter.write("\n");

               } else if (PERS_CLE.equals(tabLine[0].trim())) {
                  lucene = "npe:" + tabLine[2].trim();
                  fileWriter.write(lucene);
                  fileWriter.write(";npe;");
                  fileWriter.write(tabLine[2].trim());
                  fileWriter.write(">");
                  fileWriter.write(tabLine[3].trim());
                  fileWriter.write(";cog;UR");
                  fileWriter.write(tabLine[1].trim());
                  fileWriter.write(">UR");
                  fileWriter.write(tabLine[4].trim());
                  fileWriter.write(";cop;UR");
                  fileWriter.write(tabLine[1].trim());
                  fileWriter.write(">UR");
                  fileWriter.write(tabLine[4].trim());
                  fileWriter.write("\n");

               }
            }

         } catch (IOException e) {
            e.printStackTrace();

         } finally {

            if (csvReader != null) {
               try {
                  csvReader.close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }

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
