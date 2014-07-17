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

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

public class ExportFileTest {

   //@Ignore
   @Test
   public void generateFile() {
      File mainDirectory = new File("S:/REFERENTIEL/03- SI/SAE/PROJETS TECHNIQUES OU FONCTIONNELS/Régionalisation/Vague 4 (mi 2014)/Fichiers de fusion à blanc (FAB)/FAB3");
      Reader reader = null;
      CSVReader csvReader = null;
      Writer fileWriter = null;
      Map<String, List<String>> map = new HashMap<String, List<String>>();

      for (File directory : mainDirectory.listFiles()) {
         for (File file : directory.listFiles()) {
            if (
                  (StringUtils.equals(file.getName(),"regionalisation_coti.csv")) || 
                  (StringUtils.equals(file.getName(),"regionalisation_cpte.csv")) || 
                  (StringUtils.equals(file.getName(),"regionalisation_pers.csv"))
                  ) {
               continue;
            }
            System.out.println("Fichier comptabilisé : " + file.getAbsolutePath());
            try {
               reader = new FileReader(file);
               csvReader = new CSVReader(reader, '$');
               String[] tabLine;

               while ((tabLine = csvReader.readNext()) != null) {

                  if (!map.containsKey(tabLine[1].trim())) {
                     map.put(tabLine[1].trim(), new ArrayList<String>());
                  }

                  if (!map.get(tabLine[1].trim()).contains(tabLine[4].trim())) {
                     map.get(tabLine[1].trim()).add(tabLine[4].trim());
                  }
               }

            } catch (FileNotFoundException e) {
               e.printStackTrace();

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
                  } catch (IOException e) {
                     e.printStackTrace();
                  }
               }
            }
         }
      }

      List<String> list = new ArrayList<String>(map.keySet());
      Collections.sort(list);
      try {
         fileWriter = new FileWriter("c:/correspondances.csv");

         for (String key : list) {
            for (String value : map.get(key)) {
               fileWriter.write(key + ";" + value + "\n");
            }
         }

      } catch (IOException e) {
         e.printStackTrace();

      } finally {
         if (fileWriter != null) {
            try {
               fileWriter.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }

}
