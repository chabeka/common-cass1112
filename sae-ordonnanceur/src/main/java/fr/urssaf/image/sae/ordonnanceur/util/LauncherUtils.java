package fr.urssaf.image.sae.ordonnanceur.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Classe utilitaire pour le lancement de processus indépendants du programme
 * qui la lancé.<br>
 * <br>
 * Ici les processus sont prévus pour des JAR exécutable ou de simple classe
 * JAVA exécutables<br>
 * <br>
 * ex : on exécute un JAR exécutable service-exe.jar avec arg1 et arg2 en
 * arguments d'entrée<br>
 * <br>
 * <code>java -jar /appl/etc/service-exe.jar arg1 arg 2</code> <br>
 * <br>
 * 
 */
public final class LauncherUtils {

   private static final Logger LOG = LoggerFactory
         .getLogger(LauncherUtils.class);

   private LauncherUtils() {

   }

   /**
    * 
    * La méthode lance le processus en appelant la classe
    * {@link Runtime#exec(String)} avec le paramètre <code>command</code><br>
    * <br>
    * Exemples de commandes :
    * <ul>
    * <li><code>java -jar exemple.jar arg1 arg2 arg2</code></li>
    * </ul>
    * 
    * @param command
    *           commande à lancer
    * @return processus lancé
    * @throws IOException
    *            une exception est levée lors de l'exécution de la commande
    */
   public static Process launch(String command) throws IOException {

      Validate.notEmpty(command, "'executable' is required");

      Runtime runtime = Runtime.getRuntime();

      Process process = runtime.exec(command);

      // on trace ici les exceptions levées par le lancement du processus
      // par exemple : le commande est incomprise, les options d'exécution
      // n'existent pas, les droits sont insuffissants

      new InputStreamProcess(process).start();
      new ErrorStreamProcess(process).start();

      return process;
   }

   // on récupère les flux du processus
   // le flux de sortie de processus est fermé
   // le flux des erreurs du processus est lui affiché dans un LOG de niveau
   // ERROR
   // le code est récupéré et adapté de de
   // http://ydisanto.developpez.com/tutoriels/java/runtime-exec/
   private static class InputStreamProcess extends Thread {

      private final Process process;

      protected InputStreamProcess(Process process) {
         super();
         this.process = process;
      }

      /**
       * Fermeture du flux de sortie du processus de l'application externe
       */
      @Override
      public void run() {

         try {
            process.getInputStream().close();
         } catch (IOException e) {
            throw new NestableRuntimeException(e);
         }

      }
   }

   private static class ErrorStreamProcess extends Thread {

      private final Process process;

      protected ErrorStreamProcess(Process process) {
         super();
         this.process = process;
      }

      /**
       * Affichage du flux d'erreur du processus de l'application externe
       */
      @Override
      public void run() {
         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                  process.getErrorStream()));
            String line = "";
            try {
               // ce code permet d'éviter les boucles infinies!
               while ((line = reader.readLine()) != null) {

                  LOG.error(line);
               }
            } finally {
               reader.close();
            }
         } catch (IOException e) {
            throw new NestableRuntimeException(e);
         }
      }
   }

}
