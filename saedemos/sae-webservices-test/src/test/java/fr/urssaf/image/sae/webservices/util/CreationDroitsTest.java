/**
 * 
 */
package fr.urssaf.image.sae.webservices.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;

/**
 * Classe créant les différents éléments nécessaires à l'éxecution des cas de
 * test des webservices
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices-droits-test.xml" })
public class CreationDroitsTest {

   @Autowired
   private JobClockSupport clock;

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

   @Test
   @Ignore("Implémentation de la création des droits dans la base CASSANDRA")
   public final void createDroits() {
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
      actions.add(actionUnitaire);

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("archivage_unitaire");
      actionUnitaire.setDescription("archivage unitaire");
      actions.add(actionUnitaire);

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("archivage_masse");
      actionUnitaire.setDescription("archivage de masse");
      actions.add(actionUnitaire);

      actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode("recherche");
      actionUnitaire.setDescription("recherche");
      actions.add(actionUnitaire);

      return actions;
   }

   private Prmd createPrmd() {
      Prmd prmd = new Prmd();
      prmd.setCode(Constantes.PRMD_FULL);
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
      pagma.setCode(Constantes.PAGMA_FULl);
      pagmaSupport.create(pagma, clock.currentCLock());

      return pagma;
   }

   private Pagmp createPagmp(Prmd prmd) {
      Pagmp pagmp = new Pagmp();
      pagmp.setCode(Constantes.PAGMP_FULL);
      pagmp.setDescription("PAGMP full");
      pagmp.setPrmd(prmd.getCode());

      pagmpSupport.create(pagmp, clock.currentCLock());

      return pagmp;
   }

   private void createPagm(Pagma pagma, Pagmp pagmp) {
      Pagm pagm = new Pagm();
      pagm.setCode(Constantes.ROLE_TOUS);
      pagm.setDescription("Droit sur tous les roles et toutes les données");
      pagm.setPagma(pagma.getCode());
      pagm.setPagmp(pagmp.getCode());
      pagm.setParametres(new HashMap<String, String>());
      pagmSupport.create(Constantes.CONTRAT, pagm, clock.currentCLock());
   }

   private void createContrat() {
      ServiceContract contrat = new ServiceContract();
      contrat.setCodeClient(Constantes.CONTRAT);
      contrat.setDescription("contrat pour les TU");
      contrat.setLibelle("CS TESTS UNITAIRES");
      contrat.setViDuree(60L);
      contratSupport.create(contrat, clock.currentCLock());
   }
}
