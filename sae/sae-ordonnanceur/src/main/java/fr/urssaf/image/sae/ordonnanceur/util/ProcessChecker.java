package fr.urssaf.image.sae.ordonnanceur.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessChecker implements Runnable {

   /**
    * Logger.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ProcessChecker.class);

   protected volatile boolean processRunning = false;
   private InputStream inputStream;
   private int pid = 0;

   public ProcessChecker(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   private BufferedReader getBufferedReader(InputStream is) {
      return new BufferedReader(new InputStreamReader(is));
   }


   @Override
   public void run() {

      BufferedReader br = getBufferedReader(inputStream);
      String ligne = "";
      try {
         while ((ligne = br.readLine()) != null) {
            // Sous Windows, risque de faux positif, mais ne concerne que les postes de dev
            if (SystemUtils.IS_OS_WINDOWS
                  && ligne.contains(Integer.toString(pid))) {
               processRunning = true;
            } else if (SystemUtils.IS_OS_LINUX
                  && ligne.equals(Integer.toString(pid))) {
               processRunning = true;
            }
         }
      } catch (Exception e) {
         LOGGER.warn(
               "ProcessUtils - échec vérification existence du job de masse", e);
         processRunning = true;

      }
   }

   /**
    * @return the processRunning
    */
   public boolean isProcessRunning() {
      return processRunning;
   }

}