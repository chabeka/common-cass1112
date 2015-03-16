/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Arrays;
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
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class DispatcheurServiceTousRegistresDatasTest {

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

   private static final String ARCHIVAGE_UNITAIRE = "ARCHIVAGE_UNITAIRE_TOUS";

   @Autowired
   private DispatcheurService service;

   @Autowired
   private TraceDestinataireSupport destSupport;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private RegExploitationService exploitService;

   @Autowired
   private RegSecuriteService securiteService;

   @Autowired
   private RegTechniqueService techniqueService;

   @After
   public void after() throws Exception {
      server.resetData();
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
      Assert.assertNotNull("les infos doivent etre renseignées", trace
            .getInfos());
      Assert.assertEquals("le nombre d'infos doit etre correct", 2, trace
            .getInfos().size());
      Assert.assertTrue("le champ ip doit etre présent", trace.getInfos()
            .containsKey(IP));
      Assert.assertEquals("la valeur du champ ip doit etre correcte", IP_VALUE,
            trace.getInfos().get(IP));
      Assert.assertTrue("le champ message doit etre présent", trace.getInfos()
            .containsKey(MESSAGE));
      Assert.assertEquals("la valeur du champ message doit etre correcte",
            MESSAGE_VALUE, trace.getInfos().get(MESSAGE));
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
      Assert.assertNotNull("les infos doivent etre renseignées", trace
            .getInfos());
      Assert.assertEquals("le nombre d'infos doit etre correct", 3, trace
            .getInfos().size());
      Assert.assertTrue("le champ ip doit etre présent", trace.getInfos()
            .containsKey(IP));
      Assert.assertEquals("la valeur du champ ip doit etre correcte", IP_VALUE,
            trace.getInfos().get(IP));
      Assert.assertTrue("le champ message doit etre présent", trace.getInfos()
            .containsKey(MESSAGE));
      Assert.assertEquals("la valeur du champ message doit etre correcte",
            MESSAGE_VALUE, trace.getInfos().get(MESSAGE));
      Assert.assertTrue("le champ vi doit etre présent", trace.getInfos()
            .containsKey(VI));
      Assert.assertEquals("la valeur du champ vi doit etre correcte", VI_VALUE,
            trace.getInfos().get(VI));
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
      Assert.assertNotNull("les infos doivent etre renseignées", trace
            .getInfos());
      Assert.assertEquals("le nombre d'infos doit etre correct", 1, trace
            .getInfos().size());
      Assert.assertTrue("le champ vi doit etre présent", trace.getInfos()
            .containsKey(VI));
      Assert.assertEquals("la valeur du champ vi doit etre correcte", VI_VALUE,
            trace.getInfos().get(VI));
   }

   private void createDestinataireExploitation() {
      TraceDestinataire trace = new TraceDestinataire();
      trace.setCodeEvt(ARCHIVAGE_UNITAIRE);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(TraceDestinataireDao.COL_REG_EXPLOIT, Arrays.asList(IP, MESSAGE));
      map.put(TraceDestinataireDao.COL_REG_SECURITE, Arrays.asList(VI));
      map.put(TraceDestinataireDao.COL_REG_TECHNIQUE, Arrays.asList(IP,
            MESSAGE, VI));
      trace.setDestinataires(map);

      destSupport.create(trace, new Date().getTime());
   }
}
