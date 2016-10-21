package fr.urssaf.image.sae.test.divers.trace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class FileConverterTest {

   
   @Test
   public void convertFile() throws Exception {
      File fileIn = new File("d:\\divers\\doc_sicomor_test.tif");
      FileReader fileReader = new FileReader(fileIn);
      BufferedReader bufferReader = new BufferedReader(fileReader);
      
      String line;
      while((line = bufferReader.readLine()) != null)  {
         //System.out.println(toHex(line));
         StringBuffer lineConv = new StringBuffer();
         for (char caract : line.toCharArray()) {
            if (caract <= 32) {
               lineConv.append("\\\\x");
               lineConv.append(String.format("%02x", (int) caract));
            } else {
               lineConv.append(caract);
            }
         }
         System.out.println(line);
         System.out.println(lineConv.toString());
      }
      
      bufferReader.close();
      fileReader.close();
   }
}
