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

@Service
public class SAECassandraService {
   
   // LOGGER
   private static final Logger LOG = LoggerFactory
         .getLogger(SAECassandraUpdater.class);
   
   private int MAX_RETRY_VALUE = 3;
   private int MAX_RETRY = 3;
   
   @Autowired
   private SAECassandraDao saeDao;
   
//   PUBLIC SAECASSANDRASERVICE(CASSANDRACONFIG CONFIG){
//      //THIS.SAEDAO = NEW SAECASSANDRADAO(CONFIG);
//   }
   
   public int getMAX_RETRY() {
      return MAX_RETRY;
   }

   public void setMAX_RETRY(int mAXRETRY) {
      MAX_RETRY = mAXRETRY;
   }
   /**
    * Methode permettant la création de plusieurs column familly à partir d'une liste de ColumnFamilyDefinition
    * @param cfDefs Liste de column family à créer
    */
   protected void createColumnFamilyFromList(List<ColumnFamilyDefinition> cfDefs, boolean blockUntilComplete) {
      
        // Création des CF
      for (ColumnFamilyDefinition c : cfDefs) {
         
         if (cfExists(c.getName())) {
            LOG.debug("La famille de colonnes {} est déjà existante", c.getName());
         } else {
            LOG.debug("Création de la famille de colonnes {}", c.getName());
            try {
               createColumnFamillyFromDefinition(c, blockUntilComplete,this.getMAX_RETRY());                          
            }catch(HCassandraInternalException ex){
//               createColumnFamillyFromDefinition(c, blockUntilComplete,this.getMAX_RETRY());           
               LOG.error("Un exception s'est produite lors de la création de la column familly {}", c.getName());
            }
         }
      }
      
   }
   
   /**
    * Surcouche à la méthode addColumnFamily facilitant les tests
    * 
    * @param c
    *           définition d'une column familly
    * @param blockUntilComplete
    *           boolean permettant de dire est ce qu'on attend l'acception du
    *           cluster ou pas.
    * @param tentatives
    *          Max tentatives, contante définie au niveau de la classe
    */
   protected boolean createColumnFamillyFromDefinition(ColumnFamilyDefinition c,
         boolean blockUntilComplete, int tentatives) {
      // on décrémente le compteur
      this.setMAX_RETRY(tentatives-1);
      boolean succes = false;
      try {
         // appel de la creation de la CF
         saeDao.createColumnFamily(c, blockUntilComplete);
      } catch (HectorException e) {
         LOG.error("Error : ",e);
         LOG.debug("Echec de la création du Column Familly {} nouvelle tentative dans 5 sec", c.getName());
         try {
            // en cas d'exception on met en pause 5 sec et on re-essaie
            Thread.sleep(5000);
            if (this.getMAX_RETRY()>0 && !succes) {
               int nbTentatives = MAX_RETRY_VALUE-tentatives;
               LOG.debug("Tentative {} : création de {} ", nbTentatives, c.getName() );
               succes= createColumnFamillyFromDefinition(c, blockUntilComplete, this.getMAX_RETRY());
            }else{
               // on réinitialise le compteur
               this.setMAX_RETRY(MAX_RETRY_VALUE);
               return succes;
            }

         } catch (InterruptedException e1) {
            LOG.error("Echec lors de la mise en vielle de la création d'une famille de colonne {}", c.getName());
         }
      }
      // on réinitialise le compteur
      this.setMAX_RETRY(MAX_RETRY_VALUE);
      return succes;
   }
   
   

   
   /**
    * @param cfName
    *           Name of the CF to search for
    * @return true if the CF exists in keyspace
    */
   public boolean cfExists(String cfName) {
      List<ColumnFamilyDefinition> listeCFExist = saeDao.getColumnFamilyDefintion();
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
   public Cluster getCluster(){
      return saeDao.getCluster();
   }
   
   /**
    * 
    * @return renvoie le nom du Keyspace
    */
   public String getKeySpaceName(){
      return saeDao.getKeySpaceName();
   }

   public void setSaeDao(SAECassandraDao saeDao) {
      this.saeDao = saeDao;
   }

   public SAECassandraDao getSaeDao() {
      return saeDao;
   }
   
   
}
