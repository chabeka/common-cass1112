/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.bootstrap;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class BootStrapTest {

   /**
    * 
    */
   private static final String ILLEGAL_ARGUMENT_MESSAGE = "une erreur IllegalArgumentException était attendue";

   @Test
   public void testErreurSansArguments() {

      try {
         BootStrap.main(new String[0]);
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
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
         Assert.assertEquals("le message d'erreur doit etre correct",
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
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le fichier de configuration est inexistant", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testListeFichierEcritureExistant() {

      ClassPathResource resource = new ClassPathResource(
            "cassandra/cassandra-connection.properties");

      try {
         BootStrap.main(new String[] { "listeOrgs",
               resource.getFile().getAbsolutePath(),
               resource.getFile().getAbsolutePath() });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le fichier de sortie est existe déjà", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testMajFichierCorrespInexistant() {

      ClassPathResource resource = new ClassPathResource(
            "cassandra/cassandra-connection.properties");

      try {
         BootStrap.main(new String[] { "maj",
               resource.getFile().getAbsolutePath(), "cassandra/inexistant" });
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le fichier de correspondances est inexistant", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testDocumentsNbreParametresErrone() throws IOException {

      ClassPathResource resource = new ClassPathResource(
            "cassandra/cassandra-connection.properties");
      try {
         BootStrap.main(new String[] { "listeDocs",
               resource.getFile().getAbsolutePath() });

         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert
               .assertTrue(
                     "le message d'erreur doit etre correct",
                     exception
                           .getMessage()
                           .startsWith(
                                 "la commande est incorrecte. Les paramètres sont les suivants"));

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);
      }
   }

   @Test
   public void testDocumentsFichierDestExistant() throws IOException {

      File tempFile = File.createTempFile("bootstrap", ".log");

      ClassPathResource resource = new ClassPathResource(
            "cassandra/cassandra-connection.properties");
      try {
         BootStrap.main(new String[] { "listeDocs",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), null });

         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le fichier de sortie est existe déjà", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } finally {
         FileUtils.deleteQuietly(tempFile);
      }
   }

   @Test
   public void testDocumentsFichierPropertiesInexistant() throws IOException {

      File tempFile = File.createTempFile("bootstrap", ".log");
      FileUtils.deleteQuietly(tempFile);

      ClassPathResource resource = new ClassPathResource(
            "cassandra/cassandra-connection.properties");
      try {
         BootStrap.main(new String[] { "listeDocs",
               resource.getFile().getAbsolutePath(),
               tempFile.getAbsolutePath(), "src/inexistant" });

         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le fichier de correspondance est inexistant", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_ARGUMENT_MESSAGE);

      }
   }

}
