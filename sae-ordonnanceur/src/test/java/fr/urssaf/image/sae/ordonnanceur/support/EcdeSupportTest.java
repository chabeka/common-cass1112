package fr.urssaf.image.sae.ordonnanceur.support;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class EcdeSupportTest {

   @Autowired
   private EcdeSupport ecdeSupport;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

   @Before
   public void before() {

      // Création d'un répertoire ECDE permettant d'y déposer un sommaire.xml
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      // Dépôt d'un sommaire.xml vide dans ce répertoire
      // On n'aura besoin que de vérifier sa présence
      File fileSom = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      try {
         fileSom.createNewFile();
      } catch (IOException e) {
         throw new OrdonnanceurRuntimeException(e);
      }

   }

   @After
   public void after() {

      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien à faire
      }

   }

   @Test
   public void isLocal_true() {

      // ecde.testunit.recouv est configuré à true
      Assert.assertTrue("ecde.testunit.recouv est local", ecdeSupport
            .isLocal(ecdeTestSommaire.getUrlEcde()));

   }

   @Test
   public void isLocal_false() {

      // ecde.cer69.recouv est configuré à false
      Assert
            .assertFalse(
                  "ecde.cer69.recouv n'est pas local",
                  ecdeSupport
                        .isLocal(URI
                              .create("ecde://ecde.cer69.recouv/CS/20130916/sommaire.xml")));

   }

   @Test
   public void isEcdeDisponible_success_true() {

      boolean ecdeDispo = ecdeSupport.isEcdeDisponible(ecdeTestSommaire
            .getUrlEcde());

      Assert.assertTrue("L'URL ECDE devrait être disponible", ecdeDispo);

   }

   @Test
   public void isEcdeDisponible_success_false() {

      boolean ecdeDispo = ecdeSupport.isEcdeDisponible(ecdeTestSommaire
            .getUrlEcde());

      Assert.assertTrue("L'URL ECDE devrait être disponible", ecdeDispo);

   }

   @Test
   public void isEcdeDisponible_failure_EcdeBadURLException() {

      try {

         // L'ECDE toto.tata.tutu n'existe pas => on doit obtenir une
         // EcdeBadURLException
         // dans l'exception mère d'une OrdonnanceurRuntimeException
         ecdeSupport.isEcdeDisponible(URI
               .create("ecde://toto.tata.tutu/CS/20130916/01/sommaire.xml"));

         Assert.fail("On aurait dû avoir une exception");

      } catch (OrdonnanceurRuntimeException ex) {

         if (ex.getCause() == null) {

            Assert
                  .fail("L'exception mère aurait dû être une EcdeBadURLException, et non pas null");

         } else if (!(ex.getCause() instanceof EcdeBadURLException)) {

            Assert
                  .fail("L'exception mère aurait dû être une EcdeBadURLException, et non pas : "
                        + ex.getCause().toString());

         }

      }

   }

   @Test
   public void isEcdeDisponible_failure_EcdeBadURLFormatException() {

      try {

         // L'URL ECDE est mal formée. On doit récupérer une exception
         // EcdeBadURLFormatException dans l'exception mère d'une
         // OrdonnanceurRuntimeException
         ecdeSupport
               .isEcdeDisponible(URI
                     .create("ecdeNonValide://toto.tata.tutu/CS/20130916/01/sommaire.xml"));

         Assert.fail("On aurait dû avoir une exception");

      } catch (OrdonnanceurRuntimeException ex) {

         if (ex.getCause() == null) {

            Assert
                  .fail("L'exception mère aurait dû être une EcdeBadURLFormatException, et non pas null");

         } else if (!(ex.getCause() instanceof EcdeBadURLFormatException)) {

            Assert
                  .fail("L'exception mère aurait dû être une EcdeBadURLFormatException, et non pas : "
                        + ex.getCause().toString());

         }

      }

   }

}
