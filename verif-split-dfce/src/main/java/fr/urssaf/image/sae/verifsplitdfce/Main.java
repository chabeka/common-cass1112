package fr.urssaf.image.sae.verifsplitdfce;

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
import org.apache.commons.cli.ParseException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
   
   private final static Logger LOGGER = LoggerFactory.getLogger(Main.class); 
   
   private static String hosts;
   
   private static String username;
   
   private static String password;
   
   private static String baseName;
   
   public static void main(String[] args) {
      
      // create the command line parser
      CommandLineParser parser = new DefaultParser();
      
      // create the Options
      Options options = new Options();
      options.addOption("c", "check", false, "Permet de faire une verification simple du split");
      options.addOption("v", "verify", false, "Permet de faire une verification approfondie du split");
      
      try {
         // parse the command line arguments
         CommandLine line = parser.parse(options, args);
         
         if ((!line.hasOption("check") && !line.hasOption("verify"))
               || (line.hasOption("check") && line.hasOption("verify"))) {
            printUsage(options);
            return;
         }
         
         String nomIndex;
         String cheminConfSae;
         
         if (line.getArgList().size() != 2) {
            printUsage(options);
            return;
         } else {
            nomIndex = line.getArgList().get(0);
            cheminConfSae = line.getArgList().get(1);
            
            // verifie l'existance du fichier de conf
            File file = new File(cheminConfSae);
            if (!file.exists() || !file.isFile()) {
               LOGGER.error("Le fichier de conf {} est introuvable ou n'est pas un fichier", cheminConfSae);
               return;
            }
            
            // recupere la config principale
            Properties properties = new Properties();
            try {
               properties.load(new FileInputStream(file));
            } catch (FileNotFoundException e) {
               LOGGER.error("Le fichier de conf {} est introuvable ou n'est pas un fichier", cheminConfSae);
               return;
            } catch (IOException e) {
               LOGGER.error("Erreur de lecture du fichier de conf {}", cheminConfSae);
               return;
            }
            
            String cheminFichierConfCassandra = (String) properties.get("sae.cassandra.cheminFichierConfig");
            File fileCassandra = new File(cheminFichierConfCassandra);
            if (!fileCassandra.exists() || !fileCassandra.isFile()) {
               LOGGER.error("Le fichier de conf cassandra {} est introuvable ou n'est pas un fichier", cheminFichierConfCassandra);
               return;
            }
            
            // recupere la conf cassandra
            Properties propertiesCassandra = new Properties();
            try {
               propertiesCassandra.load(new FileInputStream(fileCassandra));
            } catch (FileNotFoundException e) {
               LOGGER.error("Le fichier de conf cassandra {} est introuvable ou n'est pas un fichier", cheminFichierConfCassandra);
               return;
            } catch (IOException e) {
               LOGGER.error("Erreur de lecture du fichier de conf {}", cheminFichierConfCassandra);
               return;
            }
            hosts = (String) propertiesCassandra.get("cassandra.hosts");
            username = (String) propertiesCassandra.get("cassandra.username");
            password = (String) propertiesCassandra.get("cassandra.password");
            
            String cheminFichierConfDfce = (String) properties.get("sae.dfce.cheminFichierConfig");
            File fileDfce = new File(cheminFichierConfDfce);
            if (!fileDfce.exists() || !fileDfce.isFile()) {
               LOGGER.error("Le fichier de conf DFCe {} est introuvable ou n'est pas un fichier", cheminFichierConfDfce);
               return;
            }
            
            // recupere la conf dfce
            Properties propertiesDfce = new Properties();
            try {
               propertiesDfce.load(new FileInputStream(fileDfce));
            } catch (FileNotFoundException e) {
               LOGGER.error("Le fichier de conf DFCe {} est introuvable ou n'est pas un fichier", cheminFichierConfDfce);
               return;
            } catch (IOException e) {
               LOGGER.error("Erreur de lecture du fichier de conf {}", cheminFichierConfDfce);
               return;
            }
            baseName = (String) propertiesDfce.get("db.baseName");
         }
         
         DocubaseDao dao = new DocubaseDao(hosts, username, password, baseName);
         
         if (!dao.verifExistanceIndex(nomIndex)) {
            LOGGER.error("L'index {} n'existe pas pour la base {}", nomIndex, baseName);
            return;
         }
         
         if (line.hasOption("check")) {
            try {
               dao.checkIndexInIndexReference(nomIndex);
            } catch (JSONException e) {
               LOGGER.error("Une erreur s'est produite lors de la consultation du json : {}", e.getMessage());
               return;
            }
         } else if (line.hasOption("verify")) {
            dao.verifRangesInTermInfoRange(nomIndex);
         } 

     }
     catch(ParseException exp) {
        LOGGER.error("Unexpected exception: {}", exp.getMessage());
     }
   }
   
   private static void printUsage(Options options) {
      HelpFormatter formater = new HelpFormatter();
      
      StringBuffer footer = new StringBuffer();
      footer.append("\n L'option check ou verify ne peuvent pas être mise ensemble\n");
      footer.append(" Vous devez cependant en mettre une des deux\n\n");
      footer.append(" NOM_INDEX     Doit contenir le nom de l'index a verifier\n");
      footer.append(" PATH_CONF     Doit contenir le chemin du fichier de conf du sae\n");
      
      formater.printHelp("java [-c] [-v] NOM_INDEX PATH_CONF", "", options, footer.toString());
   }
   
   
}
