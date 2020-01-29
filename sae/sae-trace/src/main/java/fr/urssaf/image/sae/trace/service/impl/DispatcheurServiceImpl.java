/**
 *
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegExploitationCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegExploitationSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegExploitationCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegSecuriteCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegTechniqueCqlSupport;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;

/**
 * Classe d'implémentation du support {@link DispatcheurService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 */
@Service
@Qualifier("dispatcheurService")
public class DispatcheurServiceImpl implements DispatcheurService {

  private static final String ARG_0 = "0";

  private static final String ARG_1 = "1";

  private static final String ARG_2 = "2";

  private static final String USERNAME = "_ADMIN";

  private static final String FIN_LOG = "{} - fin";

  private static final String DEBUT_LOG = "{} - début";

  private static final Logger LOGGER = LoggerFactory.getLogger(DispatcheurServiceImpl.class);

  private static final String MESSAGE_ERREUR_REGISTRE = "l'argument ${0} est obligatoire dans le ${1} ${2}";

  private static final String REG_SECURITE = "de sécurité";

  private static final String REG_EXPLOITATION = "d'exploitation";

  private static final String REG_TECHNIQUE = "technique";

  private static final String JOURNAL_EVT = "des événements SAE";

  private static final String REGISTRE = "registre";

  private static final String JOURNAL = "journal";

  private static final List<String> DEST_AUTORISES = Arrays.asList(TraceDestinataireDao.COL_HIST_ARCHIVE,
                                                                   TraceDestinataireDao.COL_HIST_EVT,
                                                                   TraceDestinataireDao.COL_REG_EXPLOIT,
                                                                   TraceDestinataireDao.COL_REG_SECURITE,
                                                                   TraceDestinataireDao.COL_REG_TECHNIQUE,
                                                                   TraceDestinataireDao.COL_JOURN_EVT);

  private static final List<String> REG_AUTORISES = Arrays.asList(TraceDestinataireDao.COL_REG_EXPLOIT,
                                                                  TraceDestinataireDao.COL_REG_SECURITE,
                                                                  TraceDestinataireDao.COL_REG_TECHNIQUE);

  private static final List<String> JOURN_AUTORISES = Arrays.asList(TraceDestinataireDao.COL_JOURN_EVT);

  private final JobClockSupport clockSupport;

  private final TraceDestinataireSupport destSupport;

  private final TraceDestinataireCqlSupport destCqlSupport;

  private final String cfNameDestinataire = "tracedestinataire";

  private final TraceRegSecuriteSupport secuSupport;

  private final TraceRegSecuriteCqlSupport secuCqlSupport;

  private final String cfNameRegSecu = "traceregsecurite";

  private final TraceRegExploitationSupport exploitSupport;

  private final TraceRegExploitationCqlSupport exploitCqlSupport;

  private final String cfNameRefExploit = "traceregexploitation";

  private final TraceRegTechniqueSupport techSupport;

  private final TraceRegTechniqueCqlSupport techCqlSupport;

  private final String cfNameRegTech = "traceregtechnique";

  private final TraceJournalEvtSupport evtSupport;

  private final TraceJournalEvtCqlSupport evtCqlSupport;

  private final String cfNameEvt = "tracejournalevt";

  private final TimeUUIDEtTimestampSupport timeUUIDSupport;

  private final DFCEServices dfceServices;

  final String KEY_ID_DOC = "idDoc";

  /**
   * Constructeur
   *
   * @param clockSupport
   *          Support pour le calcul de l'horloge sur Cassandra
   * @param destSupport
   *          Support de la classe DAO TraceDestinataireDao
   * @param secuSupport
   *          Support de la classe DAO TraceJournalEvtDao
   * @param techSupport
   *          Support de la classe DAO TraceRegTechniqueDao
   * @param exploitSupport
   *          Support de la classe DAO TraceRegExploitationDao
   * @param evtSupport
   *          Support de la classe DAO TraceJournalEvtDao
   * @param providerSupport
   *          Support pour la gestion de la connexion à DFCE
   * @param timeUUIDSupport
   *          Utilitaires pour créer des TimeUUID
   */
  @Autowired
  public DispatcheurServiceImpl(final JobClockSupport clockSupport, final TraceDestinataireSupport destSupport,
                                final TraceDestinataireCqlSupport destCqlSupport, final TraceRegSecuriteSupport secuSupport,
                                final TraceRegSecuriteCqlSupport secuCqlSupport, final TraceRegTechniqueSupport techSupport,
                                final TraceRegTechniqueCqlSupport techCqlSupport, final TraceRegExploitationSupport exploitSupport,
                                final TraceRegExploitationCqlSupport exploitCqlSupport, final TraceJournalEvtSupport evtSupport,
                                final TraceJournalEvtCqlSupport evtCqlSupport, final TimeUUIDEtTimestampSupport timeUUIDSupport,
                                final DFCEServices dfceServices) {

    this.clockSupport = clockSupport;
    this.destSupport = destSupport;
    this.secuSupport = secuSupport;
    this.techSupport = techSupport;
    this.exploitSupport = exploitSupport;
    this.evtSupport = evtSupport;

    this.destCqlSupport = destCqlSupport;
    this.secuCqlSupport = secuCqlSupport;
    this.techCqlSupport = techCqlSupport;
    this.exploitCqlSupport = exploitCqlSupport;
    this.evtCqlSupport = evtCqlSupport;

    this.timeUUIDSupport = timeUUIDSupport;

    this.dfceServices = dfceServices;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void ajouterTrace(final TraceToCreate trace) {

    final String prefix = "ajouterTrace()";
    LOGGER.debug(DEBUT_LOG, prefix);

    TraceDestinataire traceDest = new TraceDestinataire();

    final String codeEvt = trace.getCodeEvt();

    final String modeApi = ModeGestionAPI.getModeApiCf(cfNameDestinataire);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      traceDest = destCqlSupport.find(codeEvt);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)) {
      traceDest = destSupport.find(codeEvt);
    }

    for (final String type : traceDest.getDestinataires().keySet()) {
      createTrace(codeEvt, type, traceDest.getDestinataires().get(type), trace);

    }

    LOGGER.debug(FIN_LOG, prefix);

  }

  private void createTrace(final String codeEvt, final String type, final List<String> list, final TraceToCreate trace) {

    if (!DEST_AUTORISES.contains(type)) {
      LOGGER.warn("Le destinataire {0} ne doit pas exister pour l'événement {1}", new Object[] {type, codeEvt});

    } else if (REG_AUTORISES.contains(type) || JOURN_AUTORISES.contains(type)) {
      checkCategoriesValues(trace, type);
      saveTrace(trace, type, list);

    } else if (TraceDestinataireDao.COL_HIST_EVT.equals(type)) {
      saveTraceHistEvt(trace);

    } else {
      LOGGER.debug("Fonctionnalité non prise en charge pour le moment");
    }

  }

  private void checkCategoriesValues(final TraceToCreate trace, final String type) {

    if (trace == null) {
      throw new IllegalArgumentException("la trace doit etre non nulle");
    }

    String suffixe;
    if (TraceDestinataireDao.COL_REG_EXPLOIT.equals(type)) {
      suffixe = REG_EXPLOITATION;
      checkStringValue("action", trace.getAction(), suffixe, REGISTRE);

    } else if (TraceDestinataireDao.COL_REG_SECURITE.equals(type)) {
      suffixe = REG_SECURITE;
      checkStringValue("contexte", trace.getContexte(), suffixe, REGISTRE);

    } else if (TraceDestinataireDao.COL_REG_TECHNIQUE.equals(type)) {
      suffixe = REG_TECHNIQUE;
      checkStringValue("contexte", trace.getContexte(), suffixe, REGISTRE);

    } else if (TraceDestinataireDao.COL_JOURN_EVT.equals(type)) {
      suffixe = JOURNAL_EVT;
      checkStringValue("contexte", trace.getContexte(), suffixe, JOURNAL);

    } else {
      throw new IllegalArgumentException("pas de vérification prévue pour cette trace");
    }

    String typeTrace = REGISTRE;
    if (JOURN_AUTORISES.contains(type)) {
      typeTrace = JOURNAL;
    }
    checkStringValue("code événement", trace.getCodeEvt(), suffixe, typeTrace);

    // Dans certains cas, on ne dispose pas du CS ni du login
    // Par exemple, dans le cas d'un appel WS où il manque l'en-tête
    // de sécurité et pour lequel on va quand même tracer l'erreur
    // checkStringValues("contrat de service ou login", Arrays.asList(trace
    // .getContrat(), trace.getLogin()), suffixe);

  }

  private void checkStringValue(final String name, final String value, final String suffixe, final String categorie) {

    if (StringUtils.isBlank(value)) {
      final Map<String, String> map = new HashMap<>();
      map.put(ARG_0, name);
      map.put(ARG_1, categorie);
      map.put(ARG_2, suffixe);
      throw new IllegalArgumentException(StrSubstitutor.replace(MESSAGE_ERREUR_REGISTRE, map));
    }

  }

  private void saveTrace(final TraceToCreate trace, final String type, final List<String> list) {

    final String prefix = "saveTrace()";
    LOGGER.debug(DEBUT_LOG, prefix);

    final long timestamp = timeUUIDSupport.getCurrentTimestamp();
    final UUID idTrace = timeUUIDSupport.buildUUIDFromTimestamp(timestamp);
    final Date timestampTrace = timeUUIDSupport.getDateFromTimestamp(timestamp);

    switch (type) {
    case TraceDestinataireDao.COL_REG_EXPLOIT:
      createTraceRegExploitation(trace, list, prefix, idTrace, timestampTrace);
      break;

    case TraceDestinataireDao.COL_REG_SECURITE:
      createTraceRegSecurite(trace, list, prefix, idTrace, timestampTrace);
      break;

    case TraceDestinataireDao.COL_REG_TECHNIQUE:
      createTraceRegTechnique(trace, list, prefix, idTrace, timestampTrace);
      break;

    case TraceDestinataireDao.COL_JOURN_EVT:
      createTraceJournalEvt(trace, list, prefix, idTrace, timestampTrace);
      break;
    default:
      throw new IllegalArgumentException(StringUtils.replace("pas de type existant {0} à convertir", "{0}", type));
    }

    LOGGER.debug(FIN_LOG, prefix);

  }

  /**
   * Création TraceJournalEvt suivant modeApi
   * 
   * @param trace
   * @param list
   * @param prefix
   * @param idTrace
   * @param timestampTrace
   */
  private void createTraceJournalEvt(final TraceToCreate trace, final List<String> list, final String prefix, final UUID idTrace, final Date timestampTrace) {
    LOGGER.debug("{} - ajout d'une trace journal des événements", prefix);
    final TraceJournalEvt traceJournalEvt = new TraceJournalEvt(trace, list, idTrace, timestampTrace);


    final long currentCLock = clockSupport.currentCLock();

    switch (ModeGestionAPI.getModeApiCf(cfNameEvt)) {

    case ModeGestionAPI.MODE_API.DATASTAX:
      createTraceJournalEvt_DATASTAX(trace, traceJournalEvt, currentCLock);

      break;

    case ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT:
    case ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL:
      createTraceJournalEvt_HECTOR(trace, traceJournalEvt, currentCLock);
      createTraceJournalEvt_DATASTAX(trace, traceJournalEvt, currentCLock);
      break;

    case ModeGestionAPI.MODE_API.HECTOR:
      createTraceJournalEvt_HECTOR(trace, traceJournalEvt, currentCLock);
      break;

    default:
      // Rien
      break;
    }
  }

  /**
   * Création TraceRegTechnique suivant modeApi
   * 
   * @param trace
   * @param list
   * @param prefix
   * @param idTrace
   * @param timestampTrace
   */
  private void createTraceRegTechnique(final TraceToCreate trace, final List<String> list, final String prefix, final UUID idTrace, final Date timestampTrace) {
    LOGGER.debug("{} - ajout d'une trace technique", prefix);
    final TraceRegTechnique traceTechnique = new TraceRegTechnique(trace, list, idTrace, timestampTrace);

    TraceRegTechniqueCql traceCql = null;

    switch (ModeGestionAPI.getModeApiCf(cfNameRegTech)) {

    case ModeGestionAPI.MODE_API.DATASTAX:
      traceCql = UtilsTraceMapper.createTraceRegTechniqueFromThriftToCql(traceTechnique);
      techCqlSupport.create(traceCql, clockSupport.currentCLock());

      break;
    case ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT:
    case ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL:
      traceCql = UtilsTraceMapper.createTraceRegTechniqueFromThriftToCql(traceTechnique);
      techCqlSupport.create(traceCql, clockSupport.currentCLock());
      techSupport.create(traceTechnique, clockSupport.currentCLock());
      break;

    case ModeGestionAPI.MODE_API.HECTOR:
      techSupport.create(traceTechnique, clockSupport.currentCLock());
      break;

    default:
      // Rien
      break;
    }
  }

  /**
   * Création TraceRegSecurite suivant modeApi
   * @param trace
   * @param list
   * @param prefix
   * @param idTrace
   * @param timestampTrace
   */
  private void createTraceRegSecurite(final TraceToCreate trace, final List<String> list, final String prefix, final UUID idTrace, final Date timestampTrace) {
    LOGGER.debug("{} - ajout d'une trace de sécurité", prefix);
    final TraceRegSecurite traceSecurite = new TraceRegSecurite(trace, list, idTrace, timestampTrace);
    TraceRegSecuriteCql traceCql = null;

    switch (ModeGestionAPI.getModeApiCf(cfNameRegSecu)) {

    case ModeGestionAPI.MODE_API.DATASTAX:
      traceCql = UtilsTraceMapper.createTraceRegSecuFromThriftToCql(traceSecurite);
      secuCqlSupport.create(traceCql, clockSupport.currentCLock());

      break;
    case ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT:
    case ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL:
      traceCql = UtilsTraceMapper.createTraceRegSecuFromThriftToCql(traceSecurite);
      secuCqlSupport.create(traceCql, clockSupport.currentCLock());
      secuSupport.create(traceSecurite, clockSupport.currentCLock());
      break;

    case ModeGestionAPI.MODE_API.HECTOR:
      secuSupport.create(traceSecurite, clockSupport.currentCLock());
      break;

    default:
      // Rien
      break;
    }
  }

  /**
   * Création TraceRegExploitation suivant modeApi
   * 
   * @param trace
   * @param list
   * @param prefix
   * @param idTrace
   * @param timestampTrace
   */
  private void createTraceRegExploitation(final TraceToCreate trace, final List<String> list, final String prefix, final UUID idTrace,
                                          final Date timestampTrace) {
    LOGGER.debug("{} - ajout d'une trace d'exploitation", prefix);
    final TraceRegExploitation traceExploit = new TraceRegExploitation(trace, list, idTrace, timestampTrace);

    TraceRegExploitationCql traceCql = null;

    switch (ModeGestionAPI.getModeApiCf(cfNameRefExploit)) {

    case ModeGestionAPI.MODE_API.DATASTAX:
      traceCql = UtilsTraceMapper.createTraceRegExploitationFromThriftToCql(traceExploit);
      exploitCqlSupport.create(traceCql, clockSupport.currentCLock());

      break;
    case ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT:
    case ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL:
      traceCql = UtilsTraceMapper.createTraceRegExploitationFromThriftToCql(traceExploit);
      exploitCqlSupport.create(traceCql, clockSupport.currentCLock());
      exploitSupport.create(traceExploit, clockSupport.currentCLock());
      break;

    case ModeGestionAPI.MODE_API.HECTOR:
      exploitSupport.create(traceExploit, clockSupport.currentCLock());
      break;

    default:
      // Rien
      break;
    }
  }

  /**
   * Création TraceJournalEvt en mode CQL
   * 
   * @param trace
   * @param traceJournalEvt
   * @param currentCLock
   */
  private void createTraceJournalEvt_DATASTAX(final TraceToCreate trace, final TraceJournalEvt traceJournalEvt, final long currentCLock) {
    TraceJournalEvtCql traceCql;
    String idDoc;
    traceCql = UtilsTraceMapper.createTraceJournalEvtFromThriftToCql(traceJournalEvt);
    evtCqlSupport.create(traceCql, clockSupport.currentCLock());
    idDoc = getIdDoc(trace.getInfos());
    if (idDoc != null) {
      evtCqlSupport.addIndexDoc(traceCql, idDoc, currentCLock);
    }
  }

  /**
   * Création TraceJournalEvt en mode Thrift
   * 
   * @param trace
   * @param traceJournalEvt
   * @param currentCLock
   */
  private void createTraceJournalEvt_HECTOR(final TraceToCreate trace, final TraceJournalEvt traceJournalEvt, final long currentCLock) {
    String idDoc;
    evtSupport.create(traceJournalEvt, clockSupport.currentCLock());
    idDoc = getIdDoc(trace.getInfos());
    if (idDoc != null) {
      evtSupport.addIndexDoc(traceJournalEvt, idDoc, currentCLock);
    }
  }

  private void saveTraceHistEvt(final TraceToCreate trace) {
    final RMSystemEvent event = ToolkitFactory.getInstance().createRMSystemEvent();
    event.setUsername(USERNAME);
    event.setEventDescription(trace.toString());
    dfceServices.createCustomSystemEventLog(event);
  }

  private String getIdDoc(final Map<String, Object> map) {
    String idDoc = null;

    if (MapUtils.isNotEmpty(map)) {
      if (map.containsKey(KEY_ID_DOC)) {
        idDoc = String.valueOf(map.get(KEY_ID_DOC));
      }
    }
    return idDoc;

  }
}
