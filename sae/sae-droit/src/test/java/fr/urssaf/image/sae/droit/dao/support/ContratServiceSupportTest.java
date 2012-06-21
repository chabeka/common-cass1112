/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ContratServiceSupportTest {

   private static final String DESCRIPTION1 = "description1";

   private static final String CODE_CLIENT1 = "codeClient1";

   private static final String LIBELLE1 = "libelle1";

   private static final Long VI_DUREE = Long.valueOf(61);

   @Autowired
   private CassandraServerBean cassandraServer;

   @Autowired
   private ContratServiceSupport support;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test
   public void testCreateFind() {

      ServiceContract contract = new ServiceContract();
      contract.setCodeClient(CODE_CLIENT1);
      contract.setDescription(DESCRIPTION1);
      contract.setLibelle(LIBELLE1);
      contract.setViDuree(VI_DUREE);

      support.create(contract, new Date().getTime());

      ServiceContract res = support.find(LIBELLE1);

      Assert.assertNotNull("le contrat de service ne doit pas être null", res);
      Assert.assertEquals("l'identifiant (libelle) doit être correct",
            LIBELLE1, res.getLibelle());
      Assert.assertEquals("le code client doit être correct", CODE_CLIENT1, res
            .getCodeClient());
      Assert.assertEquals("la description doit être correcte", DESCRIPTION1,
            res.getDescription());
      Assert.assertEquals("la durée doit être correcte", VI_DUREE, res
            .getViDuree());

   }

   @Test
   public void testCreateDelete() {
      ServiceContract contract = new ServiceContract();
      contract.setCodeClient(CODE_CLIENT1);
      contract.setDescription(DESCRIPTION1);
      contract.setLibelle(LIBELLE1);
      contract.setViDuree(VI_DUREE);

      support.create(contract, new Date().getTime());

      support.delete(LIBELLE1, new Date().getTime());

      ServiceContract res = support.find(LIBELLE1);
      Assert.assertNull(
            "aucune référence de l'action unitaire ne doit être trouvée", res);
   }

   @Test
   public void testCreateFindAll() {

      ServiceContract contract = new ServiceContract();
      contract.setCodeClient(CODE_CLIENT1);
      contract.setDescription(DESCRIPTION1);
      contract.setLibelle(LIBELLE1);
      contract.setViDuree(VI_DUREE);

      support.create(contract, new Date().getTime());

      contract = new ServiceContract();
      contract.setCodeClient("codeClient2");
      contract.setDescription("description2");
      contract.setLibelle("libelle2");
      contract.setViDuree(Long.valueOf(62));

      support.create(contract, new Date().getTime());

      contract = new ServiceContract();
      contract.setCodeClient("codeClient3");
      contract.setDescription("description3");
      contract.setLibelle("libelle3");
      contract.setViDuree(Long.valueOf(63));

      support.create(contract, new Date().getTime());

      List<ServiceContract> list = support.findAll(10);

      Assert.assertEquals("vérification du nombre d'enregistrements", 3, list
            .size());

      for (int i = 1; i < 4; i++) {
         String codeClient = "codeClient" + i;
         String description = "description" + i;
         String libelle = "libelle" + i;
         Long duree = Long.valueOf(60 + i);

         boolean found = false;
         int index = 0;
         while (!found && index < list.size()) {
            if (libelle.equals(list.get(index).getLibelle())) {
               Assert.assertEquals("la description doit etre valide",
                     description, list.get(index).getDescription());
               Assert.assertEquals("le code client doit etre valide",
                     codeClient, list.get(index).getCodeClient());
               Assert.assertEquals("la durée doit etre valide", duree, list
                     .get(index).getViDuree());
               found = true;
            }
            index++;
         }

         Assert.assertTrue("le code " + libelle + " doit etre trouvé", found);
      }
   }

}
