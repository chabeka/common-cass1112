package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;

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

   /**
    * Ajout des droits GED
    */
   public void updateAuthorizationAccess() {
      LOGGER
            .info("Mise à jour des droits GED pour ajouter modification et suppression");

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Insertion de données
      InsertionDonnees donnees = new InsertionDonnees(saeDao.getKeyspace());
      donnees.addDroitsGed();
   }

}
