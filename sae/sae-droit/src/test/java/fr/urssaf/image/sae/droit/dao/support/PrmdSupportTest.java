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
import fr.urssaf.image.sae.droit.dao.model.Prmd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class PrmdSupportTest {

   private static final String CODE1 = "code1";

   private static final String DESCRIPTION1 = "description1";

   private static final String LUCENE1 = "lucene1";

   @Autowired
   private CassandraServerBean cassandraServer;

   @Autowired
   private PrmdSupport support;

   @After
   public void end() throws Exception {
      cassandraServer.resetData();
   }

   @Test
   public void testCreateFind() {

      Prmd prmd = new Prmd();
      prmd.setCode(CODE1);
      prmd.setDescription(DESCRIPTION1);
      prmd.setLucene(LUCENE1);

      support.create(prmd, new Date().getTime());

      Prmd res = support.find(CODE1);

      Assert.assertNotNull("le pagm ne doit pas être null", res);
      Assert.assertEquals("l'identifiant (code) doit être correct", CODE1, res
            .getCode());
      Assert.assertEquals("la description doit être correcte", DESCRIPTION1,
            res.getDescription());
      Assert.assertEquals("le pagma doit être correct", LUCENE1, res
            .getLucene());
   }

   @Test
   public void testCreateDelete() {

      Prmd prmd = new Prmd();
      prmd.setCode(CODE1);
      prmd.setDescription(DESCRIPTION1);
      prmd.setLucene(LUCENE1);

      support.create(prmd, new Date().getTime());

      support.create(prmd, new Date().getTime());

      support.delete(CODE1, new Date().getTime());

      Prmd res = support.find(CODE1);

      Assert.assertNull(
            "aucune référence de l'action unitaire ne doit être trouvée", res);
   }

   @Test
   public void testCreateFindAll() {

      Prmd prmd = new Prmd();
      prmd.setCode(CODE1);
      prmd.setDescription(DESCRIPTION1);
      prmd.setLucene(LUCENE1);

      support.create(prmd, new Date().getTime());

      prmd = new Prmd();
      prmd.setCode("code2");
      prmd.setDescription("description2");
      prmd.setLucene("lucene2");

      support.create(prmd, new Date().getTime());

      prmd = new Prmd();
      prmd.setCode("code3");
      prmd.setDescription("description3");
      prmd.setLucene("lucene3");

      support.create(prmd, new Date().getTime());

      List<Prmd> list = support.findAll(10);

      Assert.assertEquals("vérification du nombre d'enregistrements", 3, list
            .size());

      for (int i = 1; i < 4; i++) {
         String code = "code" + i;
         String description = "description" + i;
         String lucene = "lucene" + i;

         boolean found = false;
         int index = 0;
         while (!found && index < list.size()) {
            if (code.equals(list.get(index).getCode())) {
               Assert.assertEquals("la description doit être correcte",
                     description, list.get(index).getDescription());
               Assert.assertEquals("le lucene doit être correct", lucene, list
                     .get(index).getLucene());
               found = true;

            }

            index++;
         }

         Assert.assertTrue("le code " + code + " doit etre trouvé", found);
      }

   }
}
