/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.jeux.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

import fr.urssaf.image.sae.regionalisation.jeux.test.service.SommaireService;
import fr.urssaf.image.sae.regionalisation.jeux.test.service.SommaireService.MODES;

/**
 * 
 * 
 */
public class BootStrap {

   /**
    * Classe de lancement
    * 
    * @param args
    *           : arguments
    * @throws IOException
    */
   public static void main(String[] args) throws IOException {

      if (args == null || args.length != 7) {
         throw new RuntimeException("Le nombre d'arguments est incorrect");
      }

      File conf = new File(args[0]);
      if (!conf.exists() || !conf.isFile()) {
         throw new RuntimeException(
               "le fichier de configuration de connexion à la base postgres est incorrect (argument 1)");
      }

      File sommaire = new File(args[1]);
      if (sommaire.exists()) {
         throw new RuntimeException(
               "le fichier sommaire spécifié existe déjà (argument 2)");
      }
      
      File donnees = new File(args[2]);
      if (donnees.exists()) {
         throw new RuntimeException(
               "le fichier de données spécifié existe déjà (argument 3)");
      }

      int nbDocs;
      try {
         nbDocs = Integer.parseInt(args[3]);
      } catch (NumberFormatException e) {
         throw new RuntimeException(
               "Le nombre de documents à créer (argument 4) est incorrect");
      }

      int nbATraiter;
      try {
         nbATraiter = Integer.parseInt(args[4]);
      } catch (NumberFormatException e) {
         throw new RuntimeException(
               "Le nombre de documents à intégrer pour modification (argument 5) est incorrect");
      }

      int nbNonTraite;
      try {
         nbNonTraite = Integer.parseInt(args[5]);
      } catch (NumberFormatException e) {
         throw new RuntimeException(
               "Le nombre de documents à intégrer et inexistants (argument 6) est incorrect");
      }

      if (nbDocs < nbATraiter + nbNonTraite) {
         throw new RuntimeException(
               "Total de documents intégrés inférieur à la somme des documents à traiter et pas à traiter");
      }

      List<MODES> list = Arrays.asList(MODES.values());

      if (!list.contains(MODES.valueOf(args[6]))) {
         throw new RuntimeException(
               "Le fichier de génération demandé n'existe pas (argument 7)");
      }

      File propertyFile = new File(args[0]);

      FileInputStream inStream = null;
      BasicDataSource dataSource = null;
      try {
         inStream = new FileInputStream(propertyFile);
         Properties properties = new Properties();
         properties.load(inStream);

         dataSource = new BasicDataSource();
         dataSource.setDriverClassName(properties
               .getProperty("postgresql.driver"));
         dataSource.setUrl(properties.getProperty("postgresql.url"));
         dataSource.setUsername(properties.getProperty("postgresql.userName"));
         dataSource.setPassword(properties.getProperty("postgresql.pwd"));

         Connection connection = dataSource.getConnection();
         SommaireService service = new SommaireService(connection);

         service.genereSommaireMonoPdf(nbDocs, nbATraiter, nbNonTraite,
               sommaire.getAbsolutePath(), "doc1.PDF",
               "a2f93f1f121ebba0faef2c0596f2f126eacae77b", "TEST PERF", null,
               true, "SaeIntegration", false, "2012-09-04", MODES.COTI_CLE
                     .toString(), donnees);

      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);

      } catch (SQLException e) {
         throw new RuntimeException(e);
      } finally {
         if (inStream != null) {
            try {
               inStream.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }

         if (dataSource != null) {
            try {
               dataSource.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
      }

   }
}
