package fr.urssaf.image.sae.regionalisation.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.sae.regionalisation.bean.Trace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-regionalisation-service-test.xml")
@SuppressWarnings("PMD.MethodNamingConventions")
public class TraceDaoTest {

   @Autowired
   private TraceDao dao;

   @Test
   @Transactional
   public void addTraceMaj() {

      Trace trace = new Trace();
      trace.setIdSearch(BigDecimal.valueOf(2));
      trace.setIdDocument(UUID
            .fromString("cc4a5ec1-788d-4b41-baa8-d349947865bf"));
      trace.setMetaName("npe");
      trace.setOldValue("123854");
      trace.setNewValue("123856");

      dao.addTraceMaj(trace);

      List<Trace> traces = dao.findTraceMajByCriterion(BigDecimal.valueOf(2));

      Assert
            .assertEquals("le nombre de traces est inattendu", 1, traces.size());
      Assert.assertEquals("les traces sont différentes", trace, traces.get(0));

   }

   @Test
   @Transactional
   public void addTraceRec() {

      int oldRec = dao.findNbreDocs(BigDecimal.valueOf(2));

      Assert.assertNotSame("le nombre de traces est inattendu", 10, oldRec);

      dao.addTraceRec(BigDecimal.valueOf(2), 10);

      int newRec = dao.findNbreDocs(BigDecimal.valueOf(2));

      Assert.assertEquals("le nombre de traces est inattendu", 10, newRec);

   }

}
