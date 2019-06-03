/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import junit.framework.Assert;

/**
 * TODO (AC75095028) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MigrationTraceDestinataireTest {

   private static final Date DATE = new Date();

   @Autowired
   private TraceDestinataireCqlSupport supportCql;

   @Autowired
   private TraceDestinataireSupport supportThrift;

   @Autowired
   MigrationTraceDestinataire mtracedesti;

   @Autowired
   private CassandraServerBeanCql servercql;

   @Autowired
   private CassandraServerBean server;

   private static final String cfName = "tracedestinataire";

   private final List<String> list = Arrays.asList("date", "contrat");

   String[] listcodeevt = { "DFCE_CORBEILLE_DOC|OK", "DFCE_SUPPRESSION_DOC|OK", "DFCE_DEPOT_DOC|OK", "TEST|CREATE", "HIST_EVENEMENT|OK", "HIST_ARCHIVE|OK" };

   String[] listColName = { "HIST_EVENEMENT", "HIST_ARCHIVE", "REG_SECURITE", "REG_EXPLOITATION", "REG_TECHNIQUE", "JOURN_EVT" };

   @After
   public void after() throws Exception {
      server.resetData(true);
      servercql.resetData();
   }

   @Test
   public void migrationFromThriftToCql() {
      populateTableThrift();

      mtracedesti.migrationFromThriftToCql();
      final List<TraceDestinataire> listThrift = supportThrift.findAll();
      final List<TraceDestinataire> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listcodeevt.length);
      Assert.assertEquals(listThrift.size(), listCql.size());

   }

   @Test
   public void migrationFromCqlTothrift() {

      populateTableCql();
      mtracedesti.migrationFromCqlTothrift();

      final List<TraceDestinataire> listThrift = supportThrift.findAll();
      final List<TraceDestinataire> listCql = supportCql.findAll();

      Assert.assertEquals(listCql.size(), listcodeevt.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
   }
   // CLASSE UTILITAIRE

   public void populateTableCql() {
      int i = 0;
      for (final String code : listcodeevt) {
         final TraceDestinataire trace = new TraceDestinataire();
         trace.setCodeEvt(code);

         final Map<String, List<String>> dest = new HashMap<String, List<String>>();
         dest.put(listColName[i], list);
         trace.setDestinataires(dest);
         supportCql.create(trace, DATE.getTime());
         i++;
      }
   }

   private void populateTableThrift() {
      int i = 0;
      for (final String code : listcodeevt) {
         final TraceDestinataire trace = new TraceDestinataire();
         trace.setCodeEvt(code);

         final Map<String, List<String>> dest = new HashMap<String, List<String>>();
         dest.put(listColName[i], list);
         trace.setDestinataires(dest);
         supportThrift.create(trace, new Date().getTime());
         i++;
      }
   }
}
