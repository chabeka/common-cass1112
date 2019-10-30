/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class DispatcheurServiceRegistresDatasAucuneInfosTest {

   private static final String ACTION = "action";
   private static final String CONTEXTE = "contexte";
   private static final String CONTRAT_DE_SERVICE = "contrat de service";
   private static final String IP = "ip";
   private static final String IP_VALUE = "127.0.0.1";
   private static final String MESSAGE = "message";
   private static final String MESSAGE_VALUE = "le message est ici";
   private static final String VI = "vi";
   private static final String VI_VALUE = "<vi><valeur>La valeur du vi</valeur></vi>";
   private static final Map<String, Object> INFOS = new HashMap<String, Object>();
   static {
      INFOS.put(IP, IP_VALUE);
      INFOS.put(MESSAGE, MESSAGE_VALUE);
      INFOS.put(VI, VI_VALUE);
   }

   private static final String ARCHIVAGE_UNITAIRE = "ARCHIVAGE_UNITAIRE_AUCUNE_INFO";
   
   private final String cfNameDestinataire = "tracedestinatairecql";

   @Autowired
   private DispatcheurService service;

   @Autowired
   private TraceDestinataireSupport destSupport;
   
   @Autowired
   private TraceDestinataireCqlSupport destCqlSupport;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private RegExploitationService exploitService;

   @Autowired
   private RegSecuriteService securiteService;

   @Autowired
   private RegTechniqueService techniqueService;

   @Autowired
   private JournalEvtService evtService;

   @After
   public void after() throws Exception {
      server.resetData(true, MODE_API.HECTOR);
   }

   @Test
   public void testCreationTracesSucces() {
      createDestinataireExploitation();

      TraceToCreate traceToCreate = new TraceToCreate();
      traceToCreate.setCodeEvt(ARCHIVAGE_UNITAIRE);
      traceToCreate.setAction(ACTION);
      traceToCreate.setContrat(CONTRAT_DE_SERVICE);
      traceToCreate.setInfos(INFOS);
      traceToCreate.setContexte(CONTEXTE);

      service.ajouterTrace(traceToCreate);

      checkTechnique();
      checkExploitation();
      checkSecurite();
      checkJournalEvt();

   }

   private void checkExploitation() {
      // on vérifie qu'il y a un résultat

      List<TraceRegExploitationIndex> result = exploitService.lecture(DateUtils
            .addMinutes(new Date(), -5), DateUtils.addMinutes(new Date(), 5),
            1, false);
      Assert.assertNotNull(
            "une trace dans le registre technique doit etre trouvé", result);
      Assert.assertEquals(
            "on ne doit avoir qu'une seule trace dans le registre de sécurité",
            1, result.size());

      // on vérifie les infos présentes dans les infos
      TraceRegExploitation trace = exploitService.lecture(result.get(0)
            .getIdentifiant());
      Assert.assertNull("les infos ne doivent pas etre renseignées", trace
            .getInfos());
   }

   private void checkTechnique() {
      // on vérifie qu'il y a un résultat
      List<TraceRegTechniqueIndex> result = techniqueService.lecture(DateUtils
            .addMinutes(new Date(), -5), DateUtils.addMinutes(new Date(), 5),
            1, false);
      Assert.assertNotNull(
            "une trace dans le registre technique doit etre trouvé", result);
      Assert.assertEquals(
            "on ne doit avoir qu'une seule trace dans le registre de sécurité",
            1, result.size());

      // on vérifie les infos présentes dans les infos
      TraceRegTechnique trace = techniqueService.lecture(result.get(0)
            .getIdentifiant());
      Assert.assertNull("les infos ne doivent pas etre renseignées", trace
            .getInfos());
   }

   private void checkSecurite() {
      // on vérifie qu'il y a un résultat
      List<TraceRegSecuriteIndex> result = securiteService.lecture(DateUtils
            .addMinutes(new Date(), -5), DateUtils.addMinutes(new Date(), 5),
            1, false);
      Assert.assertNotNull(
            "une trace dans le registre technique doit etre trouvé", result);
      Assert.assertEquals(
            "on ne doit avoir qu'une seule trace dans le registre de sécurité",
            1, result.size());

      // on vérifie les infos présentes dans les infos
      TraceRegSecurite trace = securiteService.lecture(result.get(0)
            .getIdentifiant());
      Assert.assertNull("les infos ne doivent pas etre renseignées", trace
            .getInfos());
   }

   private void checkJournalEvt() {
      // on vérifie qu'il y a un résultat
      List<TraceJournalEvtIndex> result = evtService.lecture(DateUtils
            .addMinutes(new Date(), -5), DateUtils.addMinutes(new Date(), 5),
            1, false);
      Assert.assertNotNull(
            "une trace dans le registre technique doit etre trouvé", result);
      Assert.assertEquals(
            "on ne doit avoir qu'une seule trace dans le registre de sécurité",
            1, result.size());

      // on vérifie les infos présentes dans les infos
      TraceJournalEvt trace = evtService
            .lecture(result.get(0).getIdentifiant());
      Assert.assertNull("les infos ne doivent pas etre renseignées", trace
            .getInfos());
   }

   private void createDestinataireExploitation() {
      TraceDestinataire trace = new TraceDestinataire();
      trace.setCodeEvt(ARCHIVAGE_UNITAIRE);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(TraceDestinataireDao.COL_REG_EXPLOIT, new ArrayList<String>());
      map.put(TraceDestinataireDao.COL_REG_SECURITE, new ArrayList<String>());
      map.put(TraceDestinataireDao.COL_REG_TECHNIQUE, new ArrayList<String>());
      map.put(TraceDestinataireDao.COL_JOURN_EVT, new ArrayList<String>());
      trace.setDestinataires(map);

      final String modeApi = ModeGestionAPI.getModeApiCf(cfNameDestinataire);
      if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
        destCqlSupport.create(trace, new Date().getTime());
      } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
        destSupport.create(trace, new Date().getTime());
      } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
        destSupport.create(trace, new Date().getTime());
      }
   }
}
