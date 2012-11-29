/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class TraceRegTechniqueSupportTest {

   private static final String VALUE = "valeur";
   private static final String KEY = "clé";

   private static final Date DATE = new Date();
   private static final String LOGIN = "LE LOGIN";
   private static final String CONTRAT = "contrat de service";
   private static final String CODE_EVT = "code événement";
   private static final String STACK = "erreur java stack";
   private static final String CONTEXTE = "contexte execution";
   private static final Map<String, String> INFOS;
   private static final int MAX_LIST_SIZE = 100;

   static {
      INFOS = new HashMap<String, String>();
      INFOS.put(KEY, VALUE);
   }

   @Autowired
   private TraceRegTechniqueSupport support;

   @Autowired
   private CassandraServerBean server;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test
   public void testCreateFindSuccess() {

      UUID uuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createTrace(uuid);

      TraceRegTechnique technique = support.find(uuid);
      checkBean(technique, uuid);

      List<TraceRegTechniqueIndex> list = support.findByDate(DATE);
      Assert.assertNotNull("la liste recherchée ne doit pas etre nulle", list);
      Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
            1, list.size());
      checkBeanIndex(list.get(0), uuid);
   }

   @Test
   public void testDelete() {
      UUID uuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      createTrace(uuid);

      support.delete(new Date(), new Date().getTime());

      TraceRegTechnique technique = support.find(uuid);
      Assert.assertNull("aucune trace ne doit etre touvée", technique);

      List<TraceRegTechniqueIndex> list = support.findByDate(DATE);
      Assert.assertTrue("aucun index ne doit etre present, donc aucune trace",
            CollectionUtils.isEmpty(list));
   }

   @Test
   public void testCreateFindByPlageSuccess() {

      UUID uuid = TimeUUIDUtils.getTimeUUID(DATE.getTime());
      createTrace(uuid);

      TraceRegTechnique exploitation = support.find(uuid);
      checkBean(exploitation, uuid);

      List<TraceRegTechniqueIndex> list = support.findByDates(DateUtils
            .addHours(DATE, -2), DateUtils.addHours(DATE, -1), MAX_LIST_SIZE,
            false);

      Assert.assertNull("aucun enregistrement ne doit etre retourné", list);
      list = support.findByDates(DateUtils.addHours(DATE, -1), DateUtils
            .addHours(DATE, 1), MAX_LIST_SIZE, false);
      Assert.assertNotNull("la liste recherchée ne doit pas etre nulle", list);
      Assert.assertEquals("le nombre d'éléments de la liste doit etre correct",
            1, list.size());
      checkBeanIndex(list.get(0), uuid);
   }

   private void checkBean(TraceRegTechnique technique, UUID uuid) {
      Assert.assertNotNull("l'objet doit etre trouvé", technique);
      Assert.assertEquals("l'action doit etre correcte", CONTEXTE, technique
            .getContexte());
      Assert.assertEquals("le code evenement doit etre correcte", CODE_EVT,
            technique.getCodeEvt());
      Assert.assertEquals("le contrat doit etre correcte", CONTRAT, technique
            .getContrat());
      Assert.assertEquals("l'identifiant doit etre correcte", uuid, technique
            .getIdentifiant());
      Assert.assertEquals("le login doit etre correcte", LOGIN, technique
            .getLogin());
      Assert.assertEquals("la date doit etre correcte", DATE, technique
            .getTimestamp());
      Assert.assertEquals(
            "les infos supplémentaire doivent contenir un élément", 1,
            technique.getInfos().size());
      Assert.assertTrue("les infos supplémentaire doivent une clé correcte",
            technique.getInfos().keySet().contains(KEY));
      Assert.assertEquals("la stack trace doit etre valide", STACK, technique
            .getStacktrace());
      Assert
            .assertEquals(
                  "les infos supplémentaire doivent contenir une valeur correcte élément",
                  VALUE, technique.getInfos().get(KEY));

   }

   private void checkBeanIndex(TraceRegTechniqueIndex index, UUID uuid) {
      Assert.assertNotNull("l'objet doit etre trouvé", index);
      Assert.assertEquals("l'action doit etre correcte", CONTEXTE, index
            .getContexte());
      Assert.assertEquals("le code evenement doit etre correcte", CODE_EVT,
            index.getCodeEvt());
      Assert.assertEquals("le contrat doit etre correcte", CONTRAT, index
            .getContrat());
      Assert.assertEquals("l'identifiant doit etre correcte", uuid, index
            .getIdentifiant());
      Assert.assertEquals("le login doit etre correcte", LOGIN, index
            .getLogin());
      Assert.assertEquals("la date doit etre correcte", DATE, index
            .getTimestamp());
   }

   private void createTrace(UUID uuid) {
      TraceRegTechnique trace = new TraceRegTechnique();
      trace.setContexte(CONTEXTE);
      trace.setCodeEvt(CODE_EVT);
      trace.setContrat(CONTRAT);
      trace.setIdentifiant(uuid);
      trace.setLogin(LOGIN);
      trace.setTimestamp(DATE);
      trace.setInfos(INFOS);
      trace.setStacktrace(STACK);

      support.create(trace, new Date().getTime());
   }
}
