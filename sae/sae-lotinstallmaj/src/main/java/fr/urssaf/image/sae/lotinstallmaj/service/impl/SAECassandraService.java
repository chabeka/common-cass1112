package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.List;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.exceptions.HCassandraInternalException;
import me.prettyprint.hector.api.exceptions.HectorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;

/**
 * classe de services CASSANDRA
 * 
 * 
 */
@Service
public class SAECassandraService {

   /**
    * 
    */
   private static final int MAX_RETRY = 3;

   private static final int SLEEP_TIME = 5000;

   // LOGGER
   private static final Logger LOG = LoggerFactory
         .getLogger(SAECassandraUpdater.class);

   private final int maxRetryValue = MAX_RETRY;
   private int maxRetry = MAX_RETRY;

   @Autowired
   private SAECassandraDao saeDao;

   /**
    * 
    * @return le nombre maximum d'essais
    */
   public final int getMaxRetry() {
      return maxRetry;
   }

   /**
    * 
    * @param maxRetry le nombre maximum d'essais
    */
   public final void setMaxRetry(int maxRetry) {
      this.maxRetry = maxRetry;
   }

   /**
    * Methode permettant la création de plusieurs column familly à partir d'une
    * liste de ColumnFamilyDefinition
    * 
    * @param cfDefs
    *           Liste de column family à créer
    */
   protected final void createColumnFamilyFromList(
         List<ColumnFamilyDefinition> cfDefs, boolean blockUntilComplete) {

      // Création des CF
      for (ColumnFamilyDefinition c : cfDefs) {

         if (cfExists(c.getName())) {
            LOG.debug("La famille de colonnes {} est déjà existante", c
                  .getName());
         } else {
            LOG.debug("Création de la famille de colonnes {}", c.getName());
            try {
               createColumnFamillyFromDefinition(c, blockUntilComplete, this
                     .getMaxRetry());
            } catch (HCassandraInternalException ex) {
               // createColumnFamillyFromDefinition(c,
               // blockUntilComplete,this.getMAX_RETRY());
               LOG
                     .error(
                           "Un exception s'est produite lors de la création de la column familly {}",
                           c.getName());
            }
         }
      }

   }

   /**
    * Surcouche à la méthode addColumnFamily facilitant les tests
    * 
    * @param colDef
    *           définition d'une column familly
    * @param blockUntilComplete
    *           boolean permettant de dire est ce qu'on attend l'acception du
    *           cluster ou pas.
    * @param tentatives
    *           Max tentatives, contante définie au niveau de la classe
    */
   protected final boolean createColumnFamillyFromDefinition(
         ColumnFamilyDefinition colDef, boolean blockUntilComplete, int tentatives) {
      // on décrémente le compteur
      this.setMaxRetry(tentatives - 1);
      boolean succes = false;
      try {
         // appel de la creation de la CF
         saeDao.createColumnFamily(colDef, blockUntilComplete);
      } catch (HectorException e) {
         LOG.error("Error : ", e);
         LOG
               .debug(
                     "Echec de la création du Column Familly {} nouvelle tentative dans 5 sec",
                     colDef.getName());
         try {
            // en cas d'exception on met en pause 5 sec et on re-essaie
            Thread.sleep(SLEEP_TIME);
            if (this.getMaxRetry() > 0 && !succes) {
               int nbTentatives = maxRetryValue - tentatives;
               LOG.debug("Tentative {} : création de {} ", nbTentatives, colDef
                     .getName());
               succes = createColumnFamillyFromDefinition(colDef,
                     blockUntilComplete, this.getMaxRetry());
            } else {
               // on réinitialise le compteur
               this.setMaxRetry(maxRetryValue);
               return succes;
            }

         } catch (InterruptedException e1) {
            LOG
                  .error(
                        "Echec lors de la mise en vielle de la création d'une famille de colonne {}",
                        colDef.getName());
         }
      }
      // on réinitialise le compteur
      this.setMaxRetry(maxRetryValue);
      return succes;
   }

   /**
    * @param cfName
    *           Name of the CF to search for
    * @return true if the CF exists in keyspace
    */
   public final boolean cfExists(String cfName) {
      List<ColumnFamilyDefinition> listeCFExist = saeDao
            .getColumnFamilyDefintion();
      for (ColumnFamilyDefinition cfDef : listeCFExist) {
         if (cfDef.getName().equals(cfName)) {
            return true;
         }
      }
      return false;
   }

   /**
    * 
    * @return renvoie le cluster associé à la DAO
    */
   public final Cluster getCluster() {
      return saeDao.getCluster();
   }

   /**
    * 
    * @return renvoie le nom du Keyspace
    */
   public final String getKeySpaceName() {
      return saeDao.getKeySpaceName();
   }

}
