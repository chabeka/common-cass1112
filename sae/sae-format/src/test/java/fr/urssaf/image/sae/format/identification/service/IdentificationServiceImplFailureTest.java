package fr.urssaf.image.sae.format.identification.service;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
import fr.urssaf.image.sae.format.identification.service.impl.IdentificationServiceImpl;
import junit.framework.Assert;

/**
 * 
 * Classe testant les services de la classe {@link IdentificationServiceImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-failure-test.xml" })
public class IdentificationServiceImplFailureTest {

  @Autowired
  private IdentificationServiceImpl identificationService;

  @Before
  public void setup() throws Exception {

    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(Constantes.CF_REFERENTIEL_FORMAT, ModeGestionAPI.MODE_API.HECTOR);
    ModeGestionAPI.setListeCfsModes(modesApiTest);

  }
  @Test
  public void identifyServiceFailureBeanIntrouvable()
      throws IdentificationRuntimeException, UnknownFormatException,
      IOException {

    // Récupération du fichier de test depuis les ressources
    final ClassPathResource ressource = new ClassPathResource(
        "/identification/fmt-354.pdf");

    try {
      identificationService.identifyFile("fmt/354", ressource.getFile());
      Assert
      .fail("Une exception IdentifierInitialisationException aurait dû être levée");

    } catch (final IdentifierInitialisationException ex) {
      Assert
      .assertEquals(
                    "Le message de l'exception est incorrect",
                    "Il n'est pas possible de récupérer une instance de l'identificateur.",
                    ex.getMessage());
    }

  }

}
