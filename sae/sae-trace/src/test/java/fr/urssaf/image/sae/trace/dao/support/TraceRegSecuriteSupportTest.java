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
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class TraceRegSecuriteSupportTest {

   private static final String VALUE = "valeur";
   private static final String KEY = "clé";

   private static final Date DATE = new Date();
   private static final String LOGIN = "LE LOGIN";
   private static final String CONTRAT = "contrat de service";
   private static final String CODE_EVT = "code événement";
   private static final String CONTEXT = "contexte";
   private static final Map<String, Object> INFOS;
   private static final int MAX_LIST_SIZE = 100;
   static {
      INFOS = new HashMap<String, Object>();
      INFOS.put(KEY, VALUE);
   }

   @Autowired
   private TraceRegSecuriteSupport support;

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

      TraceRegSecurite securite = support.find(uuid);
      checkBean(securite, uuid);

      List<TraceRegSecuriteIndex> list = support.findByDate(DATE);
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

      TraceRegSecurite securite = support.find(uuid);
      Assert.assertNull("aucune trace ne doit etre touvée", securite);

      List<TraceRegSecuriteIndex> list = support.findByDate(DATE);
      Assert.assertTrue("aucun index ne doit etre present, donc aucune trace",
            CollectionUtils.isEmpty(list));
   }

   @Test
   public void testCreateFindByPlageSuccess() {

      UUID uuid = TimeUUIDUtils.getTimeUUID(DATE.getTime());
      createTrace(uuid);

      TraceRegSecurite exploitation = support.find(uuid);
      checkBean(exploitation, uuid);

      List<TraceRegSecuriteIndex> list = support.findByDates(DateUtils
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

   private void checkBean(TraceRegSecurite securite, UUID uuid) {
      Assert.assertNotNull("l'objet doit etre trouvé", securite);
      Assert.assertEquals("le contexte doit etre correcte", CONTEXT, securite
            .getContexte());
      Assert.assertEquals("le code evenement doit etre correcte", CODE_EVT,
            securite.getCodeEvt());
      Assert.assertEquals("le contrat doit etre correcte", CONTRAT, securite
            .getContrat());
      Assert.assertEquals("l'identifiant doit etre correcte", uuid, securite
            .getIdentifiant());
      Assert.assertEquals("le login doit etre correcte", LOGIN, securite
            .getLogin());
      Assert.assertEquals("la date doit etre correcte", DATE, securite
            .getTimestamp());
      Assert.assertEquals(
            "les infos supplémentaire doivent contenir un élément", 1, securite
                  .getInfos().size());
      Assert.assertTrue("les infos supplémentaire doivent une clé correcte",
            securite.getInfos().keySet().contains(KEY));
      Assert
            .assertEquals(
                  "les infos supplémentaire doivent contenir une valeur correcte élément",
                  VALUE, securite.getInfos().get(KEY));

   }

   private void checkBeanIndex(TraceRegSecuriteIndex index, UUID uuid) {
      Assert.assertNotNull("l'objet doit etre trouvé", index);
      Assert.assertEquals("le contexte doit etre correcte", CONTEXT, index
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
      TraceRegSecurite trace = new TraceRegSecurite();
      trace.setContexte(CONTEXT);
      trace.setCodeEvt(CODE_EVT);
      trace.setContrat(CONTRAT);
      trace.setIdentifiant(uuid);
      trace.setLogin(LOGIN);
      trace.setTimestamp(DATE);
      trace.setInfos(INFOS);

      support.create(trace, new Date().getTime());
   }
}
