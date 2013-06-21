/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fr.urssaf.image.sae.droit.dao.model.Pagm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class PagmSupportTest {

   private static final String ID_CLIENT = "idClient";

   private static final String CODE1 = "code1";

   private static final String DESCRIPTION1 = "description1";

   private static final String PAGMA1 = "pagma1";

   private static final String PAGMP1 = "pagmp1";

   private static final Map<String, String> PARAMETRES1 = new HashMap<String, String>();
   static {
      PARAMETRES1.put("cle1", "valeur1");
      PARAMETRES1.put("cle2", "valeur2");
   }

   @Autowired
   private CassandraServerBean cassandraServer;

   @Autowired
   private PagmSupport support;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test
   public void testCreateFind() {

      Pagm pagm = new Pagm();
      pagm.setCode(CODE1);
      pagm.setDescription(DESCRIPTION1);
      pagm.setPagma(PAGMA1);
      pagm.setPagmp(PAGMP1);
      pagm.setParametres(PARAMETRES1);

      support.create(ID_CLIENT, pagm, new Date().getTime());

      List<Pagm> list = support.find(ID_CLIENT);

      Assert.assertEquals("longueur de liste correcte", 1, list.size());

      Pagm res = list.get(0);

      Assert.assertNotNull("le pagm ne doit pas être null", res);
      Assert.assertEquals("l'identifiant (code) doit être correct", CODE1, res
            .getCode());
      Assert.assertEquals("la description doit être correcte", DESCRIPTION1,
            res.getDescription());
      Assert.assertEquals("le pagma doit être correct", PAGMA1, res.getPagma());
      Assert.assertEquals("le pagmp doit être correct", PAGMP1, res.getPagmp());

      Assert.assertEquals("il doit y avoir deux paramètres", 2, res
            .getParametres().size());
      Assert.assertTrue("toutes les clés doivent concorder", PARAMETRES1
            .keySet().containsAll(res.getParametres().keySet()));
      for (String key : PARAMETRES1.keySet()) {
         Assert.assertEquals("la valeur de la clé " + key
               + " doit etre correcte", PARAMETRES1.get(key), res
               .getParametres().get(key));
      }
   }

   @Test
   public void testCreateDelete() {

      Pagm pagm = new Pagm();
      pagm.setCode(CODE1);
      pagm.setDescription(DESCRIPTION1);
      pagm.setPagma(PAGMA1);
      pagm.setPagmp(PAGMP1);
      pagm.setParametres(PARAMETRES1);

      support.create(ID_CLIENT, pagm, new Date().getTime());

      support.delete(CODE1, new Date().getTime());

      List<Pagm> res = support.find(CODE1);

      Assert.assertTrue("aucune référence pagm ne doit être trouvée",
            res == null || res.isEmpty());
   }

   @Test
   public void testCreateFindAll() {

      Pagm pagm = new Pagm();
      pagm.setCode(CODE1);
      pagm.setDescription(DESCRIPTION1);
      pagm.setPagma(PAGMA1);
      pagm.setPagmp(PAGMP1);
      pagm.setParametres(PARAMETRES1);

      support.create(ID_CLIENT, pagm, new Date().getTime());

      pagm = new Pagm();
      pagm.setCode("code2");
      pagm.setDescription("description2");
      pagm.setPagma("pagma2");
      pagm.setPagmp("pagmp2");

      Map<String, String> param = new HashMap<String, String>();
      param.put("cle3", "valeur3");
      param.put("cle4", "valeur4");
      pagm.setParametres(param);

      support.create(ID_CLIENT, pagm, new Date().getTime());

      pagm = new Pagm();
      pagm.setCode("code3");
      pagm.setDescription("description3");
      pagm.setPagma("pagma3");
      pagm.setPagmp("pagmp3");

      param = new HashMap<String, String>();
      param.put("cle5", "valeur5");
      param.put("cle6", "valeur6");
      pagm.setParametres(param);

      support.create(ID_CLIENT, pagm, new Date().getTime());

      List<Pagm> list = support.find(ID_CLIENT);

      Assert.assertEquals("vérification du nombre d'enregistrements", 3, list
            .size());

      for (int i = 1; i < 4; i++) {
         String code = "code" + i;
         String description = "description" + i;
         String pagma = "pagma" + i;
         String pagmp = "pagmp" + i;
         int indexMap = i * 2;
         param = new HashMap<String, String>();
         param.put("cle" + (indexMap - 1), "valeur" + (indexMap - 1));
         param.put("cle" + (indexMap), "valeur" + (indexMap));
         pagm.setParametres(param);

         boolean found = false;
         int index = 0;
         while (!found && index < list.size()) {
            if (code.equals(list.get(index).getCode())) {
               Assert.assertEquals("la description doit être correcte",
                     description, list.get(index).getDescription());
               Assert.assertEquals("le pagma doit être correct", pagma, list
                     .get(index).getPagma());
               Assert.assertEquals("le pagmp doit être correct", pagmp, list
                     .get(index).getPagmp());

               Assert.assertEquals("il doit y avoir deux paramètres", 2, list
                     .get(index).getParametres().size());
               Assert.assertTrue("toutes les clés doivent concorder", param
                     .keySet().containsAll(
                           list.get(index).getParametres().keySet()));
               for (String key : param.keySet()) {
                  Assert.assertEquals("la valeur de la clé " + key
                        + " doit etre correcte", param.get(key), list
                        .get(index).getParametres().get(key));

                  found = true;
               }
            }

            index++;
         }

         Assert.assertTrue("le code " + code + " doit etre trouvé", found);
      }

   }
}
