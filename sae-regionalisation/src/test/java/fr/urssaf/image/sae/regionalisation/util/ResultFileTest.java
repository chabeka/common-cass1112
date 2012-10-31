/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * 
 */
public class ResultFileTest {

   @Test
   @Ignore
   public void logErreursLignes() {

      File file = new File("c:/suivi.log");
      Reader reader = null;
      BufferedReader bReader = null;
      File fDatas = new File("c:/datas/regionalisation_coti.csv");
      Reader dReader = null;
      BufferedReader bDReader = null;

      try {
         reader = new FileReader(file);
         bReader = new BufferedReader(reader);

         dReader = new FileReader(fDatas);
         bDReader = new BufferedReader(dReader);

         String line, dLine;
         String value, dValue;

         bReader.readLine();
         bReader.readLine();
         boolean stop = false;

         while ((line = bReader.readLine()) != null
               && (dLine = bDReader.readLine()) != null && !stop) {
            dValue = dLine.split(";")[0];
            value = line.split(";")[1].split("%")[1];

            if (!value.trim().equals(dValue.trim())) {
               System.out.println(line);
               System.out.println(dLine);
               stop = true;
            }
         }
      } catch (NumberFormatException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } finally {

         try {
            if (bReader != null) {
               bReader.close();
            }
         } catch (IOException e1) {
            e1.printStackTrace();
         }

         try {
            if (reader != null) {
               reader.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

   }

   @Test
   @Ignore
   public void getTimeFromSuivi() {
      File repertoire = new File("c:/resultats");
      File[] tabSuivis = repertoire.listFiles(new FilenameFilter() {

         @Override
         public boolean accept(File dir, String name) {
            return name.startsWith("suivi_");
         }
      });

      List<File> suivis = Arrays.asList(tabSuivis);

      Collections.sort(suivis, new Comparator<File>() {

         /**
          * {@inheritDoc}
          */
         @Override
         public int compare(File o1, File o2) {

            String index01 = o1.getName().substring(
                  o1.getName().indexOf("_") + 1, o1.getName().indexOf("."));
            String index02 = o2.getName().substring(
                  o2.getName().indexOf("_") + 1, o2.getName().indexOf("."));

            Integer int1 = Integer.valueOf(index01);
            Integer int2 = Integer.valueOf(index02);

            return int1.compareTo(int2);
         }

      });

      int total = 0;
      int fileNumber;
      File resultats = new File("c:/resultat_traitement.log");

      Writer writer = null;
      Reader reader = null;
      BufferedReader bReader = null;
      String line;
      try {
         writer = new FileWriter(resultats);
         for (File file : suivis) {
            fileNumber = 0;
            String oldValue = "";
            String newValue;
            reader = new FileReader(file);
            bReader = new BufferedReader(reader);
            String sValue;
            Integer value;
            bReader.readLine();
            bReader.readLine();
            while (StringUtils.isNotBlank((line = bReader.readLine()))) {
               
               newValue = line.split(";")[1].split("%")[1];
               if (!oldValue.equals(newValue)) {
                  sValue = line.split(";")[1].split("%")[2];
                  value = Integer.valueOf(sValue);
                  fileNumber += value.intValue();
                  oldValue = newValue;
               }
            }

            writer.write(file.getName() + " : " + fileNumber + "\n");
            total += fileNumber;
         }

         writer.write("total : " + total);

      } catch (IOException e) {
         e.printStackTrace();

      } finally {
         if (bReader != null) {
            try {
               bReader.close();
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

         if (writer != null) {
            try {
               writer.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }
}
