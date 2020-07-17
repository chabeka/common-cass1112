package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;
import fr.urssaf.image.sae.lotinstallmaj.service.InsertionDonnees;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.InsertionDonneesCQL;
import fr.urssaf.image.sae.metadata.utils.Constantes;

/**
 * Service permettant de réaliser des opérations de mises à jour spécifiques à
 * la GED
 * 
 */
@Component
public class GedCassandraUpdater {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(GedCassandraUpdater.class);

   @Autowired
   private SAECassandraDao saeDao;

   private InsertionDonnees donnees;

   @Autowired
   private ModeApiCqlSupport modeApiCqlSupport;

   @Autowired
   private InsertionDonneesImpl insertionDonneesImpl;

   @Autowired
   private InsertionDonneesCQL insertionDonneesCQL;

   private static final String CF_NAME = Constantes.CF_METADATA;


   public void setInsertionDataService() {
      if (modeApiCqlSupport.isModeThrift(CF_NAME)) {
         donnees = insertionDonneesImpl;
      } else if (modeApiCqlSupport.isModeCql(CF_NAME)) {
         donnees = insertionDonneesCQL;
      }
   }

   /**
    * Ajout des droits GED
    */
   public void updateAuthorizationAccess() {
      LOGGER
      .info("Mise à jour des droits GED pour ajouter modification et suppression");

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Insertion de données
      setInsertionDataService();
      donnees.addDroitsGed();
   }

   /**
    * Ajout des droits GED en base SAE CQL
    */
   public void updateAuthorizationAccessCQL() {

   }

}
