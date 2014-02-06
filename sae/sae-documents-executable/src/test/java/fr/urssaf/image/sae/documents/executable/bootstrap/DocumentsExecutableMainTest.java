package fr.urssaf.image.sae.documents.executable.bootstrap;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres.MODE_VERIFICATION;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class DocumentsExecutableMainTest {

   @Test
   public void verifierConfFichierParamAucunParametre() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      
      FormatValidationParametres parametres = main.verifierConfFichierParam(properties);
      Assert.assertNull("L'objet paramètres aurait du être null", parametres);
   }
   
   @Test
   public void verifierConfFichierParamModeVerification() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      
      properties.put("format.mode.verification", MODE_VERIFICATION.IDENTIFICATION.name());
      
      FormatValidationParametres parametres = main.verifierConfFichierParam(properties);
      Assert.assertNull("L'objet paramètres aurait du être null", parametres);
   }
   
   @Test
   public void verifierConfFichierParamModeVerificationRequeteLucene() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      
      properties.put("format.mode.verification", MODE_VERIFICATION.VALIDATION.name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      
      FormatValidationParametres parametres = main.verifierConfFichierParam(properties);
      Assert.assertNull("L'objet paramètres aurait du être null", parametres);
   }
   
   @Test
   public void verifierConfFichierParamModeVerificationRequeteLuceneTaillePool() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      
      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID.name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
      
      FormatValidationParametres parametres = main.verifierConfFichierParam(properties);
      Assert.assertNull("L'objet paramètres aurait du être null", parametres);
   }
   
   @Test
   public void verifierConfFichierParamModeVerificationRequeteLuceneTaillePoolNbMaxDocs() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      
      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID.name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
      properties.put("format.nombre.max.documents", "100");
      
      FormatValidationParametres parametres = main.verifierConfFichierParam(properties);
      Assert.assertNull("L'objet paramètres aurait du être null", parametres);
   }
   
   @Test
   public void verifierConfFichierParamModeVerificationRequeteLuceneTaillePoolNbMaxDocsPasExecution() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      
      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID.name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
      properties.put("format.nombre.max.documents", "100");
      properties.put("format.taille.pas.execution", "10");
      
      FormatValidationParametres parametres = main.verifierConfFichierParam(properties);
      Assert.assertNotNull("L'objet paramètres n'aurait pas du être null", parametres);
      Assert.assertEquals("Le mode de vérification n'est pas correct", MODE_VERIFICATION.IDENT_VALID, parametres.getModeVerification());
      Assert.assertEquals("La requête lucène n'est pas correcte", "srt:41882050200023", parametres.getRequeteLucene());
      Assert.assertEquals("La taille du pool de thread n'est pas correcte", 5, parametres.getTaillePool());
      Assert.assertEquals("Le nombre max de documents n'est pas correct", 100, parametres.getNombreMaxDocs());
      Assert.assertEquals("La taille du pas d'exécution n'est pas correcte", 10, parametres.getTaillePasExecution());
      Assert.assertTrue("La liste des métadonnées devrait être vide", parametres.getMetadonnees().isEmpty());
      Assert.assertEquals("Le temps max de traitement n'est pas correcte", 0, parametres.getTempsMaxTraitement());
   }
   
   @Test
   public void verifierConfFichierParamToutParametre() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      Properties properties = new Properties();
      
      properties.put("format.mode.verification", MODE_VERIFICATION.IDENT_VALID.name());
      properties.put("format.requete.lucene", "srt:41882050200023");
      properties.put("format.taille.pool", "5");
      properties.put("format.nombre.max.documents", "100");
      properties.put("format.taille.pas.execution", "10");
      properties.put("format.metadonnees", "srt,iti");
      properties.put("format.temps.max.traitement", "5");
      
      FormatValidationParametres parametres = main.verifierConfFichierParam(properties);
      Assert.assertNotNull("L'objet paramètres n'aurait pas du être null", parametres);
      Assert.assertEquals("Le mode de vérification n'est pas correct", MODE_VERIFICATION.IDENT_VALID, parametres.getModeVerification());
      Assert.assertEquals("La requête lucène n'est pas correcte", "srt:41882050200023", parametres.getRequeteLucene());
      Assert.assertEquals("La taille du pool de thread n'est pas correcte", 5, parametres.getTaillePool());
      Assert.assertEquals("Le nombre max de documents n'est pas correct", 100, parametres.getNombreMaxDocs());
      Assert.assertEquals("La taille du pas d'exécution n'est pas correcte", 10, parametres.getTaillePasExecution());
      Assert.assertFalse("La liste des métadonnées ne devrait pas être vide", parametres.getMetadonnees().isEmpty());
      Assert.assertEquals("La liste des métadonnées n'est pas correcte", "[srt, iti]",parametres.getMetadonnees().toString());
      Assert.assertEquals("Le temps max de traitement n'est pas correcte", 5, parametres.getTempsMaxTraitement());
   }
   
   @Test
   public void executePasArgument() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      main.execute(new String[] {});
   }
   
   @Test
   public void executeServiceNonReconnu() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      main.execute(new String[] { "SERVICE_NON_RECONNU" });
   }
   
   @Test
   public void executeServiceOkPasConfSAE() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      main.execute(new String[] { DocumentsExecutableMain.VERIFICATION_FORMAT });
   }
   
   @Test
   public void executeServiceOkConfSAEOkPasConfFichierParam() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      ClassPathResource configSae = new ClassPathResource("/config/sae-config-test.properties");
      
      try {
         main.execute(new String[] { DocumentsExecutableMain.VERIFICATION_FORMAT, configSae.getFile().getAbsolutePath() });
      } catch (IOException e) {
         Assert.fail("Le fichier de configuration du SAE aurait du être trouvé");
      }
   }
   
   @Test
   public void executeServiceOkConfSAEOkPasFichierParam() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      ClassPathResource configSae = new ClassPathResource("/config/sae-config-test.properties");
      
      try {
         main.execute(new String[] { DocumentsExecutableMain.VERIFICATION_FORMAT, configSae.getFile().getAbsolutePath(), "/config/fichierInexistant.properties" });
      } catch (IOException e) {
         Assert.fail("Le fichier de configuration du SAE aurait du être trouvé");
      }
   }
   
   @Test
   @DirtiesContext
   public void executeToutOk() {
      DocumentsExecutableMain main = new DocumentsExecutableMain("/applicationContext-sae-documents-executable-test.xml");
      ClassPathResource configSae = new ClassPathResource("/config/sae-config-test.properties");
      ClassPathResource fichierParametrage = new ClassPathResource("/config/formatValidation.properties");
      
      try {
         main.execute(new String[] { DocumentsExecutableMain.VERIFICATION_FORMAT, configSae.getFile().getAbsolutePath(), fichierParametrage.getFile().getAbsolutePath() });
      } catch (IOException e) {
         Assert.fail("Le fichier de configuration du SAE ou le fichier de paramètrage aurait du être trouvé");
      }
   }
}
