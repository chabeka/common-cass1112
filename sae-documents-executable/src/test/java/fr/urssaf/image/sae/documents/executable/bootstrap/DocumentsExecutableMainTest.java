package fr.urssaf.image.sae.documents.executable.bootstrap;

import java.io.IOException;
import java.util.Properties;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.documents.executable.model.AddMetadatasParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres.MODE_VERIFICATION;
import fr.urssaf.image.sae.documents.executable.service.TraitementService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class DocumentsExecutableMainTest {
   
   private final String contexteXml = "/applicationContext-sae-documents-executable-test.xml";
   
   @Test
   public void verifierConfFichierParamAucunParametre() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      Assert.assertFalse("La vérification aurait du renvoyé une erreur", main
            .verifierConfFichierParam(properties, parametres));
   }
   
   @Test
   public void verifierConfFichierParamAddMetadonnees() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      AddMetadatasParametres parametres = new AddMetadatasParametres();
      
      //-- Aucun Parametre
      Boolean result = main.vefierConfFichierParamAddMeta(properties, parametres);
      Assert.assertFalse("La vérification aurait du renvoyé une erreur", result);
      
      //-- RequeteLucene
      properties.put("addMeta.requete.lucene", "SM_ARCHIVAGE_DATE :[20120101 TO 20150401]");
      result = main.vefierConfFichierParamAddMeta(properties, parametres);
      Assert.assertFalse("La vérification aurait du renvoyer une erreur", result);
      
      //-- Taille pool
      properties.put("addMeta.taille.pool", "5");
      properties.put("addMeta.requete.lucene", "SM_ARCHIVAGE_DATE :[20120101 TO 20150401]");
      result = main.vefierConfFichierParamAddMeta(properties, parametres);
      Assert.assertFalse("La vérification aurait du renvoyer une erreur", result);
      
      //-- Taille pas d'execution
      properties.put("addMeta.taille.pool", "5");
      properties.put("addMeta.taille.pas.execution", "10000");
      properties.put("addMeta.requete.lucene", "SM_ARCHIVAGE_DATE :[20120101 TO 20150401]");
      result = main.vefierConfFichierParamAddMeta(properties, parametres);
      Assert.assertFalse("La vérification aurait du renvoyer une erreur", result);
   }
   
   @Test
   public void verifierConfFichierParamAddMetadonneesOK() {
      
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      AddMetadatasParametres parametres = new AddMetadatasParametres();
      
      Boolean result = main.vefierConfFichierParamAddMeta(properties, parametres);
      
      //-- Liste des métadonnées
      properties.put("addMeta.taille.pool", "5");
      properties.put("addMeta.taille.queue", "20");
      properties.put("addMeta.taille.pas.execution", "10000");
      properties.put("addMeta.metadonnees", "cot:1,cpt:0,drh:0");
      properties.put("addMeta.requete.lucene", "SM_ARCHIVAGE_DATE :[20120101 TO 20150401]");
      
      result = main.vefierConfFichierParamAddMeta(properties, parametres);
      
      Assert.assertTrue("La vérification ne doit renvoyer aucune erreur", result);
      
      Assert.assertEquals("La requête lucène n'est pas correcte",
            "SM_ARCHIVAGE_DATE :[20120101 TO 20150401]", parametres.getRequeteLucene());
      Assert.assertEquals("La taille du pool de thread n'est pas correcte", 5,
            parametres.getTaillePool());
      Assert.assertEquals("La taille du pas d'exécution n'est pas correcte",
            10000, parametres.getTaillePasExecution());
      Assert.assertFalse("La liste des métadonnées ne doit pas être vide",
            parametres.getMetadonnees().isEmpty());
   }

   @Test
   public void verifierConfFichierParamModeVerification() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification",
            MODE_VERIFICATION.IDENTIFICATION.name());

      Assert.assertFalse("La vérification aurait du renvoyé une erreur", main
            .verifierConfFichierParam(properties, parametres));
   }

   @Test
   public void verifierConfFichierParamModeVerificationRequeteLucene() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification", MODE_VERIFICATION.VALIDATION
            .name());
      properties.put("format.requete.lucene", "srt:41882050200023");

      Assert.assertFalse("La vérification aurait du renvoyé une erreur", main
            .verifierConfFichierParam(properties, parametres));
   }

   @Test
   public void verifierConfFichierParamModeVerificationRequeteLuceneTaillePool() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID
            .name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");

      Assert.assertFalse("La vérification aurait du renvoyé une erreur", main
            .verifierConfFichierParam(properties, parametres));
   }

   @Test
   public void verifierConfFichierParamModeVerificationRequeteLuceneTaillePoolNbMaxDocs() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID
            .name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
      properties.put("format.nombre.max.documents", "100");

      Assert.assertFalse("La vérification aurait du renvoyé une erreur", main
            .verifierConfFichierParam(properties, parametres));
   }

   @Test
   public void verifierConfFichierParamModeVerificationRequeteLuceneTaillePoolNbMaxDocsPasExecution() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID
            .name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
      properties.put("format.taille.queue", "20");
      properties.put("format.nombre.max.documents", "100");
      properties.put("format.taille.pas.execution", "10");

      Assert.assertTrue("La vérification n'aurait pas du renvoyé une erreur",
            main.verifierConfFichierParam(properties, parametres));
      Assert.assertEquals("Le mode de vérification n'est pas correct",
            MODE_VERIFICATION.IDENT_VALID, parametres.getModeVerification());
      Assert.assertEquals("La requête lucène n'est pas correcte",
            "srt:41882050200023", parametres.getRequeteLucene());
      Assert.assertEquals("La taille du pool de thread n'est pas correcte", 5,
            parametres.getTaillePool());
      Assert.assertEquals("Le nombre max de documents n'est pas correct", 100,
            parametres.getNombreMaxDocs());
      Assert.assertEquals("La taille du pas d'exécution n'est pas correcte",
            10, parametres.getTaillePasExecution());
      Assert.assertTrue("La liste des métadonnées devrait être vide",
            parametres.getMetadonnees().isEmpty());
      Assert.assertEquals("Le temps max de traitement n'est pas correcte", 0,
            parametres.getTempsMaxTraitement());
   }

   @Test
   public void verifierConfFichierParamToutParametreMaisRepertoireInexistant() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID
            .name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
      properties.put("format.nombre.max.documents", "100");
      properties.put("format.taille.pas.execution", "10");
      properties.put("format.chemin.repertoire.temporaire",
            "/repertoire-inexistant");

      Assert.assertFalse("La vérification aurait du renvoyé une erreur", main
            .verifierConfFichierParam(properties, parametres));
   }

   @Test
   public void verifierConfFichierParamToutParametre() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID
            .name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
      properties.put("format.taille.queue", "20");
      properties.put("format.nombre.max.documents", "100");
      properties.put("format.taille.pas.execution", "10");
      properties.put("format.metadonnees", "srt,iti");
      properties.put("format.temps.max.traitement", "5");
      properties.put("format.chemin.repertoire.temporaire",
            "src/test/resources/identification/");

      Assert.assertTrue("La vérification n'aurait pas du renvoyé une erreur",
            main.verifierConfFichierParam(properties, parametres));
      Assert.assertEquals("Le mode de vérification n'est pas correct",
            MODE_VERIFICATION.IDENT_VALID, parametres.getModeVerification());
      Assert.assertEquals("La requête lucène n'est pas correcte",
            "srt:41882050200023", parametres.getRequeteLucene());
      Assert.assertEquals("La taille du pool de thread n'est pas correcte", 5,
            parametres.getTaillePool());
      Assert.assertEquals("Le nombre max de documents n'est pas correct", 100,
            parametres.getNombreMaxDocs());
      Assert.assertEquals("La taille du pas d'exécution n'est pas correcte",
            10, parametres.getTaillePasExecution());
      Assert.assertFalse("La liste des métadonnées ne devrait pas être vide",
            parametres.getMetadonnees().isEmpty());
      Assert.assertEquals("La liste des métadonnées n'est pas correcte",
            "[srt, iti]", parametres.getMetadonnees().toString());
      Assert.assertEquals("Le temps max de traitement n'est pas correcte", 5,
            parametres.getTempsMaxTraitement());
   }

   @Test
   public void executePasArgument() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      main.execute(new String[] {});
   }

   @Test
   public void executeServiceNonReconnu() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      main.execute(new String[] { "SERVICE_NON_RECONNU" });
   }

   @Test
   public void executeServiceOkPasConfSAE() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      main
            .execute(new String[] { DocumentsExecutableMain.VERIFICATION_FORMAT });
   }

   @Test
   public void executeServiceOkConfSAEOkPasConfFichierParam() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      ClassPathResource configSae = new ClassPathResource(
            "/config/sae-config-test.properties");

      try {
         main.execute(new String[] {
               DocumentsExecutableMain.VERIFICATION_FORMAT,
               configSae.getFile().getAbsolutePath() });
      } catch (IOException e) {
         Assert
               .fail("Le fichier de configuration du SAE aurait du être trouvé");
      }
   }

   @Test
   public void executeServiceOkConfSAEOkPasFichierParam() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      ClassPathResource configSae = new ClassPathResource(
            "/config/sae-config-test.properties");

      try {
         main.execute(new String[] {
               DocumentsExecutableMain.VERIFICATION_FORMAT,
               configSae.getFile().getAbsolutePath(),
               "/config/fichierInexistant.properties" });
      } catch (IOException e) {
         Assert
               .fail("Le fichier de configuration du SAE aurait du être trouvé");
      }
   }

   @Test
   @DirtiesContext
   public void executeServiceValidationFormat() throws IOException {
      
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      ClassPathResource fichierParametrage = new ClassPathResource(
            "/config/formatValidation.properties");
      
      Properties properties = new Properties();
      if (!main.chargerFichierParam(fichierParametrage.getFile().getAbsolutePath(), properties)) {
         Assert.fail("Le fichier de paramètrage aurait du être trouvé");
      }

      FormatValidationParametres parametres = new FormatValidationParametres();
      if (!main.verifierConfFichierParam(properties, parametres)) {
         Assert.fail("Le fichier de paramètrage n'est pas correct");
      }
      
      //-- creation du mock
      TraitementService traitementService = EasyMock.createNiceMock(TraitementService.class);
      EasyMock.expect(traitementService.identifierValiderFichiers(parametres)).andReturn(10);
      EasyMock.replay(traitementService);
      
      //-- creation d'un contexte spring specifique
      GenericApplicationContext genericContext = new GenericApplicationContext();
      BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(TraitementService.class);
      genericContext.registerBeanDefinition("traitementService", beanDefinition.getBeanDefinition());
      genericContext.getBeanFactory().registerSingleton("traitementService", traitementService);
      genericContext.refresh();
      
      try {
         main.executeService(DocumentsExecutableMain.VERIFICATION_FORMAT, properties, genericContext);
      } catch (Throwable e) {
         Assert
               .fail("Une erreur non prévu s'est produite: " + e.getMessage());
      }
   }
   
   @Test
   @DirtiesContext
   public void executeServiceAddMeta() throws IOException {
      
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      ClassPathResource fichierParametrage = new ClassPathResource(
      "/config/addMetadatas.properties");
      
      Properties properties = new Properties();
      if (!main.chargerFichierParam(fichierParametrage.getFile().getAbsolutePath(), properties)) {
         Assert.fail("Le fichier de paramètrage aurait du être trouvé");
      }
      
      AddMetadatasParametres parametres = new AddMetadatasParametres();
      if (!main.vefierConfFichierParamAddMeta(properties, parametres)) {
         Assert.fail("Le fichier de paramètrage n'est pas correct");
      }
      
      //-- creation du mock
      TraitementService traitementService = EasyMock.createNiceMock(TraitementService.class);
      traitementService.addMetadatasToDocuments(parametres);
      EasyMock.expectLastCall();
      EasyMock.replay(traitementService);
      
      //-- creation d'un contexte spring specifique
      GenericApplicationContext genericContext = new GenericApplicationContext();
      BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(TraitementService.class);
      genericContext.registerBeanDefinition("traitementService", beanDefinition.getBeanDefinition());
      genericContext.getBeanFactory().registerSingleton("traitementService", traitementService);
      genericContext.refresh();
      
      try {
         main.executeService(DocumentsExecutableMain.ADD_METADATAS, properties, genericContext);
      } catch (Throwable e) {
         Assert
         .fail("Une erreur non prévu s'est produite: " + e.getMessage());
      }
   }
   
   @Test
   public void verifierConfFichierParamAddMetadonneesFromCSV() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      AddMetadatasParametres parametres = new AddMetadatasParametres();
      
      //-- Aucun Parametre
      Boolean result = main.vefierConfFichierParamAddMetaFromCSV(properties, parametres);
      Assert.assertFalse("La vérification aurait du renvoyé une erreur", result);
      
      //-- Taille pool
      properties.put("addMeta.taille.pool", "5");
      result = main.vefierConfFichierParamAddMetaFromCSV(properties, parametres);
      Assert.assertFalse("La vérification aurait du renvoyer une erreur", result);
      
      //-- Taille pas d'execution
      properties.put("addMeta.taille.pool", "5");
      properties.put("addMeta.taille.pas.execution", "10000");
      result = main.vefierConfFichierParamAddMetaFromCSV(properties, parametres);
      Assert.assertFalse("La vérification aurait du renvoyer une erreur", result);
      
      //-- Taille queue
      properties.put("addMeta.taille.pool", "5");
      properties.put("addMeta.taille.pas.execution", "10000");
      properties.put("addMeta.taille.queue", "20");
      result = main.vefierConfFichierParamAddMetaFromCSV(properties, parametres);
      Assert.assertFalse("La vérification aurait du renvoyer une erreur", result);
   }
   
   @Test
   public void verifierConfFichierParamAddMetadonneesFromCSVOK() {
      
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      Properties properties = new Properties();
      AddMetadatasParametres parametres = new AddMetadatasParametres();

      Boolean result = main.vefierConfFichierParamAddMetaFromCSV(properties,
            parametres);

      // -- Liste des métadonnées
      properties.put("addMeta.taille.pool", "5");
      properties.put("addMeta.taille.queue", "20");
      properties.put("addMeta.taille.pas.execution", "10000");
      properties.put("addMeta.chemin.fichier.csv",
            "src/test/resources/add-meta/addMetadatas.csv");

      result = main
            .vefierConfFichierParamAddMetaFromCSV(properties, parametres);

      Assert.assertTrue("La vérification ne doit renvoyer aucune erreur",
            result);

      Assert.assertEquals("La taille du pool de thread n'est pas correcte", 5,
            parametres.getTaillePool());
      Assert.assertEquals("La taille du pas d'exécution n'est pas correcte",
            10000, parametres.getTaillePasExecution());
      Assert.assertEquals("Le chemin du fichier n'est pas correct",
            "src/test/resources/add-meta/addMetadatas.csv", parametres
                  .getCheminFichier());
   }
   
   @Test
   @DirtiesContext
   public void executeServiceAddMetaFromCsv() throws IOException {
      
      DocumentsExecutableMain main = new DocumentsExecutableMain(contexteXml);
      ClassPathResource fichierParametrage = new ClassPathResource(
      "/config/addMetadatasFromCsv.properties");
      
      Properties properties = new Properties();
      if (!main.chargerFichierParam(fichierParametrage.getFile().getAbsolutePath(), properties)) {
         Assert.fail("Le fichier de paramètrage aurait du être trouvé");
      }
      
      AddMetadatasParametres parametres = new AddMetadatasParametres();
      if (!main.vefierConfFichierParamAddMetaFromCSV(properties, parametres)) {
         Assert.fail("Le fichier de paramètrage n'est pas correct");
      }
      
      //-- creation du mock
      TraitementService traitementService = EasyMock.createNiceMock(TraitementService.class);
      traitementService.addMetadatasToDocumentsFromCSV(parametres);
      EasyMock.expectLastCall();
      EasyMock.replay(traitementService);
      
      //-- creation d'un contexte spring specifique
      GenericApplicationContext genericContext = new GenericApplicationContext();
      BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(TraitementService.class);
      genericContext.registerBeanDefinition("traitementService", beanDefinition.getBeanDefinition());
      genericContext.getBeanFactory().registerSingleton("traitementService", traitementService);
      genericContext.refresh();
      
      try {
         main.executeService(DocumentsExecutableMain.ADD_METADATAS_FROM_CSV, properties, genericContext);
      } catch (Throwable e) {
         Assert
         .fail("Une erreur non prévu s'est produite: " + e.getMessage());
      }
   }
}
