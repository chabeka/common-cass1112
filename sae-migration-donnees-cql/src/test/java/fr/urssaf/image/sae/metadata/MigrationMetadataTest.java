/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata;

import java.util.Date;
import java.util.List;

import org.javers.core.diff.Diff;
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
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.SaeMetadataCqlSupport;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de test migration des metadata
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
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

	String[] listCodeLongs = { "ApplicationProductrice", "CodePartenaire", "AnneeExercice" };

	Boolean[] listArc = { true, true, true };

	Boolean[] listCons = { true, true, true };

	Boolean[] listDefCons = { false, false, false };

	String[] listDescription = { "Code de l'application qui a produit le fichier",
			"Code du partenaire de l'organisme (ex : huissier)", "Année de l'exercice de la facture" };

	String[] listDictName = { "", "", "" };

	Boolean[] listDispo = { true, true, true };

	Boolean[] listHasDict = { false, false, false };

	Boolean[] listIndex = { false, false, false };

	Boolean[] listInt = { false, false, false };

	String[] listLabels = { "Application Productrice du document", "CodePartenaire", "Année de l'exercice" };

	Boolean[] listLeftTrim = { true, true, false };

	Integer[] listLength = { 15, 7, 4 };

	String[] listPatterns = { "", "", "" };

	Boolean[] listReqArch = { false, false, false };

	Boolean[] listReqStor = { false, false, false };

	Boolean[] listRightTrim = { true, true, false };

	String[] listsCodeCourt = { "apr", "cpa", "aex" };

	Boolean[] listsSearch = { true, true, true };

	Boolean[] listsTransf = { true, true, true };

	String[] listsType = { "String", "String", "Integer" };

	Boolean[] listUpdate = { false, true, true };

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
			server.resetData();
			populateTableThrift();
			final List<MetadataReference> listThrift = supportThrift.findAll();
			migrationMetadata.migrationFromThriftToCql();
			final List<MetadataReference> listCql = supportCql.findAll();
			Assert.assertEquals(listThrift.size(), listCodeLongs.length);
			Assert.assertEquals(listThrift.size(), listCql.size());
			Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
		} catch (final Exception ex) {
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

	@Test
	public void diffAddTest() {

		populateTableThrift();
		migrationMetadata.migrationFromThriftToCql();

		final List<MetadataReference> listThrift = supportThrift.findAll();

		final MetadataReference metadataReference = new MetadataReference();
		metadataReference.setLongCode("LONGCODEADD");
		metadataReference.setDescription("DESCADD");
		metadataReference.setShortCode("SHORTCODEADD");
		metadataReference.setRequiredForArchival(listReqArch[0]);
		metadataReference.setRequiredForStorage(listReqStor[0]);
		metadataReference.setLength(listLength[0]);
		metadataReference.setPattern(listPatterns[0]);
		metadataReference.setConsultable(listCons[0]);
		metadataReference.setDefaultConsultable(listDefCons[0]);
		metadataReference.setSearchable(listsSearch[0]);
		metadataReference.setInternal(listInt[0]);
		metadataReference.setArchivable(listArc[0]);
		metadataReference.setLabel(listLabels[0]);
		metadataReference.setDescription(listDescription[0]);
		metadataReference.setHasDictionary(listHasDict[0]);
		metadataReference.setDictionaryName(listDictName[0]);
		metadataReference.setIsIndexed(listIndex[0]);
		metadataReference.setModifiable(listUpdate[0]);
		metadataReference.setClientAvailable(listDispo[0]);
		metadataReference.setLeftTrimable(listLeftTrim[0]);
		metadataReference.setRightTrimable(listRightTrim[0]);
		metadataReference.setTransferable(listsTransf[0]);
		supportCql.create(metadataReference);
		final List<MetadataReference> listCql = supportCql.findAll();
		final Diff diff = migrationMetadata.compareMetadatas(listThrift, listCql);
		Assert.assertTrue(diff.hasChanges());
		final String changes = diff.getChanges().get(0).toString();
		Assert.assertTrue(changes.equals("NewObject{ new object: fr.urssaf.image.sae.metadata.referential.model.MetadataReference/LONGCODEADD }"));

	}

	@Test
	public void diffDescTest() {

		populateTableThrift();
		migrationMetadata.migrationFromThriftToCql();

		final List<MetadataReference> listThrift = supportThrift.findAll();
		final List<MetadataReference> listCql = supportCql.findAll();
		listCql.get(0).setDescription("DESCDIFF");
		final Diff diff = migrationMetadata.compareMetadatas(listThrift, listCql);
		Assert.assertTrue(diff.hasChanges());
		final String changes = diff.getChanges().get(0).toString();
		Assert.assertTrue(
				changes.equals("ValueChange{ 'description' value changed from 'Code de l'application qui a produit le fichier' to 'DESCDIFF' }"));

	}
}
