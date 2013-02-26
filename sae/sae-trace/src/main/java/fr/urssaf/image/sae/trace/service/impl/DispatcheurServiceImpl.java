/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.recordmanager.RMSystemEvent;
import net.docubase.toolkit.service.ged.RecordManagerService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegExploitationSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;

/**
 * Classe d'implémentation du support {@link DispatcheurService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class DispatcheurServiceImpl implements DispatcheurService {

   private static final String ARG_0 = "0";
   private static final String ARG_1 = "1";
   private static final String ARG_2 = "2";
   private static final String USERNAME = "_ADMIN";

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DispatcheurServiceImpl.class);

   private static final String MESSAGE_ERREUR_REGISTRE = "l'argument ${0} est obligatoire dans le ${1} ${2}";

   private static final String REG_SECURITE = "de sécurité";
   private static final String REG_EXPLOITATION = "d'exploitation";
   private static final String REG_TECHNIQUE = "technique";
   private static final String JOURNAL_EVT = "des événements SAE";

   private static final String REGISTRE = "registre";
   private static final String JOURNAL = "journal";

   private static final List<String> DEST_AUTORISES = Arrays.asList(
         TraceDestinataireDao.COL_HIST_ARCHIVE,
         TraceDestinataireDao.COL_HIST_EVT,
         TraceDestinataireDao.COL_REG_EXPLOIT,
         TraceDestinataireDao.COL_REG_SECURITE,
         TraceDestinataireDao.COL_REG_TECHNIQUE,
         TraceDestinataireDao.COL_JOURN_EVT);

   private static final List<String> REG_AUTORISES = Arrays.asList(
         TraceDestinataireDao.COL_REG_EXPLOIT,
         TraceDestinataireDao.COL_REG_SECURITE,
         TraceDestinataireDao.COL_REG_TECHNIQUE);

   private static final List<String> JOURN_AUTORISES = Arrays
         .asList(TraceDestinataireDao.COL_JOURN_EVT);

   @Autowired
   private JobClockSupport clockSupport;

   @Autowired
   private TraceDestinataireSupport destSupport;

   @Autowired
   private TraceRegSecuriteSupport secuSupport;

   @Autowired
   private TraceRegExploitationSupport exploitSupport;

   @Autowired
   private TraceRegTechniqueSupport techSupport;
   
   @Autowired
   private TraceJournalEvtSupport evtSupport;

   @Autowired
   private ServiceProviderSupport providerSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void ajouterTrace(TraceToCreate trace) {

      String prefix = "ajouterTrace()";
      LOGGER.debug(DEBUT_LOG, prefix);

      String codeEvt = trace.getCodeEvt();

      TraceDestinataire traceDest = destSupport.find(codeEvt);

      for (String type : traceDest.getDestinataires().keySet()) {

         createTrace(codeEvt, type, traceDest.getDestinataires().get(type),
               trace);

      }

      LOGGER.debug(FIN_LOG, prefix);

   }

   @SuppressWarnings("PMD.ConfusingTernary")
   private void createTrace(String codeEvt, String type, List<String> list,
         TraceToCreate trace) {

      if (!DEST_AUTORISES.contains(type)) {
         LOGGER.warn(
               "Le destinataire {0} ne doit pas exister pour l'événement {1}",
               new Object[] { type, codeEvt });

      } else if (REG_AUTORISES.contains(type) || JOURN_AUTORISES.contains(type)) {
         checkCategoriesValues(trace, type);
         saveTrace(trace, type, list);

      } else if (TraceDestinataireDao.COL_HIST_EVT.equals(type)) {
         saveTraceHistEvt(trace);

      } else {
         LOGGER.debug("Fonctionnalité non prise en charge pour le moment");
      }

   }

   private void checkCategoriesValues(TraceToCreate trace, String type) {

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
         throw new IllegalArgumentException(
               "pas de vérification prévue pour cette trace");
      }

      String typeTrace = REGISTRE;
      if (JOURN_AUTORISES.contains(type)) {
         typeTrace = JOURNAL;
      }
      checkStringValue("code événement", trace.getCodeEvt(), suffixe, typeTrace);
      checkNotNullableObject("date", trace.getTimestamp(), suffixe, typeTrace);

      // Dans certains cas, on ne dispose pas du CS ni du login
      // Par exemple, dans le cas d'un appel WS où il manque l'en-tête
      // de sécurité et pour lequel on va quand même tracer l'erreur
      // checkStringValues("contrat de service ou login", Arrays.asList(trace
      // .getContrat(), trace.getLogin()), suffixe);

   }

   private void checkStringValue(String name, String value, String suffixe,
         String categorie) {

      if (StringUtils.isBlank(value)) {
         Map<String, String> map = new HashMap<String, String>();
         map.put(ARG_0, name);
         map.put(ARG_1, categorie);
         map.put(ARG_2, suffixe);
         throw new IllegalArgumentException(StrSubstitutor.replace(
               MESSAGE_ERREUR_REGISTRE, map));
      }

   }

   private void checkNotNullableObject(String name, Object object,
         String suffixe, String categorie) {

      if (object == null) {
         Map<String, String> map = new HashMap<String, String>();
         map.put(ARG_0, name);
         map.put(ARG_1, categorie);
         map.put(ARG_2, suffixe);
         throw new IllegalArgumentException(StrSubstitutor.replace(
               MESSAGE_ERREUR_REGISTRE, map));
      }

   }

   private void saveTrace(TraceToCreate trace, String type, List<String> list) {

      String prefix = "saveTrace()";
      LOGGER.debug(DEBUT_LOG, prefix);

      if (TraceDestinataireDao.COL_REG_EXPLOIT.equals(type)) {
         LOGGER.debug("{} - ajout d'une trace d'exploitation", prefix);
         TraceRegExploitation traceExploit = new TraceRegExploitation(trace,
               list);
         exploitSupport.create(traceExploit, clockSupport.currentCLock());

      } else if (TraceDestinataireDao.COL_REG_SECURITE.equals(type)) {
         LOGGER.debug("{} - ajout d'une trace de sécurité", prefix);
         TraceRegSecurite traceSecurite = new TraceRegSecurite(trace, list);
         secuSupport.create(traceSecurite, clockSupport.currentCLock());

      } else if (TraceDestinataireDao.COL_REG_TECHNIQUE.equals(type)) {
         LOGGER.debug("{} - ajout d'une trace technique", prefix);
         TraceRegTechnique traceTechnique = new TraceRegTechnique(trace, list);
         techSupport.create(traceTechnique, clockSupport.currentCLock());
      
      } else if (TraceDestinataireDao.COL_JOURN_EVT.equals(type)) {
         LOGGER.debug("{} - ajout d'une trace journal des événements", prefix);
         TraceJournalEvt traceTechnique = new TraceJournalEvt(trace, list);
         evtSupport.create(traceTechnique, clockSupport.currentCLock());
         
      } else {
         throw new IllegalArgumentException(StringUtils.replace(
               "pas de type existant {0} à convertir", "{0}", type));
      }

      LOGGER.debug(FIN_LOG, prefix);

   }

   private void saveTraceHistEvt(TraceToCreate trace) {
      RecordManagerService service = providerSupport.getRecordManagerService();
      RMSystemEvent event = ToolkitFactory.getInstance().createRMSystemEvent();
      event.setUsername(USERNAME);
      event.setEventDescription(trace.toString());
      service.createCustomSystemEventLog(event);
   }
}
