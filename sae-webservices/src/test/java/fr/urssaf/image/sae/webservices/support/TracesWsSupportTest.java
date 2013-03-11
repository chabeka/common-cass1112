package fr.urssaf.image.sae.webservices.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.service.RegExploitationService;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;
import fr.urssaf.image.sae.webservices.constantes.TracesConstantes;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;

/**
 * Tests unitaires de la classe {@link TracesWsSupport}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-traces-test.xml" })
public class TracesWsSupportTest {

   @Autowired
   private TracesWsSupport tracesSupport;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private TraceDestinataireSupport destSupport;

   @Autowired
   private RegTechniqueService regTechniqueService;

   @Autowired
   private RegSecuriteService regSecuriteService;

   @Autowired
   private RegExploitationService regExploitationService;

   @After
   public void after() throws Exception {

      server.resetData();

      AuthenticationContext.setAuthenticationToken(null);

   }

   @Before
   public void init() {

      TraceDestinataire evenement = new TraceDestinataire();
      evenement.setCodeEvt(TracesConstantes.CODE_EVT_WS_RECHERCHE_KO);
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      evenement.setDestinataires(map);
      map.put(TraceDestinataireDao.COL_REG_TECHNIQUE, Arrays
            .asList("all_infos"));
      destSupport.create(evenement, new Date().getTime());

      evenement = new TraceDestinataire();
      evenement.setCodeEvt(TracesConstantes.CODE_EVT_CHARGE_CERT_ACRACINE);
      map = new HashMap<String, List<String>>();
      evenement.setDestinataires(map);
      map.put(TraceDestinataireDao.COL_REG_TECHNIQUE, Arrays
            .asList("all_infos"));
      destSupport.create(evenement, new Date().getTime());

      evenement = new TraceDestinataire();
      evenement.setCodeEvt(TracesConstantes.CODE_EVT_CHARGE_CRL);
      map = new HashMap<String, List<String>>();
      evenement.setDestinataires(map);
      map.put(TraceDestinataireDao.COL_REG_TECHNIQUE, Arrays
            .asList("all_infos"));
      destSupport.create(evenement, new Date().getTime());

   }

   /**
    * On vérifie l'intégralité des propriétés d'une trace d'échec du WS de
    * recherche
    */
   @Test
   public void test_traceEchecWsRecherche_ToutesInfos() {

      // Préparation

      String soapRequest = "<xml><balise>contenu de la balise</balise></xml>";

      Exception exceptionMere = new Exception("Exception mère");
      Exception axisFault = new RechercheAxis2Fault("Erreur dans la recherche",
            "RechercheMetadonneesInterdite", exceptionMere);

      String clientIP = "1.2.3.4";

      authentifie();

      // Appel de la méthode d'écriture de la trace

      tracesSupport.traceEchec(TracesConstantes.CODE_EVT_WS_RECHERCHE_KO,
            "recherche", soapRequest, axisFault, clientIP);

      // Vérifie la trace créée

      String saeServeurHostname = HostnameUtil.getHostname();
      String saeServeurIP = HostnameUtil.getIP();

      verifieNombreTracesDansTraceRegSecurite(0);

      verifieNombreTracesDansTraceRegExploitation(0);

      List<TraceRegTechniqueIndex> tracesTechIndex = verifieNombreTracesDansTraceRegTechnique(1);

      TraceRegTechniqueIndex traceTechIndex = tracesTechIndex.get(0);

      assertEquals(
            "Le code événement dans la trace (dans son index) est incorrect",
            TracesConstantes.CODE_EVT_WS_RECHERCHE_KO, traceTechIndex
                  .getCodeEvt());
      assertEquals(
            "Le contrat de service dans la trace (dans son index) est incorrect",
            "CodeContratService", traceTechIndex.getContrat());
      checkPagms(traceTechIndex.getPagms());
      assertEquals("Le login dans la trace (dans son index) est incorrect",
            "LeIdUser", traceTechIndex.getLogin());
      assertEquals("Le contexte dans la trace (dans son index) est incorrect",
            "recherche", traceTechIndex.getContexte());

      TraceRegTechnique trace = regTechniqueService.lecture(traceTechIndex
            .getIdentifiant());

      assertEquals("Le code événement dans la trace est incorrect",
            TracesConstantes.CODE_EVT_WS_RECHERCHE_KO, trace.getCodeEvt());
      assertEquals("Le contrat de service dans la trace est incorrect",
            "CodeContratService", trace.getContrat());
      checkPagms(trace.getPagms());
      assertEquals("Le login dans la trace est incorrect", "LeIdUser", trace
            .getLogin());
      assertEquals("Le contexte dans la trace est incorrect", "recherche",
            trace.getContexte());
      assertEquals("La stacktrace dans la trace est incorrect", ExceptionUtils
            .getFullStackTrace(axisFault), trace.getStacktrace());
      assertTrue("Les infos supplémentaires ne devraient pas être vides",
            MapUtils.isNotEmpty(trace.getInfos()));
      assertEquals(
            "Le nombre d'informations supplémentaires est différent de l'attendu",
            4, trace.getInfos().size());
      assertEquals("L'information supplémentaire soapRequest est incorrect",
            soapRequest, trace.getInfos().get("soapRequest"));
      assertEquals(
            "L'information supplémentaire saeServeurHostname est incorrect",
            saeServeurHostname, trace.getInfos().get("saeServeurHostname"));
      assertEquals("L'information supplémentaire saeServeurIP est incorrect",
            saeServeurIP, trace.getInfos().get("saeServeurIP"));
      assertEquals("L'information supplémentaire clientIP est incorrect",
            clientIP, trace.getInfos().get("clientIP"));

   }

   @Test
   public void test_traceEchecWsRecherche_SansInfosAuth() {

      // Préparation

      String soapRequest = "<xml><balise>contenu de la balise</balise></xml>";

      Exception exceptionMere = new Exception("Exception mère");
      Exception axisFault = new RechercheAxis2Fault("Erreur dans la recherche",
            "RechercheMetadonneesInterdite", exceptionMere);

      String clientIP = "5.6.7.8";

      // Appel de la méthode d'écriture de la trace

      tracesSupport.traceEchec(TracesConstantes.CODE_EVT_WS_RECHERCHE_KO,
            "recherche", soapRequest, axisFault, clientIP);

      // Vérifie la trace créée

      List<TraceRegTechniqueIndex> tracesTechIndex = verifieNombreTracesDansTraceRegTechnique(1);

      TraceRegTechniqueIndex traceTechIndex = tracesTechIndex.get(0);

      Assert.assertTrue("Le contrat de service ne devrait pas être renseigné",
            StringUtils.isEmpty(traceTechIndex.getContrat()));
      Assert.assertTrue("La liste des PAGM ne devrait pas être renseignée",
            CollectionUtils.isEmpty(traceTechIndex.getPagms()));
      Assert.assertTrue("Le login ne devrait pas être renseigné", StringUtils
            .isEmpty(traceTechIndex.getLogin()));

      TraceRegTechnique trace = regTechniqueService.lecture(traceTechIndex
            .getIdentifiant());

      Assert.assertTrue("Le contrat de service ne devrait pas être renseigné",
            StringUtils.isEmpty(trace.getContrat()));
      Assert.assertTrue("La liste des PAGM ne devrait pas être renseignée",
            CollectionUtils.isEmpty(trace.getPagms()));
      Assert.assertTrue("Le login ne devrait pas être renseigné", StringUtils
            .isEmpty(trace.getLogin()));

   }

   private List<TraceRegTechniqueIndex> verifieNombreTracesDansTraceRegTechnique(
         int expected) {

      List<TraceRegTechniqueIndex> tracesIndex = regTechniqueService.lecture(
            DateUtils.addDays(new Date(), -1),
            DateUtils.addDays(new Date(), 1), 100, true);

      if (expected <= 0) {
         assertTrue(
               "La liste des traces du registre de surveillance technique devrait être vide",
               CollectionUtils.isEmpty(tracesIndex));
      } else {
         assertFalse(
               "La liste des traces du registre de surveillance technique ne devrait pas être vide",
               CollectionUtils.isEmpty(tracesIndex));
         assertEquals(
               "Le nombre de traces dans le registre de surveillance technique est incorrect",
               expected, tracesIndex.size());
      }

      return tracesIndex;

   }

   private List<TraceRegSecuriteIndex> verifieNombreTracesDansTraceRegSecurite(
         int expected) {

      List<TraceRegSecuriteIndex> tracesIndex = regSecuriteService.lecture(
            DateUtils.addDays(new Date(), -1),
            DateUtils.addDays(new Date(), 1), 100, true);

      if (expected <= 0) {
         assertTrue(
               "La liste des traces du registre de sécurité devrait être vide",
               CollectionUtils.isEmpty(tracesIndex));
      } else {
         assertFalse(
               "La liste des traces du registre de sécurité ne devrait pas être vide",
               CollectionUtils.isEmpty(tracesIndex));
         assertEquals(
               "Le nombre de traces dans le registre de sécurité est incorrect",
               expected, tracesIndex.size());
      }

      return tracesIndex;

   }

   private List<TraceRegExploitationIndex> verifieNombreTracesDansTraceRegExploitation(
         int expected) {

      List<TraceRegExploitationIndex> tracesIndex = regExploitationService
            .lecture(DateUtils.addDays(new Date(), -1), DateUtils.addDays(
                  new Date(), 1), 100, true);

      if (expected <= 0) {
         assertTrue(
               "La liste des traces du registre d'exploitation technique devrait être vide",
               CollectionUtils.isEmpty(tracesIndex));
      } else {
         assertFalse(
               "La liste des traces du registre d'exploitation technique ne devrait pas être vide",
               CollectionUtils.isEmpty(tracesIndex));
         assertEquals(
               "Le nombre de traces dans le registre d'exploitation est incorrect",
               expected, tracesIndex.size());
      }

      return tracesIndex;

   }

   private void checkPagms(List<String> pagms) {
      Assert.assertNotNull("La liste des PAGM ne devrait pas être null", pagms);
      Assert.assertEquals("La liste des PAGM devrait contenir 2 éléments", 2,
            pagms.size());
      Assert.assertTrue("La liste des PAGM devrait contenir le PAGM \"PAGM1\"",
            pagms.contains("PAGM1"));
      Assert.assertTrue("La liste des PAGM devrait contenir le PAGM \"PAGM2\"",
            pagms.contains("PAGM2"));
   }

   private void authentifie() {

      VIContenuExtrait viExtrait = new VIContenuExtrait();

      viExtrait.setIdUtilisateur("LeIdUser");

      viExtrait.setCodeAppli("CodeContratService");

      viExtrait.getPagms().add("PAGM1");
      viExtrait.getPagms().add("PAGM2");

      String[] roles = new String[] { "role1", "role2" };

      AuthenticationToken authentication = AuthenticationFactory
            .createAuthentication(viExtrait.getIdUtilisateur(), viExtrait,
                  roles, viExtrait.getSaeDroits());

      AuthenticationContext.setAuthenticationToken(authentication);

   }

   @Test
   public void traceChargementCertAcRacine() {

      // Préparation

      String fichier1 = "/rep1/fichier1.crt";
      String fichier2 = "/rep1/fichier2.crt";
      String fichier3 = "/rep2/fichier3.crt";

      List<File> fichiers = Arrays.asList(new File(fichier1),
            new File(fichier2), new File(fichier3));

      authentifie();

      // Appel de la méthode d'écriture de la trace

      tracesSupport.traceChargementCertAcRacine(fichiers);

      // Vérifie la trace créée

      String saeServeurHostname = HostnameUtil.getHostname();
      String saeServeurIP = HostnameUtil.getIP();

      verifieNombreTracesDansTraceRegSecurite(0);

      verifieNombreTracesDansTraceRegExploitation(0);

      List<TraceRegTechniqueIndex> tracesTechIndex = verifieNombreTracesDansTraceRegTechnique(1);

      TraceRegTechniqueIndex traceTechIndex = tracesTechIndex.get(0);

      assertEquals(
            "Le code événement dans la trace (dans son index) est incorrect",
            TracesConstantes.CODE_EVT_CHARGE_CERT_ACRACINE, traceTechIndex
                  .getCodeEvt());
      assertEquals(
            "Le contrat de service dans la trace (dans son index) est incorrect",
            "CodeContratService", traceTechIndex.getContrat());
      checkPagms(traceTechIndex.getPagms());
      assertEquals("Le login dans la trace (dans son index) est incorrect",
            "LeIdUser", traceTechIndex.getLogin());
      assertEquals("Le contexte dans la trace (dans son index) est incorrect",
            "ChargementCertACRacine", traceTechIndex.getContexte());

      TraceRegTechnique trace = regTechniqueService.lecture(traceTechIndex
            .getIdentifiant());

      assertEquals("Le code événement dans la trace est incorrect",
            TracesConstantes.CODE_EVT_CHARGE_CERT_ACRACINE, trace.getCodeEvt());
      assertEquals("Le contrat de service dans la trace est incorrect",
            "CodeContratService", trace.getContrat());
      checkPagms(trace.getPagms());
      assertEquals("Le login dans la trace est incorrect", "LeIdUser", trace
            .getLogin());
      assertEquals("Le contexte dans la trace est incorrect",
            "ChargementCertACRacine", trace.getContexte());
      assertTrue("Les infos supplémentaires ne devraient pas être vides",
            MapUtils.isNotEmpty(trace.getInfos()));
      assertEquals(
            "Le nombre d'informations supplémentaires est différent de l'attendu",
            3, trace.getInfos().size());
      assertEquals(
            "L'information supplémentaire saeServeurHostname est incorrect",
            saeServeurHostname, trace.getInfos().get("saeServeurHostname"));
      assertEquals("L'information supplémentaire saeServeurIP est incorrect",
            saeServeurIP, trace.getInfos().get("saeServeurIP"));
      assertEquals("L'information supplémentaire fichiers est incorrect",
            Arrays.asList(new File(fichier1).getAbsolutePath(), new File(
                  fichier2).getAbsolutePath(), new File(fichier3)
                  .getAbsolutePath()), trace.getInfos().get("fichiers"));

   }

}
