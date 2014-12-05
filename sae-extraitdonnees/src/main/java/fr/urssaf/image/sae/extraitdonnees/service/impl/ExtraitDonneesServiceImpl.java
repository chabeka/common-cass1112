package fr.urssaf.image.sae.extraitdonnees.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.extraitdonnees.bean.CassandraConfig;
import fr.urssaf.image.sae.extraitdonnees.bean.Compteurs;
import fr.urssaf.image.sae.extraitdonnees.dao.BasesReferenceDao;
import fr.urssaf.image.sae.extraitdonnees.dao.DocInfoDao;
import fr.urssaf.image.sae.extraitdonnees.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.extraitdonnees.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.extraitdonnees.iterator.CassandraIterator;
import fr.urssaf.image.sae.extraitdonnees.service.ExtraitDonneesService;
import fr.urssaf.image.sae.extraitdonnees.support.CassandraSupport;

/**
 * Implémentation du service {@link ExtraitDonneesService}
 */
@Service
public final class ExtraitDonneesServiceImpl implements ExtraitDonneesService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ExtraitDonneesServiceImpl.class);

   private static final int NB_DOC_POUR_PAS_LOG = 10000;

   @Autowired
   private CassandraSupport cassandraSupport;

   @Autowired
   private DocInfoDao infoDao;

   @Autowired
   private BasesReferenceDao basesReferenceDao;

   /**
    * {@inheritDoc}
    */
   @Override
   public void extraitUuid(File fichierSortie, int nbDocsSouhaites,
         boolean isVirtuel, CassandraConfig cassandraConfig) {

      // Trace
      traceParametresService(fichierSortie, nbDocsSouhaites, isVirtuel,
            cassandraConfig);

      // Connexion à Cassandra
      cassandraSupport.connect(cassandraConfig);
      try {

         // Récupère le nom de la base DFCE sur laquelle travailler
         String nomBaseGed = determineNomBaseGed();

         // Ouverture du fichier de sortie
         OutputStream outStream = new FileOutputStream(fichierSortie);
         try {

            // Construction de l'itérateur Cassandra
            AllRowsQuery<DocInfoKey, String> query = infoDao.getQuery(
                  "SM_BASE_ID", "SM_UUID", "SM_VIRTUAL");
            CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
                  query);

            // Création de l'objet qui va contenir les compteurs
            Compteurs compteurs = new Compteurs();

            // Variables utilisées au sein de l'itérateur
            Map<String, String> mapCassandra;

            // Itère sur les rows de DocInfo
            while (iterator.hasNext()) {

               // Lecture de la ligne dans Cassandra
               mapCassandra = iterator.next();

               // Traite la ligne
               traiteLigneDocInfo(nomBaseGed, isVirtuel, outStream,
                     mapCassandra, compteurs);

               // Sort de la boucle si on a dépassé le nombre max de doc à
               // parcourir
               if (compteurs.getNbDocsSortis() >= nbDocsSouhaites) {
                  break;
               }

            }

            // Affichage des compteurs totaux
            LOGGER.info("Total de docs parcourus : {}", compteurs
                  .getNbDocsTraites());
            LOGGER.info("Total de docs analysés dans la base {}: {}",
                  nomBaseGed, compteurs.getNbDocsDansBase());
            LOGGER.info("Total de docs sortis dans le fichier {} : {}",
                  fichierSortie, compteurs.getNbDocsSortis());

         } finally {

            // Fermeture du fichier de sortie
            try {
               outStream.close();
            } catch (IOException ex) {
               LOGGER
                     .error(
                           "Erreur survenue lors de fermeture du fichier de sortie {}: {}",
                           fichierSortie.getAbsolutePath(), ex);
            }

         }

      } catch (IOException ex) {
         throw new ErreurTechniqueException(ex.getMessage(), ex);
      } finally {

         // Déconnexion de Cassandra
         cassandraSupport.disconnect();

      }

   }

   private String determineNomBaseGed() {

      // Récupère la requête depuis la DAO
      AllRowsQuery<String, String> query = basesReferenceDao.getQuery();

      // Contruit un objet List avec les noms de toutes les bases
      List<String> bases = new ArrayList<String>();
      CassandraIterator<String> iterator = new CassandraIterator<String>(query);
      Map<String, String> mapCassandra;
      while (iterator.hasNext()) {
         mapCassandra = iterator.next();
         bases.add(mapCassandra.get("baseId"));
      }

      // Retire de la liste la base des journaux DAILY_LOG_ARCHIVE_BASE
      bases.remove("DAILY_LOG_ARCHIVE_BASE");

      // Il ne doit rester qu'un seul élément dans la liste
      if (CollectionUtils.isEmpty(bases)) {
         throw new ErreurTechniqueException(
               "Impossible de déterminer le nom de la base GED contenant les documents: aucune base GED trouvée");
      } else if (bases.size() > 1) {
         throw new ErreurTechniqueException(
               String
                     .format(
                           "Impossible de déterminer le nom de la base GED contenant les documents: plusieurs bases GED trouvées, alors qu'on n'en attendait qu'une seule (Liste des bases: %s)",
                           concat(bases)));
      }

      // Renvoie le nom de la base documentaire
      String nomBaseGed = bases.get(0);
      LOGGER
            .debug("Nom de la base GED contenant les documents: {}", nomBaseGed);
      return nomBaseGed;

   }

   private String concat(List<String> liste) {
      if (CollectionUtils.isEmpty(liste)) {
         return StringUtils.EMPTY;
      } else {
         StringBuilder sBuilder = new StringBuilder();
         for (String item : liste) {
            sBuilder.append(item);
            sBuilder.append(";");
         }
         return sBuilder.toString();
      }

   }

   private void traceParametresService(File fichierSortie, int nbDocsSouhaites,
         boolean isVirtuel, CassandraConfig cassandraConfig) {

      LOGGER
            .debug(
                  "Paramètres de lancement du service extraitUuid() : [Fichier de sortie = {}] ; [Nb max de doc = {}] ; [Documents virtuels = {}] ; [Cassandra serveur(s) = {}] ; [Cassandra port = {}] ; [Cassandra login = {}]",
                  new Object[] {
                        fichierSortie.getAbsolutePath(),
                        nbDocsSouhaites == Integer.MAX_VALUE ? "Pas de limite"
                              : nbDocsSouhaites, isVirtuel ? "Oui" : "Non",
                        cassandraConfig.getServers(),
                        cassandraConfig.getPort(), cassandraConfig.getUser() });

   }

   private void traiteLigneDocInfo(String nomBaseGed, boolean isVirtuel,
         OutputStream outStream, Map<String, String> mapCassandra,
         Compteurs compteurs) throws IOException {

      // Vérifie que l'on se trouve bien sur un document
      // de la base documentaire attendu
      String idDoc = mapCassandra.get("SM_UUID");
      String nomBaseDfce = mapCassandra.get("SM_BASE_ID");

      if (StringUtils.equals(nomBaseDfce, nomBaseGed)
            && StringUtils.isNotBlank(idDoc)) {

         boolean bVirtuel = StringUtils.equals(mapCassandra.get("SM_VIRTUAL"),
               "true");

         if (isVirtuel && bVirtuel) {

            ecritDocumentDansFichierDeSortie(idDoc, outStream, compteurs);

         } else if (!isVirtuel && !bVirtuel) {

            ecritDocumentDansFichierDeSortie(idDoc, outStream, compteurs);

         }

         compteurs.incrementeNbDocsDansBase();

      }

      // Compteurs
      compteurs.incrementeNbDocsTraites();
      if ((compteurs.getNbDocsTraites() % NB_DOC_POUR_PAS_LOG) == 0) {
         LOGGER
               .info(
                     "Nombre de docs parcourus : {} (dont {} dans la base {}). Nombre de docs sortis dans le fichier : {}",
                     new Object[] { compteurs.getNbDocsTraites(),
                           compteurs.getNbDocsDansBase(), nomBaseGed,
                           compteurs.getNbDocsSortis() });
      }

   }

   private void ecritDocumentDansFichierDeSortie(String idDoc,
         OutputStream outStream, Compteurs compteurs) throws IOException {

      IOUtils.write(idDoc, outStream);
      IOUtils.write("\r\n", outStream);
      compteurs.incrementeNbDocsSortis();

   }

}
