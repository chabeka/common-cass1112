package fr.urssaf.image.sae.integration.meta.service;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-integration-meta-test.xml" })
@Ignore
public class MetadonneeServiceTest {

   @Autowired
   private MetadonneeService metaService;

   @Test
   public void metaCreation() {

      File fichierXml = new File(
            "src/test/resources/jeuxTest/saemeta-meta-create.xml");

      metaService.traitement(fichierXml);

   }

   @Test
   public void dicoCreation() {

      File fichierXml = new File(
            "src/test/resources/jeuxTest/saemeta-dico-create.xml");

      metaService.traitement(fichierXml);

   }

}
