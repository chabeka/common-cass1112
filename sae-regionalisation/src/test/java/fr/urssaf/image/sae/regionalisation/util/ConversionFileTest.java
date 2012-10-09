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
   
   @Ignore
   @Test
   public void generateFile() {
      File file = new File("c:/result.log");
      Reader reader = null;
      CSVReader csvReader = null;
      Writer fileWriter = null;

      
      try {
         reader = new FileReader(file);
         csvReader = new CSVReader(reader, '$');
         fileWriter = new FileWriter("c:/regionalisation.csv");
         String[] tabLine;
         String lucene;
         
         while ((tabLine = csvReader.readNext()) != null) {
            if (COTI_CLE.equals(tabLine[0].trim())) {
               lucene = "nce:" + tabLine[2].trim() + " AND cog:UR" + tabLine[1].trim();
               fileWriter.write(lucene);
               fileWriter.write(";nce;");
               fileWriter.write(tabLine[3].trim());
               fileWriter.write(";cog;UR");
               fileWriter.write(tabLine[4].trim());
               fileWriter.write("\n");
               
            } else if (CPTE_CLE.equals(tabLine[0])) {
               lucene = "nci:" + tabLine[2].trim() + " AND cog:UR" + tabLine[1].trim();
               fileWriter.write(lucene);
               fileWriter.write(";nci;");
               fileWriter.write(tabLine[3].trim());
               fileWriter.write(";cog;UR");
               fileWriter.write(tabLine[4].trim());
               fileWriter.write("\n");
               
            } else if (PERS_CLE.equals(tabLine[0].trim())) {
               lucene = "npe:" + tabLine[2].trim() + " AND cog:UR" + tabLine[1].trim();
               fileWriter.write(lucene);
               fileWriter.write(";npe;");
               fileWriter.write(tabLine[3].trim());
               fileWriter.write(";cog;UR");
               fileWriter.write(tabLine[4].trim());
               fileWriter.write("\n");
               
            }
         }
         
         
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         if (csvReader != null) {
            try {
               csvReader.close();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         
         if (fileWriter != null) {
            try {
               fileWriter.close();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
      
      
   }
   
}
