/**
 *   (AC75095351) Classe demigration qui regroupe toutes les classes de migration
 */
package fr.urssaf.image.sae.all;

import java.util.Date;

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

import fr.urssaf.image.sae.commons.MigrationParameters;
import fr.urssaf.image.sae.commons.support.ParametersSupport;
import fr.urssaf.image.sae.commons.support.cql.ParametersCqlSupport;
import fr.urssaf.image.sae.droit.MigrationActionUnitaire;
import fr.urssaf.image.sae.droit.MigrationContratService;
import fr.urssaf.image.sae.droit.MigrationFormatControlProfil;
import fr.urssaf.image.sae.droit.MigrationPagm;
import fr.urssaf.image.sae.droit.MigrationPagma;
import fr.urssaf.image.sae.droit.MigrationPagmp;
import fr.urssaf.image.sae.droit.MigrationPrmd;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ContratServiceCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.FormatControlProfilCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmpCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PrmdCqlSupport;
import fr.urssaf.image.sae.format.MigrationReferentielFormat;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.dao.support.cql.ReferentielFormatCqlSupport;
import fr.urssaf.image.sae.metadata.MigrationDictionary;
import fr.urssaf.image.sae.metadata.MigrationMetadata;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.DictionaryCqlSupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.SaeMetadataCqlSupport;
import fr.urssaf.image.sae.rnd.MigrationRnd;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.cql.RndCqlSupport;
import fr.urssaf.image.sae.spring.batch.MigrationSequences;


/**
 * (AC75095351)
 * Classe de test pour migration de toutes les tables concernés par les droits
 * thrift vers cql et cql vers thrift
 * La mise en commentaire permet de tester séparément des classes de migration
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MigrationAllTest {

  private static final Date DATE = new Date();

  @Autowired
  private FormatControlProfilCqlSupport supportFormatControlProfilCql;

  @Autowired
  private ActionUnitaireCqlSupport supportActionUnitaireCql;

  @Autowired
  private PagmaCqlSupport supportPagmaCql;

  @Autowired
  private PagmpCqlSupport supportPagmpCql;

  @Autowired
  private PrmdCqlSupport supportPrmdCql;

  @Autowired
  private PagmCqlSupport supportPagmCql;

  @Autowired
  private ContratServiceCqlSupport supportContratServiceCql;

  @Autowired
  private ParametersCqlSupport supportParametersCql;

  @Autowired
  private DictionaryCqlSupport supportDictionaryCql;

  @Autowired
  private SaeMetadataCqlSupport supportMetadataCql;

  @Autowired
  private ReferentielFormatCqlSupport supportReferentielFormatCql;

  @Autowired
  private RndCqlSupport supportRndCql;

  @Autowired
  private FormatControlProfilSupport supportFormatControlProfilThrift;

  @Autowired
  private ActionUnitaireSupport supportActionUnitaireThrift;

  @Autowired
  private PagmaSupport supportPagmaThrift;

  @Autowired
  private PagmpSupport supportPagmpThrift;

  @Autowired
  private PrmdSupport supportPrmdThrift;

  @Autowired
  private PagmSupport supportPagmThrift;

  @Autowired
  private ContratServiceSupport supportContratServiceThrift;

  @Autowired
  private ParametersSupport supportParametersThrift;

  @Autowired
  private DictionarySupport supportDictionaryThrift;

  @Autowired
  private SaeMetadataSupport supportMetadataThrift;

  @Autowired
  private ReferentielFormatSupport supportReferentielFormatThrift;

  @Autowired
  private RndSupport supportRndThrift;

  @Autowired
  MigrationFormatControlProfil migrationFormatControlProfil;

  @Autowired
  MigrationActionUnitaire migrationActionUnitaire;

  @Autowired
  MigrationPagma migrationPagma;

  @Autowired
  MigrationPagmp migrationPagmp;

  @Autowired
  MigrationPrmd migrationPrmd;

  @Autowired
  MigrationPagm migrationPagm;

  @Autowired
  MigrationContratService migrationContratService;

  @Autowired
  MigrationParameters migrationParameters;

  @Autowired
  MigrationDictionary migrationDictionary;

  @Autowired
  MigrationMetadata migrationMetadata;

  @Autowired
  MigrationReferentielFormat migrationReferentielFormat;

  @Autowired
  MigrationRnd migrationRnd;

  @Autowired
  MigrationSequences migrationSequences;


  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationAllTest.class);

  @Test
  public void migrationFromThriftToCql() {
    try {

      // Actions Unitaires
      /*
       * migrationActionUnitaire.migrationFromThriftToCql();
       * final List<ActionUnitaire> listActionUnitaireThrift = supportActionUnitaireThrift.findAll();
       * final List<ActionUnitaire> listActionUnitaireCql = supportActionUnitaireCql.findAll();
       * Assert.assertEquals(listActionUnitaireThrift.size(), listActionUnitaireCql.size());
       */
      // Format Control Profil
      /*
       * migrationFormatControlProfil.migrationFromThriftToCql();
       * final List<FormatControlProfil> listFormatControlProfilThrift = supportFormatControlProfilThrift.findAll();
       * final List<FormatControlProfil> listFormatControlProfilCql = supportFormatControlProfilCql.findAll();
       * Assert.assertEquals(listFormatControlProfilThrift.size(), listFormatControlProfilCql.size());
       */

      // Pagma

      /*
       * migrationPagma.migrationFromThriftToCql();
       * final List<Pagma> listPagmaThrift = supportPagmaThrift.findAll();
       * final List<Pagma> listPagmaCql = supportPagmaCql.findAll();
       * Assert.assertEquals(listPagmaThrift.size(), listPagmaCql.size());
       */

      // Pagmp

      /*
       * migrationPagmp.migrationFromThriftToCql();
       * final List<Pagmp> listPagmpThrift = supportPagmpThrift.findAll();
       * final List<Pagmp> listPagmpCql = supportPagmpCql.findAll();
       * Assert.assertEquals(listPagmpThrift.size(), listPagmpCql.size());
       */

      // Prmd

      /*
       * migrationPrmd.migrationFromThriftToCql();
       * final List<Prmd> listPrmdThrift = supportPrmdThrift.findAll();
       * final List<Prmd> listPrmdCql = supportPrmdCql.findAll();
       * Assert.assertEquals(listPrmdThrift.size(), listPrmdCql.size());
       */

      // Pagm
      /*
       * LOGGER.info("[1] MIGRATION PAGM THRIFT-> CQL DEBUT");
       * final List<String> keys = migrationPagm.migrationFromThriftToCql();
       * final List<PagmCql> listPagmThrift = migrationPagm.getListPagmCqlFromThrift(keys);
       * LOGGER.info("[2] Nb Pagm Thrift:" + listPagmThrift.size());
       * final List<PagmCql> listPagmCql = supportPagmCql.findAll();
       * LOGGER.info("[3] Nb Pagm Cql:" + listPagmCql.size());
       * Assert.assertEquals(listPagmThrift.size(), listPagmCql.size());
       * LOGGER.info("[4]Tailles Listes Pagm  identiques");
       * // On vérifie que les listes sont identiques
       * Assert.assertTrue(CompareUtils.compareListsGeneric(listPagmThrift, listPagmCql));
       * LOGGER.info("[5] Listes Pagm identiques");
       * LOGGER.info("[6] MIGRATION PAGM THRIFT-> CQL FIN");
       */
      /*
       * migrationPagm.migrationFromThriftToCql();
       * // final List<Pagm> listPagmThrift = supportPagmThrift.findAll();
       * final List<PagmCql> listPagmCql = supportPagmCql.findAll();
       * Assert.assertEquals(true, false);
       */

      // DroitContratService

      /*
       * migrationContratService.migrationFromThriftToCql();
       * final List<ServiceContract> listContratServiceThrift = supportContratServiceThrift.findAll();
       * final List<ServiceContract> listContratServiceCql = supportContratServiceCql.findAll();
       * Assert.assertEquals(listContratServiceThrift.size(), listContratServiceCql.size());
       */

      // Parametres
      /*
       * LOGGER.info("[1] MIGRATION PARAMETERS THRIFT-> CQL DEBUT");
       * final List<ParameterCql> listParametersThrift = migrationParameters.getListParametersCqlFromThrift();
       * LOGGER.info("[2] Nb Parameters Thrift:" + listParametersThrift.size());
       * migrationParameters.migrationFromThriftToCql();
       * final List<ParameterCql> listParametersCql = supportParametersCql.findAll();
       * LOGGER.info("[3] Nb Parameters Cql:" + listParametersCql.size());
       * Assert.assertEquals(listParametersThrift.size(), listParametersCql.size());
       * LOGGER.info("[4]Tailles Listes Parameters Format identiques");
       * // On vérifie que les listes sont identiques
       * Assert.assertTrue(CompareUtils.compareListsGeneric(listParametersThrift, listParametersCql));
       * LOGGER.info("[5] Listes Parameters identiques");
       * LOGGER.info("[6] MIGRATION PARAMETERS THRIFT-> CQL FIN");
       */

      // Dictionary
      /*
       * migrationDictionary.migrationFromThriftToCql();
       * final List<Dictionary> listDictionaryThrift = supportDictionaryThrift.findAll();
       * final List<Dictionary> listDictionaryCql = supportDictionaryCql.findAll();
       * // On vérifie que les listes ont la même taille
       * Assert.assertEquals(listDictionaryThrift.size(), listDictionaryCql.size());
       * // On vérifie que les listes sont identiques
       * Assert.assertTrue(CompareUtils.compareListsDictionary(listDictionaryThrift, listDictionaryCql));
       */

      // Metadata
      /*
       * migrationMetadata.migrationFromThriftToCql();
       * final List<MetadataReference> listMetadataThrift = supportMetadataThrift.findAll();
       * final List<MetadataReference> listMetadataCql = supportMetadataCql.findAll();
       * // On vérifie que les listes ont la même taille
       * Assert.assertEquals(listMetadataThrift.size(), listMetadataCql.size());
       * // On vérifie que les listes sont identiques
       * Assert.assertTrue(CompareUtils.compareListsGeneric(listMetadataThrift, listMetadataCql));
       */

      // RefrentielFormat
      /*
       * LOGGER.info("[1] MIGRATION REFERENTIELFORMAT THRIFT-> CQL");
       * migrationReferentielFormat.migrationFromThriftToCql();
       * final List<FormatFichier> listFormatFichierThrift = supportReferentielFormatThrift.findAll();
       * LOGGER.info("[2] Nb formatFichier Thrift:" + listFormatFichierThrift.size());
       * final List<FormatFichier> listFormatFichierCql = supportReferentielFormatCql.findAll();
       * LOGGER.info("[3] Nb formatFichier Cql:" + listFormatFichierCql.size());
       * // On vérifie que les listes ont la même taille
       * Assert.assertEquals(listFormatFichierThrift.size(), listFormatFichierCql.size());
       * LOGGER.info("[4]Tailles Listes Referentiel Format identiques");
       * // On vérifie que les listes sont identiques
       * Assert.assertTrue(CompareUtils.compareListsGeneric(listFormatFichierThrift, listFormatFichierCql));
       * LOGGER.info("[5] Listes Referentiel Format identiques");
       */

      // Rnd
      /*
       * LOGGER.info("[1] MIGRATION RND THRIFT-> CQL");
       * migrationRnd.migrationFromThriftToCql();
       * final List<TypeDocument> listTypeDocumentThrift = supportRndThrift.findAll();
       * LOGGER.info("[2] Nb TypeDocument Thrift:" + listTypeDocumentThrift.size());
       * final List<TypeDocument> listTypeDocumentCql = supportRndCql.findAll();
       * LOGGER.info("[3] Nb TypeDocument Cql:" + listTypeDocumentCql.size());
       * // On vérifie que les listes ont la même taille
       * Assert.assertEquals(listTypeDocumentThrift.size(), listTypeDocumentCql.size());
       * LOGGER.info("[4] Tailles Listes Rnd identiques");
       * // On vérifie que les listes sont identiques
       * Assert.assertTrue(CompareUtils.compareListsGeneric(listTypeDocumentCql, listTypeDocumentCql));
       * LOGGER.info("[5]  Listes Rnd identiques");
       */
      // Sequences
      LOGGER.info("[1] MIGRATION SEQUENCES THRIFT-> CQL");
      migrationSequences.migrationFromThriftToCql();
      LOGGER.info("[2] MIGRATION SEQUENCES THRIFT-> CQL");
    }

    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  @Test
  public void migrationFromCqlTothrift() {
    // Action Unitaires
    /*
     * migrationActionUnitaire.migrationFromCqlTothrift();
     * final List<ActionUnitaire> listActionUnitaireThrift = supportActionUnitaireThrift.findAll();
     * final List<ActionUnitaire> listActionUnitaireCql = supportActionUnitaireCql.findAll();
     * Assert.assertEquals(listActionUnitaireThrift.size(), listActionUnitaireCql.size());
     */
    // Format Control Profil
    /*
     * migrationFormatControlProfil.migrationFromCqlTothrift();
     * final List<FormatControlProfil> listFormatControlProfilThrift = supportFormatControlProfilThrift.findAll();
     * final List<FormatControlProfil> listFormatControlProfilCql = supportFormatControlProfilCql.findAll();
     * Assert.assertEquals(listFormatControlProfilThrift.size(), listFormatControlProfilCql.size());
     */
    // Pagma

    /*
     * migrationPagma.migrationFromCqlTothrift();
     * final List<Pagma> listPagmaThrift = supportPagmaThrift.findAll();
     * final List<Pagma> listPagmaCql = supportPagmaCql.findAll();
     * Assert.assertEquals(listPagmaThrift.size(), listPagmaCql.size());
     */

    // Pagmp

    /*
     * migrationPagmp.migrationFromCqlTothrift();
     * final List<Pagmp> listPagmaThrift = supportPagmpThrift.findAll();
     * final List<Pagmp> listPagmaCql = supportPagmpCql.findAll();
     * Assert.assertEquals(listPagmaThrift.size(), listPagmaCql.size());
     */

    // Prmd

    /*
     * migrationPrmd.migrationFromCqlTothrift();
     * final List<Prmd> listPrmdThrift = supportPrmdThrift.findAll();
     * final List<Prmd> listPrmdCql = supportPrmdCql.findAll();
     * Assert.assertEquals(listPrmdThrift.size(), listPrmdCql.size());
     */

    // Pagm

    /*
     * migrationPagm.migrationFromCqlTothrift();
     * //final List<Pagm> listPagmThrift = supportPagmThrift.findAll();
     * final List<PagmCql> listPagmCql = supportPagmCql.findAll();
     * //Assert.assertEquals(listPrmdThrift.size(), listPrmdCql.size());
     */

    // ContratService

    /*
     * migrationContratService.migrationFromCqlTothrift();
     * final List<ServiceContract> listContratServiceThrift = supportContratServiceThrift.findAll();
     * final List<ServiceContract> listContratServiceCql = supportContratServiceCql.findAll();
     * Assert.assertEquals(listContratServiceThrift.size(), listContratServiceCql.size());
     */

  }

}
