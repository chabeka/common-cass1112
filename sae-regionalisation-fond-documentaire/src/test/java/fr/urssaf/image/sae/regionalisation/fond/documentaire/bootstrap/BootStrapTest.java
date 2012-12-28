/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.bootstrap;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class BootStrapTest {

   /**
    * 
    */
   private static final String CASSANDRA_PROPERTIES = "cassandra/cassandra-connection.properties";
   private static final String MESSAGE_CORRECT = "le message d'erreur doit etre correct";
   private static final String ILLEGAL_ARGUMENT_MESSAGE = "une erreur IllegalArgumentException était attendue";

   private File tempFile;

   @Before
   public void init() throws IOException {
      tempFile = File.createTempFile("bootstrap", ".log");
   }

   @After
   public void end() {
      FileUtils.deleteQuietly(tempFile);
   }

   @Test
   public void testErreurSansArguments() {

      try {
         BootStrap.main(new String[0]);
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "la ligne de commande est erronée", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testModeErrone() {

      try {
         BootStrap.main(new String[] { "un", "deux", "trois" });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "La commande désirée est inexistante", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testModeFichierConfInexistant() {

      try {
         BootStrap
               .main(new String[] { "listeOrgs", "conf/inexistant", "trois" });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "le fichier de configuration est inexistant", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testListeFichierEcritureExistant() {

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);

      try {
         BootStrap.main(new String[] { "listeOrgs",
               resource.getFile().getAbsolutePath(),
               resource.getFile().getAbsolutePath() });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "le fichier de sortie est existe déjà", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testDocumentsNbreParametresErrone() throws IOException {

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);
      try {
         BootStrap.main(new String[] { "listeDocs",
               resource.getFile().getAbsolutePath() });

         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue(MESSAGE_CORRECT, exception.getMessage().startsWith(
               "la commande est incorrecte. Les paramètres sont les suivants"));

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testDocumentsFichierDestExistant() throws IOException {

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);
      try {
         BootStrap.main(new String[] { "listeDocs",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), null });

         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "le fichier de sortie est existe déjà", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } finally {
         FileUtils.deleteQuietly(tempFile);
      }
   }

   @Test
   public void testDocumentsFichierPropertiesInexistant() throws IOException {

      FileUtils.deleteQuietly(tempFile);

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);
      try {
         BootStrap.main(new String[] { "listeDocs",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), "src/inexistant" });

         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "le fichier de correspondance est inexistant", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      }
   }

   @Test
   public void testMajNombreParametresErronne() throws IOException {
      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);

      try {
         BootStrap.main(new String[] { "maj",
               resource.getFile().getAbsolutePath() });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue(MESSAGE_CORRECT, exception.getMessage().startsWith(
               "la commande est incorrecte. Les paramètres sont les suivants"));

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      }

   }

   @Test
   public void testMajFichierDonneesInexistant() throws IOException {
      FileUtils.deleteQuietly(tempFile);

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);

      try {
         BootStrap
               .main(new String[] { "maj",
                     resource.getFile().getAbsolutePath(),
                     tempFile.getAbsolutePath(), tempFile.getAbsolutePath(),
                     "1", "2" });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "le fichier de données est inexistant", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }

   }

   @Test
   public void testMajFichierCorrespondanceInexistant() throws IOException {

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);

      try {
         BootStrap.main(new String[] { "maj",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), "fichier/inexistant", "1", "2" });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "le fichier de correspondances est inexistant", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }

   }

   @Test
   public void testMajIndexDepartNonNumerique() throws IOException {

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);

      try {
         BootStrap.main(new String[] { "maj",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), tempFile.getAbsolutePath(), "12s",
               "2" });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "l'index du premier enregistrement doit être un numérique",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }

   }

   @Test
   public void testMajIndexFinNonNumerique() throws IOException {

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);

      try {
         BootStrap.main(new String[] { "maj",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), tempFile.getAbsolutePath(), "1",
               "45s" });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "l'index du dernier enregistrement doit être un numérique",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }

   }

   @Test
   public void testMajIndexDepartSuperieurIndexFin() throws IOException {

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);

      try {
         BootStrap.main(new String[] { "maj",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), tempFile.getAbsolutePath(), "45",
               "12" });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "l'index du premier enregistrement doit être un supérieur "
                     + "à l'index du dernier enregistrement", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }

   }
   
   
   @Test
   public void testNonIntegresNbreParametresErrone() throws IOException {

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);
      try {
         BootStrap.main(new String[] { "listeNonIntegres",
               resource.getFile().getAbsolutePath() });

         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertTrue(MESSAGE_CORRECT, exception.getMessage().startsWith(
               "la commande est incorrecte. Les paramètres sont les suivants"));

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testNonIntegresFichierDestExistant() throws IOException {

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);
      try {
         BootStrap.main(new String[] { "listeNonIntegres",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), null });

         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "le fichier de sortie est existe déjà", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } finally {
         FileUtils.deleteQuietly(tempFile);
      }
   }

   @Test
   public void testNonIntegresFichierPropertiesInexistant() throws IOException {

      FileUtils.deleteQuietly(tempFile);

      ClassPathResource resource = new ClassPathResource(CASSANDRA_PROPERTIES);
      try {
         BootStrap.main(new String[] { "listeNonIntegres",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), "src/inexistant" });

         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_CORRECT,
               "le fichier de correspondance est inexistant", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      }
   }

}
