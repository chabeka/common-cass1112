/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
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
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class DispatcheurServiceSecuriteDatasTest {

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

   private static final String ARCHIVAGE_UNITAIRE = "ARCHIVAGE_UNITAIRE_SECURITE";

   private static final String MESSAGE_ERREUR = "l'argument ${0} est obligatoire dans le registre ${1}";

   @Autowired
   private DispatcheurService service;

   @Autowired
   private TraceDestinataireSupport destSupport;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private RegSecuriteService secuService;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test
   public void testCreationTraceSecuriteErreurContexteNonRenseigne() {
      createDestinataireSecurite();

      TraceToCreate traceToCreate = new TraceToCreate();
      traceToCreate.setCodeEvt(ARCHIVAGE_UNITAIRE);

      try {
         service.ajouterTrace(traceToCreate);
         Assert.fail("une erreur IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {

         Map<String, String> map = new HashMap<String, String>();
         map.put("0", CONTEXTE);
         map.put("1", "de sécurité");
         String message = StrSubstitutor.replace(MESSAGE_ERREUR, map);

         Assert.assertEquals("l'exception doit être correcte", message,
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("une erreur IllegalArgumentException est attendue");
      }
   }

   @Test
   public void testCreationTraceSecuriteSuccesContratNonRenseigne() {
      createDestinataireSecurite();

      TraceToCreate traceToCreate = new TraceToCreate();
      traceToCreate.setCodeEvt(ARCHIVAGE_UNITAIRE);
      traceToCreate.setContexte(CONTEXTE);

      service.ajouterTrace(traceToCreate);

      // on vérifie qu'il y a un résultat
      List<TraceRegSecuriteIndex> result = secuService.lecture(DateUtils
            .addMinutes(new Date(), -5), DateUtils.addMinutes(new Date(), 5),
            1, false);
      Assert.assertNotNull(
            "une trace dans le registre technique doit etre trouvé", result);
      Assert.assertEquals(
            "on ne doit avoir qu'une seule trace dans le registre de sécurité",
            1, result.size());

      // on vérifie la trace
      TraceRegSecurite trace = secuService.lecture(result.get(0)
            .getIdentifiant());
      Assert.assertTrue("Le contrat de service ne doit pas être renseigné",
            StringUtils.isEmpty(trace.getContratService()));

   }

   @Test
   public void testCreationTraceSecuriteSucces() {
      createDestinataireSecurite();

      TraceToCreate traceToCreate = new TraceToCreate();
      traceToCreate.setCodeEvt(ARCHIVAGE_UNITAIRE);
      traceToCreate.setContexte(CONTEXTE);
      traceToCreate.setContrat(CONTRAT_DE_SERVICE);
      traceToCreate.setInfos(INFOS);

      service.ajouterTrace(traceToCreate);

      // on vérifie qu'il y a un résultat
      List<TraceRegSecuriteIndex> result = secuService.lecture(DateUtils
            .addMinutes(new Date(), -5), DateUtils.addMinutes(new Date(), 5),
            1, false);
      Assert.assertNotNull(
            "une trace dans le registre technique doit etre trouvé", result);
      Assert.assertEquals(
            "on ne doit avoir qu'une seule trace dans le registre de sécurité",
            1, result.size());

      // on vérifie les infos présentes dans les infos
      TraceRegSecurite trace = secuService.lecture(result.get(0)
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

   private void createDestinataireSecurite() {
      TraceDestinataire trace = new TraceDestinataire();
      trace.setCodeEvt(ARCHIVAGE_UNITAIRE);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map
            .put(TraceDestinataireDao.COL_REG_SECURITE, Arrays.asList(IP,
                  MESSAGE));
      trace.setDestinataires(map);
      trace.setDestinataires(map);
      destSupport.create(trace, new Date().getTime());
   }
}
