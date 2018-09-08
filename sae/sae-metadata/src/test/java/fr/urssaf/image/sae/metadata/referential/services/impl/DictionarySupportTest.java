package fr.urssaf.image.sae.metadata.referential.services.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;

/**
 * classe de test du service {@link DictionarySupportImpl}
 *
 */
// FIXME : réactiver les testes après la mise en place des fichiers application
// contexte
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class DictionarySupportTest {

   @Autowired
   private DictionarySupport dictSupport;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private JobClockSupport clock;


   @After
   public void after() throws Exception {
      server.resetData();
   }

   /**
    * Test de création d'un dictionnaire
    *
    * @throws DictionaryNotFoundException
    */
   @Test
   public void createDictTest() throws DictionaryNotFoundException {

      final String id = "dictionnaireTest";
      final String value = "ValeurTest";
      dictSupport.addElement(id, value, clock.currentCLock());
      final Dictionary dict = dictSupport.find(id);
      Assert.assertNotNull(dict);
      Assert.assertTrue(id.equals(dict.getId()));

   }

   /**
    * Test de création d'un dictionnaire existant, l'ancienne valeur devra être
    * remplacée par la nouvelle
    */
   @Test
   public void createExistingDictTest() throws DictionaryNotFoundException {

      final String id = "dicExist";
      final String value = "NewMeta";
      final Dictionary dictExistant = dictSupport.find(id);

      dictSupport.addElement(id, value, clock.currentCLock());
      final Dictionary dict = dictSupport.find(id);
      Assert.assertNotNull(dict);
      Assert.assertTrue(!dictExistant.equals(dict));

   }

   /**
    * Test de modification de métadonnée existante
    */
   @Test
   public void modifyExistingDictTest() throws DictionaryNotFoundException {

      final String id = "dicExist";
      final String value = "NewMeta";
      final Dictionary dictExistant = dictSupport.find(id);

      dictSupport.addElement(id, value, clock.currentCLock());
      final Dictionary dict = dictSupport.find(id);
      Assert.assertNotNull(dict);
      Assert.assertTrue(!dictExistant.equals(dict));

   }

   /**
    * Test de recherche d'un dictionnaire inexistant
    */
   @Test
   public void findDictTest() {

      final String id = "dicNotExist";
      try {
         final Dictionary dictExistant = dictSupport.find(id);
      } catch (final DictionaryNotFoundException ex) {
         Assert.assertTrue(ex.getMessage().equals(
               "Le dictionnaire dicNotExist n'a pas été trouvé"));
      }

   }

   /**
    * Test de supression d'un dictionnaire existant
    *
    * @throws DictionaryNotFoundException
    */
   @Ignore
   // Initialement la supression de la colonne unique impliquait un dictionnaire
   // vide, le fonctionnement a été modifié pour considérer un dictionnaire sans
   // colonne comme un dictionnaire inexistant
   @Test
   public void deleteColumnTest() throws DictionaryNotFoundException {

      final String id = "dicExist";
      final String value = "Meta";
      final Dictionary dictExistant = dictSupport.find(id);
      Assert.assertTrue(dictExistant.getEntries().size() > 0);
      dictSupport.deleteElement(id, value, clock.currentCLock());
      final Dictionary deletedDict = dictSupport.find(id);
      Assert.assertTrue(deletedDict.getEntries().size() == 0);
   }

   /**
    * Test de supression d'une colonne inexistante
    */

   @Test
   public void deleteColumnNotExistDictTest()
         throws DictionaryNotFoundException {

      final String id = "dicExist";
      final String value = "Meta2";
      final Dictionary dictExistant = dictSupport.find(id);
      Assert.assertTrue(dictExistant.getEntries().size() == 1);
      dictSupport.deleteElement(id, value, clock.currentCLock());
      final Dictionary deletedDict = dictSupport.find(id);
      Assert.assertTrue(dictExistant.getEntries().equals(
                                                         deletedDict.getEntries()));
   }
}
