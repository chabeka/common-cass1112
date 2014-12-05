package fr.urssaf.image.sae.extraitdonnees.bootstrap;

import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.extraitdonnees.bean.CassandraConfig;
import fr.urssaf.image.sae.extraitdonnees.factory.SAEApplicationContextFactory;
import fr.urssaf.image.sae.extraitdonnees.service.ExtraitDonneesService;

/**
 * Classe de démarrage du JAR exécutable
 * 
 */
public final class BootStrap {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(BootStrap.class);

   private static final int CASS_DEFAULT_PORT = 9160;

   private static final String ARG_HELP = "help";
   private static final String ARG_VIRTUEL = "virtuel";
   private static final String ARG_NB_MAX_DOCS = "nbMaxDoc";
   private static final String ARG_CHEMIN_FIC_SORTIE = "fichier-sortie";
   private static final String ARG_CASS_SERVEURS = "cassandra-servers";
   private static final String ARG_CASS_PORT = "cassandra-port";
   private static final String ARG_CASS_LOGIN = "cassandra-login";
   private static final String ARG_CASS_PASSWORD = "cassandra-password";

   /**
    * Constructeur
    */
   private BootStrap() {
   }

   /**
    * Classe de démarrage de l'application
    * 
    * @param args
    *           liste des arguments
    * @throws ParseException
    *            lorsque la framework commons-cli ne parvient pas à parser la
    *            ligne de commande
    */
   public static void main(String[] args) throws ParseException {

      try {

         // Création du parser de la ligne de commande
         CommandLineParser parser = new GnuParser();

         // Préparation des options de la ligne de commande
         Options options = prepareOptions();

         // Parse la ligne de commande à l'aide du paramétrage des options
         // Le framework peut lever une exception ParseException
         CommandLine cmd = parser.parse(options, args);

         // Regarde si la demande n'est pas uniquement l'affichage du usage
         if (uniquementUsage(cmd, options)) {
            return;
         }

         // Traite le flag "document virtuel"
         boolean virtuel = lectureParametreDocumentVirtuel(cmd);

         // Traite le nombre max de documents à sortir
         int nbMaxDoc = lectureParametreNbMaxDoc(cmd);

         // Traite le fichier de sortie
         File fichierSortie = lectureParametreFichierSortie(cmd, options);
         if (fichierSortie == null) {
            return;
         }

         // Traite les paramètres Cassandra
         CassandraConfig cassandraConf = new CassandraConfig();
         boolean paramsCassOk = lectureParametresCassandra(cmd, options,
               cassandraConf);
         if (!paramsCassOk) {
            return;
         }

         // Trace les arguments de la ligne de commande
         traceParametres(cassandraConf, virtuel, nbMaxDoc, fichierSortie);

         // Création du contexte Spring
         ApplicationContext context = SAEApplicationContextFactory
               .load("/applicationContext-sae-extraitdonnees.xml");

         // Récupération du bean de service dans le conteneur Spring
         ExtraitDonneesService service = context
               .getBean(ExtraitDonneesService.class);

         // Exécution du service
         service.extraitUuid(fichierSortie, nbMaxDoc, virtuel, cassandraConf);

      } catch (Exception ex) {
         // intercepte toutes les exceptions pour sortir une trace
         LOGGER.error(ex.getMessage(), ex);
      }

   }

   protected static Options prepareOptions() {

      // Création des options
      Options options = new Options();

      // L'option d'aide
      OptionBuilder.withLongOpt(ARG_HELP);
      OptionBuilder.withDescription("affiche ce message");
      options.addOption(OptionBuilder.create());

      // Documents virtuels
      OptionBuilder.withLongOpt(ARG_VIRTUEL);
      OptionBuilder
            .withDescription("extrait des UUID de documents virtuels au lieu de documents non virtuels");
      options.addOption(OptionBuilder.create());

      // Nombre maximum de documents souhaités
      OptionBuilder.withLongOpt(ARG_NB_MAX_DOCS);
      OptionBuilder
            .withDescription("nombre maximum de documents souhaités dans le fichier de sortie");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("nombre");
      options.addOption(OptionBuilder.create());

      // Chemin complet du fichier de sortie
      OptionBuilder.withLongOpt(ARG_CHEMIN_FIC_SORTIE);
      OptionBuilder
            .withDescription("chemin complet du fichier de sortie dans lequel écrire les UUID des documents (nouveau fichier)");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("fichier");
      options.addOption(OptionBuilder.create());

      // Cassandra: adresse des serveurs
      OptionBuilder.withLongOpt(ARG_CASS_SERVEURS);
      OptionBuilder
            .withDescription("adresses des serveurs Cassandra (les séparer par une virgule). Exemple: serveur1,serveur2");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("serveurs");
      options.addOption(OptionBuilder.create());

      // Cassandra: numéro de port
      OptionBuilder.withLongOpt(ARG_CASS_PORT);
      OptionBuilder
            .withDescription("port du service Cassandra (valeur par défaut: 9160)");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("port");
      options.addOption(OptionBuilder.create());

      // Cassandra: login de connexion
      OptionBuilder.withLongOpt(ARG_CASS_LOGIN);
      OptionBuilder.withDescription("login de connexion à Cassandra");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("login");
      options.addOption(OptionBuilder.create());

      // Cassandra: login de connexion
      OptionBuilder.withLongOpt(ARG_CASS_PASSWORD);
      OptionBuilder.withDescription("password de connexion à Cassandra");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("password");
      options.addOption(OptionBuilder.create());

      // Renvoie du résultat
      return options;

   }

   private static void printUsage(Options options) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("sae-extraitdonnees.jar", options);
   }

   private static void printErreurLigneCommande(Options options, String erreur) {
      PrintWriter printWriter = new PrintWriter(System.out);
      printWriter.println(erreur);
      printWriter.println();
      printWriter.flush();
      printUsage(options);
   }

   private static boolean lectureParametresCassandra(CommandLine cmd,
         Options options, CassandraConfig cassandraConf) {

      // Traite la liste des serveurs Cassandra
      if (!cmd.hasOption(ARG_CASS_SERVEURS)) {
         printErreurLigneCommande(
               options,
               String
                     .format(
                           "Erreur: l'argument obligatoire %s est absent de la ligne de commande",
                           ARG_CASS_SERVEURS));
         return false;
      }
      cassandraConf.setServers(cmd.getOptionValue(ARG_CASS_SERVEURS));

      // Traite le numéro de port de connexion à Cassandra
      if (cmd.hasOption(ARG_CASS_PORT)) {
         cassandraConf.setPort(Integer.parseInt(cmd
               .getOptionValue(ARG_CASS_PORT)));
      } else {
         cassandraConf.setPort(CASS_DEFAULT_PORT);
      }

      // Traite le login Cassandra
      cassandraConf.setUser(StringUtils.trimToEmpty(cmd
            .getOptionValue(ARG_CASS_LOGIN)));

      // Traite le password Cassandra
      cassandraConf.setPassword(StringUtils.trimToEmpty(cmd
            .getOptionValue(ARG_CASS_PASSWORD)));

      // Renvoie true si on arrive jusque là
      return true;

   }

   private static boolean uniquementUsage(CommandLine cmd, Options options) {

      // Si aucun argument dans la ligne de commande
      // Ou Si uniquement --help
      // On affiche le usage, et on quitte le programme

      if ((cmd.getOptions().length == 0)
            || ((cmd.getOptions().length == 1) && (cmd.hasOption(ARG_HELP)))) {
         printUsage(options);
         return true;
      } else {
         return false;
      }

   }

   private static int lectureParametreNbMaxDoc(CommandLine cmd) {

      int nbMaxDoc;

      if (cmd.hasOption(ARG_NB_MAX_DOCS)) {
         nbMaxDoc = Integer.parseInt(cmd.getOptionValue(ARG_NB_MAX_DOCS));
      } else {
         nbMaxDoc = Integer.MAX_VALUE;
      }

      return nbMaxDoc;

   }

   private static boolean lectureParametreDocumentVirtuel(CommandLine cmd) {

      return cmd.hasOption(ARG_VIRTUEL);

   }

   private static File lectureParametreFichierSortie(CommandLine cmd,
         Options options) {

      // Vérifie que l'option a été renseignée
      if (!cmd.hasOption(ARG_CHEMIN_FIC_SORTIE)) {
         printErreurLigneCommande(
               options,
               String
                     .format(
                           "Erreur: l'argument obligatoire %s est absent de la ligne de commande",
                           ARG_CHEMIN_FIC_SORTIE));
         return null;
      }
      // Lit l'option et la tranforme en objet File
      String cheminFichierSortie = cmd.getOptionValue(ARG_CHEMIN_FIC_SORTIE);
      File fichierSortie = new File(cheminFichierSortie);
      // Vérifie que le fichier n'existe pas déjà
      if (fichierSortie.exists()) {
         printErreurLigneCommande(
               options,
               "Erreur: Le fichier de sortie existe, il faut obligatoire spécifier un nouveau fichier");
         return null;
      }

      // Renvoie l'objet File
      return fichierSortie;

   }

   private static void traceParametres(CassandraConfig cassandraConf,
         boolean virtuel, int nbMaxDoc, File fichierSortie) {

      LOGGER
            .info(
                  "Paramètres de lancement du programme : [Cassandra-Serveurs={}] ; [Cassandra-Port={}] ; [Cassandra-Login={}] ; [DocumentsVirtuels={}] ; [NbMaxDoc={}] ; [FichierDeSortie={}]",
                  new Object[] {
                        cassandraConf.getServers(),
                        cassandraConf.getPort(),
                        cassandraConf.getUser(),
                        virtuel ? "Oui" : "Non",
                        nbMaxDoc == Integer.MAX_VALUE ? "Pas de limite"
                              : nbMaxDoc, fichierSortie.getAbsolutePath() });

   }

}
