package fr.urssaf.image.sae.regionalisation.jeux.test.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import org.apache.commons.io.output.FileWriterWithEncoding;

import fr.urssaf.image.sae.regionalisation.jeux.test.service.ecriture.CotiCleSommaireWriter;

public final class SommaireService {

   private Connection connection;

   public enum MODES {
      COTI_CLE
   };

   private static final String SQL_INSERT_CRITERES = "INSERT INTO CRITERES (LUCENE, TRAITE) values (?, false)";
   private static final String SQL_INSERT_COTI_CLE = "INSERT INTO METADONNEES (id_critere, nce, cog) values (?, ?, ?)";
   private static final DecimalFormat FORMAT_COG = new DecimalFormat("000");
   private static final DecimalFormat FORMAT_NCE = new DecimalFormat(
         "000000000000000000");

   public SommaireService(Connection connection) {
      this.connection = connection;
   }

   /**
    * génération du fichier sommaire.xml, des enregistrements associés en base
    * et du fichier de traitement
    * 
    * @param nbDocs
    *           nombre de documents à insérer dans le fichier sommaire.xml
    * @param nbATraiter
    *           nombre de documents à traiter (fichier + bdd)
    * @param nbNonTraite
    *           nombre de documents inexistants à insérer (fichier + bdd)
    * @param cheminEcritureFichierSommaire
    *           chemin complet vers le fichier sommaire.xml
    * @param objNumCheminEtNomFichier
    *           chemin du PDF
    * @param hash
    *           hash du PDF
    * @param denomination
    *           dénomination à utiliser dans le fichier sommaire.xml
    * @param siren
    *           siren à utiliser dans le fichier sommaire.xml
    * @param sirenAleatoire
    *           indicateur d'insertion d'un siren aléatoire
    * @param applicationTraitement
    *           application de traitement
    * @param avecNumeroRecours
    *           indicateur utilisation numéro de recours
    * @param dateCreation
    *           date de création
    * @param mode
    *           type à utiliser comme modèle : COTI_CLE, etc.
    * @param donnees
    *           fichier de données généré en résultat
    */
   public void genereSommaireMonoPdf(int nbDocs, int nbATraiter,
         int nbNonTraite, String cheminEcritureFichierSommaire,
         String objNumCheminEtNomFichier, String hash, String denomination,
         String siren, boolean sirenAleatoire, String applicationTraitement,
         boolean avecNumeroRecours, String dateCreation, String mode,
         File donnees) {

      FileWriterWithEncoding writer = null;
      File file = null;
      FileWriterWithEncoding donneesWriter = null;
      try {
         connection.setAutoCommit(false);

         file = new File(cheminEcritureFichierSommaire);
         writer = new FileWriterWithEncoding(file, "UTF-8", false);
         donneesWriter = new FileWriterWithEncoding(donnees, "UTF-8", false);

         ecritureSommaire_EnTete(writer);

         if (mode.equals(MODES.COTI_CLE.toString())) {
            createAndSaveCotiCle(writer, objNumCheminEtNomFichier, nbDocs,
                  hash, denomination, siren, sirenAleatoire,
                  applicationTraitement, avecNumeroRecours, dateCreation,
                  nbATraiter, nbNonTraite, donneesWriter);
         }

         ecritureSommaire_Pied(writer);

         connection.commit();

      } catch (SQLException e) {
         e.printStackTrace();

      } catch (IOException e) {
         e.printStackTrace();

      } finally {

         if (donneesWriter != null) {
            try {
               donneesWriter.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }

         if (writer != null) {
            try {
               writer.close();
            } catch (Exception e) {
               e.printStackTrace();
            }
         }

      }

   }

   private void createAndSaveCotiCle(FileWriterWithEncoding writer,
         String objNumCheminEtNomFichier, int nbDocs, String hash,
         String denomination, String siren, boolean sirenAleatoire,
         String applicationTraitement, boolean avecNumeroRecours,
         String dateCreation, int nbATraiter, int nbNonTraite,
         FileWriterWithEncoding donneesWriter) throws IOException, SQLException {

      String nce, cog;

      PreparedStatement stmtCreateCriteres = connection
            .prepareStatement(SQL_INSERT_CRITERES);
      PreparedStatement stmtCreateMetas = connection
            .prepareStatement(SQL_INSERT_COTI_CLE);

      // Pour chaque document
      for (int i = 1; i <= nbDocs; i++) {

         cog = calculRand(0, 999, FORMAT_COG);
         nce = calculRand(0L, 999999999999999999L, FORMAT_NCE);

         CotiCleSommaireWriter
               .ecrireNoeud(writer, objNumCheminEtNomFichier, hash,
                     denomination, siren, sirenAleatoire, i,
                     applicationTraitement, avecNumeroRecours, dateCreation,
                     nce, cog);

         if (i <= nbATraiter) {
            createRecord(nce, cog, stmtCreateCriteres, stmtCreateMetas,
                  donneesWriter);
         } else if (i <= nbATraiter + nbNonTraite) {
            cog = calculRand(0, 999, FORMAT_COG);
            nce = calculRand(0L, 999999999999999999L, FORMAT_NCE);
            createRecord(nce, cog, stmtCreateCriteres, stmtCreateMetas,
                  donneesWriter);
         }

         if (i % 200 == 0) {
            System.out.println("document " + i);
         }
      }

   }

   private void createRecord(String nce, String cog,
         PreparedStatement stmtCreateCriteres,
         PreparedStatement stmtCreateMetas, FileWriterWithEncoding writer)
         throws SQLException, IOException {

      String lucene = "nce:" + nce + " AND cog:" + cog;
      stmtCreateCriteres.setString(1, lucene);
      stmtCreateCriteres.executeUpdate();

      Statement statement = connection.createStatement();

      cog = calculRand(0, 999, FORMAT_COG);
      nce = calculRand(0L, 999999999999999999L, FORMAT_NCE);

      ResultSet rs = statement.executeQuery("SELECT LASTVAL()");
      if (rs.next()) {
         long id = rs.getLong(1);
         stmtCreateMetas.setLong(1, id);
         stmtCreateMetas.setString(2, nce);
         stmtCreateMetas.setString(3, cog);

         stmtCreateMetas.executeUpdate();

      } else {
         throw new SQLException("Impossible de récupérer l'identifiant");
      }

      writer.write(lucene);
      writer.write(";nce;" + nce);
      writer.write(";cog;" + cog + "\n");

   }

   /**
    * @param i
    * @param j
    * @param formatCog
    * @return
    */
   private String calculRand(long lower, long higher, DecimalFormat formatCog) {

      long random = (long) (Math.random() * (higher + 1 - lower)) + lower;
      return formatCog.format(random);
   }

   private void ecritureSommaire_EnTete(FileWriterWithEncoding writer)
         throws IOException {

      writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      writer.write("\r\n");
      writer.write("<som:sommaire");
      writer.write("\r\n");
      writer.write("   xmlns:som=\"http://www.cirtil.fr/sae/sommaireXml\"");
      writer.write("\r\n");
      writer
            .write("   xmlns:somres=\"http://www.cirtil.fr/sae/commun_sommaire_et_resultat\"");
      writer.write("\r\n");
      writer
            .write("   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
      writer.write("\r\n");
      writer.write("   <som:batchMode>TOUT_OU_RIEN</som:batchMode>");
      writer.write("\r\n");
      writer.write("   <som:documents>");
      writer.write("\r\n");

   }

   private void ecritureSommaire_Pied(FileWriterWithEncoding writer)
         throws IOException {

      writer.write("   </som:documents>");
      writer.write("\r\n");
      writer.write("   <som:documentsVirtuels />");
      writer.write("\r\n");
      writer.write("</som:sommaire>");
      writer.write("\r\n");

   }
}