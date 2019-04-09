package fr.urssaf.image.sae.indexcounterupdater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Point d'entrée de l'application.
 * Ce job permet de fiabiliser les compteurs d'index gérés par DFCE, en attendant la correction du jira suivant : https://docubase.atlassian.net/projects/CRTL/issues/CRTL-185
 * Le principe du job est le suivant :
 * - le job est s'exécute sur une durée paramétrable
 * - sachant qu'il ne sera pas possible de traiter tous les ranges de tous les index tous les jours : on traite les index et les ranges dans un ordre aléatoire
 * - pour chaque range choisi :
 * -- on le parcourir intégralement. On compte le nombre total et le nombre distincts d'éléments pour ce range
 * -- on met à jour sa propriété COUNT dans index_reference
 * -- on met à jour la propriété total_use_count en conséquence
 * -- on met à jour distinct_use_count en extrapolant par rapport aux ranges parcourus pour cet index
 */
public class Main {

   private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

   private static String cassandraServers;

   private static String cassandraUsername;

   private static String cassandraPassword;

   private static String dfceBaseName;

   /**
    * Point d'entrée de l'application
    */
   public static void main(final String[] args) {

      try {
         // create the command line parser
         final CommandLineParser parser = new DefaultParser();

         // create the Options
         final Options options = new Options();
         options.addOption("m",
                           "max-execution-time",
                           true,
                           "Durée maximale d'exécution du job, en secondes");
         options.addOption("f",
                           "sae-conf-file",
                           true,
                           "Localisation du fichier de configuration du SAE. Permet de récupérer un certains nombre d'informations : serveurs, login et password cassandra, ainsi que le nom de la base DFCE");
         options.addOption("c",
                           "cassandra-servers",
                           true,
                           "Liste des serveurs cassandra à joindre, séparés par des virgules");
         options.addOption("u",
                           "cassandra-username",
                           true,
                           "(optionnel) User cassandra à utiliser. Si non spécifié, le login par défaut est utilisé.");
         options.addOption("p",
                           "cassandra-password",
                           true,
                           "(optionnel) Password cassandra à utiliser. Si non spécifié, le mot de passe par défaut est utilisé.");
         options.addOption("b",
                           "dfce-base-name",
                           true,
                           "(optionnel) Nom de la base DFCE. Si non spécifié, le script tente de le déterminer tout seul.");
         options.addOption("l",
                           "cassandra-local-dc",
                           true,
                           "Datacenter local cassandra");
         options.addOption("s",
                           "simulation-mode",
                           false,
                           "Active le mode simulation : ne met pas à jour la table index_reference");

         // parse the command line arguments
         CommandLine line;
         try {
            line = parser.parse(options, args);
         }
         catch (final UnrecognizedOptionException e) {
            printUsage(options, e.getMessage());
            return;
         }

         // On vérifie qu'on a toutes les options nécessaires
         if ((!line.hasOption("max-execution-time"))) {
            printUsage(options, "--max-execution-time est obligatoire");
            return;
         }
         if ((!line.hasOption("cassandra-local-dc"))) {
            printUsage(options, "--cassandra-local-dc est obligatoire");
            return;
         }
         if ((!line.hasOption("sae-conf-file"))) {

            if ((!line.hasOption("cassandra-servers"))) {
               printUsage(options, "Il faut spécifier soit le paramètre sae-conf-file, soit les paramètres d'accès à cassandra");
               return;
            }
         }

         // Lecture des paramètres
         final int maxExecutionTime = Integer.parseInt(line.getOptionValue("max-execution-time"));
         final String cassandraLocalDC = line.getOptionValue("cassandra-local-dc");
         final boolean simulationMode = line.hasOption("simulation-mode");

         if ((line.hasOption("sae-conf-file"))) {
            final String cheminConfSae = line.getOptionValue("sae-conf-file");
            readSaeConfFile(cheminConfSae);
         } else {
            readOptions(line);
         }

         // Lancement du job
         final IndexCounterUpdater updater = new IndexCounterUpdater(cassandraServers,
                                                                     cassandraUsername,
                                                                     cassandraPassword,
                                                                     cassandraLocalDC,
                                                                     dfceBaseName,
                                                                     maxExecutionTime,
                                                                     simulationMode);
         updater.start();
      }
      catch (final Exception e) {
         LOGGER.error("Exception reçue : ", e);
      }
   }

   /**
    * Lecture des options sur la ligne de commande, sans passer par le fichier de conf du SAE
    * 
    * @param line
    *           la ligne de commande
    */
   private static void readOptions(final CommandLine line) {
      cassandraServers = line.getOptionValue("cassandra-servers");
      cassandraUsername = line.getOptionValue("cassandra-username", "root");
      cassandraPassword = line.getOptionValue("cassandra-password", "regina4932");
      dfceBaseName = line.getOptionValue("dfce-base-name", "");
   }

   private static void readSaeConfFile(final String cheminConfSae) {

      // récupère la config principale
      final File file = new File(cheminConfSae);
      final Properties properties = new Properties();
      try {
         properties.load(new FileInputStream(file));
      }
      catch (final FileNotFoundException e) {
         LOGGER.error(
                      "Le fichier de conf {} est introuvable ou n'est pas un fichier",
                      cheminConfSae);
         System.exit(1);
      }
      catch (final IOException e) {
         LOGGER.error("Erreur de lecture du fichier de conf {}",
                      cheminConfSae);
         System.exit(1);
      }

      // récupère la conf cassandra
      final String cheminFichierConfCassandra = (String) properties
                                                                   .get("sae.cassandra.cheminFichierConfig");
      final File fileCassandra = new File(cheminFichierConfCassandra);
      final Properties propertiesCassandra = new Properties();
      try {
         propertiesCassandra.load(new FileInputStream(fileCassandra));
      }
      catch (final FileNotFoundException e) {
         LOGGER.error(
                      "Le fichier de conf cassandra {} est introuvable ou n'est pas un fichier",
                      cheminFichierConfCassandra);
         System.exit(1);
      }
      catch (final IOException e) {
         LOGGER.error("Erreur de lecture du fichier de conf {}",
                      cheminFichierConfCassandra);
         System.exit(1);
      }
      cassandraServers = (String) propertiesCassandra.get("cassandra.hosts");
      cassandraUsername = (String) propertiesCassandra.get("cassandra.username");
      cassandraPassword = (String) propertiesCassandra.get("cassandra.password");

      // récupère la conf dfce
      final String cheminFichierConfDfce = (String) properties
                                                              .get("sae.dfce.cheminFichierConfig");
      final File fileDfce = new File(cheminFichierConfDfce);
      final Properties propertiesDfce = new Properties();
      try {
         propertiesDfce.load(new FileInputStream(fileDfce));
      }
      catch (final FileNotFoundException e) {
         LOGGER.error(
                      "Le fichier de conf DFCe {} est introuvable ou n'est pas un fichier",
                      cheminFichierConfDfce);
         System.exit(1);
      }
      catch (final IOException e) {
         LOGGER.error("Erreur de lecture du fichier de conf {}",
                      cheminFichierConfDfce);
         System.exit(1);
      }
      dfceBaseName = (String) propertiesDfce.get("db.baseName");
   }

   private static void printUsage(final Options options, final String message) {
      final HelpFormatter formater = new HelpFormatter();

      final StringBuilder header = new StringBuilder();
      header.append("\nUtilitaire permettant de consolider les compteurs d'index DFCE\n");

      final String footer = StringUtils.isEmpty(message) ? "" : "\n" + message;
      formater.printHelp("java max-execution-time MAX_TIME --cassandra-local-dc DC [--sae-conf-file SAE_CONF_FILE | --cassandra-servers \"SERVER1,SERVER2,..\"]",
                         header.toString(),
                         options,
                         footer);
   }

}
