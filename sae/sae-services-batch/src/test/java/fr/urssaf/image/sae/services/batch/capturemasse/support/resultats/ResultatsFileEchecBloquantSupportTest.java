/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.ErreurTraitement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml" })
public class ResultatsFileEchecBloquantSupportTest {

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private ResultatsFileEchecBloquantSupport support;

   private EcdeTestSommaire ecdeTestSommaire;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeDirectoryObligatoire() {
      ErreurTraitement erreurTraitement = new ErreurTraitement();
      erreurTraitement
            .setException(new CaptureMasseSommaireFormatValidationException(
                  null));
      support.writeResultatsFile(null, erreurTraitement);
   }

   @Test(expected = IllegalArgumentException.class)
   public void erreurObligatoire() {
      support.writeResultatsFile(new File(""), null);
   }

   @Test
   public void testErreurBloquante() {

      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      CaptureMasseSommaireFormatValidationException erreur = new CaptureMasseSommaireFormatValidationException(
            new ParserConfigurationException(
                  "erreur lors de la configuration du parser"));
      ErreurTraitement erreurTraitement = new ErreurTraitement();
      erreurTraitement.setCodeErreur(Constantes.ERR_BUL001);
      erreurTraitement.setMessageErreur(StringUtils.EMPTY);
      erreurTraitement.setException(erreur);
      support.writeResultatsFile(ecdeDirectory, erreurTraitement);

      File resultatsFile = new File(ecdeDirectory, "resultats.xml");

      Assert.assertTrue("le fichier resultats.xml doit exister", resultatsFile
            .exists());
      Assert.assertTrue("le fichier resultats.xml doit etre non vide",
            resultatsFile.length() > 0);

   }
}
