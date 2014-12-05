package fr.urssaf.image.sae.extraitdonnees.service;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.extraitdonnees.bean.CassandraConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-extraitdonnees-test.xml" })
@Ignore
public class ExtraitDicoServiceTest {

   @Autowired
   private ExtraitDonneesService extraitDicoService;
   
   @Test
   public void extraitDonneesTest() throws IOException {

      File fichierSortie = new File("c:/divers/data.txt");
      int nbDocsSouhaites = 10000;
      // int nbDocsSouhaites = Integer.MAX_VALUE;
      boolean isVirtuel = false;

      CassandraConfig cassandraConfig = buildCassandraConfig();
      
      
      extraitDicoService.extraitUuid(fichierSortie, nbDocsSouhaites, isVirtuel, cassandraConfig);

   }
   
   
   private CassandraConfig buildCassandraConfig() throws IOException {
      
      Properties props = new Properties();
      
      ClassPathResource ficProps = new ClassPathResource("cassandra/cassandra-connection.properties"); 
      
      props.load(ficProps.getInputStream());
      
      CassandraConfig cassandraConfig = new CassandraConfig();
      
      cassandraConfig.setServers(props.getProperty("cassandra.servers"));
      cassandraConfig.setPort(Integer.parseInt(props.getProperty("cassandra.port")));
      cassandraConfig.setUser(props.getProperty("cassandra.user"));
      cassandraConfig.setPassword(props.getProperty("cassandra.password"));
      
      return cassandraConfig;
      
   }

}
