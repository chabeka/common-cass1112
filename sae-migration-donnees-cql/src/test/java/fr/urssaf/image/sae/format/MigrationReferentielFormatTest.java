/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.format;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.dao.support.cql.ReferentielFormatCqlSupport;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.utils.CompareUtils;


/**
 * (AC75095351) Classe de test migration des referentielFormat
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationReferentielFormatTest {

  @Autowired
  private ReferentielFormatCqlSupport supportCql;

  @Autowired
  private ReferentielFormatSupport supportThrift;

  @Autowired
  MigrationReferentielFormat migrationReferentielFormat;

  @Autowired
  private CassandraServerBean server;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationReferentielFormatTest.class);



  String[] listIdFormat = {"xls", "html", "pptm", "fmt/353", "pdf"};

  String[] listTypeMime = {"xls", "text/html", "application/vnd.ms-powerpoint.presentation.macroEnabled.12", "image/tiff", "application/pdf"};

  String[] listExtension = {"xls", "html", "pptm", "tif,tiff", "pdf"};

  String[] listDescription = {"Fichier MS Excel version 97/2003",
                              "Fichier à langage de balisage d'hypertexte (HyperText Markup Language)",
                              "Fichier MS PowerPoint macro", "Fichier TIFF", "Tous fichiers PDF sans précision de version"};



  Boolean[] listAutoriseGED = {true, false, false, true, true};

  Boolean[] listVisualisable = {true, true, false, false, true};

  String[] listValidator = {"", "", "", "", ""};

  String[] listIdentificateurs = {"", "", "", "", ""};

  String[] listConvertisseur = {"", "", "", "tiffToPdfConvertisseurImpl", "pdfSplitterImpl"};


  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données ReferentielFormat vers referentielFormatcql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      final List<FormatFichier> listThrift = supportThrift.findAll();
      migrationReferentielFormat.migrationFromThriftToCql();
      final List<FormatFichier> listCql = supportCql.findAll();
      Assert.assertEquals(listThrift.size(), listIdFormat.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  private void populateTableThrift() {
    int i = 0;
    for (final String idFormat : listIdFormat) {
      supportThrift.create(createFormatFichier(i, idFormat), new Date().getTime());
      i++;
    }
  }

  /**
   * Migration des données droitreferentielFormatcql vers DroitReferentielFormat
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationReferentielFormat.migrationFromCqlTothrift();
    final List<FormatFichier> listThrift = supportThrift.findAll();
    final List<FormatFichier> listCql = supportCql.findAll();
    Assert.assertEquals(listCql.size(), listIdFormat.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table droitreferentielFormatcql
   */
  private void populateTableCql() {
    int i = 0;
    for (final String idFormat : listIdFormat) {
      supportCql.create(createFormatFichier(i, idFormat));
      i++;
    }
  }

  /**
   * Création de l'entité FormatFichier lié à l'indice i
   * 
   * @param i
   * @param idFormat
   * @param formatFichier
   */
  private FormatFichier createFormatFichier(final int i, final String idFormat) {
    final FormatFichier formatFichier = new FormatFichier();
    formatFichier.setIdFormat(idFormat);
    formatFichier.setTypeMime(listTypeMime[i]);
    formatFichier.setExtension(listExtension[i]);
    formatFichier.setDescription(listDescription[i]);
    formatFichier.setAutoriseGED(listAutoriseGED[i]);
    formatFichier.setVisualisable(listVisualisable[i]);
    formatFichier.setValidator(listValidator[i]);
    formatFichier.setIdentificateur(listIdentificateurs[i]);
    formatFichier.setConvertisseur(listConvertisseur[i]);

    return formatFichier;
  }

}
