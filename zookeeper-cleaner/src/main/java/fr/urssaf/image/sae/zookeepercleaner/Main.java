package fr.urssaf.image.sae.zookeepercleaner;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.recipes.locks.ChildReaper;
import org.apache.curator.framework.recipes.locks.Reaper.Mode;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Point d'entrée de l'application.
 */
public class Main {

   private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

   private static final int BASE_SLEEP_TIME = 100; // En milli-secondes

   private static final int MAX_RETRIES = 10;

   /**
    * Point d'entrée de l'application
    * 
    * @param path
    */
   public static void main(final String[] args) {

      try {
         // create the command line parser
         final CommandLineParser parser = new DefaultParser();

         // create the Options
         final Options options = new Options();
         options.addOption("z",
               "zookeeper-server",
               true,
               "(optionnel) nom du serveur zookeeper à contacter");
         options.addOption("p",
               "path",
               true,
               "(optionnel, plusieurs valeurs possibles) chemins zookeeper (par défaut : Transfert et JobRequest)");
         options.addOption("m",
               "max-execution-time",
               true,
               "durée d'exécution, en secondes");
         options.addOption("c",
               "count-only",
               false,
               "pour afficher le nombre de noeuds actuels, sans lancer le nettoyage");

         // parse the command line arguments
         CommandLine line;
         try {
            line = parser.parse(options, args);
         }
         catch (final UnrecognizedOptionException e) {
            printUsage(options, e.getMessage());
            return;
         }

         final String zookeeperServer = line.getOptionValue("zookeeper-server", "localhost");
         String[] zookeeperPaths = line.getOptionValues("path");
         if (zookeeperPaths == null) {
            zookeeperPaths = new String[] {"Transfert", "JobRequest"};
         }

         if (line.hasOption("count-only")) {
            countNodes(zookeeperServer, zookeeperPaths);
            return;
         }

         // On vérifie qu'on a toutes les options nécessaires
         if (!line.hasOption("max-execution-time")) {
            printUsage(options, "--max-execution-time est obligatoire");
            return;
         }
         final long maxExecutionTime = Long.parseLong(line.getOptionValue("max-execution-time"));

         // Lancement du job
         startCleaning(zookeeperServer, maxExecutionTime, zookeeperPaths);
      }
      catch (final Exception e) {
         LOGGER.error("Exception reçue : ", e);
      }
   }



   /**
    * @param zookeeperServer
    * @param maxExecutionTime
    * @throws Exception
    */
   public static void startCleaning(final String zookeeperServer, long maxExecutionTime, final String[] paths) throws Exception {
      final String connectionString = zookeeperServer + ":2181";
      final CuratorFramework zkClient = getClient(connectionString, "SAE");
      final ScheduledExecutorService executor = ChildReaper.newExecutorService();
      final ChildReaper childReaper = new ChildReaper(zkClient, "/" + paths[0], Mode.REAP_INDEFINITELY, executor, 10000);

      for (int i = 1; i < paths.length; i++) {
         childReaper.addPath("/" + paths[i]);
      }

      childReaper.start();
      if (maxExecutionTime == 0) {
         maxExecutionTime = Long.MAX_VALUE;
      }
      executor.awaitTermination(maxExecutionTime, TimeUnit.SECONDS);
      zkClient.close();
   }

   /**
    * @param zookeeperServer
    * @throws Exception
    */
   public static void countNodes(final String zookeeperServer, final String[] paths) throws Exception {
      final String connectionString = zookeeperServer + ":2181";
      final CuratorFramework zkClient = getClient(connectionString, "SAE");
      for (int i = 0; i < paths.length; i++) {
         final String path = "/" + paths[i];
         try {
            final List<String> children = zkClient.getChildren().forPath(path);
            LOGGER.info("Nombre d'enfants pour {} : {}", path, children.size());
         }
         catch (final KeeperException e) {
            if (e.code() == Code.NONODE) {
               LOGGER.info("Le noeud " + path + " n'existe pas");
            } else {
               LOGGER.warn("Exception pour le chemin " + path, e);
            }
         }
      }
      zkClient.close();
   }

   /**
    * Renvoie une connexion à zookeeper. Attention, il est de la responsabilité
    * de l'appelant de détruire l'objet (appel de la méthode close) lorsque
    * celui-ci n'est plus utilisé.
    * 
    * @param connexionString
    *           correspond à la chaîne de connexion vers le serveur zookeeper
    * @param namespace
    *           namespace de l'application
    * @return connexion
    * @throws IOException
    *            quand on n'arrive pas à joindre zookeeper
    */
   public static CuratorFramework getClient(final String connexionString,
         final String namespace) {
      final Builder builder = CuratorFrameworkFactory.builder();
      builder.connectString(connexionString).namespace(namespace);
      builder.retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIME,
            MAX_RETRIES));
      final CuratorFramework zkClient = builder.build();

      zkClient.getConnectionStateListenable()
      .addListener(
            new ConnectionStateListener() {

               @Override
               public void stateChanged(final CuratorFramework client,
                     final ConnectionState newState) {

                  LOGGER.debug("Etat connexion: {}", newState.toString());

               }
            });

      zkClient.start();

      return zkClient;

   }

   private static void printUsage(final Options options, final String message) {
      final HelpFormatter formater = new HelpFormatter();

      final StringBuilder header = new StringBuilder();
      header.append("\nUtilitaire permettant de nettoyer les données de zookeeper\n");

      final String footer = StringUtils.isEmpty(message) ? "" : "\n" + message;
      formater.printHelp("java [--count-only] [--path PATH ...]--max-execution-time MAX_TIME [--zookeeper-server ZOOKEEPER_SERVER]",
            header.toString(),
            options,
            footer);
   }

}
