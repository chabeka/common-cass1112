package fr.urssaf.image.sae.webservices.skeleton.cql;

import static junit.framework.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.PingSecureRequest;
import fr.cirtil.www.saeservice.PingSecureResponse;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ContratServiceCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmpCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PrmdCqlSupport;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.utils.TraceDestinataireCqlUtils;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;
import fr.urssaf.image.sae.webservices.skeleton.SaeServiceSkeletonInterface;
import fr.urssaf.image.sae.webservices.util.Axis2Utils;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml",
      "/applicationContext-security-test.xml",
"/applicationContext-sae-vi-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class PingSecureCqlTest {

   private static final String PRMD_FULL = "PRMD_FULL";

   private static final String PAGMA_FULL = "PAGMA_FULL";

   private static final String PAGMP_FULL = "PAGMP_FULL";

   private static final String ROLE_TOUS = "ROLE_TOUS";

   private static final String CONTRAT = "TESTS_UNITAIRES";

   /*
    * @Autowired
    * private JobClockSupport clock;
    */
   @Autowired
   private ActionUnitaireCqlSupport actionSupport;

   @Autowired
   private PrmdCqlSupport prmdSupport;

   @Autowired
   private PagmaCqlSupport pagmaSupport;

   @Autowired
   private PagmpCqlSupport pagmpSupport;

   @Autowired
   private PagmCqlSupport pagmSupport;

   @Autowired
   private ContratServiceCqlSupport contratSupport;

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   private MessageContext ctx;

   @Autowired
   ModeApiCqlSupport modeApiCqlSupport;

   @Autowired
   TraceDestinataireCqlSupport traceDestinataireCqlSupport;

   @Before
   public void before() {
      modeApiCqlSupport.initTables(MODE_API.DATASTAX);
      ctx = new MessageContext();
      MessageContext.setCurrentMessageContext(ctx);

      final VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setCodeAppli("TU");
      extrait.setIdUtilisateur("login_test");
      final SaeDroits droits = new SaeDroits();
      extrait.setSaeDroits(droits);

      final Authentication authentication = new TestingAuthenticationToken(extrait,
            "password_test", new String[] { "ROLE_TOUS" });

      SecurityContextHolder.getContext().setAuthentication(authentication);

      createAllTraceDestinataire();
      createDroits();

   }

   @After
   public void after() {
      SecurityContextHolder.getContext().setAuthentication(null);
   }

   private final void createDroits() {
      final List<ActionUnitaire> actions = createActionUnitaire();
      final Prmd prmd = createPrmd();
      final Pagma pagma = createPagma(actions);
      final Pagmp pagmp = createPagmp(prmd);
      createPagm(pagma, pagmp);
      createContrat();
   }

   private List<ActionUnitaire> createActionUnitaire() {
      final List<ActionUnitaire> actions = new ArrayList<>();

      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("consultation");
      actionUnitaire.setDescription("consultation");
      actionSupport.create(actionUnitaire);
      actions.add(actionUnitaire);

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("archivage_unitaire");
      actionUnitaire.setDescription("archivage unitaire");
      actionSupport.create(actionUnitaire);
      actions.add(actionUnitaire);

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("archivage_masse");
      actionUnitaire.setDescription("archivage de masse");
      actionSupport.create(actionUnitaire);
      actions.add(actionUnitaire);

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("recherche");
      actionUnitaire.setDescription("recherche");
      actionSupport.create(actionUnitaire);
      actions.add(actionUnitaire);

      return actions;
   }

   private Prmd createPrmd() {
      final Prmd prmd = new Prmd();
      prmd.setCode(PRMD_FULL);
      prmd.setDescription("full acces");
      prmdSupport.create(prmd);

      return prmd;
   }

   private Pagma createPagma(final List<ActionUnitaire> actions) {

      final List<String> codes = new ArrayList<>(actions.size());
      for (final ActionUnitaire action : actions) {
         codes.add(action.getCode());
      }
      final Pagma pagma = new Pagma();
      pagma.setActionUnitaires(codes);
      pagma.setCode(PAGMA_FULL);
      pagmaSupport.create(pagma);

      return pagma;
   }

   private Pagmp createPagmp(final Prmd prmd) {
      final Pagmp pagmp = new Pagmp();
      pagmp.setCode(PAGMP_FULL);
      pagmp.setDescription("PAGMP full");
      pagmp.setPrmd(prmd.getCode());

      pagmpSupport.create(pagmp);

      return pagmp;
   }

   private void createPagm(final Pagma pagma, final Pagmp pagmp) {
      final PagmCql pagmCql = new PagmCql();
      pagmCql.setCode(ROLE_TOUS);
      pagmCql.setDescription("Droit sur tous les roles et toutes les données");
      pagmCql.setPagma(pagma.getCode());
      pagmCql.setPagmp(pagmp.getCode());
      pagmCql.setParametres(new HashMap<String, String>());
      pagmCql.setIdClient(CONTRAT);

      pagmSupport.create(pagmCql);
   }

   private void createContrat() {
      final ServiceContract contrat = new ServiceContract();
      contrat.setCodeClient(CONTRAT);
      contrat.setDescription("contrat pour les TU");
      contrat.setLibelle("CS TESTS UNITAIRES");
      contrat.setViDuree(60L);
      // contrat.setIdPki("CN=IGC/A");
      contrat.setListPki(Arrays.asList("CN=IGC/A"));
      contratSupport.create(contrat);
   }

   private PingSecureRequest createPingSecureRequest(final String filePath) {

      Axis2Utils.initMessageContext(ctx, filePath);

      try {

         final XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return PingSecureRequest.Factory.parse(reader);

      } catch (final Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   @Test
   @Ignore("Ne marche plus, car les certificats de la PKI AED sont expirés")
   public void pingSecure() throws AxisFault {

      final PingSecureRequest request = createPingSecureRequest("src/test/resources/request/pingsecure_success.xml");

      final PingSecureResponse response = skeleton.pingSecure(request);

      assertEquals("Test du ping",
            "Les services du SAE sécurisés par authentification sont en ligne",
            response.getPingString());

      final AuthenticationToken authentification = AuthenticationContext
            .getAuthenticationToken();

      final List<String> actions = new ArrayList<>(authentification.getSaeDroits().keySet());

      assertEquals("le nombre d'actions unitaires est incorrect", 4, actions
            .size());

      for (final String action : authentification.getSaeDroits().keySet()) {
         final List<SaePrmd> prmds = authentification.getSaeDroits().get(action);
         assertEquals("nombre de prmd attendus incorrects pour " + action, 1,
               prmds.size());
         assertEquals("prmd attendu incorrect", PRMD_FULL, prmds.get(0)
               .getPrmd().getCode());
      }
   }

   /**
    * Création des données TraceDestinataire pour effectuer les tests des services en Cql
    */
   private void createAllTraceDestinataire() {
      final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-traces.xml");
      final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "TraceDestinataire");
      // final List<Row> list = DataCqlUtils.deserialize(url.getPath());

      final List<TraceDestinataire> listTraceDestinataire = TraceDestinataireCqlUtils.convertRowsToTraceDestinataires(list);
      for (final TraceDestinataire traceDestinataire : listTraceDestinataire) {
         traceDestinataireCqlSupport.create(traceDestinataire, new Date().getTime());
      }
   }

}
