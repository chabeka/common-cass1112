package fr.urssaf.image.commons.cassandra.spring.batch.helper;

import org.cassandraunit.DataLoader;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * Classe utilitaire facilitant la création d'un serveur cassandra local
 * par un bean spring.
 *
 */
public class LocalCassandraServerBean implements InitializingBean, DisposableBean  {

   private static final Logger LOG = LoggerFactory.getLogger(LocalCassandraServerBean.class);
   private String dataSet; 
   private boolean shouldInit= false;

   /**
    * Indique quel jeu de données cassandraUnit doit être utilisé lors
    * de l'initialisation du serveur cassandra
    * @param dataSet Jeu de données
    */
   public final void setDataSet(String dataSet) {
      this.dataSet = dataSet;
   }
   
   /**
    * Indique s'il faut lancer un serveur cassandra local
    * @param init vrai s'il faut lancer un serveur local
    */
   public final void setShouldInit(boolean init) {
      shouldInit = init;
   }
   
   @Override
   public final void destroy() throws Exception {
      // Pas besoin d'arrêter le serveur
   }

   @Override
   public final void afterPropertiesSet() throws Exception {
      LOG.debug("initLocalCassandraServer : init={} - dataSet={}", shouldInit, dataSet);
      if (!shouldInit) return;
      EmbeddedCassandraServerHelper.startEmbeddedCassandra();
      if (dataSet!=null && !dataSet.isEmpty()) {
         DataLoader dataLoader = new DataLoader("TestCluster", "localhost:9171");
         ClassPathXmlDataSet set = new ClassPathXmlDataSet(dataSet);
         LOG.debug("initLocalCassandraServer : keyspace={}", set.getKeyspace().getName());
         dataLoader.load(set);
      }
   }
}
