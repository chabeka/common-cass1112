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
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.model.SaeContratService;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;

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
   private SaeDroitService saeDroitService;

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
      List<ServiceContract> datas = saeDroitService.findAllContractService(100);

      Assert.assertEquals("le nombre de contrat attendu doit etre correct", 2,
            datas.size());

      checkValues(datas.get(0));
      checkValues(datas.get(1));
   }

   /**
    * @param serviceContract
    */
   private void checkValues(ServiceContract serviceContract) {
      List<String> pagms;
      List<String> pagmas;
      List<String> actions;
      List<String> pagmps;
      List<String> prmds;

      if ("CODE_CLIENT_1".equals(serviceContract.getCodeClient())) {
         pagms = Arrays.asList(CONTRAT_1_LISTE_PAGM);
         pagmas = Arrays.asList(CONTRAT_1_LISTE_PAGMA);
         pagmps = Arrays.asList(CONTRAT_1_LISTE_PAGMP);
         actions = Arrays.asList(CONTRAT_1_LISTE_ACTIONS_UNITAIRES);
         prmds = Arrays.asList(CONTRAT_1_LISTE_PRMD);

         Assert.assertEquals("La description du CS est incorrecte",
               "code contrat numero 1", serviceContract.getDescription());
         Assert.assertEquals("Le libellé du CS est incorrecte",
               "libelle du code client 1", serviceContract.getLibelle());
         Assert.assertEquals("La durée de vie d'un VI est incorret", Long
               .valueOf(120), serviceContract.getViDuree());
         Assert.assertEquals("L'identifiant de la PKI est incorrect", null,
               serviceContract.getIdPki());
         Assert
               .assertEquals(
                     "Le flag de vérification du nom du certificat applicatif est incorrect",
                     false, serviceContract.isVerifNommage());
         Assert.assertEquals(
               "L'identifiant du certificat applicatif client est incorrect",
               null, serviceContract.getIdCertifClient());
         Assert.assertTrue("La liste des PKI doit contenir 1 élément",
               !CollectionUtils.isEmpty(serviceContract.getListPki())
                     && serviceContract.getListPki().size() == 1);
         Assert.assertEquals("la pki doit etre correcte", "pki 1",
               serviceContract.getListPki().get(0));
         Assert.assertTrue(
               "La liste des certificats clients devrait être vide",
               CollectionUtils.isEmpty(serviceContract.getListCertifsClient()));

      } else {
         pagms = Arrays.asList(CONTRAT_2_LISTE_PAGM);
         pagmas = Arrays.asList(CONTRAT_2_LISTE_PAGMA);
         pagmps = Arrays.asList(CONTRAT_2_LISTE_PAGMP);
         actions = Arrays.asList(CONTRAT_2_LISTE_ACTIONS_UNITAIRES);
         prmds = Arrays.asList(CONTRAT_2_LISTE_PRMD);

         Assert.assertEquals("La description du CS est incorrecte",
               "code contrat numero 2", serviceContract.getDescription());
         Assert.assertEquals("Le libellé du CS est incorrecte",
               "libelle du code client 2", serviceContract.getLibelle());
         Assert.assertEquals("La durée de vie d'un VI est incorret", Long
               .valueOf(240), serviceContract.getViDuree());
         Assert.assertEquals("L'identifiant de la PKI est incorrect", null,
               serviceContract.getIdPki());
         Assert
               .assertEquals(
                     "Le flag de vérification du nom du certificat applicatif est incorrect",
                     true, serviceContract.isVerifNommage());
         Assert.assertEquals(
               "L'identifiant du certificat applicatif client est incorrect",
               null, serviceContract.getIdCertifClient());

         Assert.assertFalse("La liste des PKI ne devrait pas être vide",
               CollectionUtils.isEmpty(serviceContract.getListPki()));
         Assert.assertEquals("Le nombre de PKI dans la liste est incorrect", 2,
               serviceContract.getListPki().size());
         Assert.assertEquals("La 1ère PKI de la liste est incorrect", "pki 2",
               serviceContract.getListPki().get(0));
         Assert.assertEquals("La 2ème PKI de la liste est incorrect",
               "pki 2.1", serviceContract.getListPki().get(1));

         Assert.assertFalse(
               "La liste des certificats clients ne devrait pas être vide",
               CollectionUtils.isEmpty(serviceContract.getListCertifsClient()));
         Assert.assertEquals(
               "Le nombre de certificats clients dans la liste est incorrect",
               2, serviceContract.getListCertifsClient().size());
         Assert.assertEquals(
               "Le 1er certificat client de la liste est incorrect",
               "id certif client 2", serviceContract.getListCertifsClient()
                     .get(0));
         Assert.assertEquals(
               "Le 2ème certificat client de la liste est incorrect",
               "id certif client 2.1", serviceContract.getListCertifsClient()
                     .get(1));

      }

      // vérification que tous les pagms sont bien présents
      SaeContratService saeCs = saeDroitService
            .getFullContratService(serviceContract.getCodeClient());
      Assert.assertEquals(
            "le nombre de pagm doit etre correct pour le code client "
                  + serviceContract.getCodeClient(), pagms.size(), saeCs
                  .getSaePagms().size());
      for (SaePagm pagm : saeCs.getSaePagms()) {
         Assert.assertTrue("le pagm " + pagm.getCode()
               + " doit etre présent dans la liste du contrat de service "
               + serviceContract.getCodeClient(), pagms
               .contains(pagm.getCode()));

         // vérification que tous les pagmas sont présents
         Assert.assertTrue("le pagma " + pagm.getPagma().getCode()
               + " doit etre présent dans la liste du contrat de service "
               + serviceContract.getCodeClient(), pagmas.contains(pagm
               .getPagma().getCode()));

         // vérification que tous les pagmps sont présents
         Assert.assertTrue("le pagmp " + pagm.getPagmp().getCode()
               + " doit etre présent dans la liste du contrat de service "
               + serviceContract.getCodeClient(), pagmps.contains(pagm
               .getPagmp().getCode()));

         // vérification que tous les prmds sont présents
         List<SaePrmd> saePrmd = saeCs.getSaePrmds();
         Assert.assertEquals(
               "le nombre de prmd doit etre correct pour le code client "
                     + serviceContract.getCodeClient(), prmds.size(), saePrmd
                     .size());
         for (SaePrmd prmd : saePrmd) {
            Assert.assertTrue("le prmd " + prmd.getPrmd().getCode()
                  + " doit etre présent dans la liste du contrat de service "
                  + serviceContract.getCodeClient(), prmds.contains(prmd
                  .getPrmd().getCode()));
         }

         // vérification que toutes les actions unitaires sont présentes
         for (String action : pagm.getPagma().getActionUnitaires()) {
            Assert.assertTrue("l'action " + action
                  + " doit etre présent dans la liste du contrat de service "
                  + serviceContract.getCodeClient(), actions.contains(action));
         }

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
      contract.setIdPki("pki 1");
      contract.setVerifNommage(false);
      contract.setIdCertifClient(null);
      contratSupport.create(contract, new Date().getTime());

      contract = new ServiceContract();
      contract.setCodeClient("CODE_CLIENT_2");
      contract.setDescription("code contrat numero 2");
      contract.setLibelle("libelle du code client 2");
      contract.setViDuree(Long.valueOf(240L));
      contract.setIdPki("pki 2");
      contract.setVerifNommage(true);
      contract.setIdCertifClient("id certif client 2");
      contract.setListPki(Arrays.asList("pki 2", "pki 2.1"));
      contract.setListCertifsClient(Arrays.asList("id certif client 2",
            "id certif client 2.1"));
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
