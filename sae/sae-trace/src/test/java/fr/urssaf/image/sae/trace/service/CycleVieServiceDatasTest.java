/**
 *
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.dao.support.CycleVieSupport;
import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class CycleVieServiceDatasTest {

  private static final String VALUE = "valeur";

  private static final String KEY = "clé";

  private static final String TYPE_EVT = "CUSTOM";

  private static final String USERNAME = "_ADMIN";

  private static final String CONTRAT = "contrat de service";

  private static final String CODE_EVT = "code événement";

  private static final String ACTION = "action";

  private static final Map<String, Object> INFOS;
  static {
    INFOS = new HashMap<>();
    INFOS.put(KEY, VALUE);
  }

  @Autowired
  private CycleVieService service;

  @Autowired
  private CycleVieSupport support;


  @Test
  public void testCreation() {
    // Date de début : date courante moins 30 secondes
    final Calendar calendar = Calendar.getInstance();
    final long t = calendar.getTimeInMillis();
    final Date startDate = new Date(t - 30000);

    final UUID uuid = UUID.randomUUID();
    createTrace(uuid);
    Date endDate = new Date();

    endDate = DateUtils.addDays(endDate, 1);
    endDate = DateUtils.truncate(endDate, Calendar.DATE);

    final List<DfceTraceDoc> result = service
        .lecture(startDate, endDate, 100, true);

    Assert.assertNotNull("il doit y avoir un résultat", result);

    boolean traceOK = false;
    for (final DfceTraceDoc dfceTraceDoc : result) {
      if (TYPE_EVT.equals(dfceTraceDoc.getTypeEvt()) && USERNAME.equals(dfceTraceDoc.getLogin())) {
        traceOK = true;
      }
    }

    Assert.assertEquals("La trace insérée doit être trouvée", true, traceOK);
  }

  private void createTrace(final UUID uuid) {
    final TraceToCreate trace = new TraceToCreate();
    trace.setAction(ACTION);
    trace.setCodeEvt(CODE_EVT);
    trace.setContrat(CONTRAT);
    trace.setInfos(INFOS);

    support.create(trace);
  }
}
