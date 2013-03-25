package fr.urssaf.image.sae.ecde;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;

/**
 * Tests unitaires de la classe {@link EcdeSourceManagerImpl}
 */
@SuppressWarnings( { "PMD.MethodNamingConventions", "PMD.LongVariable" })
public class EcdeSourceManagerImplTest {

   /**
    * Cas de test : On essaye de charger un fichier qui n'existe pas<br>
    * <br>
    * Résultat attendu : une exception de type EcdeBadFileException avec le
    * message "Le fichier : %sn'existe pas.", où % est le chemin complet du
    * fichier en question
    */
   @Test
   public void load_failure_EcdeBadFileException() {

      // Instantiation de l'objet à tester
      EcdeSourceManager manager = new EcdeSourceManagerImpl();

      // Construction d'un chemin complet de fichier inexistant
      // pour que cela fonctionne à la fois sous Windows et sous Linux
      File tempDirectory = FileUtils.getTempDirectory();
      File fichierExistePas = new File(tempDirectory, UUID.randomUUID()
            .toString());
      String cheminFichierExistePas = fichierExistePas.getAbsolutePath();

      try {

         // Appel de la méthode à tester
         manager.load(new File(cheminFichierExistePas));

         // Si on arrive jusque là, le test est en échec
         // En effet, on attendait une exception
         fail("On attendait une exception EcdeBadFileException");

      } catch (EcdeBadFileException e) {

         // Vérification du message
         String expected = String.format("Le fichier : %sn'existe pas.",
               cheminFichierExistePas);
         String actual = e.getMessage();
         assertEquals("Le message de l'exception attendue est incorrect",
               expected, actual);

      }

   }

   /**
    * Cas de test : On charge un fichier qui existe et dont la syntaxe est
    * correcte<br>
    * <br>
    * Résultat attendu : l'objet du modèle est chargé, avec les bonnes valeurs
    * 
    * @throws IOException
    * @throws EcdeBadFileException
    */
   @Test
   public void load_success() throws EcdeBadFileException, IOException {

      // Chargement du fichier de test depuis les ressources du projet
      ClassPathResource ressource = new ClassPathResource(
            "/ecdesources/ecdesources_ok.xml");

      // Instantiation de l'objet à tester
      EcdeSourceManager manager = new EcdeSourceManagerImpl();

      // Appel de la méthode à tester
      EcdeSources ecdeSources = manager.load(ressource.getFile());

      // Vérifications

      assertNotNull("L'objet modèle chargé depuis le fichier est null",
            ecdeSources);

      EcdeSource[] tabSource = ecdeSources.getSources();
      assertNotNull("Erreur technique, probableemnt", tabSource);

      assertEquals("Le nombre d'ECDE configuré ne correspond pas à l'attendu",
            3, tabSource.length);

      assertEquals("L'host de l'ECDE #1 est incorrect", "host1", tabSource[0]
            .getHost());
      assertEquals("Le basePath de l'ECDE #1 est incorrect", "basePath1",
            tabSource[0].getBasePath().getName());
      assertEquals("Le flag local de l'ECDE #1 est incorrect", false,
            tabSource[0].isLocal());

      assertEquals("L'host de l'ECDE #2 est incorrect", "host2", tabSource[1]
            .getHost());
      assertEquals("Le basePath de l'ECDE #2 est incorrect", "basePath2",
            tabSource[1].getBasePath().getName());
      assertEquals("Le flag local de l'ECDE #2 est incorrect", true,
            tabSource[1].isLocal());

      assertEquals("L'host de l'ECDE #3 est incorrect", "host3", tabSource[2]
            .getHost());
      assertEquals("Le basePath de l'ECDE #3 est incorrect", "basePath3",
            tabSource[2].getBasePath().getName());
      assertEquals("Le flag local de l'ECDE #3 est incorrect", false,
            tabSource[2].isLocal());

   }

}
