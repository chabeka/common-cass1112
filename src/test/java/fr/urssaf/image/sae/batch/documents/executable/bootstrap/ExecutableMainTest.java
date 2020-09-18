package fr.urssaf.image.sae.batch.documents.executable.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dfce.manager.DFCEConnectionParameter;
import fr.urssaf.image.sae.batch.documents.executable.model.AbstractParametres;
import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationsEnvironnement;
import fr.urssaf.image.sae.batch.documents.executable.model.DeleteDocsParametres;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.ConfigurationServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-test.xml"})
public class ExecutableMainTest {

   private final String BASE_PATH = "src/test/resources";

   private final String envsPath = BASE_PATH + "/environnements-test.xml";

   private final File confEnvFile = new File(envsPath);

   // -- Liste liste des environnements
   ConfigurationsEnvironnement envList;

   @Before
   public void init() throws IOException {
      ConfigurationServiceImpl configSce;
      configSce = new ConfigurationServiceImpl();
      envList = configSce.chargerConfiguration(confEnvFile);
   }

   @Test
   public void getDfceConfigurationTest() throws FileNotFoundException {
      final Properties dfce = ExecutableMain.getDfceConfiguration(envList.getConfiguration("ENV_DEVELOPPEMENT"));

      final String dfce_host = dfce.getProperty(DFCEConnectionParameter.DFCE_HOSTNAME);
      final String dfce_base = dfce.getProperty(DFCEConnectionParameter.DFCE_BASE_NAME);
      final String dfce_port = dfce.getProperty(DFCEConnectionParameter.DFCE_HOSTPORT);
      final String dfce_login = dfce.getProperty(DFCEConnectionParameter.DFCE_LOGIN);
      final String dfce_psswd = dfce.getProperty(DFCEConnectionParameter.DFCE_PASSWORD);
      final String dfce_tmout = dfce.getProperty(DFCEConnectionParameter.DFCE_TIMEOUT);
      final String dfce_ctxRoot = dfce.getProperty(DFCEConnectionParameter.DFCE_CONTEXTROOT);
      final String dfce_secure = dfce.getProperty(DFCEConnectionParameter.DFCE_SECURE);

      Assert.assertEquals("Valeur inattendue", "cer69-ds4int.cer69.recouv", dfce_host);
      Assert.assertEquals("Valeur inattendue", "SAE-TEST", dfce_base);
      Assert.assertEquals("Valeur inattendue", "8080", dfce_port);
      Assert.assertEquals("Valeur inattendue", "_ADMIN", dfce_login);
      Assert.assertEquals("Valeur inattendue", "DOCUBASE", dfce_psswd);
      Assert.assertEquals("Valeur inattendue", "30000", dfce_tmout);
      Assert.assertEquals("Valeur inattendue", "/dfce-webapp/", dfce_ctxRoot);
      Assert.assertEquals("Valeur inattendue", "false", dfce_secure);
   }

   private Properties setCommonProperties(final Properties properties) {
      properties.put("param.taille.pool", "5");
      properties.put("param.taille.pas.execution", "10000");
      properties.put("param.taille.queue", "20");
      properties.put("param.queue.sleep.time.ms", "200");
      return properties;
   }

   @Test
   public void testExecutableEmptyArgs() {
      final String[] args = {};
      ExecutableMain.main(args);
   }

   @Test
   public void verifierConfParamsImportDocsOK() {

      final ExecutableMain main = new ExecutableMain(confEnvFile);
      Properties properties = new Properties();
      final AbstractParametres parametres = new DeleteDocsParametres();

      // -- Liste des métadonnées
      properties = setCommonProperties(properties);
      properties.put("param.chemin.dossier.travrail", "target");
      final Boolean result = main.vefierParametresExecution(properties, parametres, "IMPORT_DOCUMENTS");

      Assert.assertTrue("La vérification ne doit renvoyer aucune erreur", result);
      Assert.assertEquals("La taille du pool de thread n'est pas correcte",
                          5,
                          parametres.getTaillePool());
      Assert.assertEquals("La taille du pas d'exécution n'est pas correcte",
                          10000,
                          parametres.getTaillePasExecution());
      Assert.assertEquals("La valeur temps d'attente (en ms) du RejectedExecutionHandler est invalide",
                          200,
                          parametres.getQueueSleepTime());
      Assert.assertEquals("Le chemin du dossier de travail est invalide",
                          "target",
                          parametres.getDossierTravail());
   }

   @Test
   public void verifierConfParamsExportDocsOK() {

      final ExecutableMain main = new ExecutableMain(confEnvFile);
      Properties properties = new Properties();
      final AbstractParametres parametres = new DeleteDocsParametres();

      // -- Liste des métadonnées
      properties = setCommonProperties(properties);
      properties.put("param.chemin.dossier.travrail", "target");
      properties.put("param.requete.lucene", "SM_ARCHIVAGE_DATE:20120101");
      final Boolean result = main.vefierParametresExecution(properties, parametres, "EXPORT_DOCUMENTS");

      Assert.assertTrue("La vérification ne doit renvoyer aucune erreur", result);
      Assert.assertEquals("La taille du pool de thread n'est pas correcte",
                          5,
                          parametres.getTaillePool());
      Assert.assertEquals("La taille du pas d'exécution n'est pas correcte",
                          10000,
                          parametres.getTaillePasExecution());
      Assert.assertEquals("La valeur temps d'attente (en ms) du RejectedExecutionHandler est invalide",
                          200,
                          parametres.getQueueSleepTime());
      Assert.assertEquals("Le chemin du dossier de travail est invalide",
                          "target",
                          parametres.getDossierTravail());
   }

   @Test
   public void verifierConfParamsDeleteDocsOK() {

      final ExecutableMain main = new ExecutableMain(confEnvFile);
      Properties properties = new Properties();
      final AbstractParametres parametres = new DeleteDocsParametres();

      // -- Liste des métadonnées
      properties = setCommonProperties(properties);
      properties.put("param.requete.lucene", "SM_ARCHIVAGE_DATE :[20120101 TO 20150401]");
      final Boolean result = main.vefierParametresExecution(properties, parametres, "DELETE_DOCUMENTS");

      Assert.assertTrue("La vérification ne doit renvoyer aucune erreur", result);
      Assert.assertEquals("La taille du pool de thread n'est pas correcte",
                          5,
                          parametres.getTaillePool());
      Assert.assertEquals("La taille du pas d'exécution n'est pas correcte",
                          10000,
                          parametres.getTaillePasExecution());
      Assert.assertEquals("La valeur temps d'attente (en ms) du RejectedExecutionHandler est invalide",
                          200,
                          parametres.getQueueSleepTime());
      Assert.assertEquals("La requête lucène n'est pas correcte",
                          "SM_ARCHIVAGE_DATE :[20120101 TO 20150401]",
                          parametres.getRequeteLucene());
   }

   @Test
   public void verifierConfParamsImportDocsWorkDir() {

      final ExecutableMain main = new ExecutableMain(confEnvFile);
      Properties properties = new Properties();
      final AbstractParametres parametres = new DeleteDocsParametres();

      // -- Liste des métadonnées
      properties = setCommonProperties(properties);
      properties.put("param.chemin.dossier.travrail", "");
      final Boolean result = main.vefierParametresExecution(properties, parametres, "IMPORT_DOCUMENTS");

      Assert.assertFalse("La vérification doit echouer si le dossier de travail n'est pas spécifié ou est invalide", result);
   }

   @Test
   public void verifierConfParamsExportDocsWorkDir() {

      final ExecutableMain main = new ExecutableMain(confEnvFile);
      Properties properties = new Properties();
      final AbstractParametres parametres = new DeleteDocsParametres();

      // -- Liste des métadonnées
      properties = setCommonProperties(properties);
      properties.put("param.chemin.dossier.travail", "");
      final Boolean result = main.vefierParametresExecution(properties, parametres, "EXPORT_DOCUMENTS");

      Assert.assertFalse("La vérification doit echouer si le dossier de travail n'est pas spécifié ou est invalide", result);
   }

   @Test
   public void verifierConfParamsDeleteDocsMissingLucene() {

      final ExecutableMain main = new ExecutableMain(confEnvFile);
      Properties properties = new Properties();
      final AbstractParametres parametres = new DeleteDocsParametres();

      // -- Liste des métadonnées
      properties = setCommonProperties(properties);
      properties.put("param.requete.lucene", "");
      final Boolean result = main.vefierParametresExecution(properties, parametres, "DELETE_DOCUMENTS");
      Assert.assertFalse("La vérification doit echouer si lq requete lucene est absente ou invalide", result);
   }

   @Test
   public void executePasArgument() {
      try {
         final ExecutableMain main = new ExecutableMain(null);
         main.execute(new String[] {});
      }
      catch (final RuntimeException e) {
         if (!e.getMessage().equals("Le chemin du fichier ne peut être null")) {
            Assert.fail("Le message de l'exception n'est pas celui attendu");
         }
         return;
      }
      Assert.fail("Une exception était attendue.");
   }

   @Test
   public void executeServiceNonReconnu() {
      try {
         ExecutableMain.main(new String[] {"SERVICE_NON_RECONNU", "ENV_DEVELOPPEMENT"});
      }
      catch (final RuntimeException e) {
         Assert.assertThat(e.getMessage(), JUnitMatchers.containsString("existe pas"));
         return;
      }
      Assert.fail("Une exception était attendue.");
   }

   @Test
   public void executeEnvironnementNonReconnu() {
      try {
         ExecutableMain.main(new String[] {ExecutableMain.IMPORT_DOCUMENTS, "ENV_INCONNU"});
      }
      catch (final RuntimeException e) {
         Assert.assertThat(e.getMessage(), JUnitMatchers.containsString("existe pas"));
         return;
      }
      Assert.fail("Une exception était attendue.");
   }

   @Test
   @Ignore
   public void executeServiceDeleteDocs() throws IOException {
      final String req = "SM_ARCHIVAGE_DATE:20150407";
      final ExecutableMain main = new ExecutableMain(confEnvFile);
      main.executeService(envList, "DELETE_DOCUMENTS", "ENV_DEVELOPPEMENT", null, req);
   }

   @Test
   @Ignore
   public void executeServiceImportDocs() throws IOException {
      final ExecutableMain main = new ExecutableMain(confEnvFile);
      final String wkdir = BASE_PATH + File.separator + "TRANSFERTS"
            + File.separator + "EXPORT_20150401_151230";
      main.executeService(envList, "IMPORT_DOCUMENTS", "ENV_DEVELOPPEMENT", wkdir, null);
   }

   @Test
   @Ignore
   public void executeServiceExportDocs() throws FileNotFoundException {
      final String wkdir = BASE_PATH + File.separator + "TRANSFERTS";
      final String req = "SM_ARCHIVAGE_DATE:[20150101 TO 20150320]";
      final ExecutableMain main = new ExecutableMain(confEnvFile);
      main.executeService(envList, "EXPORT_DOCUMENTS", "INTEGRATION_INTERNE_GNT", wkdir, req);
   }

   @Test
   public void KO_deleteDocsOnProduction() throws FileNotFoundException {
      final String req = "SM_METADATAT:SAMPLE";
      final ExecutableMain main = new ExecutableMain(confEnvFile);
      try {
         main.executeService(envList, "DELETE_DOCUMENTS", "ENV_MOCK_PRODUCTION", null, req);
      }
      catch (final RuntimeException e) {
         final String msg = "ERREUR : DELETE interdit en PRODUCTION !";
         Assert.assertEquals("Le message attendu n'est pas correct", msg, e.getMessage());
      }
   }

   @Test
   public void KO_importDocsOnProduction() throws FileNotFoundException {
      final String wDir = "/tmp/EXPORT_20150403_105520";
      final ExecutableMain main = new ExecutableMain(confEnvFile);
      try {
         main.executeService(envList, "IMPORT_DOCUMENTS", "ENV_MOCK_PRODUCTION", wDir, null);
      }
      catch (final RuntimeException e) {
         final String msg = "ERREUR : Impossible de lancer le service IMPORT_DOCUMENTS sur l'environnement ENV_MOCK_PRODUCTION";
         Assert.assertEquals("Le message attendu n'est pas correct", msg, e.getMessage());
      }
   }

   /**
    * Test REEL
    * Export de documents
    */
   @Test
   @Ignore
   public void ExportIntegrationNationaleGns() {
      final String LUCENE = "SM_ARCHIVAGE_DATE:[20190904 TO 20190904]";
      final String[] args = {
                             "EXPORT_DOCUMENTS",
                             "INTEGRATION_CLIENTE_PAJE_GNT",
                             "C:/Tmp",
                             LUCENE
      };
      ExecutableMain.main(args);
   }

   @Test
   public void removeDebugMode() {
      if (ExecutableMain.DEBUG_MODE) {
         Assert.assertFalse("DEBUG_MODE : Doit passer à false", ExecutableMain.DEBUG_MODE);
      }
   }
}
