package fr.urssaf.image.sae.documents.executable.service.cql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.documents.executable.service.FormatFichierService;
import fr.urssaf.image.sae.documents.executable.utils.Constantes;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.support.facade.ReferentielFormatSupportFacade;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.FormatFichierUtils;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;
import net.docubase.toolkit.model.document.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class FormatFichierServiceCqlTest {

  @Autowired
  private FormatFichierService formatFichierService;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @Autowired
  private ReferentielFormatSupportFacade referentielFormatSupportFacade;

  @Before
  public void setup() throws Exception {

    modeApiSupport.initTables(ModeGestionAPI.MODE_API.DATASTAX);
    createReferentielFormat();
  }

  private final File file = new File(
      "src/test/resources/identification/PdfaValide.pdf");

  private final File doc = new File(
      "src/test/resources/identification/word.doc");



  private Document createDocument(final String idFormat) {
    final Document document = new Document();
    document.setUuid(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    document.setArchivageDate(new Date());
    document.setType("2.3.1.1.12");
    document.addCriterion("cse", "CS1");
    document.addCriterion("apr", "GED");
    document.addCriterion("atr", "GED");
    document.addCriterion("ffi", idFormat);
    return document;
  }

  private List<String> createMetadonnees() {
    return Arrays.asList(Constantes.getMETADONNEES_DEFAULT());
  }

  @Test
  public void identifierFichierWithUnknownFormatException()
      throws FileNotFoundException, IOException {
    Assert
    .assertFalse(
                 "Le format 'idFormat' ne devrait pas être connu dans le référentiel des formats",
                 formatFichierService.identifierFichier("idFormat", file,
                                                        createDocument("idFormat"), createMetadonnees()));
  }

  @Test
  public void identifierFichierWithIdentifierInitialisationException()
      throws FileNotFoundException, IOException {
    Assert
    .assertFalse(
                 "L'identificateur du format 'format1' n'aurait pas du être instancié",
                 formatFichierService.identifierFichier("format1", file,
                                                        createDocument("format1"), createMetadonnees()));
  }

  @Test
  public void identifierFichierFormatNonValid() throws FileNotFoundException,
  IOException {
    Assert
    .assertFalse(
                 "L'identification du document n'aurait pas du réussir car ce n'est pas un pdf/a",
                 formatFichierService.identifierFichier("fmt/354", doc,
                                                        createDocument("fmt/354"), createMetadonnees()));
  }

  @Test
  public void identifierFichierFormatValid() throws FileNotFoundException,
  IOException {
    Assert.assertTrue("L'identification du document aurait du réussir",
                      formatFichierService.identifierFichier("fmt/354", file,
                                                             createDocument("fmt/354"), createMetadonnees()));
  }

  @Test
  public void validerFichier() throws UnknownFormatException,
  ValidatorInitialisationException, IOException,
  ValidatorUnhandledException {
    final ValidationResult validationResult = formatFichierService.validerFichier(
                                                                                  "fmt/354", file);
    Assert.assertNotNull(
                         "Le résultat de la validation ne devrait pas être null",
                         validationResult);
    Assert.assertTrue("Le fichier aurait du être valide", validationResult
                      .isValid());
  }

  private void createReferentielFormat() {
    final URL url = this.getClass().getResource("/cassandra-local-datasets/cassandra-local-dataset-sae-documents-executable.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "ReferentielFormat");
    final List<FormatFichier> listFormatFichier = FormatFichierUtils.convertRowsToFormatFichier(list);
    for (final FormatFichier formatFichier : listFormatFichier) {
      referentielFormatSupportFacade.create(formatFichier);
    }
  }
}