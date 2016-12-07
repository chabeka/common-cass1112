package fr.urssaf.image.sae.batch.documents.executable.aspect;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.manager.DFCEConnectionFactory;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.batch.documents.executable.bootstrap.ExecutableMain;
import fr.urssaf.image.sae.batch.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationEnvironnement;
import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationsEnvironnement;
import fr.urssaf.image.sae.batch.documents.executable.service.DfceService;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.ConfigurationServiceImpl;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.DfceServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
public class ParamDfceTest {

   private static DfceService dfceService;
   
   @BeforeClass
   public static void init() throws IOException {
      ConfigurationServiceImpl configSce;
      configSce = new ConfigurationServiceImpl();
      File fichierConfEnv = new File("src/test/resources/environnements-test.xml");
      
      //-- Liste liste des envirennements
      
      Properties dfceConfigProp;
      ConfigurationsEnvironnement envList;  
      
      ConfigurationEnvironnement destConfigEnv;
      envList = configSce.chargerConfiguration(fichierConfEnv);
      destConfigEnv = envList.getConfiguration("ENV_DEVELOPPEMENT");
      
      dfceConfigProp = ExecutableMain.getDfceConfiguration(destConfigEnv);
      
      DFCEConnection dfceConnection = DFCEConnectionFactory
         .createDFCEConnectionByDFCEConfiguration(dfceConfigProp);
      
      dfceService = new DfceServiceImpl(dfceConnection);
      //dfceService.ouvrirConnexion();
   }

   @Test
   public void validExecuterRequeteRequeteLuceneNull() throws SearchQueryParseException {
      try {
         dfceService.executerRequete(null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [requeteLucene].",
                     ex.getMessage());
      }
   }

   @Test
   public void validRecupererContenuDocumentNull() {
      try {
         dfceService.recupererContenu(null);
         Assert
               .fail("Une exception ParametreRuntimeException aurait dû être levée");
      } catch (ParametreRuntimeException ex) {
         Assert
               .assertEquals(
                     "Le message de l'exception est incorrect",
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [document].",
                     ex.getMessage());
      }
   }
}
