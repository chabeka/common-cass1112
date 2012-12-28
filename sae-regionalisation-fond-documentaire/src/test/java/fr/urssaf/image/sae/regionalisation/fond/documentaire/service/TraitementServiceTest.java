/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.common.Constants;
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

   @Autowired
   private DocInfoService docsInfoService;

   private File file;
   private List<String> listUuid;

   @Before
   public void init() throws IOException {
      file = File.createTempFile("datas", ".log");
   }

   @After
   public void end() {
      FileUtils.deleteQuietly(file);

      EasyMock.reset(docInfoService, cassandraSupport, docsInfoService);
   }

   @Test
   @Ignore("Le test est compliqué suite à la modification de code")
   public void testEcritureFichier() throws IOException, CassandraException {

      initMockEcriture();

      service.writeCodesOrganismes(file.getAbsolutePath());

      EasyMock.verify(docInfoService, cassandraSupport);

      List<String> lines = FileUtils.readLines(file);

      Assert.assertEquals("le nombre de lignes doit etre correct", 2, lines
            .size());

      List<String> attendus = Arrays.asList("UR123;cog;1", "UR345;cop;1");

      for (String attendu : attendus) {
         Assert.assertTrue("l'élement " + attendu
               + " doit etre présent dans le fichier", lines.contains(attendu));
      }

   }

   private void initMockEcriture() throws CassandraException {

      Map<String, Long> codes = new HashMap<String, Long>();
      codes.put("UR123;cog", 1L);
      codes.put("UR345;cop", 1L);

      EasyMock.expect(docInfoService.getCodesOrganismes()).andReturn(codes);

      cassandraSupport.connect();
      EasyMock.expectLastCall().once();

      cassandraSupport.disconnect();
      EasyMock.expectLastCall().once();

      EasyMock.replay(docInfoService, cassandraSupport);

   }

   @Test
   @Ignore("Le test est compliqué suite à la modification de code")
   public void testEcritureUuid() throws IOException, CassandraException {

      initMockUuid();

      ClassPathResource resource = new ClassPathResource(
            "properties/corresp.properties");
      service.writeDocUuidsToUpdate(file.getAbsolutePath(), resource.getFile()
            .getAbsolutePath());

      EasyMock.verify(docsInfoService, cassandraSupport);

      List<String> lines = FileUtils.readLines(file);

      Assert.assertEquals("le nombre de lignes doit etre correct", 2, lines
            .size());

      List<String> attendus = Arrays.asList(listUuid.get(0), listUuid.get(2));

      for (String attendu : attendus) {
         Assert.assertTrue("l'élement " + attendu
               + "doit etre présent dans le fichier", lines.contains(attendu));
      }

   }

   private void initMockUuid() throws CassandraException {

      listUuid = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID()
            .toString(), UUID.randomUUID().toString());

      List<Map<String, String>> listDocs = new ArrayList<Map<String, String>>();
      Map<String, String> map = new HashMap<String, String>();
      map.put(Constants.CODE_ORG_GEST, "UR123");
      map.put(Constants.CODE_ORG_PROP, "UR124");
      map.put(Constants.UUID, listUuid.get(0));
      listDocs.add(map);
      map = new HashMap<String, String>();
      map.put(Constants.CODE_ORG_GEST, "UR125");
      map.put(Constants.CODE_ORG_PROP, "UR126");
      map.put(Constants.UUID, listUuid.get(1));
      listDocs.add(map);
      map = new HashMap<String, String>();
      map.put(Constants.CODE_ORG_GEST, "UR127");
      map.put(Constants.CODE_ORG_PROP, "UR123");
      map.put(Constants.UUID, listUuid.get(2));
      listDocs.add(map);

      EasyMock.expect(docsInfoService.getInfosDoc()).andReturn(listDocs);

      cassandraSupport.connect();
      EasyMock.expectLastCall().once();

      cassandraSupport.disconnect();
      EasyMock.expectLastCall().once();

      EasyMock.replay(docsInfoService, cassandraSupport);

   }

}
