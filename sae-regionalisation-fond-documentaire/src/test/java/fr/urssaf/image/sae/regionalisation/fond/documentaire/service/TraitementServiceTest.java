/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-regionalisation-cassandra-test.xml",
      "/applicationContext-sae-regionalisation-service-mock-test.xml" })
public class TraitementServiceTest {

   @Autowired
   private DocInfoService docInfoService;

   @Autowired
   private CassandraSupport cassandraSupport;

   @Autowired
   private TraitementService service;

   private File file;

   @Before
   public void init() throws IOException {
      file = File.createTempFile("datas", ".log");
   }

   @After
   public void end() {
      FileUtils.deleteQuietly(file);
   }

   @Test
   public void testEcritureFichier() throws IOException, CassandraException {

      initMock();

      service.writeCodesOrganismes(file.getAbsolutePath());

      List<String> lines = FileUtils.readLines(file);

      Assert.assertEquals("le nombre de lignes doit etre correct", 2, lines
            .size());

      List<String> attendus = Arrays.asList("UR123", "UR345");

      for (String attendu : attendus) {
         Assert.assertTrue("l'élement " + attendu
               + "doit etre présent dans le fichier", lines.contains(attendu));
      }

   }

   private void initMock() throws CassandraException {
      EasyMock.expect(docInfoService.getCodesOrganismes()).andReturn(
            Arrays.asList("UR123", "UR345"));

      cassandraSupport.connect();
      EasyMock.expectLastCall().once();

      cassandraSupport.disconnect();
      EasyMock.expectLastCall().once();

      EasyMock.replay(docInfoService, cassandraSupport);

   }

}
