package fr.urssaf.image.sae.batch.documents.executable.service;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationsEnvironnement;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.ConfigurationServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
public class ConfigurationServiceTest {
   
   
   private String envsConfPath = "src/test/resources/environnements-test.xml";

   @Test
   public void chargerConfigurationTest() throws IOException {
      
      ConfigurationServiceImpl configSce;
      configSce = new ConfigurationServiceImpl();
      File fichierConfEnv = new File(envsConfPath);
      //FileInputStream confEnvInput = new FileInputStream(new File(envsConfPath));
      
      Assert.assertEquals("Le fichier de configuration doit exister", true, fichierConfEnv.exists());
      
      //-- Liste liste des envirennements
      ConfigurationsEnvironnement envList;  
      envList = configSce.chargerConfiguration(fichierConfEnv);
      
      Assert.assertEquals("Le nombre de confgurations est incorrect", 3, envList.getListeNoms().size());
      Assert.assertEquals("La liste des environnements est invalide", true, envList.existe("ENV_DEVELOPPEMENT"));
      Assert.assertEquals("La liste des environnements est invalide", true, envList.existe("INTEGRATION_INTERNE_GNT"));
      Assert.assertEquals("La liste des environnements est invalide", true, envList.existe("ENV_MOCK_PRODUCTION"));
   }
}
