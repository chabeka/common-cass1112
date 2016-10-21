package fr.urssaf.image.sae.test.dfce17;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(BlockJUnit4ClassRunner.class)
public class SplitGeneratorTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(SplitGeneratorTest.class);
   
   @Test
   public void generateSplitForUUID() {
      // parametre du split
      String valeurMax = "ffffffff-ffff-ffff-ffff-ffffffffffff";
      int nbRange = 32;
      
      // boucle sur les ranges a creer
      for (int index = 0; index < nbRange; index++) {
      
         StringBuffer buffer = new StringBuffer();
         boolean first = true;
         // split en partie
         String[] parties = valeurMax.split("-");
         for (int index2 = 0; index2 < parties.length; index2++) {
            
            // recupere la partie et la converti en decimal
            Long maxPartie = Long.parseLong(parties[index2], 16);
            
            // calcul le nombre d'element par partie
            BigDecimal nbElementPartie = new BigDecimal(maxPartie).divide(new BigDecimal(nbRange), 2, RoundingMode.HALF_EVEN);
          
            // calcul la valeur de cette partie
            BigDecimal valeurEnDouble = nbElementPartie.multiply(new BigDecimal(index));
            
            // calcul de la valeur de l'index de range
            Long valeur = Long.valueOf(round(valeurEnDouble.doubleValue(), 2));
            
            // ajout du buffer
            if (!first) {
               buffer.append("-");
            }
            String hexa = Long.toHexString(valeur);
            buffer.append(StringUtils.leftPad(hexa, parties[index2].length(), "0"));
            first = false;
         }
         
         LOGGER.debug("{}", buffer.toString());
      }
   }
   
   public static Long round(double value, int places) {
      if (places < 0) throw new IllegalArgumentException();

      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(places, RoundingMode.HALF_EVEN);
      return bd.longValue();
   }
   
   @Test
   public void generateSplitRangeForUUID() {
      // parametre du split
      String valeurMax = "ffffffff-ffff-ffff-ffff-ffffffffffff";
      int nbRange = 300;
      List<String> ranges = new ArrayList<String>();
      
      // boucle sur les ranges a creer
      for (int index = 0; index < nbRange; index++) {
      
         StringBuffer buffer = new StringBuffer();
         boolean first = true;
         // split en partie
         String[] parties = valeurMax.split("-");
         for (int index2 = 0; index2 < parties.length; index2++) {
            
            // recupere la partie et la converti en decimal
            Long maxPartie = Long.parseLong(parties[index2], 16);
            
            // calcul le nombre d'element par partie
            BigDecimal nbElementPartie = new BigDecimal(maxPartie).divide(new BigDecimal(nbRange), 2, RoundingMode.HALF_EVEN);
          
            // calcul la valeur de cette partie
            BigDecimal valeurEnDouble = nbElementPartie.multiply(new BigDecimal(index));
            
            // calcul de la valeur de l'index de range
            Long valeur = Long.valueOf(round(valeurEnDouble.doubleValue(), 2));
            
            // ajout du buffer
            if (!first) {
               buffer.append("-");
            }
            String hexa = Long.toHexString(valeur);
            buffer.append(StringUtils.leftPad(hexa, parties[index2].length(), "0"));
            first = false;
         }
         
         ranges.add(buffer.toString());
         LOGGER.debug("{}", buffer.toString());
      }
      
      if (ranges.size() != nbRange) {
         LOGGER.error("nombre de range incorrect");
      } else {
         // mise en forme des splits
         String s = "[min_lower_bound TO " + ranges.get(1) + "[";
         for(int i = 1; i < ranges.size() - 1; i++) {
            s += "|[" + ranges.get(i) + " TO " + ranges.get(i+1) + "[";
         }
         s += "|[" + ranges.get(ranges.size() - 1) + " TO max_upper_bound]";
         LOGGER.info("{}", s);
      }
   }
   
   @Test
   public void generateSplitRangeForUUID_GIVN() {
      // parametre du split
      String valeurMax = "ffffffff-ffff-ffff-ffff-ffffffffffff";
      int nbRange = 5;
      List<String> ranges = new ArrayList<String>();
      
      // boucle sur les ranges a creer
      for (int index = 0; index < nbRange; index++) {
      
         StringBuffer buffer = new StringBuffer();
         boolean first = true;
         // split en partie
         String[] parties = valeurMax.split("-");
         for (int index2 = 0; index2 < parties.length; index2++) {
            
            // recupere la partie et la converti en decimal
            Long maxPartie = Long.parseLong(parties[index2], 16);
            
            // calcul le nombre d'element par partie
            BigDecimal nbElementPartie = new BigDecimal(maxPartie).divide(new BigDecimal(nbRange), 2, RoundingMode.HALF_EVEN);
          
            // calcul la valeur de cette partie
            BigDecimal valeurEnDouble = nbElementPartie.multiply(new BigDecimal(index));
            
            // calcul de la valeur de l'index de range
            Long valeur = Long.valueOf(round(valeurEnDouble.doubleValue(), 2));
            
            // ajout du buffer
            if (!first) {
               buffer.append("-");
            }
            String hexa = Long.toHexString(valeur);
            buffer.append(StringUtils.leftPad(hexa, parties[index2].length(), "0"));
            first = false;
         }
         
         ranges.add(buffer.toString());
         LOGGER.debug("{}", buffer.toString());
      }
      
      if (ranges.size() != nbRange) {
         LOGGER.error("nombre de range incorrect");
      } else {
         // mise en forme des splits
         String s = "[min_lower_bound TO " + ranges.get(1) + "[";
         for(int i = 1; i < ranges.size() - 1; i++) {
            s += "|[" + ranges.get(i) + " TO " + ranges.get(i+1) + "[";
         }
         s += "|[" + ranges.get(ranges.size() - 1) + " TO max_upper_bound]";
         LOGGER.info("{}", s);
      }
   }
}
