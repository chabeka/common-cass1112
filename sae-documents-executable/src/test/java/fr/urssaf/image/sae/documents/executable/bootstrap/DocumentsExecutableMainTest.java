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

import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres.MODE_VERIFICATION;
import fr.urssaf.image.sae.documents.executable.service.TraitementService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class DocumentsExecutableMainTest {
   
   @Test
   public void verifierConfFichierParamAucunParametre() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      Assert.assertFalse("La vérification aurait du renvoyé une erreur", main
            .verifierConfFichierParam(properties, parametres));
   }

   @Test
   public void verifierConfFichierParamModeVerification() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification",
            MODE_VERIFICATION.IDENTIFICATION.name());

      Assert.assertFalse("La vérification aurait du renvoyé une erreur", main
            .verifierConfFichierParam(properties, parametres));
   }

   @Test
   public void verifierConfFichierParamModeVerificationRequeteLucene() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
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
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
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
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
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
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID
            .name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
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
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
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
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      FormatValidationParametres parametres = new FormatValidationParametres();

      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID
            .name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
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
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
      main.execute(new String[] {});
   }

   @Test
   public void executeServiceNonReconnu() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
      main.execute(new String[] { "SERVICE_NON_RECONNU" });
   }

   @Test
   public void executeServiceOkPasConfSAE() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
      main
            .execute(new String[] { DocumentsExecutableMain.VERIFICATION_FORMAT });
   }

   @Test
   public void executeServiceOkConfSAEOkPasConfFichierParam() {
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
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
      DocumentsExecutableMain main = new DocumentsExecutableMain(
            "/applicationContext-sae-documents-executable-test.xml");
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
   public void executeService() throws IOException {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
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
      
      // creation du mock
      TraitementService traitementService = EasyMock.createNiceMock(TraitementService.class);
      EasyMock.expect(traitementService.identifierValiderFichiers(parametres)).andReturn(10);
      EasyMock.replay(traitementService);
      
      // creation d'un contexte spring specifique
      GenericApplicationContext genericContext = new GenericApplicationContext();
      BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(TraitementService.class);
      genericContext.registerBeanDefinition("traitementService", beanDefinition.getBeanDefinition());
      genericContext.getBeanFactory().registerSingleton("traitementService", traitementService);
      genericContext.refresh();
      
      try {
         main.executeService(DocumentsExecutableMain.VERIFICATION_FORMAT, genericContext, parametres);
      } catch (Throwable e) {
         Assert
               .fail("Une erreur non prévu s'est produite: " + e.getMessage());
      }
   }
}
