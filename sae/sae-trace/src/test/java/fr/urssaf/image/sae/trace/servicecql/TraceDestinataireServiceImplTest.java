/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.servicecql;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.commons.TraceDestinataireEnum;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.daocql.service.support.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.service.TraceDestinaireService;
import junit.framework.Assert;

/**
 * TODO (AC75095028) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class TraceDestinataireServiceImplTest {
  @Autowired
  @Qualifier("cqlServiceImpl")
  TraceDestinaireService tracedestinataireservice;

  @Autowired
  TraceDestinataireCqlSupport tracesupprot;

  private final List<String> list = Arrays.asList("date", "contrat");

  @Test
  public void create_new_trace_and_find_it_with_succes() {

    final List<TraceDestinataire> traces = tracesupprot.findAll();
    final String code = "TEST|CREATE";
    final TraceDestinataire trace = new TraceDestinataire();
    trace.setCodeEvt(code);

    final Map<String, List<String>> dest = new HashMap<String, List<String>>();
    dest.put(TraceDestinataireEnum.HIST_ARCHIVE.name(), list);
    trace.setDestinataires(dest);

    tracesupprot.create(trace, new Date().getTime());
    final TraceDestinataire traceFromDB = tracesupprot.findById(code);

    Assert.assertEquals("le code événement doit etre correct",
                        code,
                        traceFromDB.getCodeEvt());
    Assert.assertNotNull("la liste des destinataires doit etre non nulle",
                         traceFromDB.getDestinataires());
    Assert
          .assertEquals(
                        "il doit y avoir un et un seul élément dans la map des destinataires",
                        1,
                        traceFromDB.getDestinataires().size());
    Assert.assertEquals("la clé présente doit etre valide",
                        TraceDestinataireEnum.HIST_ARCHIVE.name(),
                        traceFromDB
                                   .getDestinataires().keySet().toArray(new String[0])[0]);

    final List<String> values = traceFromDB.getDestinataires().get(
                                                                   TraceDestinataireEnum.HIST_ARCHIVE.name());
    Assert.assertEquals(
                        "le nombre d'éléments de la liste doit etre correcte", 2, values
                                                                                        .size());
    Assert.assertTrue("tous les éléments présents doivent etre corrects",
                      list.containsAll(values));
  }

  @Test(expected = TraceRuntimeException.class)
  public void create_new_trace_with_bad_code() {
    final List<TraceDestinataire> allTraces = tracesupprot.findAll();
    final TraceDestinataire trace = new TraceDestinataire();
    trace.setCodeEvt("WS_REPRISE_MASSE|KO");

    final Map<String, List<String>> dest = new HashMap<String, List<String>>();
    dest.put("BAD_CODE|KO", list);
    trace.setDestinataires(dest);

    tracesupprot.create(trace, new Date().getTime());

  }

  @Test
  public void create_delete_with_success() {
    final String code = "TEST|DELETE";
    final String colName = TraceDestinataireEnum.HIST_ARCHIVE.name();

    final TraceDestinataire trace = new TraceDestinataire();
    trace.setCodeEvt(code);

    final Map<String, List<String>> dest = new HashMap<String, List<String>>();
    dest.put(TraceDestinataireEnum.HIST_ARCHIVE.name(), list);
    trace.setDestinataires(dest);
    tracesupprot.create(trace, new Date().getTime());

    tracesupprot.delete(code, new Date().getTime());

    try {
      tracesupprot.findById(code);
      Assert.fail("une exception " + NoSuchElementException.class.getName()
          + " est attendue");

    }
    catch (final NoSuchElementException exception) {

      Assert
            .assertEquals("la trace d'origine doit etre correcte",
                          NoSuchElementException.class,
                          exception.getClass());
    }
    catch (final Exception exception) {
      Assert.fail("une exception " + NoSuchElementException.class.getName()
          + " est attendue");
    }
  }
}
