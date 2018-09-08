package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.services.DictionaryService;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;

/**
 * classe de test du service {@link DictionarySupportImpl}
 *
 */
// FIXME : réactiver les testes après la mise en place des fichiers application
// contexte
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class DictionaryServiceTest {

   @Autowired
   private DictionarySupport dictSupport;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private DictionaryService service;

   @After
   public void after() throws Exception {
      server.resetData();
   }



   /**
    * Test de création d'un dictionnaire
    */
   @Test
   public void testService() throws DictionaryNotFoundException {
      final String id = "dictionnaireTest";
      final List<String> values = Arrays.asList("ValeurTest", "ValeurTest2");
      service.addElements(id, values);
      final Dictionary dict = dictSupport.find(id);
      Assert.assertNotNull(dict);
      Assert.assertTrue(id.equals(dict.getId()));
   }

}
