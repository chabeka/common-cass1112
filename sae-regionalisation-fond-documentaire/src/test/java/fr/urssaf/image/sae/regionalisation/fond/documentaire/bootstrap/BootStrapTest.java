/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.bootstrap;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class BootStrapTest {

   @Test
   public void testErreurSansArguments() {

      try {
         BootStrap.main(new String[0]);
         Assert.fail("une erreur IllegalArgumentException était attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "la ligne de commande est erronée", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("une erreur IllegalArgumentException était attendue");
      }
   }

   @Test
   public void testModeErrone() {

      try {
         BootStrap.main(new String[] { "un", "deux", "trois" });
         Assert.fail("une erreur IllegalArgumentException était attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le mode voulu n'existe pas", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("une erreur IllegalArgumentException était attendue");
      }
   }

   @Test
   public void testModeFichierConfInexistant() {

      try {
         BootStrap.main(new String[] { "liste", "conf/inexistant", "trois" });
         Assert.fail("une erreur IllegalArgumentException était attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le fichier de configuration est inexistant", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail("une erreur IllegalArgumentException était attendue");
      }
   }

   @Test
   public void testListeFichierEcritureExistant() {

      ClassPathResource resource = new ClassPathResource(
            "cassandra/cassandra-connection.properties");

      try {
         BootStrap.main(new String[] { "liste",
               resource.getFile().getAbsolutePath(),
               resource.getFile().getAbsolutePath() });
         Assert.fail("une erreur IllegalArgumentException était attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le fichier de sortie est existe déjà", exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("une erreur IllegalArgumentException était attendue");
      }
   }

   @Test
   public void testMajFichierCorrespInexistant() {

      ClassPathResource resource = new ClassPathResource(
            "cassandra/cassandra-connection.properties");

      try {
         BootStrap.main(new String[] { "maj",
               resource.getFile().getAbsolutePath(), "cassandra/inexistant" });
         Assert.fail("une erreur IllegalArgumentException était attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le fichier de correspondances est inexistant", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail("une erreur IllegalArgumentException était attendue");
      }
   }

}
