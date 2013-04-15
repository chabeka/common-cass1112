package fr.urssaf.image.sae.integration.meta.service;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-integration-meta-test.xml" })
public class MetadonneeServiceTest {

   @Autowired
   private MetadonneeService metaService;

   @Test
   public void traitementTest() {

      File fichierXml = new File("src/test/resources/jeuxTest/saemeta.xml");

      metaService.traitement(fichierXml);

   }

}
