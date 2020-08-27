package fr.urssaf.image.sae.lotinstallmaj.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.lotinstallmaj.component.DFCEConnexionComponent;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotUnknownDFCEVersion;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.DFCEKeyspaceConnecter;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileLoader;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileSet;

/**
 * Service de mise à jour de la base DFCE
 */
public abstract class DFCECassandraUpdaterV2 {

  private static final Logger LOG = LoggerFactory.getLogger(DFCECassandraUpdaterV2.class);

  private static final String MESSAGE_DFCE_EST_A_JOUR = "La base de données DFCE est déja en version {} de nom : {}";

  private static final String MESSAGE_DEBUT_MISE_A_JOUR = "Début de la mise à jour de la base en version {}";

  private static final String MESSAGE_FIN_MISE_A_JOUR = "Fin de l'installation de la mise à jour {}";

  private static final String MESSAGE_CHANGE_VERSION_DFCE = "Passer la version en Base de DFCE à {}";

  @Autowired
  private DFCEConnexionComponent dfceConnexionTester;

  public static final int LAST_DFCE_AVAILABLE_VERSION = 3;

  public enum VersionDFCE {

    DFCE_192_TO_200_SCHEMA("/cql/dfce-1.9.2-TO-2.0.0-schema.cql", 1, "v2.0.0"),
    DFCE_200_TO_210_SCHEMA("/cql/dfce-2.0.0-TO-2.1.0-schema.cql", 2, "v2.1.0"),
    DFCE_210_TO_230_SCHEMA("/cql/dfce-2.1.0-TO-2.3.0-schema.cql", 3, "v2.3.0"),

    DFCE_230_TO_192_SCHEMA("/cql/dfce-2.3.0-TO-1.9.2-schema.cql", "v1.9.2");

    private final String cqlRelativeClasspath;

    private final int numVersion;

    private final String versionName;

    private static final Map<Integer, String> maps = new HashMap<>();

    private VersionDFCE(final String cqlRelativeClasspath, final String versionName) {
      this.cqlRelativeClasspath = cqlRelativeClasspath;
      this.versionName = versionName;
      numVersion = 0;
    }

    private VersionDFCE(final String cqlRelativeClasspath, final int numVersion, final String versionName) {
      this.cqlRelativeClasspath = cqlRelativeClasspath;
      this.numVersion = numVersion;
      this.versionName = versionName;
    }

    /**
     * @return the cqlRelativeClasspath
     */
    public String getCqlRelativeClasspath() {
      return cqlRelativeClasspath;
    }

    /**
     * @return the numVersion
     */
    public int getNumVersion() {
      return numVersion;
    }

    /**
     * @return the versionName
     */
    public String getVersionName() {
      return versionName;
    }

    static {
      for (final VersionDFCE version : VersionDFCE.values()) {
        maps.put(version.getNumVersion(), version.getVersionName());
      }
    }

    public static final String getVersionNameById(final int version) {
      return maps.get(version);
    }

  }

  @Autowired
  protected DFCEKeyspaceConnecter dfcecf;

  /**
   * Mise à jour vers la version 200
   */
  public final void update192ToVersion200() {
    final long currentVersion = getDatabaseVersionDFCE();
    final String currentVersionName = VersionDFCE.getVersionNameById((int) currentVersion);

    if (currentVersion >= VersionDFCE.DFCE_192_TO_200_SCHEMA.getNumVersion()) {
      LOG.info(MESSAGE_DFCE_EST_A_JOUR, currentVersion, currentVersionName);
      return;
    }
    LOG.info(MESSAGE_DEBUT_MISE_A_JOUR, VersionDFCE.DFCE_192_TO_200_SCHEMA);
    CQLDataFileSet cqlData;
    cqlData = new CQLDataFileSet(VersionDFCE.DFCE_192_TO_200_SCHEMA.getCqlRelativeClasspath());
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(dfcecf.getSession());
    dataLoader.load(cqlData);	
    LOG.info(MESSAGE_FIN_MISE_A_JOUR, VersionDFCE.DFCE_192_TO_200_SCHEMA);

    // Met à jour la version de la base DFCE
    LOG.info(MESSAGE_CHANGE_VERSION_DFCE, VersionDFCE.DFCE_192_TO_200_SCHEMA.getNumVersion());
    try {
      setDatabaseVersionDFCE(VersionDFCE.DFCE_192_TO_200_SCHEMA.getNumVersion());
      System.out.println("passé");
    }
    catch (final Exception e) {
      throw new MajLotRuntimeException(e);
    }
  }
  /**
   * Mise à jour vers la version 210
   */
  public final void update200ToVersion210() {
    final long currentVersion = getDatabaseVersionDFCE();
    final String currentVersionName = VersionDFCE.getVersionNameById((int) currentVersion);

    if (currentVersion >= VersionDFCE.DFCE_200_TO_210_SCHEMA.getNumVersion()) {
      LOG.info(MESSAGE_DFCE_EST_A_JOUR, currentVersion, currentVersionName);
      return;
    }

    if (VersionDFCE.DFCE_200_TO_210_SCHEMA.getNumVersion() - currentVersion > 1) {
      update192ToVersion200();
    }

    LOG.info(MESSAGE_DEBUT_MISE_A_JOUR, VersionDFCE.DFCE_200_TO_210_SCHEMA);
    CQLDataFileSet cqlData;
    cqlData = new CQLDataFileSet(VersionDFCE.DFCE_200_TO_210_SCHEMA.cqlRelativeClasspath);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(dfcecf.getSession());
    dataLoader.load(cqlData);
    LOG.info(MESSAGE_FIN_MISE_A_JOUR, VersionDFCE.DFCE_200_TO_210_SCHEMA);

    // Met à jour la version de la base DFCE
    LOG.info(MESSAGE_CHANGE_VERSION_DFCE, VersionDFCE.DFCE_200_TO_210_SCHEMA.getNumVersion());
    setDatabaseVersionDFCE(VersionDFCE.DFCE_200_TO_210_SCHEMA.getNumVersion());
  }

  /**
   * Mise à jour en la version 230
   */
  public final void update210ToVersion230() {
    final long currentVersion = getDatabaseVersionDFCE();
    final String currentVersionName = VersionDFCE.getVersionNameById((int) currentVersion);

    if (currentVersion >= VersionDFCE.DFCE_210_TO_230_SCHEMA.getNumVersion()) {
      LOG.info(MESSAGE_DFCE_EST_A_JOUR, currentVersion, currentVersionName);
      return;
    }

    if (VersionDFCE.DFCE_210_TO_230_SCHEMA.getNumVersion() - currentVersion > 1) {
      update200ToVersion210();
    }

    LOG.info(MESSAGE_DEBUT_MISE_A_JOUR, VersionDFCE.DFCE_210_TO_230_SCHEMA);
    CQLDataFileSet cqlData;
    cqlData = new CQLDataFileSet(VersionDFCE.DFCE_210_TO_230_SCHEMA.cqlRelativeClasspath);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(dfcecf.getSession());
    dataLoader.load(cqlData);
    LOG.info(MESSAGE_FIN_MISE_A_JOUR, VersionDFCE.DFCE_210_TO_230_SCHEMA);

    // Met à jour la version de la base DFCE
    LOG.info(MESSAGE_CHANGE_VERSION_DFCE, VersionDFCE.DFCE_210_TO_230_SCHEMA.getNumVersion());
    setDatabaseVersionDFCE(VersionDFCE.DFCE_210_TO_230_SCHEMA.getNumVersion());
  }

  /**
   * Retour arrière de la version 2.3.1 à la version 1.9.2
   */
  public final void update230ToVersion192() {

    final int VERSION_INITIAL = 0;
    final long currentVersion = getDatabaseVersionDFCE();

    if (currentVersion == VERSION_INITIAL) {
      LOG.info(MESSAGE_DFCE_EST_A_JOUR, currentVersion, "Initial");
      return;
    }

    LOG.info("Retour à la version [initial] de la base DFCE");
    CQLDataFileSet cqlData;
    cqlData = new CQLDataFileSet(VersionDFCE.DFCE_230_TO_192_SCHEMA.cqlRelativeClasspath);
    final CQLDataFileLoader dataLoader = new CQLDataFileLoader(dfcecf.getSession());
    dataLoader.load(cqlData);
    LOG.info("Fin du Rollback en version [initial] de la base DFCE");

    // Met à jour la version de la base DFCE
    setDatabaseVersionDFCE(VERSION_INITIAL);
  }

  /**
   * Installation de la dernière version disponible de la base DFCE
   * 
   * @return true si la base à été mise à jour et false si elle était à jour
   * @throws MajLotGeneralException
   */
  public final boolean isReadyForUpdate() throws MajLotUnknownDFCEVersion {

    boolean isReady = false;

    final boolean isBaseSAE = isKeyspaceSAE();
    final long saeBdVersion = getDatabaseVersion();

    // Si base SAE créé et la version de la base est >= 33 ==> version dfce = 3
    if (isBaseSAE && saeBdVersion >= 33) {
      setDatabaseVersionDFCE(3);
    } else {
      //
    }

    // S'il existe une nouvelle mise à jour de la base DFCE
    final long dfceBdVersion = getDatabaseVersionDFCE();
    if (LAST_DFCE_AVAILABLE_VERSION > dfceBdVersion) {
      if (LAST_DFCE_AVAILABLE_VERSION == VersionDFCE.DFCE_192_TO_200_SCHEMA.getNumVersion()) {
        update192ToVersion200();
      } else if (LAST_DFCE_AVAILABLE_VERSION == VersionDFCE.DFCE_200_TO_210_SCHEMA.getNumVersion()) {
        update200ToVersion210();
      } else if (LAST_DFCE_AVAILABLE_VERSION == VersionDFCE.DFCE_210_TO_230_SCHEMA.getNumVersion()) {
        update210ToVersion230();
      } else {
        throw new MajLotUnknownDFCEVersion(LAST_DFCE_AVAILABLE_VERSION);
      }
    } else {
      LOG.info("DFCE est déjà à jour...");
    }

    // Tester la connexion à dfce
    try {
      isReady = dfceConnexionTester.testerWithServiceProvider();
    }
    catch (final Exception e) {
      LOG.info("Erreur de connexion à la webapp DFCE, Détails : {}", e.getMessage());
    }
    LOG.info("Test de connexion : {}", isReady ? "OK" : "KO");

    return isReady;
  }

  protected abstract long getDatabaseVersion();

  protected abstract void setDatabaseVersion(int version);

  protected abstract long getDatabaseVersionDFCE();

  protected abstract void setDatabaseVersionDFCE(int version);

  protected abstract boolean isKeyspaceSAE();

}
