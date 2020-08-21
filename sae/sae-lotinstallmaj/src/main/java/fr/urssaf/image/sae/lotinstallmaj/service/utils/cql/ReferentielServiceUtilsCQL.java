package fr.urssaf.image.sae.lotinstallmaj.service.utils.cql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.droit.dao.cql.impl.FormatControlProfilCqlDaoImpl;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.dao.support.cql.FormatControlProfilCqlSupport;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.cql.impl.ReferentielFormatCqlDaoImpl;
import fr.urssaf.image.sae.format.referentiel.dao.support.cql.ReferentielFormatCqlSupport;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.daocql.impl.TraceDestinataireCqlDaoImpl;

@Service
public class ReferentielServiceUtilsCQL {

  /**
   * Logger
   */
  private static final Logger LOG = LoggerFactory
      .getLogger(ReferentielServiceUtilsCQL.class);


  private final TraceDestinataireCqlDaoImpl traceDestinataireCqlDaoImpl;

  private final ReferentielFormatCqlSupport referentielFormatCqlSupport;

  private final FormatControlProfilCqlSupport formatControlProfilCqlSupport;


  /**
   * Libelle début de traitement sur référentiel des événements
   */
  private static final String LIBELLE_DEBUT_TRAITEMENT_REF_EVT = "Mise à jour du référentiel des événements";

  /**
   * Libelle début de traitement sur référentiel des événements
   */
  private static final String LIBELLE_DEBUT_TRAITEMENT_REF_FMT = "Mise à jour du référentiel des formats";

  /**
   * All infos
   */
  private static final String ALL_INFOS = "all_infos";

  /**
   * REG_TECHNIQUE
   */
  private static final String REG_TECHNIQUE = "REG_TECHNIQUE";

  /**
   * JOURN_EVT
   */
  private static final String JOURN_EVT = "JOURN_EVT";

  /**
   * Format fmt/354
   */
  private static final String FORMAT_FMT_354 = "fmt/354";

  /**
   * Format tar gz
   */
  private static final String FORMAT_TAR_GZ = "crtl/1";

  /**
   * format tiff
   */
  private static final String FORMAT_TIFF = "fmt/353";

  /**
   * Format txt
   */
  private static final String FORMAT_TXT = "x-fmt/111";

  /**
   * Format PNG
   */
  private static final String FORMAT_PNG = "fmt/13";

  /**
   * Format JPG
   */
  private static final String FORMAT_JPG = "fmt/44";

  /**
   * Format fmt/354
   */
  private static final String FORMAT_PDF = "pdf";

  private static final String FORMAT_CSV = "csv";

  private static final String FORMAT_DOC = "doc";

  private static final String FORMAT_DOCX = "docx";

  private static final String FORMAT_PPT = "ppt";

  private static final String FORMAT_DOCM = "docm";

  private static final String FORMAT_GIF = "gif";

  private static final String FORMAT_MIG_WAT2 = "migrationW2";

  private static final String FORMAT_HTML = "html";

  private static final String FORMAT_ZIP = "zip";

  private static final String FORMAT_XML = "xml";

  private static final String FORMAT_XLSX = "xlsx";

  private static final String FORMAT_XLSM = "xslm";

  private static final String FORMAT_XLSB = "xlsb";

  private static final String FORMAT_XLS = "xls";

  private static final String FORMAT_PPTX = "pptx";

  private static final String FORMAT_PPTM = "pptm";

  private static final String FORMAT_RTF = "rtf";

  private static final String FORMAT_EML = "eml";

  @Autowired
  public ReferentielServiceUtilsCQL(final CassandraCQLClientFactory ccf) {
    traceDestinataireCqlDaoImpl = new TraceDestinataireCqlDaoImpl(ccf);
    referentielFormatCqlSupport = new ReferentielFormatCqlSupport(new ReferentielFormatCqlDaoImpl(ccf));
    formatControlProfilCqlSupport = new FormatControlProfilCqlSupport(new FormatControlProfilCqlDaoImpl(ccf));
  }

  private void addTraceDestinataire(final String key, final String typeTrace, final List<String> destinataires) {
    final TraceDestinataire traceDestinataire = new TraceDestinataire();
    traceDestinataire.setCodeEvt(key);

    final Map<String, List<String>> mapDestinataires = new HashMap<>();
    mapDestinataires.put(typeTrace, destinataires);
    traceDestinataire.setDestinataires(mapDestinataires);
    traceDestinataireCqlDaoImpl.saveWithMapper(traceDestinataire);
  }

  /**
   * Initialisation du référentiel des événements en V1
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV1() {

    LOG.info("Initialisation du référentiel des événements CQL");

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // WS_RECHERCHE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_RECHERCHE|KO", REG_TECHNIQUE, allInfos);

    // WS_CAPTURE_MASSE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_CAPTURE_MASSE|KO", REG_TECHNIQUE, allInfos);

    // WS_CAPTURE_UNITAIRE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_CAPTURE_UNITAIRE|KO", REG_TECHNIQUE, allInfos);

    // WS_CONSULTATION|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_CONSULTATION|KO", REG_TECHNIQUE, allInfos);

    // WS_PING_SECURE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_PING_SECURE|KO", REG_TECHNIQUE, allInfos);

    // CAPTURE_MASSE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("CAPTURE_MASSE|KO", REG_TECHNIQUE, allInfos);

    // DFCE_DEPOT_DOC|OK
    // dans le journal des événements SAE avec all_infos
    addTraceDestinataire("DFCE_DEPOT_DOC|OK", JOURN_EVT, allInfos);

    // DFCE_SUPPRESSION_DOC|OK
    // dans le journal des événements SAE avec all_infos
    addTraceDestinataire("DFCE_SUPPRESSION_DOC|OK", JOURN_EVT, allInfos);

    // WS_LOAD_CERTS_ACRACINE|OK
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_LOAD_CERTS_ACRACINE|OK", REG_TECHNIQUE, allInfos);

    // WS_LOAD_CRLS|OK
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_LOAD_CRLS|OK", REG_TECHNIQUE, allInfos);

  }

  /**
   * Référentiel des événements en V2 Ajout de l'évenement MAJ_VERSION_RND|OK
   * (Automatisation RND)
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV2() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // MAJ_VERSION_RND|OK
    // dans le journal des événements SAE avec all_infos
    addTraceDestinataire("MAJ_VERSION_RND|OK", JOURN_EVT, allInfos);
    addTraceDestinataire("MAJ_VERSION_RND|OK", REG_TECHNIQUE, allInfos);

    addTraceDestinataire("DFCE_MODIF_DOC|OK", JOURN_EVT, allInfos);
  }

  /**
   * Référentiel des événements en V3 Ajout de l'évenement ORDO_ECDE_DISPO|KO
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV3() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // ORDO_ECDE_DISPO|KO
    // dans le registre de surveillance technique avec all_infos
    // dans le registre d'exploitation avec all_infos
    addTraceDestinataire("ORDO_ECDE_DISPO|KO", REG_TECHNIQUE, allInfos);
    addTraceDestinataire("ORDO_ECDE_DISPO|KO", "REG_EXPLOITATION", allInfos);
  }

  /**
   * Référentiel des événements en V4 Ajout des évenements IGC_LOAD_CRLS|KO,
   * WS_LOAD_CRLS|KO, ERREUR_IDENT_FORMAT_FICHIER|INFO et
   * ERREUR_VALID_FORMAT_FICHIER |INFO
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV4() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // IGC_LOAD_CRLS|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("IGC_LOAD_CRLS|KO", REG_TECHNIQUE, allInfos);

    // WS_LOAD_CRLS|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_LOAD_CRLS|KO", REG_TECHNIQUE, allInfos);

    // ERREUR_IDENT_FORMAT_FICHIER|INFO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("ERREUR_IDENT_FORMAT_FICHIER|INFO", REG_TECHNIQUE, allInfos);

    // ERREUR_VALID_FORMAT_FICHIER|INFO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("ERREUR_VALID_FORMAT_FICHIER|INFO", REG_TECHNIQUE, allInfos);

    // META_VAL_ESPACE|INFO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("META_VAL_ESPACE|INFO", REG_TECHNIQUE, allInfos);
  }

  /**
   * Référentiel des événements en V5 Ajout des évenements :
   * <li>
   * DFCE_TRANSFERT_DOC|OK</li>
   * <li>WS_TRANSFERT|KO</li>
   * 
   * @param keyspace
   *           Keyspace
   * @since 06/10/2014
   * @author Michael PAMBO OGNANA
   */
  public void addReferentielEvenementV5() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // -- DFCE_TRANSFERT_DOC|OK
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("DFCE_TRANSFERT_DOC|OK", JOURN_EVT, allInfos);

    // -- WS_TRANSFERT|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_TRANSFERT|KO", REG_TECHNIQUE, allInfos);
  }

  /**
   * Référentiel des événements en V6 Ajout des évenements :
   * <li>
   * WS_SUPPRESSION|KO</li>
   * <li>WS_MODIFICATION|KO</li>
   * <li>
   * WS_RECUPERATION_METAS|KO</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV6() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // -- WS_SUPPRESSION|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_SUPPRESSION|KO", REG_TECHNIQUE, allInfos);

    // -- WS_MODIFICATION|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_MODIFICATION|KO", REG_TECHNIQUE, allInfos);

    // -- WS_RECUPERATION_METAS|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_RECUPERATION_METAS|KO", REG_TECHNIQUE, allInfos);
  }

  /**
   * Référentiel des événements en V7 Ajout des évenements :
   * <li>
   * WS_AJOUT_NOTE|KO</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV7() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // -- WS_AJOUT_NOTE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_AJOUT_NOTE|KO", REG_TECHNIQUE, allInfos);
  }

  /**
   * Référentiel des événements en V8 Ajout des évenements :
   * <li>
   * WS_GET_DOC_FORMAT_ORIGINE|KO</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV8() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // -- WS_GET_DOC_FORMAT_ORIGINE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_GET_DOC_FORMAT_ORIGINE|KO", REG_TECHNIQUE, allInfos);
  }

  /**
   * Référentiel des événements en V9 Ajout des évenements :
   * <li>
   * DFCE_DEPOT_ATTACHC|OK</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV9() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // DFCE_DEPOT_ATTACH|OK
    // dans le journal des événements SAE avec all_infos
    addTraceDestinataire("DFCE_DEPOT_ATTACH|OK", JOURN_EVT, allInfos);
  }

  /**
   * Référentiel des événements en V10 Ajout des évenements :
   * <li>
   * WS_SUPPRESSION_MASSE|KO</li>
   * <li>WS_RESTORE_MASSE|KO</li>
   * <li>
   * SUPPRESSION_MASSE|KO</li>
   * <li>RESTORE_MASSE_KO</li>
   * <li>
   * DFCE_CORBEILLE_DOC|OK</li>
   * <li>DFCE_RESTORE_DOC|OK</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV10() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // WS_SUPPRESSION_MASSE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_SUPPRESSION_MASSE|KO", REG_TECHNIQUE, allInfos);

    // WS_RESTORE_MASSE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_RESTORE_MASSE|KO", REG_TECHNIQUE, allInfos);

    // SUPPRESSION_MASSE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("SUPPRESSION_MASSE|KO", REG_TECHNIQUE, allInfos);

    // RESTORE_MASSE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("RESTORE_MASSE|KO", REG_TECHNIQUE, allInfos);

    // DFCE_CORBEILLE_DOC|OK
    // dans le journal des événements SAE avec all_infos
    addTraceDestinataire("DFCE_CORBEILLE_DOC|OK", JOURN_EVT, allInfos);

    // DFCE_RESTORE_DOC|OK
    // dans le journal des événements SAE avec all_infos
    addTraceDestinataire("DFCE_RESTORE_DOC|OK", JOURN_EVT, allInfos);
  }

  /**
   * Référentiel des événements en V11 Ajout des évenements :
   * <li>
   * WS_ETAT_TRAITEMENTS_MASSE|KO</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielEvenementV11() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);

    final List<String> allInfos = Arrays.asList(ALL_INFOS);

    // WS_ETAT_TRAITEMENTS_MASSE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_ETAT_TRAITEMENTS_MASSE|KO", REG_TECHNIQUE, allInfos);
  }

  /**
   * Référentiel des événements en V12 Ajout des évenements :
   * <li>
   * WS_MODIFICATION_MASSE|KO</li>
   * <li>MODIFICATION_MASSE|KO</li>
   */
  public void addReferentielEvenementV12() {

    final List<String> allInfos = Arrays.asList("all_infos");

    // WS_MODIFICATION_MASSE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("WS_MODIFICATION_MASSE|KO", REG_TECHNIQUE, allInfos);

    // MODIFICATION_MASSE|KO
    // dans le registre de surveillance technique avec all_infos
    addTraceDestinataire("MODIFICATION_MASSE|KO", REG_TECHNIQUE, allInfos);

    // WS_TRANSFERT_MASSE|KO
    addTraceDestinataire("WS_TRANSFERT_MASSE|KO", REG_TECHNIQUE, allInfos);

    // TRANSFERT_MASSE|KO
    addTraceDestinataire("TRANSFERT_MASSE|KO", REG_TECHNIQUE, allInfos);

    // WS_DEBLOCAGE|KO
    addTraceDestinataire("WS_DEBLOCAGE|KO", REG_TECHNIQUE, allInfos);
  }

  /**
   * Ajout des données dans le référentiel des formats :
   * <li>fmt/354</li>
   * <li>
   * crtl/1</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielFormat() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_FMT);
    final FormatFichier formatFichierPDF = new FormatFichier();
    formatFichierPDF.setDescription("PDF/A 1b");
    formatFichierPDF.setExtension(FORMAT_PDF);
    formatFichierPDF.setTypeMime("application/pdf");
    formatFichierPDF.setVisualisable(Boolean.TRUE);
    formatFichierPDF.setIdentificateur("pdfaIdentifierImpl");
    formatFichierPDF.setValidator("pdfaValidatorImpl");
    formatFichierPDF.setIdFormat(FORMAT_FMT_354);

    referentielFormatCqlSupport.create(formatFichierPDF);

    final FormatFichier formatFichierGZ = new FormatFichier();
    formatFichierGZ.setDescription("Journal SAE, XML basé sur XSD, compressé en tar.gz");
    formatFichierGZ.setExtension("tar.gz");
    formatFichierGZ.setTypeMime("application/x-gzip");
    formatFichierGZ.setVisualisable(Boolean.FALSE);
    formatFichierGZ.setIdFormat(FORMAT_TAR_GZ);

    referentielFormatCqlSupport.create(formatFichierGZ);
  }

  /**
   * Ajout des données dans le référentiel des formats en V2 :
   * <li>fmt/353</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielFormatV2() {
    final FormatFichier formatFichierTIFF = new FormatFichier();
    formatFichierTIFF.setDescription("Fichier TIFF");
    formatFichierTIFF.setExtension("tif");
    formatFichierTIFF.setTypeMime("image/tiff");
    formatFichierTIFF.setVisualisable(Boolean.FALSE);
    formatFichierTIFF.setIdentificateur("pdfaIdentifierImpl");
    formatFichierTIFF.setConvertisseur("tiffToPdfConvertisseurImpl");
    formatFichierTIFF.setIdFormat(FORMAT_TIFF);

    referentielFormatCqlSupport.create(formatFichierTIFF);
  }

  /**
   * Ajout des données dans le référentiel des formats en V3 :
   * <li>x-fmt/111</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielFormatV3() {

    final FormatFichier formatFichierTXT = new FormatFichier();
    formatFichierTXT.setDescription("Fichier TXT (par exemple cold)");
    formatFichierTXT.setExtension("txt");
    formatFichierTXT.setTypeMime("text/plain");
    formatFichierTXT.setVisualisable(Boolean.TRUE);
    formatFichierTXT.setIdFormat(FORMAT_TXT);

    referentielFormatCqlSupport.create(formatFichierTXT);
  }

  /**
   * Ajout des données dans le référentiel des formats en V4 :
   * <li>pdf</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielFormatV4() {

    final FormatFichier formatFichierPDF = new FormatFichier();
    formatFichierPDF.setDescription("Tous fichiers PDF sans précision de version");
    formatFichierPDF.setExtension(FORMAT_PDF);
    formatFichierPDF.setTypeMime("application/pdf");
    formatFichierPDF.setVisualisable(Boolean.TRUE);
    formatFichierPDF.setConvertisseur("pdfSplitterImpl");
    formatFichierPDF.setIdFormat(FORMAT_PDF);

    referentielFormatCqlSupport.create(formatFichierPDF);
  }

  /**
   * Ajout des données dans le référentiel des formats en V5 :
   * <li>fmt/13</li>
   * <li>fmt/44</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielFormatV5() {

    final FormatFichier formatFichierPNG = new FormatFichier();
    formatFichierPNG.setDescription("Fichier PNG");
    formatFichierPNG.setExtension("png");
    formatFichierPNG.setTypeMime("image/png");
    formatFichierPNG.setVisualisable(Boolean.TRUE);
    formatFichierPNG.setIdFormat(FORMAT_PNG);

    final FormatFichier formatFichierJPG = new FormatFichier();
    formatFichierJPG.setDescription("Fichier JPG");
    formatFichierJPG.setExtension("jpg");
    formatFichierJPG.setTypeMime("image/jpeg");
    formatFichierJPG.setVisualisable(Boolean.TRUE);
    formatFichierJPG.setIdFormat(FORMAT_JPG);

    referentielFormatCqlSupport.create(formatFichierPNG);
    referentielFormatCqlSupport.create(formatFichierJPG);
  }

  /**
   * Ajout des données dans le référentiel des formats en V6 :
   * <li>migrationW2</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielFormatV6() {

    final FormatFichier formatFichierMigW2 = new FormatFichier();
    formatFichierMigW2.setDescription("Fichier migration WAT2");
    formatFichierMigW2.setExtension("*");
    formatFichierMigW2.setTypeMime("application/octet-stream");
    formatFichierMigW2.setVisualisable(Boolean.FALSE);
    formatFichierMigW2.setIdFormat(FORMAT_MIG_WAT2);
    referentielFormatCqlSupport.create(formatFichierMigW2);

    final FormatFichier formatFichierDoc = new FormatFichier();
    formatFichierDoc.setDescription("Fichier MS Word version 97/2003");
    formatFichierDoc.setExtension(FORMAT_DOC);
    formatFichierDoc.setTypeMime("application/msword");
    formatFichierDoc.setVisualisable(Boolean.TRUE);
    formatFichierDoc.setIdFormat(FORMAT_DOC);
    referentielFormatCqlSupport.create(formatFichierDoc);

    final FormatFichier formatFichierDocx = new FormatFichier();
    formatFichierDocx.setDescription("Fichier MS Word version 2007 et +");
    formatFichierDocx.setExtension(FORMAT_DOCX);
    formatFichierDocx.setTypeMime("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    formatFichierDocx.setVisualisable(Boolean.TRUE);
    formatFichierDocx.setIdFormat(FORMAT_DOCX);
    referentielFormatCqlSupport.create(formatFichierDocx);

    final FormatFichier formatFichierDocm = new FormatFichier();
    formatFichierDocm.setDescription("Fichier macro MS Word");
    formatFichierDocm.setExtension(FORMAT_DOCM);
    formatFichierDocm.setTypeMime("application/vnd.ms-word.document.macroEnabled.12");
    formatFichierDocm.setVisualisable(Boolean.FALSE);
    formatFichierDocm.setIdFormat(FORMAT_DOCM);
    referentielFormatCqlSupport.create(formatFichierDocm);

    final FormatFichier formatFichierXLS = new FormatFichier();
    formatFichierXLS.setDescription("Fichier MS Excel version 97/2003");
    formatFichierXLS.setExtension(FORMAT_XLS);
    formatFichierXLS.setTypeMime("application/vnd.ms-excel");
    formatFichierXLS.setVisualisable(Boolean.TRUE);
    formatFichierXLS.setIdFormat(FORMAT_XLS);
    referentielFormatCqlSupport.create(formatFichierXLS);

    final FormatFichier formatFichierXLSX = new FormatFichier();
    formatFichierXLSX.setDescription("Fichier MS Excel version 2007 et +");
    formatFichierXLSX.setExtension(FORMAT_XLSX);
    formatFichierXLSX.setTypeMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    formatFichierXLSX.setVisualisable(Boolean.TRUE);
    formatFichierXLSX.setIdFormat(FORMAT_XLSX);
    referentielFormatCqlSupport.create(formatFichierXLSX);

    final FormatFichier formatFichierXLSM = new FormatFichier();
    formatFichierXLSM.setDescription("Fichier macro MS Excel");
    formatFichierXLSM.setExtension(FORMAT_XLSM);
    formatFichierXLSM.setTypeMime("application/vnd.ms-excel.sheet.macroEnabled.12");
    formatFichierXLSM.setVisualisable(Boolean.FALSE);
    formatFichierXLSM.setIdFormat(FORMAT_XLSM);
    referentielFormatCqlSupport.create(formatFichierXLSM);

    final FormatFichier formatFichierXLSB = new FormatFichier();
    formatFichierXLSB.setDescription("Fichier binaire MS Excel");
    formatFichierXLSB.setExtension(FORMAT_XLSB);
    formatFichierXLSB.setTypeMime("application/vnd.ms-excel.sheet.binary.macroEnabled.12");
    formatFichierXLSB.setVisualisable(Boolean.FALSE);
    formatFichierXLSB.setIdFormat(FORMAT_XLSB);
    referentielFormatCqlSupport.create(formatFichierXLSB);

    final FormatFichier formatFichierGIF = new FormatFichier();
    formatFichierGIF.setDescription("Fichier image numerique");
    formatFichierGIF.setExtension(FORMAT_GIF);
    formatFichierGIF.setTypeMime("image/gif");
    formatFichierGIF.setVisualisable(Boolean.TRUE);
    formatFichierGIF.setIdFormat(FORMAT_GIF);
    referentielFormatCqlSupport.create(formatFichierGIF);

    final FormatFichier formatFichierCSV = new FormatFichier();
    formatFichierCSV.setDescription("Fichier tableur (Comma Separated Values)");
    formatFichierCSV.setExtension(FORMAT_CSV);
    formatFichierCSV.setTypeMime("text/csv");
    formatFichierCSV.setVisualisable(Boolean.TRUE);
    formatFichierCSV.setIdFormat(FORMAT_CSV);
    referentielFormatCqlSupport.create(formatFichierCSV);

    final FormatFichier formatFichierPPT = new FormatFichier();
    formatFichierPPT.setDescription("Fichier MS PowerPoint version 97/2003");
    formatFichierPPT.setExtension(FORMAT_PPT);
    formatFichierPPT.setTypeMime("application/vnd.ms-powerpoint");
    formatFichierPPT.setVisualisable(Boolean.TRUE);
    formatFichierPPT.setIdFormat(FORMAT_PPT);
    referentielFormatCqlSupport.create(formatFichierPPT);

    final FormatFichier formatFichierXML = new FormatFichier();
    formatFichierXML.setDescription("Fichier à langage de balisage extensible (eXtensible Markup Language)");
    formatFichierXML.setExtension(FORMAT_XML);
    formatFichierXML.setTypeMime("application/xhtml+xml");
    formatFichierXML.setVisualisable(Boolean.TRUE);
    formatFichierXML.setIdFormat(FORMAT_XML);
    referentielFormatCqlSupport.create(formatFichierXML);

    final FormatFichier formatFichierZIP = new FormatFichier();
    formatFichierZIP.setDescription("Fichier archive ZIP");
    formatFichierZIP.setExtension(FORMAT_ZIP);
    formatFichierZIP.setTypeMime("application/zip");
    formatFichierZIP.setVisualisable(Boolean.FALSE);
    formatFichierZIP.setIdFormat(FORMAT_ZIP);
    referentielFormatCqlSupport.create(formatFichierZIP);

    final FormatFichier formatFichierHTML = new FormatFichier();
    formatFichierHTML.setDescription("Fichier à langage de balisage d'hypertexte (HyperText Markup Language)");
    formatFichierHTML.setExtension(FORMAT_HTML);
    formatFichierHTML.setTypeMime("text/html");
    formatFichierHTML.setVisualisable(Boolean.TRUE);
    formatFichierHTML.setIdFormat(FORMAT_HTML);
    referentielFormatCqlSupport.create(formatFichierHTML);
  }

  /**
   * Ajout des données dans le référentiel des formats en V6 :
   * <li>migrationW2</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addReferentielFormatV6Bis() {

    final FormatFichier formatFichierPPTX = new FormatFichier();
    formatFichierPPTX.setDescription("Fichier MS PowerPoint version 2007 et +");
    formatFichierPPTX.setExtension(FORMAT_PPTX);
    formatFichierPPTX.setTypeMime("application/vnd.openxmlformats-officedocument.presentationml.presentation");
    formatFichierPPTX.setVisualisable(Boolean.TRUE);
    formatFichierPPTX.setAutoriseGED(Boolean.FALSE);
    formatFichierPPTX.setIdFormat(FORMAT_PPTX);
    referentielFormatCqlSupport.create(formatFichierPPTX);

    final FormatFichier formatFichierPPTM = new FormatFichier();
    formatFichierPPTM.setDescription("Fichier MS PowerPoint macro");
    formatFichierPPTM.setExtension(FORMAT_PPTM);
    formatFichierPPTM.setTypeMime("application/vnd.ms-powerpoint.presentation.macroEnabled.12");
    formatFichierPPTM.setVisualisable(Boolean.FALSE);
    formatFichierPPTM.setAutoriseGED(Boolean.FALSE);
    formatFichierPPTM.setIdFormat(FORMAT_PPTM);
    referentielFormatCqlSupport.create(formatFichierPPTM);
  }

  /**
   * Ajout des données dans le référentiel des formats en V7 :
   * <li>modification
   * fmt/13 en png</li>
   */
  public void addReferentielFormatV7() {

    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_FMT);

    // Modification de l'indentifiant fmt/13 en png
    final String formatPNG = "png";
    try {
      referentielFormatCqlSupport.delete(FORMAT_PNG);
    }
    catch (final UnknownFormatException e) {
      throw new MajLotRuntimeException(e);
    }

    final FormatFichier formatFichierPNG = new FormatFichier();
    formatFichierPNG.setDescription("Fichier PNG");
    formatFichierPNG.setExtension(formatPNG);
    formatFichierPNG.setTypeMime("image/png");
    formatFichierPNG.setVisualisable(Boolean.TRUE);
    formatFichierPNG.setAutoriseGED(Boolean.TRUE);
    formatFichierPNG.setIdFormat(formatPNG);
    referentielFormatCqlSupport.create(formatFichierPNG);

    LOG.info("Format modifié : {}", formatPNG);
  }

  /**
   * Modification des données dans le référentiel des formats : Ajout d'un
   * convertisseur pour le format
   * <li>fmt/354</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void modifyReferentielFormatFmt354() {
    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_FMT);

    final FormatFichier formatFichierPDF = referentielFormatCqlSupport.find(FORMAT_FMT_354);
    if (formatFichierPDF != null) {
      formatFichierPDF.setConvertisseur("pdfSplitterImpl");
      LOG.info("Format modifié : fmt/354");
    }
    LOG.info("Format modifié : fmt/354");
  }

  /**
   * Modification de l''extension pour le format Tiff
   * <li>fmt/353</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void modifyReferentielFormatFmt353() {
    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_FMT);


    final FormatFichier formatFichierTIFF = referentielFormatCqlSupport.find(FORMAT_TIFF);
    if (formatFichierTIFF != null) {
      formatFichierTIFF.setExtension("tif,tiff");
      referentielFormatCqlSupport.create(formatFichierTIFF);
    }

    LOG.info("Format modifié : {}", FORMAT_TIFF);
  }

  /**
   * Modification des données dans le référentiel des formats : Ajout d'un
   * convertisseur pour le format
   * <li>fmt/44</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void modifyReferentielFormatFmt44() {
    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_FMT);

    final FormatFichier formatFichierJPG = referentielFormatCqlSupport.find(FORMAT_JPG);
    if (formatFichierJPG != null) {
      formatFichierJPG.setExtension("jpg,jpeg");
      referentielFormatCqlSupport.create(formatFichierJPG);
    }

    LOG.info("Format modifié : fmt/44");
  }

  /**
   * Modification des données dans le référentiel des formats : Ajout d'un
   * convertisseur pour le format
   * <li>crtl/1</li>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void modifyReferentielFormatCrtl1() {
    LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_FMT);

    final FormatFichier formatFichierGZ = referentielFormatCqlSupport.find(FORMAT_TAR_GZ);
    if (formatFichierGZ != null) {
      formatFichierGZ.setExtension("tar.gz,gz");
      formatFichierGZ.setAutoriseGED(Boolean.FALSE);
      referentielFormatCqlSupport.create(formatFichierGZ);
    }

    LOG.info("Format modifié : {}", FORMAT_TAR_GZ);
  }

  /**
   * Les profils de controle pour les formats.
   * Ajout de 3 éléments pour le format fmt/354:
   * <ul>
   * <li>Identification seule</li>
   * <li>Validation seule</li>
   * <li>Identification et validation</li>
   * </ul>
   * 
   * @param keyspace
   *           Keyspace
   */
  public void addFormatControleProfil() {

    final String validationModeStrict = "STRICT";

    final FormatControlProfil formatControlProfil = new FormatControlProfil();
    formatControlProfil.setFormatCode("IDENT_FMT_354");
    formatControlProfil.setDescription("format de controle gérant exclusivement l'identification du fmt/354");

    final FormatProfil formatProfil = new FormatProfil();
    formatProfil.setFileFormat(FORMAT_FMT_354);
    formatProfil.setFormatIdentification(true);
    formatProfil.setFormatValidation(false);
    formatProfil.setFormatValidationMode(validationModeStrict);

    formatControlProfil.setControlProfil(formatProfil);
    formatControlProfilCqlSupport.create(formatControlProfil);

    /*****/

    final FormatControlProfil formatControlProfil1 = new FormatControlProfil();
    formatControlProfil1.setFormatCode("VALID_FMT_354");
    formatControlProfil1.setDescription("format de controle gérant exclusivement la validation du fmt/354");

    final FormatProfil formatProfil1 = new FormatProfil();
    formatProfil1.setFileFormat(FORMAT_FMT_354);
    formatProfil1.setFormatIdentification(false);
    formatProfil1.setFormatValidation(true);
    formatProfil1.setFormatValidationMode(validationModeStrict);

    formatControlProfil1.setControlProfil(formatProfil1);
    formatControlProfilCqlSupport.create(formatControlProfil1);

    /******/

    final FormatControlProfil formatControlProfil2 = new FormatControlProfil();
    formatControlProfil2.setFormatCode("IDENT_VALID_FMT_354");

    final FormatProfil formatProfil2 = new FormatProfil();
    formatProfil2.setFileFormat(FORMAT_FMT_354);
    formatProfil2.setFormatIdentification(true);
    formatProfil2.setFormatValidation(true);
    formatProfil2.setFormatValidationMode(validationModeStrict);

    formatControlProfil2.setControlProfil(formatProfil2);
    formatControlProfilCqlSupport.create(formatControlProfil2);
  }

  /**
   * Methode permettant d'ajouter la ligne autorisé en GED dans le CF
   * ReferentielFormat.
   */
  public void addColumnAutoriseGEDReferentielFormat() {

    // tar.gz
    final FormatFichier formatFichierGZ = referentielFormatCqlSupport.find(FORMAT_TAR_GZ);
    if (formatFichierGZ != null) {
      formatFichierGZ.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierGZ);
    }
    // csv
    final FormatFichier formatFichierCSV = referentielFormatCqlSupport.find(FORMAT_CSV);
    if (formatFichierCSV != null) {
      formatFichierCSV.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierCSV);
    }
    // doc
    final FormatFichier formatFichierDOC = referentielFormatCqlSupport.find(FORMAT_DOC);
    if (formatFichierDOC != null) {
      formatFichierDOC.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierDOC);
    }
    // docm
    final FormatFichier formatFichierDOCM = referentielFormatCqlSupport.find(FORMAT_DOCM);
    if (formatFichierDOCM != null) {
      formatFichierDOCM.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierDOCM);
    }
    // docx
    final FormatFichier formatFichierDOCX = referentielFormatCqlSupport.find(FORMAT_DOCX);
    if (formatFichierDOCX != null) {
      formatFichierDOCX.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierDOCX);
    }
    // png
    final FormatFichier formatFichierPNG = referentielFormatCqlSupport.find(FORMAT_PNG);
    if (formatFichierPNG != null) {
      formatFichierPNG.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierPNG);
    }
    // tif
    final FormatFichier formatFichierTIF = referentielFormatCqlSupport.find(FORMAT_TIFF);
    if (formatFichierTIF != null) {
      formatFichierTIF.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierTIF);
    }
    // pdf
    final FormatFichier formatFichierPDF = referentielFormatCqlSupport.find(FORMAT_FMT_354);
    if (formatFichierPDF != null) {
      formatFichierPDF.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierPDF);
    }
    // jpeg
    final FormatFichier formatFichierJPG = referentielFormatCqlSupport.find(FORMAT_JPG);
    if (formatFichierJPG != null) {
      formatFichierJPG.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierJPG);
    }
    // gif
    final FormatFichier formatFichierGIF = referentielFormatCqlSupport.find(FORMAT_GIF);
    if (formatFichierGIF != null) {
      formatFichierGIF.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierGIF);
    }
    // html
    final FormatFichier formatFichierHTML = referentielFormatCqlSupport.find(FORMAT_HTML);
    if (formatFichierHTML != null) {
      formatFichierHTML.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierHTML);
    }
    // migrationW2
    final FormatFichier formatFichierW2 = referentielFormatCqlSupport.find(FORMAT_MIG_WAT2);
    if (formatFichierW2 != null) {
      formatFichierW2.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierW2);
    }
    // pdf
    final FormatFichier formatFichierPDF1 = referentielFormatCqlSupport.find(FORMAT_PDF);
    if (formatFichierPDF1 != null) {
      formatFichierPDF1.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierPDF1);
    }
    // ppt
    final FormatFichier formatFichierPPT = referentielFormatCqlSupport.find(FORMAT_PPT);
    if (formatFichierPPT != null) {
      formatFichierPPT.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierPPT);
    }
    // pptx
    final FormatFichier formatFichierPPTX = referentielFormatCqlSupport.find(FORMAT_PPTX);
    if (formatFichierPPTX != null) {
      formatFichierPPTX.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierPPTX);
    }
    // xls
    final FormatFichier formatFichierXLS = referentielFormatCqlSupport.find(FORMAT_XLS);
    if (formatFichierXLS != null) {
      formatFichierXLS.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierXLS);
    }
    // xlsb
    final FormatFichier formatFichierXLSB = referentielFormatCqlSupport.find(FORMAT_XLSB);
    if (formatFichierXLSB != null) {
      formatFichierXLSB.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierXLSB);
    }
    // xlsm
    final FormatFichier formatFichierXLM = referentielFormatCqlSupport.find(FORMAT_XLSM);
    if (formatFichierXLM != null) {
      formatFichierXLM.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierXLM);
    }
    // xlsx
    final FormatFichier formatFichierXLSX = referentielFormatCqlSupport.find(FORMAT_XLSX);
    if (formatFichierXLSX != null) {
      formatFichierXLSX.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierXLSX);
    }
    // xml
    final FormatFichier formatFichierXML = referentielFormatCqlSupport.find(FORMAT_XML);
    if (formatFichierXML != null) {
      formatFichierXML.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierXML);
    }
    // zip
    final FormatFichier formatFichierZIP = referentielFormatCqlSupport.find(FORMAT_ZIP);
    if (formatFichierZIP != null) {
      formatFichierZIP.setAutoriseGED(false);
      referentielFormatCqlSupport.create(formatFichierZIP);
    }

    // txt
    final FormatFichier formatFichierTXT = referentielFormatCqlSupport.find(FORMAT_TXT);
    if (formatFichierTXT != null) {
      formatFichierTXT.setAutoriseGED(true);
      referentielFormatCqlSupport.create(formatFichierTXT);
    }
  }

  /**
   * Référentiel des événements en V13 Ajout des évenements :
   * <li>
   * WS_REPRISE_MASSE|KO</li>
   * <li>REPRISE_MASSE|KO</li>
   */
  public void addReferentielEvenementV13() {
    final List<String> allInfos = Arrays.asList("all_infos");

    addTraceDestinataire("WS_REPRISE_MASSE|KO", REG_TECHNIQUE, allInfos);
    addTraceDestinataire("REPRISE_MASSE|KO", REG_TECHNIQUE, allInfos);
  }

  /**
   * Référentiel des événements en V14 Ajout des évenements :
   * <li>
   * WS_COPIE|KO</li>
   */
  public void addReferentielEvenementV14() {
    final List<String> allInfos = Arrays.asList("all_infos");

    addTraceDestinataire("DFCE_COPIE_DOC|OK", JOURN_EVT, allInfos);
    addTraceDestinataire("WS_COPIE|KO", REG_TECHNIQUE, allInfos);
  }

  /**
   * Référentiel des événements en V15 Ajout des évenements :
   * <li>
   * WS_DEBLOCAGE|OK</li>
   */
  public void addReferentielEvenementV15() {
    final List<String> allInfos = Arrays.asList("all_infos");

    addTraceDestinataire("WS_DEBLOCAGE|OK", REG_TECHNIQUE, allInfos);
  }

  /**
   * Ajout des formats RTF et EML
   */
  public void addReferentielFormatV8() {
    // RTF
    final FormatFichier formatFichierRTF = new FormatFichier();
    formatFichierRTF.setDescription("Format rtf");
    formatFichierRTF.setExtension("rtf");
    formatFichierRTF.setTypeMime("application/rtf");
    formatFichierRTF.setVisualisable(Boolean.TRUE);
    formatFichierRTF.setAutoriseGED(Boolean.FALSE);
    formatFichierRTF.setIdFormat(FORMAT_RTF);
    referentielFormatCqlSupport.create(formatFichierRTF);

    // EML
    final FormatFichier formatFichierEML = new FormatFichier();
    formatFichierEML.setDescription("Fichier mail");
    formatFichierEML.setExtension("eml");
    formatFichierEML.setTypeMime("message/rfc822");
    formatFichierEML.setVisualisable(Boolean.FALSE);
    formatFichierEML.setAutoriseGED(Boolean.FALSE);
    formatFichierEML.setIdFormat(FORMAT_EML);
    referentielFormatCqlSupport.create(formatFichierEML);

  }

}
