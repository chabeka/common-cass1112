/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.Arrays;
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
import fr.urssaf.image.sae.droit.dao.model.Pagma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class PagmaSupportTest {

   private static final String CODE = "code";

   private static final String ACTION = "action";

   private static final String CODE1 = "code1";

   private static final String[] ACTIONS1 = new String[] { "action1", "action2" };

   @Autowired
   private CassandraServerBean cassandraServer;

   @Autowired
   private PagmaSupport support;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test
   public void testCreateFind() {

      List<String> listeAu = Arrays.asList(ACTIONS1);

      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(listeAu);
      pagma.setCode(CODE1);

      support.create(pagma, new Date().getTime());

      Pagma res = support.find(CODE1);

      Assert.assertNotNull("le pagma ne doit pas être null", res);
      Assert.assertEquals("l'identifiant (code) doit être correct", CODE1, res
            .getCode());

      Assert.assertTrue(
            "la liste d'origine doit contenir toute la liste récupérée",
            listeAu.containsAll(res.getActionUnitaires()));

      Assert.assertTrue(
            "la liste récupérée doit contenir toute la liste d'origine", res
                  .getActionUnitaires().containsAll(listeAu));
   }

   @Test
   public void testCreateDelete() {

      List<String> listeAu = Arrays.asList(ACTIONS1);

      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(listeAu);
      pagma.setCode(CODE1);

      support.create(pagma, new Date().getTime());

      support.delete(CODE1, new Date().getTime());

      Pagma res = support.find(CODE1);

      Assert.assertNull(
            "aucune référence de l'action unitaire ne doit être trouvée", res);
   }

   @Test
   public void testCreateFindAll() {

      for (int i = 1; i < 4; i++) {
         List<String> listAu = new ArrayList<String>();
         int max = i * 2;
         listAu.add(ACTION + (max - 1));
         listAu.add(ACTION + max);

         Pagma pagma = new Pagma();
         pagma.setCode(CODE + i);
         pagma.setActionUnitaires(listAu);

         support.create(pagma, new Date().getTime());
      }

      List<Pagma> list = support.findAll(10);

      Assert.assertEquals("vérification du nombre d'enregistrements", 3, list
            .size());

      for (int i = 1; i < 4; i++) {
         List<String> listAu = new ArrayList<String>();
         int max = i * 2;
         listAu.add(ACTION + (max - 1));
         listAu.add(ACTION + max);
         String code = CODE + i;

         boolean found = false;
         int index = 0;
         while (!found && index < list.size()) {
            if (code.equals(list.get(index).getCode())) {
               Assert
                     .assertTrue(
                           "la liste d'origine doit contenir toute la liste récupérée",
                           listAu.containsAll(list.get(index)
                                 .getActionUnitaires()));

               Assert
                     .assertTrue(
                           "la liste récupérée doit contenir toute la liste d'origine",
                           list.get(index).getActionUnitaires().containsAll(
                                 listAu));
               found = true;
            }
            index++;
         }

         Assert.assertTrue("le code " + code + " doit etre trouvé", found);
      }
   }

}
