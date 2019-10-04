/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.servicecql;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.trace.commons.TraceDestinataireEnum;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.service.TraceDestinaireService;
import junit.framework.Assert;

/**
 * TODO (AC75095028) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class TraceDestinataireServiceImplTest {

  @Autowired
  TraceDestinaireService tracedestinataireservice;

  @Autowired
  TraceDestinataireCqlSupport traceDestinataireCqlSupport;

  @Autowired
  TraceDestinataireCqlSupport tracesupportCql;

  @Autowired
  TraceDestinataireSupport tracesupport;

  @Autowired
  CassandraServerBean server;

  private final List<String> list = Arrays.asList("date", "contrat");

  private final String cfName = "tracedestinataire";

  @Test
  public void init() {
    try {
      if (server.isCassandraStarted()) {
        server.clearAndLoad();
      }
      Assert.assertTrue(true);
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetCodeEvenementByTypeTrace() {
    GestionModeApiUtils.setModeApiThrift(cfName);
    final String code = "TEST|CREATE";
    final TraceDestinataire trace = new TraceDestinataire();
    trace.setCodeEvt(code);

    final Map<String, List<String>> dest = new HashMap<>();
    dest.put(TraceDestinataireEnum.REG_TECHNIQUE.name(), list);
    trace.setDestinataires(dest);

    tracesupportCql.create(trace, new Date().getTime());
    tracesupport.create(trace, new Date().getTime());

    final List<String> str = tracedestinataireservice.getCodeEvenementByTypeTrace("REG_TECHNIQUE");
    Assert.assertEquals(14, str.size());

    final List<TraceDestinataire> dests = traceDestinataireCqlSupport.findAll();
    Assert.assertEquals(1, dests.size());

    Assert.assertEquals("TEST|CREATE", dests.get(0).getCodeEvt());
    Assert.assertNotNull(dests.get(0).getDestinataires().get("REG_TECHNIQUE"));

  }
}
