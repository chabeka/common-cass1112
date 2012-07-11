/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.Arrays;
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
import fr.urssaf.image.sae.droit.dao.model.Prmd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class PrmdSupportTest {

   private static final String CODE1 = "code1";

   private static final String DESCRIPTION1 = "description1";

   private static final String LUCENE1 = "lucene1";

   private static final String BEAN1 = "bean1";

   private static final Map<String, List<String>> MAP1;
   static {
      MAP1 = new HashMap<String, List<String>>();
      MAP1.put("cle1", Arrays.asList(new String[]{"valeur1"}));
   }

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
      prmd.setBean(BEAN1);
      prmd.setMetadata(MAP1);

      support.create(prmd, new Date().getTime());

      Prmd res = support.find(CODE1);

      Assert.assertNotNull("le pagm ne doit pas être null", res);
      Assert.assertEquals("l'identifiant (code) doit être correct", CODE1, res
            .getCode());
      Assert.assertEquals("la description doit être correcte", DESCRIPTION1,
            res.getDescription());
      Assert.assertEquals("le pagma doit être correct", LUCENE1, res
            .getLucene());
      Assert.assertEquals("le bean doit être correct", BEAN1, res.getBean());
      Assert.assertEquals("il doit y avoir un élément dans les metadonnees", 1,
            res.getMetadata().size());
      Assert.assertTrue("l'élément cle1 doit être présent", res.getMetadata()
            .keySet().contains("cle1"));
      Assert.assertEquals("la valeur de cle1 doit être correcte", "valeur1",
            res.getMetadata().get("cle1").get(0));
   }

   @Test
   public void testCreateDelete() {

      Prmd prmd = new Prmd();
      prmd.setCode(CODE1);
      prmd.setDescription(DESCRIPTION1);
      prmd.setLucene(LUCENE1);
      prmd.setBean(BEAN1);
      prmd.setMetadata(new HashMap<String, List<String>>());

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
      prmd.setBean(BEAN1);
      prmd.setMetadata(MAP1);

      support.create(prmd, new Date().getTime());

      prmd = new Prmd();
      prmd.setCode("code2");
      prmd.setDescription("description2");
      prmd.setLucene("lucene2");
      prmd.setBean("bean2");
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put("cle2", Arrays.asList(new String[]{"valeur2"}));
      prmd.setMetadata(map);

      support.create(prmd, new Date().getTime());

      prmd = new Prmd();
      prmd.setCode("code3");
      prmd.setDescription("description3");
      prmd.setLucene("lucene3");
      prmd.setBean("bean3");
      map = new HashMap<String, List<String>>();
      map.put("cle3", Arrays.asList(new String[]{"valeur3"}));
      prmd.setMetadata(map);

      support.create(prmd, new Date().getTime());

      List<Prmd> list = support.findAll(10);

      Assert.assertEquals("vérification du nombre d'enregistrements", 3, list
            .size());

      for (int i = 1; i < 4; i++) {
         String code = "code" + i;
         String description = "description" + i;
         String lucene = "lucene" + i;
         String bean = "bean" + i;
         String cle = "cle" + i;
         String valeur = "valeur" + i;

         boolean found = false;
         int index = 0;
         while (!found && index < list.size()) {
            if (code.equals(list.get(index).getCode())) {
               Assert.assertEquals("la description doit être correcte",
                     description, list.get(index).getDescription());
               Assert.assertEquals("le lucene doit être correct", lucene, list
                     .get(index).getLucene());
               Assert.assertEquals("le bean doit être correct", bean, list.get(
                     index).getBean());
               Assert.assertEquals("un seul élément dans les parametres", 1,
                     list.get(index).getMetadata().size());
               Assert.assertTrue("la clé " + cle
                     + " doit être présente dans les parametres", list.get(index)
                     .getMetadata().keySet().contains(cle));
               Assert.assertEquals("la valeur de la clé " + cle
                     + " doit être correcte", valeur, list.get(index).getMetadata()
                     .get(cle).get(0));
               found = true;

            }

            index++;
         }

         Assert.assertTrue("le code " + code + " doit etre trouvé", found);
      }

   }
}
