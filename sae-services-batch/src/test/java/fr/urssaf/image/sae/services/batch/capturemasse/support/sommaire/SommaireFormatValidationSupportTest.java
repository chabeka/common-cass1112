/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFormatValidationException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-services-batch-test.xml"})
public class SommaireFormatValidationSupportTest {

  @Autowired
  private SommaireFormatValidationSupport support;

  @Autowired
  private EcdeTestTools ecdeTestTools;

  private EcdeTestSommaire ecdeTestSommaire;

  @Before
  public void init() {
    ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
  }

  @After
  public void end() {
    try {
      ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
    }
    catch (final IOException e) {
      // rien à faire
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEcdeObligatoire()
      throws CaptureMasseSommaireFormatValidationException {

    support.validationSommaire(null);
    Assert.fail("sortie aspect attendue");

  }

  @Test
  public void testSommaireErrone() throws IOException,
      CaptureMasseSommaireFormatValidationException {

    final File ecdeDirectory = ecdeTestSommaire.getRepEcde();
    final File sommaire = new File(ecdeDirectory, "sommaire.xml");

    final ClassPathResource resSommaire = new ClassPathResource(
                                                                "sommaire/sommaire_format_failure.xml");
    final FileOutputStream fos = new FileOutputStream(sommaire);
    IOUtils.copy(resSommaire.getInputStream(), fos);

    try {

      support.validationSommaire(sommaire);

      Assert.fail("la validation doit lever une exception de type CaptureMasseSommaireFormatValidationException");

    }
    catch (final CaptureMasseSommaireFormatValidationException e) {

      Assert.assertEquals("le message de l'exception est inattendu",
                          "Aucun document du sommaire ne sera traité dans le SAE.",
                          e.getMessage());
    }

  }

  @Test
  public void testSommaireValide() {

    try {

      final File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      final File sommaire = new File(ecdeDirectory, "sommaire.xml");

      final ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      final FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      support.validationSommaire(sommaire);
    }
    catch (final IOException e) {
      Assert.fail("le fichier sommaire.xml doit être valide");
    }
    catch (final CaptureMasseSommaireFormatValidationException e) {
      Assert.fail("le fichier sommaire.xml doit être valide");
    }

  }

  @Test
  public void testSommaireValideTransfert() {

    try {

      final File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      final File sommaire = new File(ecdeDirectory, "sommaire.xml");

      final ClassPathResource resSommaire = new ClassPathResource(
                                                                  "sommaire_transf.xml");
      final FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      support.validationSommaire(sommaire);
    }
    catch (final IOException e) {
      Assert.fail("le fichier sommaire.xml doit être valide");
    }
    catch (final CaptureMasseSommaireFormatValidationException e) {
      Assert.fail("le fichier sommaire.xml doit être valide");
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testFichierSommaireBatchFichierNull()
      throws CaptureMasseSommaireFormatValidationException,
      CaptureMasseSommaireFileNotFoundException {

    support.validerModeBatch(null, "RR");

    Assert.fail("exception attendue");
  }

  @Test(expected = CaptureMasseSommaireFileNotFoundException.class)
  public void testFichierSommaireBatchModeBatchVide()
      throws CaptureMasseSommaireFormatValidationException,
      CaptureMasseSommaireFileNotFoundException {

    support.validerModeBatch(new File(""), "");

    Assert.fail("exception attendue");
  }

  @Test(expected = CaptureMasseSommaireFormatValidationException.class)
  public void testBatchModeNonAttendu()
      throws CaptureMasseSommaireFormatValidationException,
      CaptureMasseSommaireFileNotFoundException {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_success.xml");

    support.validerModeBatch(sommaire, "RR");

    Assert.fail("exception attendue");

  }

  @Test
  public void testBatchModeValide() {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_success.xml");

    try {
      support.validerModeBatch(sommaire, "TOUT_OU_RIEN");
    }
    catch (final CaptureMasseSommaireFormatValidationException e) {
      Assert.fail("on attend un retour valide");
    }
    catch (final CaptureMasseSommaireFileNotFoundException e) {
      Assert.fail("on attend un retour valide");
    }

  }

  @Test(expected = CaptureMasseSommaireFormatValidationException.class)
  public void testUniciteIdGedFailure() throws CaptureMasseSommaireFormatValidationException {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_idged_failure.xml");

    support.validerUniciteIdGed(sommaire);
  }

  @Test
  public void testUniciteIdGedSuccess() {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_idged_succes.xml");

    try {
      support.validerUniciteIdGed(sommaire);
    }
    catch (final CaptureMasseSommaireFormatValidationException e) {
      Assert.fail("on attend un retour valide");
    }

  }

  @Test
  public void testUniciteMetaFailure() {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_idged_failure.xml");

    final List<Integer> indexListAttendu = Arrays.asList(0, 2);

    List<Integer> indexList = null;
    try {
      indexList = support.validerUniciteMeta(sommaire, "IdGed");
    }
    catch (final IOException e) {
      Assert.fail("Pas d'exception attendu");
    }

    Assert.assertNotNull(indexList);
    Assert.assertArrayEquals(indexList.toArray(), indexListAttendu.toArray());
  }

  @Test
  public void testUniciteMetaSuccess() {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_idged_succes.xml");

    final List<Integer> indexListAttendu = Arrays.asList();

    List<Integer> indexList = null;
    try {
      indexList = support.validerUniciteMeta(sommaire, "IdGed");
    }
    catch (final IOException e) {
      Assert.fail("Pas d'exception attendu");
    }

    Assert.assertNotNull(indexList);
    Assert.assertArrayEquals(indexList.toArray(), indexListAttendu.toArray());
  }

  @Test
  public void testUniciteTagFailure() {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_uuid_failure.xml");

    final List<Integer> indexListAttendu = Arrays.asList(0, 2);

    List<Integer> indexList = null;
    try {
      indexList = support.validerUniciteTag(sommaire, "UUID");
    }
    catch (final IOException e) {
      Assert.fail("Pas d'exception attendu");
    }

    Assert.assertNotNull(indexList);
    Assert.assertArrayEquals(indexList.toArray(), indexListAttendu.toArray());
  }

  @Test
  public void testUniciteTagSuccess() {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_uuid_succes.xml");

    final List<Integer> indexListAttendu = Arrays.asList();

    List<Integer> indexList = null;
    try {
      indexList = support.validerUniciteTag(sommaire, "UUID");
    }
    catch (final IOException e) {
      Assert.fail("Pas d'exception attendu");
    }

    Assert.assertNotNull(indexList);
    Assert.assertArrayEquals(indexList.toArray(), indexListAttendu.toArray());
  }

  @Test
  public void testUniciteTagFailure2() {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_uuid_failure2.xml");

    final List<Integer> indexListAttendu = Arrays.asList(0, 2);

    List<Integer> indexList = null;
    try {
      indexList = support.validerUniciteTag(sommaire, "UUID");
    }
    catch (final IOException e) {
      Assert.fail("Pas d'exception attendu");
    }

    Assert.assertNotNull(indexList);
    Assert.assertArrayEquals(indexList.toArray(), indexListAttendu.toArray());
  }

  @Test
  public void testUniciteTagSuccess2() {

    final File sommaire = new File(
                                   "src/test/resources/sommaire/sommaire_uuid_succes2.xml");

    final List<Integer> indexListAttendu = Arrays.asList();

    List<Integer> indexList = null;
    try {
      indexList = support.validerUniciteTag(sommaire, "UUID");
    }
    catch (final IOException e) {
      Assert.fail("Pas d'exception attendu");
    }

    Assert.assertNotNull(indexList);
    Assert.assertArrayEquals(indexList.toArray(), indexListAttendu.toArray());
  }

}
