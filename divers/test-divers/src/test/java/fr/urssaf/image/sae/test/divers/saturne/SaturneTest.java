package fr.urssaf.image.sae.test.divers.saturne;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(BlockJUnit4ClassRunner.class)
public class SaturneTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(SaturneTest.class);
   
   private final static String[] SERVEURS = { 
      "cnp31saecas1.cer31.recouv", "cnp31saecas2.cer31.recouv", "cnp31saecas3.cer31.recouv", 
      "cnp31saecas4.cer31.recouv", "cnp31saecas5.cer31.recouv", "cnp31saecas6.cer31.recouv",
      "cnp69saecas1.cer69.recouv", "cnp69saecas2.cer69.recouv", "cnp69saecas3.cer69.recouv", 
      "cnp69saecas4.cer69.recouv", "cnp69saecas5.cer69.recouv", "cnp69saecas6.cer69.recouv",
      "cnp75saecas1.ur750.recouv", "cnp75saecas2.ur750.recouv", "cnp75saecas3.ur750.recouv", 
      "cnp75saecas4.ur750.recouv", "cnp75saecas5.ur750.recouv", "cnp75saecas6.ur750.recouv"
   };
   
   @Test
   @Deprecated
   public void etatRepair() throws IOException, ParseException {
      
      byte[] lines = new byte[4096];
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
         .appendHours()
         .appendSuffix("h")
         .appendMinutes()
         .appendSuffix("m")
         .appendSeconds()
         .appendSuffix("s")
         .toFormatter();
      
      LOGGER.info("Copier / coller l'état d'avancement du repair de Saturne : ");
      int nbCar = System.in.read(lines);
      
      String linesInStr = new String(lines).substring(0, nbCar);
      
      String serveur = "";
      Date debut = null;
      Date fin;
      Map<String, Duration> serveurs = new TreeMap<String, Duration>(); 
      
      for (String line : linesInStr.split("\n")) {
         if (line.contains("Execution du Repair")) {
            int index = line.indexOf("Execution du Repair ") + 24;
            serveur = line.substring(index, index + 25);
            String heure = line.substring(1, 20);
            debut = format.parse(heure);
         } else if (line.contains("OK")) {
            String heure = line.substring(1, 20);
            fin = format.parse(heure);
            Duration duree = new Duration(new DateTime(debut.getTime()), new DateTime(fin.getTime()));
            serveurs.put(serveur, duree);
            
            // reinitialisation des variables
            serveur = "";
            debut = null;
            fin = null;
         }
      }
      if (StringUtils.isNotEmpty(serveur) && debut != null) {
         // serveur en cours
         serveurs.put(serveur, null);
      }
      
      int nbServeurNonReparee = 0;
      for (String serveurCassandra : SERVEURS) {
         if (serveurs.containsKey(serveurCassandra)) {
            Duration duree = serveurs.get(serveurCassandra);
            if (duree != null) {
               LOGGER.info("Serveur {} : Repair termine en {}", new String[] { serveurCassandra, periodFormatter.print(duree.toPeriod())});
            } else {
               LOGGER.info("Serveur {} : Repair en cours", serveurCassandra);
            }
         } else {
            LOGGER.info("Serveur {} : Repair non fait", serveurCassandra);
            nbServeurNonReparee++;
         }
      }
      LOGGER.info("{} serveurs non reparee", nbServeurNonReparee);
   }
}
