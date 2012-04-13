package fr.urssaf.image.sae.webservices.service.support.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.MessageFormat;

import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.webservices.service.support.LauncherSupport;
import fr.urssaf.image.sae.webservices.service.support.exception.LauncherRuntimeException;

/**
 * Le traitement est ici un processus.<br>
 * Un processus est indépendant du programme qui la lancé.<br>
 * <br>
 * Ici les processus sont prévus pour des JAR exécutable ou de simple classe
 * JAVA exécutables<br>
 * <br>
 * ex : on exécute un JAR exécutable service-exe.jar avec arg1 et arg2 en
 * arguments d'entrée<br>
 * <br>
 * <code>java -jar /appl/etc/service-exe.jar arg1 arg 2</code> <br>
 * <br>
 * Si l'exécutable s'abonne à un serveur JMX en locale il est possible pour le
 * programme qui a lancé le processus de suivre le traitement et par la même
 * occassion que le traitement est en cours d'exécution.<br>
 * <br>
 * Pour que le programme puisse vérifier que le processus est en cours
 * d'exécution au moyen de JMX le processus doit contenir les options pour
 * s'abonner à un serveur JMX en local<br>
 * Les options sont :
 * <ul>
 * <li><code>-Dcom.sun.management.jmxremote</code></li>
 * <li><code>-Dcom.sun.management.jmxremote.port={port JMX}</code></li>
 * <li><code>-Dcom.sun.management.jmxremote.authenticate={true/false}</code></li>
 * <li><code>-Dcom.sun.management.jmxremote.ssl={true/false}</code></li>
 * </ul>
 * ex : même exemple que précédement mais on l'abonne à un serveur JMX en local
 * sur le port 9999 sans sécurité ni authentification.<br>
 * <br>
 * <code>java -jar -Dcom.sun.management.jmxremote service-ex.jar -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false /appl/etc/service-exe.jar arg1 arg 2</code>
 * <br>
 * <br>
 * Le programme est capable dans <u><b>ce cas</b></u> de vérifier que le
 * traitement est en cours d'exécution en essayant de se connecter au serveur
 * JMX en local sur le port 9999.
 * 
 * 
 * 
 * 
 */
public class ProcessusLauncherSupportImpl implements LauncherSupport {

   private static final Logger LOG = LoggerFactory
         .getLogger(ProcessusLauncherSupportImpl.class);

   private static final String JMX_PATTERN = "service:jmx:rmi:///jndi/rmi://:{0,number,#}/jmxrmi";

   private static final String SEPARATOR = " ";

   private final JMXServiceURL serviceURL;

   private final String executable;

   private static final String PREFIX_LOG = "archivageEnMasse()";

   /***
    * Il est important que le numéro du port JMX indiqué dans le paramètre
    * <code>jmxPort</code> soit identique à celui contenu dans l'exécutable
    * 
    * @param executable
    *           exécutable du processus
    * @param jmxPort
    *           port JMX pour vérifier que le traitement est en cours d'exéction
    */
   public ProcessusLauncherSupportImpl(String executable, int jmxPort) {

      Assert.notNull(executable, "'executable' is required");
      Assert.notNull(jmxPort, "'jmxPort' is required");

      this.executable = executable;

      String jmxURL = MessageFormat.format(JMX_PATTERN,
            new Object[] { jmxPort });

      try {
         this.serviceURL = new JMXServiceURL(jmxURL);
      } catch (MalformedURLException e) {
         throw new LauncherRuntimeException(
               "L'URL de connection au serveur JMX '" + jmxURL
                     + "' est incorrecte", e);
      }
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * La méthode tente de se connecter au serveur JMX à l'adresse
    * {@value #JMX_PATTERN} avec le port indiqué dans le constructeur<br>
    * <br>
    * Si une exception {@link IOException} alors cela signifie qu'un traitement
    * est en cours sur ce port
    * 
    */
   @Override
   public final boolean isLaunched() {

      boolean isLaunched = true;

      try {

         JMXConnectorFactory.connect(serviceURL, null);
         LOG.debug("{} - Un processus est en cours sur le connecteur JMX: {}",
               PREFIX_LOG, serviceURL);
      } catch (IOException e) {
         LOG
               .debug(
                     "{} - Aucun processus n'est en cours sur sur le connecteur JMX: {}",
                     PREFIX_LOG, serviceURL);
         isLaunched = false;
      }

      return isLaunched;
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * La méthode lance le processus en appelant la classe
    * {@link Runtime#exec(String)}<br>
    * <br>
    * La commande est composée du paramètre <code>executable</code> et de la
    * suite des <code>parameters</code> séparé par des espaces
    */
   @Override
   public final void launch(Object... parameters) {

      Runtime runtime = Runtime.getRuntime();
      // Ajout d'uuid
      String uuid = MDC.get("log_contexte_uuid");
      String executableWithUuid = StringUtils.replace(executable,
            "_UUID_TO_REPLACE", uuid);
      // TODO préférer un exécutable avec des paramètres {0},{1}...
      String command = StringUtils.join(new String[] { executableWithUuid,
            StringUtils.join(parameters, SEPARATOR) }, SEPARATOR);

      LOG.debug("{} - Lancement du processus: {}", PREFIX_LOG, command);
      try {

         final Process process = runtime.exec(command);

         // on trace ici les exceptions levées par le lancement du processus
         // par exemple : le commande est incomprise, les options d'exécution
         // n'existent pas, les droits sont insuffissants

         new InputStreamProcess(process).start();
         new ErrorStreamProcess(process).start();

      } catch (IOException e) {
         throw new LauncherRuntimeException("Le lancement du processus '"
               + command + "' a échoué", e);
      }
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

                  LOG.error("{} - {}", PREFIX_LOG, line);
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
