package fr.urssaf.image.sae.regionalisation.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-regionalisation-service-test.xml")
@SuppressWarnings("PMD.MethodNamingConventions")
public class TraceDaoTest {

   @Autowired
   private TraceDao dao;

   @Before
   public void init() throws IOException {
      dao.open("12");
   }

   @After
   public void end() {
      dao.close();
      FileUtils.deleteQuietly(dao.getFile());
   }

   @Test
   public void addTraceMaj() throws IOException {

      Trace trace = new Trace();
      trace.setIdDocument(UUID
            .fromString("cc4a5ec1-788d-4b41-baa8-d349947865bf"));
      trace.setMetaName("npe");
      trace.setOldValue("123854");
      trace.setNewValue("123856");
      trace.setLineNumber(0);

      dao.addTraceMaj(trace);

      // récupération des traces
      List<String> lines = FileUtils.readLines(dao.getFile());

      Assert.assertEquals("le nombre de traces est inattendu", 1, lines.size());

      Map<String, String> map = new HashMap<String, String>();
      map.put(Constants.TRACE_ID_DOCUMENT,
            "cc4a5ec1-788d-4b41-baa8-d349947865bf");
      map.put(Constants.TRACE_META_NAME, "npe");
      map.put(Constants.TRACE_OLD_VALUE, "123854");
      map.put(Constants.TRACE_NEW_VALUE, "123856");
      map.put(Constants.TRACE_LIGNE, "0");

      String ligneAttendue = StrSubstitutor.replace(Constants.TRACE_OUT_MAJ,
            map);
      Assert.assertEquals("la trace réalisée doit être correcte",
            ligneAttendue, lines.get(0));

   }

   @Test
   public void addTraceRec() throws IOException {

      String requeteLucene = "maRe<quete";

      dao.addTraceRec(requeteLucene, 0, 2, true);

      Map<String, String> map = new HashMap<String, String>();
      map.put(Constants.TRACE_REQUETE_LUCENE, requeteLucene);
      map.put(Constants.TRACE_LIGNE, "0");
      map.put(Constants.TRACE_DOC_COUNT, "2");
      map.put(Constants.TRACE_INDIC_MAJ, Constants.TRACE_UPDATE_TRUE);

      String attendu = StrSubstitutor.replace(Constants.TRACE_OUT_REC, map);
      List<String> lines = FileUtils.readLines(dao.getFile());

      Assert.assertEquals(
            "le nombre de ligne dans le fichier doit etre correct", 1, lines
                  .size());

      Assert.assertEquals("la ligne générée doit être correcte", attendu, lines
            .get(0));

   }

}
