package fr.urssaf.image.sae.pile.travaux.dao;

import java.util.List;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.pile.travaux.dao.impl.JobQueueDaoImpl;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;


public class MainTest {

   /**
    * Test permettant de vérifier que l'application ferme toutes les threads à la fin du main
    * @param args
    */
   public static void main(String[] args) {
      
      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] {"/applicationContext-sae-pile-travaux-test.xml"});
      
      JobQueueDaoImpl dao = context.getBean(JobQueueDaoImpl.class);
      @SuppressWarnings("unused")
      List<JobRequest> list = dao.getAllJobs(10);

      // Dans le cas d'un serveur cassandra in-process :
      CassandraServerBean cassandraServer = context.getBean(CassandraServerBean.class);
      boolean stopCassandra = false;
      if (cassandraServer.getStartLocal()) {
         // Il faut fermer le cluster créé par le DataLoader de cassandraUnit
         cassandraServer.shutdownTestCluster();
         // Et il faudra tuer le serveur cassandra
         stopCassandra = true;
      }
      
      // Si on ne lance pas de serveur cassandra et zookeeper in-process : il
      // suffit de fermer le context spring de cette manière :
      context.close();

      // Arrêt du serveur cassandra in-process
      if (stopCassandra) {
         EmbeddedCassandraServerHelper.stopEmbeddedCassandra();
         // Pour autant, il reste des threads du serveur cassandra, donc un exit est nécessaire
         System.exit(0);
      }

      System.out.println("fini");
   }

}
