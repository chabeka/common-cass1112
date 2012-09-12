/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.model.ServiceContractDatas;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ContratServiceSupportTest {

   @Autowired
   private CassandraServerBean cassandraServer;

   @Autowired
   private ContratServiceSupport contratSupport;

   @Autowired
   private PagmSupport pagmSupport;

   @Autowired
   private PagmaSupport pagmaSupport;

   @Autowired
   private PrmdSupport prmdSupport;

   @Autowired
   private PagmpSupport pagmpSupport;

   @Autowired
   private ActionUnitaireSupport actionSupport;

   @Autowired
   private ContratServiceDatasSupport serviceDatasSupport;

   private static final String[] CONTRAT_1_LISTE_PAGM = new String[] {
         "consultRecherchePermitAll", "capturesMasseUnitaireDenyAll",
         "consultDenyAll" };
   private static final String[] CONTRAT_2_LISTE_PAGM = new String[] { "captureEnMassePermitAll" };

   private static final String[] CONTRAT_1_LISTE_PAGMA = new String[] {
         "consultRecherche", "capturesMasseUnitaire", "consult" };
   private static final String[] CONTRAT_2_LISTE_PAGMA = new String[] { "captureEnMasse" };

   private static final String[] CONTRAT_1_LISTE_ACTIONS_UNITAIRES = new String[] {
         "consultation", "recherche", "captureMasse", "captureUnitaire" };
   private static final String[] CONTRAT_2_LISTE_ACTIONS_UNITAIRES = new String[] { "captureMasse" };

   private static final String[] CONTRAT_1_LISTE_PRMD = new String[] {
         "permitBean", "denyBean" };
   private static final String[] CONTRAT_2_LISTE_PRMD = new String[] { "permitBean" };

   private static final String[] CONTRAT_1_LISTE_PAGMP = new String[] {
         "permitAll", "denyAll" };
   private static final String[] CONTRAT_2_LISTE_PAGMP = new String[] { "permitAll" };

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Before
   public void init() {
      createActionsUnitaires();
      createPagma();
      createPrmd();
      createPagmp();
      createContrats();
      createPagm();
   }

   @Test
   public void getAll() {
      List<ServiceContractDatas> datas = serviceDatasSupport.findAll(100);

      Assert.assertEquals("le nombre de contrat attendu doit etre correct", 2,
            datas.size());

      checkValues(datas.get(0));
      checkValues(datas.get(1));
   }

   /**
    * @param serviceContractDatas
    */
   private void checkValues(ServiceContractDatas serviceContractDatas) {
      List<String> pagms;
      List<String> pagmas;
      List<String> actions;
      List<String> pagmps;
      List<String> prmds;

      if ("CODE_CLIENT_1".equals(serviceContractDatas.getCodeClient())) {
         pagms = Arrays.asList(CONTRAT_1_LISTE_PAGM);
         pagmas = Arrays.asList(CONTRAT_1_LISTE_PAGMA);
         pagmps = Arrays.asList(CONTRAT_1_LISTE_PAGMP);
         actions = Arrays.asList(CONTRAT_1_LISTE_ACTIONS_UNITAIRES);
         prmds = Arrays.asList(CONTRAT_1_LISTE_PRMD);
      } else {
         pagms = Arrays.asList(CONTRAT_2_LISTE_PAGM);
         pagmas = Arrays.asList(CONTRAT_2_LISTE_PAGMA);
         pagmps = Arrays.asList(CONTRAT_2_LISTE_PAGMP);
         actions = Arrays.asList(CONTRAT_2_LISTE_ACTIONS_UNITAIRES);
         prmds = Arrays.asList(CONTRAT_2_LISTE_PRMD);
      }

      // vérification que tous les pagms sont bien présents
      Assert.assertEquals(
            "le nombre de pagm doit etre correct pour le code client "
                  + serviceContractDatas.getCodeClient(), pagms.size(),
            serviceContractDatas.getPagms().size());
      for (Pagm pagm : serviceContractDatas.getPagms()) {
         Assert.assertTrue("le pagm " + pagm.getCode()
               + " doit etre présent dans la liste du contrat de service "
               + serviceContractDatas.getCodeClient(), pagms.contains(pagm
               .getCode()));
      }

      // vérification que tous les pagmas sont présents
      Assert.assertEquals(
            "le nombre de pagma doit etre correct pour le code client "
                  + serviceContractDatas.getCodeClient(), pagmas.size(),
            serviceContractDatas.getPagmas().size());
      for (Pagma pagma : serviceContractDatas.getPagmas()) {
         Assert.assertTrue("le pagma " + pagma.getCode()
               + " doit etre présent dans la liste du contrat de service "
               + serviceContractDatas.getCodeClient(), pagmas.contains(pagma
               .getCode()));
      }

      // vérification que tous les pagmps sont présents
      Assert.assertEquals(
            "le nombre de pagmp doit etre correct pour le code client "
                  + serviceContractDatas.getCodeClient(), pagmps.size(),
            serviceContractDatas.getPagmps().size());
      for (Pagmp pagmp : serviceContractDatas.getPagmps()) {
         Assert.assertTrue("le pagmp " + pagmp.getCode()
               + " doit etre présent dans la liste du contrat de service "
               + serviceContractDatas.getCodeClient(), pagmps.contains(pagmp
               .getCode()));
      }
      
      // vérification que tous les prmds sont présents
      Assert.assertEquals(
            "le nombre de prmd doit etre correct pour le code client "
                  + serviceContractDatas.getCodeClient(), prmds.size(),
            serviceContractDatas.getPrmds().size());
      for (Prmd prmd : serviceContractDatas.getPrmds()) {
         Assert.assertTrue("le prmd " + prmd.getCode()
               + " doit etre présent dans la liste du contrat de service "
               + serviceContractDatas.getCodeClient(), prmds.contains(prmd
               .getCode()));
      }
      
      // vérification que toutes les actions unitaires sont présentes
      Assert.assertEquals(
            "le nombre d'actions unitaires doit etre correct pour le code client "
                  + serviceContractDatas.getCodeClient(), actions.size(),
            serviceContractDatas.getActions().size());
      for (ActionUnitaire action : serviceContractDatas.getActions()) {
         Assert.assertTrue("l'action " + action.getCode()
               + " doit etre présent dans la liste du contrat de service "
               + serviceContractDatas.getCodeClient(), actions.contains(action
               .getCode()));
      }
      
      
   }

   private void createActionsUnitaires() {
      ActionUnitaire action = new ActionUnitaire();
      action.setCode("consultation");
      action.setDescription("consultation de documents");
      actionSupport.create(action, new Date().getTime());

      action = new ActionUnitaire();
      action.setCode("recherche");
      action.setDescription("recherche de documents");
      actionSupport.create(action, new Date().getTime());

      action = new ActionUnitaire();
      action.setCode("captureMasse");
      action.setDescription("capture de masse de documents");
      actionSupport.create(action, new Date().getTime());

      action = new ActionUnitaire();
      action.setCode("captureUnitaire");
      action.setDescription("capture unitaire de documents");
      actionSupport.create(action, new Date().getTime());
   }

   private void createPagma() {
      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "consultation",
            "recherche" }));
      pagma.setCode("consultRecherche");
      pagmaSupport.create(pagma, new Date().getTime());

      pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "captureMasse",
            "captureUnitaire" }));
      pagma.setCode("capturesMasseUnitaire");
      pagmaSupport.create(pagma, new Date().getTime());

      pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "consultation" }));
      pagma.setCode("consult");
      pagmaSupport.create(pagma, new Date().getTime());

      pagma = new Pagma();
      pagma.setActionUnitaires(Arrays.asList(new String[] { "captureMasse" }));
      pagma.setCode("captureEnMasse");
      pagmaSupport.create(pagma, new Date().getTime());

   }

   private void createPrmd() {
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("permitBean");
      prmd.setDescription("prmd autorisant tout");
      prmdSupport.create(prmd, new Date().getTime());

      prmd = new Prmd();
      prmd.setBean("denyAll");
      prmd.setCode("denyBean");
      prmd.setDescription("prmd refusant tout");
      prmdSupport.create(prmd, new Date().getTime());

   }

   private void createPagmp() {
      Pagmp pagmp = new Pagmp();
      pagmp.setCode("permitAll");
      pagmp.setDescription("autorisation de tout");
      pagmp.setPrmd("permitBean");
      pagmpSupport.create(pagmp, new Date().getTime());

      pagmp = new Pagmp();
      pagmp.setCode("denyAll");
      pagmp.setDescription("autorisation de tout");
      pagmp.setPrmd("denyBean");
      pagmpSupport.create(pagmp, new Date().getTime());
   }

   private void createContrats() {
      ServiceContract contract = new ServiceContract();
      contract.setCodeClient("CODE_CLIENT_1");
      contract.setDescription("code contrat numero 1");
      contract.setLibelle("libelle du code client 1");
      contract.setViDuree(Long.valueOf(120L));
      contratSupport.create(contract, new Date().getTime());

      contract = new ServiceContract();
      contract.setCodeClient("CODE_CLIENT_2");
      contract.setDescription("code contrat numero 2");
      contract.setLibelle("libelle du code client 2");
      contract.setViDuree(Long.valueOf(240L));
      contratSupport.create(contract, new Date().getTime());
   }

   private void createPagm() {
      Pagm pagm = new Pagm();
      pagm.setCode("consultRecherchePermitAll");
      pagm.setDescription("consultation et recherche et autorisation sur tout");
      pagm.setPagma("consultRecherche");
      pagm.setPagmp("permitAll");
      pagmSupport.create("CODE_CLIENT_1", pagm, new Date().getTime());

      pagm = new Pagm();
      pagm.setCode("capturesMasseUnitaireDenyAll");
      pagm.setDescription("capture masse et unitaires refusées sur tout");
      pagm.setPagma("capturesMasseUnitaire");
      pagm.setPagmp("denyAll");
      pagmSupport.create("CODE_CLIENT_1", pagm, new Date().getTime());

      pagm = new Pagm();
      pagm.setCode("consultDenyAll");
      pagm.setDescription("consultations refusées sur tout");
      pagm.setPagma("consult");
      pagm.setPagmp("denyAll");
      pagmSupport.create("CODE_CLIENT_1", pagm, new Date().getTime());

      pagm = new Pagm();
      pagm.setCode("captureEnMassePermitAll");
      pagm.setDescription("captureEnMasse autorisée sur tout");
      pagm.setPagma("captureEnMasse");
      pagm.setPagmp("permitAll");
      pagmSupport.create("CODE_CLIENT_2", pagm, new Date().getTime());

   }

}
