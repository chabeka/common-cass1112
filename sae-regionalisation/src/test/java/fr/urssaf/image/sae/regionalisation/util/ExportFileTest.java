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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

public class ExportFileTest {

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
         fileWriter = new FileWriter("c:/correspondances.csv");
         String[] tabLine;
         Map<String, List<String>> map = new HashMap<String, List<String>>();

         while ((tabLine = csvReader.readNext()) != null) {

            if (!map.containsKey(tabLine[1].trim())) {
               map.put(tabLine[1].trim(), new ArrayList<String>());
            }

            if (!map.get(tabLine[1].trim()).contains(tabLine[4].trim())) {
               map.get(tabLine[1].trim()).add(tabLine[4].trim());
            }
         }
         
         List<String> list = new ArrayList<String>(map.keySet());
         Collections.sort(list);
         
         for (String key : list) {
            for (String value : map.get(key)) {
               fileWriter.write(key + ";" + value + "\n");
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
