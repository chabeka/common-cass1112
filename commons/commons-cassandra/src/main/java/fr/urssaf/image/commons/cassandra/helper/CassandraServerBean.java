package fr.urssaf.image.commons.cassandra.helper;

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
public class CassandraServerBean implements InitializingBean, DisposableBean  {

   private static final Logger LOG = LoggerFactory.getLogger(CassandraServerBean.class);
   private String dataSet;
   private boolean startLocal= false;
   private String hosts = null;

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
    * @param startLocal vrai s'il faut lancer un serveur local
    */
   public final void setStartLocal(boolean startLocal) {
      this.startLocal = startLocal;
   }
   
   /**
    * @return vrai si le serveur cassandra est lancé localement
    */
   public final boolean getStartLocal() {
      return this.startLocal;
   }
   
   @Override
   public final void destroy() throws Exception {
      // Pas besoin d'arrêter le serveur
   }

   @Override
   public final void afterPropertiesSet() throws Exception {
      LOG.debug("CassandraServerBean : startLocal={} - dataSet={}", startLocal, dataSet);
      if (!startLocal) return;
      EmbeddedCassandraServerHelper.startEmbeddedCassandra();
      if (dataSet!=null && !dataSet.isEmpty()) {
         DataLoader dataLoader = new DataLoader("TestCluster", "localhost:9171");
         ClassPathXmlDataSet set = new ClassPathXmlDataSet(dataSet);
         LOG.debug("CassandraServerBean : keyspace={}", set.getKeyspace().getName());
         dataLoader.load(set);
      }
   }

   /**
    * Réinitialise les données de la base cassandra locale, avec le jeu de données
    * utilisé initialement lors de la création du serveur
    * @throws Exception    Une erreur est survenue
    */
   public final void resetData() throws Exception {
      resetData(dataSet);
   }
   
   /**
    * Réinitialise les données de la base cassandra locale
    * @param newDataSets    Jeu(x) de données à utiliser
    * @throws Exception    Une erreur est survenue
    */
   public final void resetData(String... newDataSets) throws Exception {
      LOG.debug("CassandraServerBean : reseting data...");
      if (!startLocal) return;
      EmbeddedCassandraServerHelper.startEmbeddedCassandra();
      for (String newDataSet : newDataSets) {
         if (newDataSet!=null && !newDataSet.isEmpty()) {
            DataLoader dataLoader = new DataLoader("TestCluster", "localhost:9171");
            ClassPathXmlDataSet set = new ClassPathXmlDataSet(newDataSet);
            dataLoader.load(set);
         }
      }
   }

   /**
    * Dans le cas d'un cassandra zookeeper non local, il s'agit de la chaîne de connexion
    * @param hosts    Chaîne de connexion (ex : "toto.toto.com:9160,titi.titi.com:9160")
    */
   public final void setHosts(String hosts) {
      this.hosts = hosts;
   }

   /**
    * Renvoie la chaîne de connexion au serveur cassandra
    * @return chaîne de connexion
    */
   public final String getHosts() {
      if (startLocal) {
         // Petite bidouille : on met le serveur localhost 3 fois : ça permet de tenter 3 fois
         // l'opération si elle échoue la 1ere fois (ça arrive lorsque le serveur cassandra local
         // ne se lance pas assez rapidement)
         return "localhost:9171,localhost:9171,localhost:9171"; 
      }
      else {
         return hosts;
      }
   }

}
