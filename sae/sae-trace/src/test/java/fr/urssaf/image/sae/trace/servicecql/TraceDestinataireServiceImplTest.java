/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.servicecql;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.commons.TraceDestinataireEnum;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.service.TraceDestinaireService;
import junit.framework.Assert;

/**
 * TODO (AC75095028) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class TraceDestinataireServiceImplTest {

   @Autowired
   TraceDestinaireService tracedestinataireservice;

   @Autowired
   TraceDestinataireCqlSupport tracesupprot;

   private final List<String> list = Arrays.asList("date", "contrat");

   @Test
   public void testGetCodeEvenementByTypeTrace() {

      final String code = "TEST|CREATE";
      final TraceDestinataire trace = new TraceDestinataire();
      trace.setCodeEvt(code);

      final Map<String, List<String>> dest = new HashMap<String, List<String>>();
      dest.put(TraceDestinataireEnum.REG_TECHNIQUE.name(), list);
      trace.setDestinataires(dest);

      tracesupprot.create(trace, new Date().getTime());

      final List<String> str = tracedestinataireservice.getCodeEvenementByTypeTrace("REG_TECHNIQUE");
      Assert.assertEquals(1, str.size());
   }
}
