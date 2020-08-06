package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.constantes.LotVersion;
import fr.urssaf.image.sae.lotinstallmaj.dao.cql.SAECassandraDaoCQL;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.service.impl.SAECassandraUpdater;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileLoader;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileSet;

@Component
public class SAECassandraUpdaterCQL {

  private static final Logger LOG = LoggerFactory.getLogger(SAECassandraUpdaterCQL.class);

  private static final String SAE_CREATE_TRACES_SCRIPT = "/cql/sae-traces.cql";
  private static final String SAE_CREATE_JOB_SPRING_SCRIPT = "/cql/sae-job-spring.cql";
  private static final String SAE_CREATE_PILE_TRAVAUX_SCRIPT = "/cql/sae-pile-travaux.cql";
  private static final String SAE_CREATE_MODE_API_SCRIPT = "/cql/modeapi.cql";

  private static final String SAE_CREATE_COMMONS_SCRIPT = "/cql/sae-commons.cql";
  private static final String SAE_CREATE_TABLES_DROITS_SCRIPT = "/cql/sae-droits.cql";
  private static final String SAE_CREATE_TABLES_FORMAT_SCRIPT = "/cql/sae-format.cql";
  private static final String SAE_CREATE_TABLES_METADATA_SCRIPT = "/cql/sae-metadata.cql";
  private static final String SAE_CREATE_TABLES_RND_SCRIPT = "/cql/sae-rnd.cql";

  // Script de delete
  private static final String SAE_DELETE_MODE_API_SCRIPT = "DROP TABLE modeapi;";
  private static final String SAE_DELETE_TABLES_TRACES_SCRIPT = "/cql/delete-tables-traces.cql";
  private static final String SAE_DELETE_TABLES_JOBSPRING_SCRIPT = "/cql/delete-tables-jobspring.cql";
  private static final String SAE_DELETE_TABLES_PILESTRAVAUX_SCRIPT = "/cql/delete-tables-piletravaux.cql";

  private static final String LITERAL_VERSION_ALREADY_INSTALLED = "La base de données est déja en version {}";

  private static final String SAE_CREATE_MODE_API_SCRIPT_SET_DATASTAX = "/cql/modeapi-set-datastax.cql";

  @Autowired
  private SAEKeyspaceConnecter saecf;

  @Autowired
  private InsertionDonneesCQL insertionDonneesCQL;

  @Autowired
  private SAECassandraDaoCQL saeCassandraDaoCQL;

  @Autowired
  private SAECassandraUpdater saeCassandraUpdater;

  /**
   * Creation des tables de traces
   */
  public final void createTablesTraces() {
    LOG.info("Début de l'opération : creation des tables traces cql");

    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_TRACES_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : creation des tables traces cql");
  }

  /**
   * Creation des tables pile des travaux
   */
  public final void createTablesPileTravaux() {
    LOG.info("Début de l'opération : creation des tables pile travaux cql");

    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_PILE_TRAVAUX_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);	

    LOG.info("Fin de l'opération : creation des tables pile travaux cql");
  }

  /**
   * Creation des tables job spring
   */
  public final void createTablesJobSpring() {
    LOG.info("Début de l'opération : creation des tables job spring cql");

    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_JOB_SPRING_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : creation des tables job spring cql");
  }

  /**
   * Creation des tables job spring
   */
  public final void createTablesModeapi() {
    LOG.info("Début de l'opération : creation de la  table modeapi cql");


    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_MODE_API_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : creation de la table modeapi cql");
  }

  /**
   * Creation de la table ModeApi avec valeur par défaut "DATASTAX" comme valeur par défaut
   * pour la colonne mode
   */
  public final void createTablesModeapiCQL() {
    LOG.info("Début de l'opération : creation de la  table modeapi cql");

    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_MODE_API_SCRIPT_SET_DATASTAX);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : creation de la table modeapi cql");
  }


  /**
   * Creation des tables Commons
   */
  public final void createTablesCommons() {
    LOG.info("Début de l'opération : creation de la  table Commons cql");


    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_COMMONS_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : creation de la table Commons cql");
  }  

  /**
   * Creation des tables Droits
   */
  public final void createTablesDroits() {
    LOG.info("Début de l'opération : creation de la  table Droits cql");


    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_TABLES_DROITS_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : creation de la table Droits cql");
  }

  /**
   * Creation des tables Formats
   */
  public final void createTablesFormats() {
    LOG.info("Début de l'opération : creation de la  table Format cql");


    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_TABLES_FORMAT_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : creation de la table Format cql");
  }

  /**
   * Creation des tables Metadata
   */
  public final void createTablesMetadata() {
    LOG.info("Début de l'opération : creation de la  table Metadata cql");


    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_TABLES_METADATA_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : creation de la table Metadata cql");
  }
  /**
   * Creation des tables RND
   */
  public final void createTablesRND() {
    LOG.info("Début de l'opération : creation de la  table RND cql");


    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_CREATE_TABLES_RND_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : creation de la table RND cql");
  }

  /**
   * Suppression de la table modeapi 
   */
  public final void deleteTablesModeapi() {
    LOG.info("Début de l'opération : suppression de la  table modeapi cql");

    final String query = SAE_DELETE_MODE_API_SCRIPT;
    saecf.getSession().execute(query);

    LOG.info("Fin de l'opération : suppression de la table modeapi cql");
  }

  /**
   * Suppression des tables traces 
   */
  public final void deleteTablesTraces() {
    LOG.info("Début de l'opération : suppression des tables traces");


    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_DELETE_TABLES_TRACES_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : suppression des tables traces");
  }

  /**
   * Suppression des tables job spring 
   */
  public final void deleteTablesJobSpring() {
    LOG.info("Début de l'opération : suppression des tables job spring");


    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_DELETE_TABLES_JOBSPRING_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : suppression des tables job spring");
  }

  /**
   * Suppression des tables job spring 
   */
  public final void deleteTablesPilesTravaux() {
    LOG.info("Début de l'opération : suppression des tables pile travaux");


    CQLDataFileSet cqlData;
    //
    cqlData = new CQLDataFileSet(SAE_DELETE_TABLES_PILESTRAVAUX_SCRIPT);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
    dataLoader.load(cqlData);

    LOG.info("Fin de l'opération : suppression des tables pile travaux");
  }

  /**
   * Création du schéma initial
   */
  public void createCQLSchema() {
    LOG.info("Début de l'opération : creation des tables cql SAE ");

    createTablesModeapi();
    createTablesTraces();
    createTablesPileTravaux();
    createTablesJobSpring();

    createTablesCommons();
    createTablesDroits();
    createTablesFormats();
    createTablesMetadata();
    createTablesRND();

    LOG.info("Fin de l'opération : creation des tables cql SAE");
  }

  /**
   * Création du schéma initial
   */
  public void createTableModeAPI() {
    LOG.info("Creation de la table mode API");
    createTablesModeapi();
    LOG.info("Fin de l'opération");
  }

  private void rattrapagePourDebutVersioningCQL() {
    rattrapage1();
    rattrapage2();
    rattrapage3();
    rattrapage4();
  }

  /**
   * Supprimer la table mode api avant de lancer ce rattrapage
   */
  public void lancerRattrapage() {
    createTablesModeapiCQL();
    rattrapagePourDebutVersioningCQL();
  }

  /**
   * Rattrapage de l'état de la base CQL en terme de données
   */
  private void rattrapage1() {
    insertionDonneesCQL.addTracabiliteParameters();
    insertionDonneesCQL.addReferentielEvenementV1();

    insertionDonneesCQL.addReferentielEvenementV2();
    insertionDonneesCQL.addRndParameters();

    insertionDonneesCQL.addReferentielEvenementV3();

    insertionDonneesCQL.addReferentielFormat();
    insertionDonneesCQL.addFormatControleProfil();
    insertionDonneesCQL.addDroits();
    insertionDonneesCQL.addReferentielEvenementV4();
  }

  private void rattrapage2() {
    insertionDonneesCQL.addReferentielEvenementV5();
    insertionDonneesCQL.addReferentielFormatV2();

    insertionDonneesCQL.addReferentielEvenementV6();

    insertionDonneesCQL.addReferentielEvenementV7();
    insertionDonneesCQL.addActionUnitaireNote();

    insertionDonneesCQL.addActionUnitaireRechercheParIterateur();
    insertionDonneesCQL.modifyActionUnitaireAjoutNote();
    insertionDonneesCQL.modifyReferentielFormatFmt354();
    insertionDonneesCQL.addReferentielFormatV3();

    insertionDonneesCQL.addActionUnitaireNote2();
  }

  private void rattrapage3() {
    insertionDonneesCQL.addReferentielEvenementV8();
    insertionDonneesCQL.addActionUnitaireAjoutDocAttache();

    insertionDonneesCQL.addReferentielFormatV4();
    insertionDonneesCQL.addReferentielEvenementV9();

    insertionDonneesCQL.addActionUnitaireTraitementMasse();
    insertionDonneesCQL.addReferentielEvenementV10();
    insertionDonneesCQL.majPrmdExpReguliere160600();

    insertionDonneesCQL.addReferentielEvenementV11();
    insertionDonneesCQL.addReferentielFormatV5();
    insertionDonneesCQL.addCorbeilleParameters();
  }

  /**
   * Rattrapage jusqu'à la version correspondante à la version 33 de la base Thrift
   */
  private void rattrapage4() {
    // Ajout de l'action unitaire suppression et modification
    insertionDonneesCQL.addActionUnitaireSuppressionModification();

    // Ajout de l'action unitaire copie
    insertionDonneesCQL.addActionUnitaireCopie();

    // Ajout des nouveaux formats à gérer
    insertionDonneesCQL.addReferentielFormatV6();
    // Modification du format fmt/353 (extension .tiff)
    insertionDonneesCQL.modifyReferentielFormatFmt353();
    // Modification du format fmt/44 (extension .jpeg)
    insertionDonneesCQL.modifyReferentielFormatFmt44();
    // Ajout de la colonne "Autorisé en GED" dans le referentiel des formats
    insertionDonneesCQL.addColumnAutoriseGEDReferentielFormat();

    // Ajout des nouveaux formats à gérer
    insertionDonneesCQL.addReferentielFormatV6Bis();
    // Modification du format fmt/353 (extension .tiff)
    insertionDonneesCQL.modifyReferentielFormatCrtl1();

    // Ajout droits unitaires nouveaux traitements de masse.
    insertionDonneesCQL.addActionUnitaireTraitementMasse2();
    // Ajout des évenements
    try {
      insertionDonneesCQL.addReferentielEvenementV12();
    }
    catch (final Exception e) {
      throw new MajLotRuntimeException(e);
    }

    // Ajout droit unitaire reprise des traitements de masse.
    insertionDonneesCQL.addActionUnitaireRepriseMasse();
    // Ajout des évenements
    insertionDonneesCQL.addReferentielEvenementV13();
    // Modification du format png
    insertionDonneesCQL.addReferentielFormatV7();
    // Mise à jour du CS_V2 pour ajouter l'action reprise_masse à tous les PAGM
    insertionDonneesCQL.majPagmCsV2AjoutActionReprise170900();

    // Mise à jour des contrat de service pour passage à la PKI nationale
    insertionDonneesCQL.majPagmCsPourPKINationale180300();

    // Ajout des évenements
    insertionDonneesCQL.addReferentielEvenementV14();
  }

  /**
   * Methode permettant de modifier la version de la base de données.
   * 
   * @param version
   *           version de la base de données.
   */
  public void updateDatabaseVersion(final int version) {
    // On positionne la version de la base de données
    saeCassandraDaoCQL.updateDatabaseVersion(version);
  }

  /**
   * Met à jour la version de la base DFCE
   * 
   * @param version
   */
  public void updateDatabaseVersionDFCE(final int version) {
    // On positionne la version de la base de données
    saeCassandraDaoCQL.updateDatabaseVersionDFCE(version);
  }

  /**
   * Recupère la version actuelle de la base SAE
   * 
   * @return
   */
  public long getDatabaseVersion() {
    return saeCassandraDaoCQL.getDatabaseVersion();
  }

  /**
   * Recupère la version courante de la base de données DFCE
   * 
   * @return
   */
  public long getDatabaseVersionDFCE() {
    return saeCassandraDaoCQL.getDatabaseVersionDFCE();
  }

  public InsertionDonneesCQL getInsertionDataService() {
    return insertionDonneesCQL;
  }

  /**
   * @param isRedo
   */
  public void updateToVersion34(final boolean isRedo) {
    final long version = saeCassandraDaoCQL.getDatabaseVersion();
    final int versionToInstalled = LotVersion.CASSANDRA_DFCE_201100.getNumVersionLot();

    if (!isRedo && version >= versionToInstalled) {
      LOG.info(LITERAL_VERSION_ALREADY_INSTALLED, versionToInstalled);
      return;
    }

    if (!isRedo && versionToInstalled - version > 1) {
      saeCassandraUpdater.updateToVersion33(isRedo);
    }

    LOG.info("Mise à jour du keyspace SAE en version {}", versionToInstalled);

    // -- On se connecte au keyspace
    insertionDonneesCQL.addReferentielEvenementV15();

    // TODO: Creation des nouveaux format

    // On positionne la version à 34
    if (!isRedo) {
      saeCassandraDaoCQL.updateDatabaseVersion(versionToInstalled);
    }
  }

}
