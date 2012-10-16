/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

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
      File[] directories = parentDirectory.listFiles();
      Reader reader = null;
      CSVReader csvReader = null;
      Writer fileWriter = null;
      Writer smallWriter = null;
      try {
         fileWriter = new FileWriter("c:/regionalisation.csv");
         smallWriter = new FileWriter("c:/echantillon.csv");

         for (File directory : directories) {

            for (File file : directory.listFiles()) {
               try {
                  reader = new FileReader(file);
                  csvReader = new CSVReader(reader, '$');
                  String[] tabLine;
                  String lucene = null;
                  String newLucene = null;

                  int index = 0;
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

                        newLucene = "nce:" + tabLine[3].trim();

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

                        newLucene = "nci:" + tabLine[3].trim();

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

                        newLucene = "npe:" + tabLine[3].trim();

                     }

                     if (index == 0) {
                        smallWriter.write(lucene);
                        smallWriter.write("\n");
                        smallWriter.write(newLucene);
                        smallWriter.write("\n");
                        smallWriter.flush();
                        index++;
                     }
                  }
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
               }
            }

         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      
      } catch (IOException e) {
         e.printStackTrace();
      
         if (fileWriter != null) {
            try {
               fileWriter.close();
            } catch (IOException ex) {
               ex.printStackTrace();
            }
         }
         
         if (smallWriter != null) {
            try {
               smallWriter.close();
            } catch (IOException ex) {
               ex.printStackTrace();
            }
         }
      }

   }

}
