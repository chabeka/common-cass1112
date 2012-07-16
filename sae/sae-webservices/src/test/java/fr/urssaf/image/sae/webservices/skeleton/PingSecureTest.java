package fr.urssaf.image.sae.webservices.skeleton;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.After;
import org.junit.Before;
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
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;
import fr.urssaf.image.sae.webservices.util.Axis2Utils;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml",
      "/applicationContext-security-test.xml",
      "/applicationContext-sae-vi-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class PingSecureTest {

   private static final String PRMD_FULL = "PRMD_FULL";

   private static final String PAGMA_FULL = "PAGMA_FULL";

   private static final String PAGMP_FULL = "PAGMP_FULL";

   private static final String ROLE_TOUS = "ROLE_TOUS";

   private static final String CONTRAT = "TESTS_UNITAIRES";

   @Autowired
   private JobClockSupport clock;

   @Autowired
   private ActionUnitaireSupport actionSupport;

   @Autowired
   private PrmdSupport prmdSupport;

   @Autowired
   private PagmaSupport pagmaSupport;

   @Autowired
   private PagmpSupport pagmpSupport;

   @Autowired
   private PagmSupport pagmSupport;

   @Autowired
   private ContratServiceSupport contratSupport;

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   private MessageContext ctx;

   @Before
   public void before() {

      ctx = new MessageContext();
      MessageContext.setCurrentMessageContext(ctx);

      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setCodeAppli("TU");
      extrait.setIdUtilisateur("login_test");
      SaeDroits droits = new SaeDroits();
      extrait.setSaeDroits(droits);

      Authentication authentication = new TestingAuthenticationToken(extrait,
            "password_test", new String[] { "ROLE_TOUS" });

      SecurityContextHolder.getContext().setAuthentication(authentication);

      createDroits();

   }

   @After
   public void after() {
      SecurityContextHolder.getContext().setAuthentication(null);
   }

   private final void createDroits() {
      List<ActionUnitaire> actions = createActionUnitaire();
      Prmd prmd = createPrmd();
      Pagma pagma = createPagma(actions);
      Pagmp pagmp = createPagmp(prmd);
      createPagm(pagma, pagmp);
      createContrat();
   }

   private List<ActionUnitaire> createActionUnitaire() {
      List<ActionUnitaire> actions = new ArrayList<ActionUnitaire>();

      ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("consultation");
      actionUnitaire.setDescription("consultation");
      actionSupport.create(actionUnitaire, clock.currentCLock());
      actions.add(actionUnitaire);

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("archivage_unitaire");
      actionUnitaire.setDescription("archivage unitaire");
      actionSupport.create(actionUnitaire, clock.currentCLock());
      actions.add(actionUnitaire);

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("archivage_masse");
      actionUnitaire.setDescription("archivage de masse");
      actionSupport.create(actionUnitaire, clock.currentCLock());
      actions.add(actionUnitaire);

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("recherche");
      actionUnitaire.setDescription("recherche");
      actionSupport.create(actionUnitaire, clock.currentCLock());
      actions.add(actionUnitaire);

      return actions;
   }

   private Prmd createPrmd() {
      Prmd prmd = new Prmd();
      prmd.setCode(PRMD_FULL);
      prmd.setDescription("full acces");
      prmdSupport.create(prmd, clock.currentCLock());

      return prmd;
   }

   private Pagma createPagma(List<ActionUnitaire> actions) {

      List<String> codes = new ArrayList<String>(actions.size());
      for (ActionUnitaire action : actions) {
         codes.add(action.getCode());
      }
      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(codes);
      pagma.setCode(PAGMA_FULL);
      pagmaSupport.create(pagma, clock.currentCLock());

      return pagma;
   }

   private Pagmp createPagmp(Prmd prmd) {
      Pagmp pagmp = new Pagmp();
      pagmp.setCode(PAGMP_FULL);
      pagmp.setDescription("PAGMP full");
      pagmp.setPrmd(prmd.getCode());

      pagmpSupport.create(pagmp, clock.currentCLock());

      return pagmp;
   }

   private void createPagm(Pagma pagma, Pagmp pagmp) {
      Pagm pagm = new Pagm();
      pagm.setCode(ROLE_TOUS);
      pagm.setDescription("Droit sur tous les roles et toutes les données");
      pagm.setPagma(pagma.getCode());
      pagm.setPagmp(pagmp.getCode());
      pagm.setParametres(new HashMap<String, String>());
      pagmSupport.create(CONTRAT, pagm, clock.currentCLock());
   }

   private void createContrat() {
      ServiceContract contrat = new ServiceContract();
      contrat.setCodeClient(CONTRAT);
      contrat.setDescription("contrat pour les TU");
      contrat.setLibelle("CS TESTS UNITAIRES");
      contrat.setViDuree(60L);
      contratSupport.create(contrat, clock.currentCLock());
   }

   private PingSecureRequest createPingSecureRequest(String filePath) {

      Axis2Utils.initMessageContext(ctx, filePath);

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return PingSecureRequest.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   @Test
   public void pingSecure() throws AxisFault {

      PingSecureRequest request = createPingSecureRequest("src/test/resources/request/pingsecure_success.xml");

      PingSecureResponse response = skeleton.pingSecure(request);

      assertEquals("Test du ping",
            "Les services du SAE sécurisés par authentification sont en ligne",
            response.getPingString());

      AuthenticationToken authentification = AuthenticationContext
            .getAuthenticationToken();

      SaeDroits saeDroits = authentification.getDetails();
      List<String> actions = new ArrayList<String>(saeDroits.keySet());

      assertEquals("le nombre d'actions unitaires est incorrect", 4, actions
            .size());

      for (String action : saeDroits.keySet()) {
         List<SaePrmd> prmds = saeDroits.get(action);
         assertEquals("nombre de prmd attendus incorrects pour " + action, 1,
               prmds.size());
         assertEquals("prmd attendu incorrect", PRMD_FULL, prmds.get(0)
               .getPrmd().getCode());
      }

   }

}
