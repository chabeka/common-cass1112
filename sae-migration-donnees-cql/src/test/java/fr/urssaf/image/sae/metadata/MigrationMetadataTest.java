/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata;

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
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.SaeMetadataCqlSupport;
import fr.urssaf.image.sae.testutils.CompareUtils;

/**
 * (AC75095351) Classe de test migration des metadata
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationMetadataTest {

  @Autowired
  private SaeMetadataCqlSupport supportCql;

  @Autowired
  private SaeMetadataSupport supportThrift;

  @Autowired
  MigrationMetadata migrationMetadata;

  @Autowired
  private CassandraServerBean server;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationMetadataTest.class);



  String[] listCodeLongs = {"ApplicationProductrice", "CodePartenaire", "AnneeExercice"};

  Boolean[] listArc = {true, true, true};

  Boolean[] listCons = {true, true, true};

  Boolean[] listDefCons = {false, false, false};

  String[] listDescription = {"Code de l'application qui a produit le fichier",
                              "Code du partenaire de l'organisme (ex : huissier)",
  "Année de l'exercice de la facture"};

  String[] listDictName = {"", "", ""};

  Boolean[] listDispo = {true, true, true};

  Boolean[] listHasDict = {false, false, false};

  Boolean[] listIndex = {false, false, false};

  Boolean[] listInt = {false, false, false};

  String[] listLabels = {"Application Productrice du document", "CodePartenaire", "Année de l'exercice"};

  Boolean[] listLeftTrim = {true, true, false};

  Integer[] listLength = {15, 7, 4};

  String[] listPatterns = {"", "", ""};

  Boolean[] listReqArch = {false, false, false};

  Boolean[] listReqStor = {false, false, false};

  Boolean[] listRightTrim = {true, true, false};

  String[] listsCodeCourt = {"apr", "cpa", "aex"};

  Boolean[] listsSearch = {true, true, true};

  Boolean[] listsTransf = {true, true, true};

  String[] listsType = {"String", "String", "Integer"};

  Boolean[] listUpdate = {false, true, true};


  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données DroitMetadata vers droitmetadatacql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      final List<MetadataReference> listThrift = supportThrift.findAll();
      migrationMetadata.migrationFromThriftToCql();
      final List<MetadataReference> listCql = supportCql.findAll();
      Assert.assertEquals(listThrift.size(), listCodeLongs.length);
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
    for (final String longCode : listCodeLongs) {
      final MetadataReference metadata = new MetadataReference();
      metadata.setLongCode(longCode);
      metadata.setShortCode(listsCodeCourt[i]);
      metadata.setType(listsType[i]);
      metadata.setRequiredForArchival(listReqArch[i]);
      metadata.setRequiredForStorage(listReqStor[i]);
      metadata.setLength(listLength[i]);
      metadata.setPattern(listPatterns[i]);
      metadata.setConsultable(listCons[i]);
      metadata.setDefaultConsultable(listDefCons[i]);
      metadata.setSearchable(listsSearch[i]);
      metadata.setInternal(listInt[i]);
      metadata.setArchivable(listArc[i]);
      metadata.setLabel(listLabels[i]);
      metadata.setDescription(listDescription[i]);
      metadata.setHasDictionary(listHasDict[i]);
      metadata.setDictionaryName(listDictName[i]);
      metadata.setIsIndexed(listIndex[i]);
      metadata.setModifiable(listUpdate[i]);
      metadata.setClientAvailable(listDispo[i]);
      metadata.setLeftTrimable(listLeftTrim[i]);
      metadata.setRightTrimable(listRightTrim[i]);
      metadata.setTransferable(listsTransf[i]);
      supportThrift.create(metadata, new Date().getTime());
      i++;
    }
  }

  /**
   * Migration des données droitmetadatacql vers DroitMetadata
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationMetadata.migrationFromCqlTothrift();
    final List<MetadataReference> listThrift = supportThrift.findAll();
    final List<MetadataReference> listCql = supportCql.findAll();
    Assert.assertEquals(listCql.size(), listCodeLongs.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table droitmetadatacql
   */
  private void populateTableCql() {
    int i = 0;
    for (final String longCode : listCodeLongs) {
      final MetadataReference metadata = new MetadataReference();
      metadata.setLongCode(longCode);
      metadata.setShortCode(listsCodeCourt[i]);
      metadata.setType(listsType[i]);
      metadata.setRequiredForArchival(listReqArch[i]);
      metadata.setRequiredForStorage(listReqStor[i]);
      metadata.setLength(listLength[i]);
      metadata.setPattern(listPatterns[i]);
      metadata.setConsultable(listCons[i]);
      metadata.setDefaultConsultable(listDefCons[i]);
      metadata.setSearchable(listsSearch[i]);
      metadata.setInternal(listInt[i]);
      metadata.setArchivable(listArc[i]);
      metadata.setLabel(listLabels[i]);
      metadata.setDescription(listDescription[i]);
      metadata.setHasDictionary(listHasDict[i]);
      metadata.setDictionaryName(listDictName[i]);
      metadata.setIsIndexed(listIndex[i]);
      metadata.setModifiable(listUpdate[i]);
      metadata.setClientAvailable(listDispo[i]);
      metadata.setLeftTrimable(listLeftTrim[i]);
      metadata.setRightTrimable(listRightTrim[i]);
      metadata.setTransferable(listsTransf[i]);
      supportCql.create(metadata);
      i++;
    }
  }
}
