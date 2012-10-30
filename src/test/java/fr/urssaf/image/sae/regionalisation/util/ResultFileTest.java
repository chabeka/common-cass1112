/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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

}
