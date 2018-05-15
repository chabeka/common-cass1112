package fr.urssaf.image.sae.ordonnanceur.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessChecker implements Callable<Boolean> {

   /**
    * Logger.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ProcessChecker.class);

   protected volatile boolean processRunning = false;
   private final InputStream inputStream;
   private final InputStream errorStream;
   private int pid = 0;

   public ProcessChecker(InputStream inputStream, InputStream errorStream,
         int pid) {
      this.inputStream = inputStream;
      this.errorStream = errorStream;
      this.pid = pid;
   }

   private BufferedReader getBufferedReader(InputStream is) {
      return new BufferedReader(new InputStreamReader(is));
   }

   @Override
   public Boolean call() throws Exception {

      BufferedReader br = getBufferedReader(inputStream);

      BufferedReader brerror = getBufferedReader(errorStream);

      String ligne = "";
      String ligneError = "";
      try {
         while ((ligneError = brerror.readLine()) != null) {
            if (!ligneError.isEmpty()) {
               processRunning = true;
               LOGGER.warn(
                     "ProcessUtils - échec vérification existence du job de masse (execution commande)");
               throw new RuntimeException(
                     "Une erreur a eu lieu lors de l'execution de la commande de vérification de l'existence du process");
            }
         }

         while ((ligne = br.readLine()) != null) {
            // Sous Windows, risque de faux positif, mais ne concerne que les
            // postes de dev
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
         throw new RuntimeException(
               "Une erreur a eu lieu lors de la vérification de l'existence du process");
      }

      return processRunning;
   }

}