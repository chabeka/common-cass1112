package fr.urssaf.image.sae.webservices.support.cql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.service.RegExploitationService;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;
import fr.urssaf.image.sae.webservices.constantes.TracesConstantes;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;
import fr.urssaf.image.sae.webservices.support.TracesWsSupport;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;
import org.junit.Assert;

/**
 * Tests unitaires de la classe {@link TracesWsSupportCqlTest}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-traces-test.xml" })
public class TracesWsSupportCqlTest {

  @Autowired
  private TracesWsSupport tracesSupport;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private TraceDestinataireCqlSupport destSupport;

  @Autowired
  private RegTechniqueService regTechniqueService;

  @Autowired
  private RegSecuriteService regSecuriteService;

  @Autowired
  private RegExploitationService regExploitationService;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;



  @After
  public void after() throws Exception {

    server.resetData(true, MODE_API.DATASTAX);
    AuthenticationContext.setAuthenticationToken(null);

  }

  @Before
  public void init() {
    modeApiCqlSupport.initTables(MODE_API.DATASTAX);
    TraceDestinataire evenement = new TraceDestinataire();
    evenement.setCodeEvt(TracesConstantes.CODE_EVT_WS_RECHERCHE_KO);
    Map<String, List<String>> map = new HashMap<>();
    evenement.setDestinataires(map);
    map.put(TraceDestinataireDao.COL_REG_TECHNIQUE,
            Arrays.asList("all_infos"));
    destSupport.create(evenement, new Date().getTime());

    evenement = new TraceDestinataire();
    evenement.setCodeEvt(TracesConstantes.CODE_EVT_CHARGE_CERT_ACRACINE);
    map = new HashMap<>();
    evenement.setDestinataires(map);
    map.put(TraceDestinataireDao.COL_REG_TECHNIQUE,
            Arrays.asList("all_infos"));
    destSupport.create(evenement, new Date().getTime());

    evenement = new TraceDestinataire();
    evenement.setCodeEvt(TracesConstantes.CODE_EVT_CHARGE_CRL);
    map = new HashMap<>();
    evenement.setDestinataires(map);
    map.put(TraceDestinataireDao.COL_REG_TECHNIQUE,
            Arrays.asList("all_infos"));
    destSupport.create(evenement, new Date().getTime());

  }

  /**
   * On vérifie l'intégralité des propriétés d'une trace d'échec du WS de
   * recherche
   */
  @Test
  public void test_traceEchecWsRecherche_ToutesInfos() {

    // Préparation

    final String soapRequest = "<xml><balise>contenu de la balise</balise></xml>";

    final Exception exceptionMere = new Exception("Exception mère");
    final Exception axisFault = new RechercheAxis2Fault(
                                                        "RechercheMetadonneesInterdite", "Erreur dans la recherche",
                                                        exceptionMere);

    final String clientIP = "1.2.3.4";

    authentifie();

    // Appel de la méthode d'écriture de la trace

    tracesSupport.traceEchec(TracesConstantes.CODE_EVT_WS_RECHERCHE_KO,
                             "recherche", soapRequest, axisFault, clientIP);

    // Vérifie la trace créée

    final String saeServeurHostname = HostnameUtil.getHostname();
    final String saeServeurIP = HostnameUtil.getIP();

    verifieNombreTracesDansTraceRegSecurite(0);

    verifieNombreTracesDansTraceRegExploitation(0);

    final List<TraceRegTechniqueIndex> tracesTechIndex = verifieNombreTracesDansTraceRegTechnique(1);

    final TraceRegTechniqueIndex traceTechIndex = tracesTechIndex.get(0);

    assertEquals(
                 "Le code événement dans la trace (dans son index) est incorrect",
                 TracesConstantes.CODE_EVT_WS_RECHERCHE_KO,
                 traceTechIndex.getCodeEvt());
    assertEquals(
                 "Le contrat de service dans la trace (dans son index) est incorrect",
                 "CodeContratService", traceTechIndex.getContrat());
    checkPagms(traceTechIndex.getPagms());
    assertEquals("Le login dans la trace (dans son index) est incorrect",
                 "LeIdUser", traceTechIndex.getLogin());
    assertEquals("Le contexte dans la trace (dans son index) est incorrect",
                 "recherche", traceTechIndex.getContexte());

    final TraceRegTechnique trace = regTechniqueService.lecture(traceTechIndex
                                                                .getIdentifiant());

    assertEquals("Le code événement dans la trace est incorrect",
                 TracesConstantes.CODE_EVT_WS_RECHERCHE_KO, trace.getCodeEvt());
    assertEquals("Le contrat de service dans la trace est incorrect",
                 "CodeContratService", trace.getContratService());
    checkPagms(trace.getPagms());
    assertEquals("Le login dans la trace est incorrect", "LeIdUser",
                 trace.getLogin());
    assertEquals("Le contexte dans la trace est incorrect", "recherche",
                 trace.getContexte());
    assertEquals("La stacktrace dans la trace est incorrect",
                 ExceptionUtils.getFullStackTrace(axisFault), trace.getStacktrace());
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

    final String soapRequest = "<xml><balise>contenu de la balise</balise></xml>";

    final Exception exceptionMere = new Exception("Exception mère");
    final Exception axisFault = new RechercheAxis2Fault(
                                                        "RechercheMetadonneesInterdite", "Erreur dans la recherche",
                                                        exceptionMere);

    final String clientIP = "5.6.7.8";

    // Appel de la méthode d'écriture de la trace

    tracesSupport.traceEchec(TracesConstantes.CODE_EVT_WS_RECHERCHE_KO,
                             "recherche", soapRequest, axisFault, clientIP);

    // Vérifie la trace créée

    final List<TraceRegTechniqueIndex> tracesTechIndex = verifieNombreTracesDansTraceRegTechnique(1);

    final TraceRegTechniqueIndex traceTechIndex = tracesTechIndex.get(0);

    Assert.assertTrue("Le contrat de service ne devrait pas être renseigné",
                      StringUtils.isEmpty(traceTechIndex.getContrat()));
    Assert.assertTrue("La liste des PAGM ne devrait pas être renseignée",
                      CollectionUtils.isEmpty(traceTechIndex.getPagms()));
    Assert.assertTrue("Le login ne devrait pas être renseigné",
                      StringUtils.isEmpty(traceTechIndex.getLogin()));

    final TraceRegTechnique trace = regTechniqueService.lecture(traceTechIndex
                                                                .getIdentifiant());

    Assert.assertTrue("Le contrat de service ne devrait pas être renseigné",
                      StringUtils.isEmpty(trace.getContratService()));
    Assert.assertTrue("La liste des PAGM ne devrait pas être renseignée",
                      CollectionUtils.isEmpty(trace.getPagms()));
    Assert.assertTrue("Le login ne devrait pas être renseigné",
                      StringUtils.isEmpty(trace.getLogin()));

  }

  private List<TraceRegTechniqueIndex> verifieNombreTracesDansTraceRegTechnique(
                                                                                final int expected) {

    final List<TraceRegTechniqueIndex> tracesIndex = regTechniqueService.lecture(
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
                                                                              final int expected) {

    final List<TraceRegSecuriteIndex> tracesIndex = regSecuriteService.lecture(
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
                                                                                      final int expected) {

    final List<TraceRegExploitationIndex> tracesIndex = regExploitationService
        .lecture(DateUtils.addDays(new Date(), -1),
                 DateUtils.addDays(new Date(), 1), 100, true);

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

  private void checkPagms(final List<String> pagms) {
    Assert.assertNotNull("La liste des PAGM ne devrait pas être null", pagms);
    Assert.assertEquals("La liste des PAGM devrait contenir 2 éléments", 2,
                        pagms.size());
    Assert.assertTrue("La liste des PAGM devrait contenir le PAGM \"PAGM1\"",
                      pagms.contains("PAGM1"));
    Assert.assertTrue("La liste des PAGM devrait contenir le PAGM \"PAGM2\"",
                      pagms.contains("PAGM2"));
  }

  private void authentifie() {

    final VIContenuExtrait viExtrait = new VIContenuExtrait();

    viExtrait.setIdUtilisateur("LeIdUser");

    viExtrait.setCodeAppli("CodeContratService");

    viExtrait.getPagms().add("PAGM1");
    viExtrait.getPagms().add("PAGM2");

    final String[] roles = new String[] { "role1", "role2" };

    final AuthenticationToken authentication = AuthenticationFactory
        .createAuthentication(viExtrait.getIdUtilisateur(), viExtrait,
                              roles);

    AuthenticationContext.setAuthenticationToken(authentication);

  }

  @Test
  public void traceChargementCertAcRacine() {

    // Préparation

    final String fichier1 = "/rep1/fichier1.crt";
    final String fichier2 = "/rep1/fichier2.crt";
    final String fichier3 = "/rep2/fichier3.crt";

    final List<String> fichiers = Arrays.asList(fichier1, fichier2, fichier3);

    authentifie();

    // Appel de la méthode d'écriture de la trace

    tracesSupport.traceChargementCertAcRacine(fichiers);

    // Vérifie la trace créée

    final String saeServeurHostname = HostnameUtil.getHostname();
    final String saeServeurIP = HostnameUtil.getIP();

    verifieNombreTracesDansTraceRegSecurite(0);

    verifieNombreTracesDansTraceRegExploitation(0);

    final List<TraceRegTechniqueIndex> tracesTechIndex = verifieNombreTracesDansTraceRegTechnique(1);

    final TraceRegTechniqueIndex traceTechIndex = tracesTechIndex.get(0);

    assertEquals(
                 "Le code événement dans la trace (dans son index) est incorrect",
                 TracesConstantes.CODE_EVT_CHARGE_CERT_ACRACINE,
                 traceTechIndex.getCodeEvt());
    assertEquals(
                 "Le contrat de service dans la trace (dans son index) est incorrect",
                 "CodeContratService", traceTechIndex.getContrat());
    checkPagms(traceTechIndex.getPagms());
    assertEquals("Le login dans la trace (dans son index) est incorrect",
                 "LeIdUser", traceTechIndex.getLogin());
    assertEquals("Le contexte dans la trace (dans son index) est incorrect",
                 "ChargementCertACRacine", traceTechIndex.getContexte());

    final TraceRegTechnique trace = regTechniqueService.lecture(traceTechIndex
                                                                .getIdentifiant());

    assertEquals("Le code événement dans la trace est incorrect",
                 TracesConstantes.CODE_EVT_CHARGE_CERT_ACRACINE, trace.getCodeEvt());
    assertEquals("Le contrat de service dans la trace est incorrect",
                 "CodeContratService", trace.getContratService());
    checkPagms(trace.getPagms());
    assertEquals("Le login dans la trace est incorrect", "LeIdUser",
                 trace.getLogin());
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

    // Le tableau est enregistré sous forme de string
    String fichiersInfos = String.valueOf(trace.getInfos().get("fichiers"));
    fichiersInfos = fichiersInfos.substring(1, fichiersInfos.length() - 1);
    fichiersInfos = fichiersInfos.replaceAll(" ", "");
    final String[] tab = fichiersInfos.split(",");
    final List<String> list = Arrays.asList(tab);

    assertEquals("L'information supplémentaire fichiers est incorrect",
                 fichiers,
                 list);
  }

}
