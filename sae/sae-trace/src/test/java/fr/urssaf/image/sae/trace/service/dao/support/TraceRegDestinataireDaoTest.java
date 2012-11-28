/**
 * 
 */
package fr.urssaf.image.sae.trace.service.dao.support;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class TraceRegDestinataireDaoTest {

   @Autowired
   private TraceDestinataireSupport support;

   @Autowired
   private CassandraServerBean server;

   private final List<String> list = Arrays.asList("date", "contrat");

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test(expected = TraceRuntimeException.class)
   public void createTestFailureCodeInexistant() {
      createTrace("TEST|CREATE", "colonneNonRepertoriee");
   }

   @Test
   public void createFindTestSuccess() {
      String code = "TEST|CREATE";
      createTrace(code, TraceDestinataireDao.COL_HIST_ARCHIVE);

      TraceDestinataire destinataire = support.find(code);
      Assert.assertEquals("le code événement doit etre correct", code,
            destinataire.getCodeEvt());
      Assert.assertNotNull("la liste des destinataires doit etre non nulle",
            destinataire.getDestinataires());
      Assert
            .assertEquals(
                  "il doit y avoir un et un seul élément dans la map des destinataires",
                  1, destinataire.getDestinataires().size());
      Assert.assertEquals("la clé présente doit etre valide",
            TraceDestinataireDao.COL_HIST_ARCHIVE, destinataire
                  .getDestinataires().keySet().toArray(new String[0])[0]);
      List<String> values = destinataire.getDestinataires().get(
            TraceDestinataireDao.COL_HIST_ARCHIVE);
      Assert.assertEquals(
            "le nombre d'éléments de la liste doit etre correcte", 2, values
                  .size());
      Assert.assertTrue("tous les éléments présents doivent etre corrects",
            list.containsAll(values));
   }

   @Test
   public void createDeleteSuccess() {
      String code = "TEST|DELETE";
      String colName = TraceDestinataireDao.COL_HIST_EVT;
      createTrace(code, colName);
      support.delete(code, new Date().getTime());

      try {
         support.find(code);
         Assert.fail("une exception " + TraceRuntimeException.class.getName()
               + " est attendue");

      } catch (TraceRuntimeException exception) {
         Assert
               .assertEquals("la trace d'origine doit etre correcte",
                     InvalidCacheLoadException.class, exception.getCause()
                           .getClass());
      } catch (Exception exception) {
         Assert.fail("une exception " + TraceRuntimeException.class.getName()
               + " est attendue");
      }
   }

   private void createTrace(String code, String colName) {
      TraceDestinataire trace = new TraceDestinataire();
      trace.setCodeEvt(code);

      Map<String, List<String>> dest = new HashMap<String, List<String>>();
      dest.put(colName, list);
      trace.setDestinataires(dest);

      support.create(trace, new Date().getTime());
   }

}
