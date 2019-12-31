package fr.urssaf.image.sae.metadata.referential.support.cql;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;


/**
 * classe de test du service {@link DictionarySupportImpl}
 *
 */

// contexte
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-metadata-test.xml"})
// @DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DictionarySupportCqlTest {

  @Autowired
  private DictionaryCqlSupport dictSupport;

  @Autowired
  private CassandraServerBean server;


  @After
  public void end() throws Exception {
    server.resetDataOnly();
  }

  @Test
  public void init() throws Exception {

    if (server.isCassandraStarted()) {
      server.resetData();
    }
    Assert.assertTrue(true);
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
    dictSupport.addElement(id, value);
    final Dictionary dict = dictSupport.find(id);
    Assert.assertNotNull(dict);
    Assert.assertTrue(id.equals(dict.getIdentifiant()));

  }

  /**
   * Test de création d'un dictionnaire existant, l'ancienne valeur devra être
   * remplacée par la nouvelle
   */
  /*
   * @Test
   * public void createExistingDictTest() throws DictionaryNotFoundException {
   * final String id = "dicExist";
   * final String value = "NewMeta";
   * final Dictionary dictExistant = dictSupport.find(id);
   * dictSupport.addElement(id, value);
   * final Dictionary dict = dictSupport.find(id);
   * Assert.assertNotNull(dict);
   * Assert.assertTrue(!dictExistant.equals(dict));
   * }
   */

  /**
   * Test de modification de métadonnée existante
   */
  /*
   * @Test
   * public void modifyExistingDictTest() throws DictionaryNotFoundException {
   * final String id = "dicExist";
   * final String value = "NewMeta";
   * final Dictionary dictExistant = dictSupport.find(id);
   * dictSupport.addElement(id, value);
   * final Dictionary dict = dictSupport.find(id);
   * Assert.assertNotNull(dict);
   * Assert.assertTrue(!dictExistant.equals(dict));
   * }
   */

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
    dictSupport.deleteElement(id, value, new Date().getTime());
    final Dictionary deletedDict = dictSupport.find(id);
    Assert.assertTrue(deletedDict.getEntries().size() == 0);
  }

  /**
   * Test de supression d'un élément de dictionnaire inexistant
   */

  @Test
  public void deleteColumnNotExistDictTest()
      throws DictionaryNotFoundException {

    final String id = "dicExist";
    final String value1 = "Meta1";
    final String value2 = "Meta2";
    // On créé le dictionnaire avec la valeur Meta1
    dictSupport.addElement(id, value1);
    final Dictionary dictExistant = dictSupport.find(id);
    Assert.assertTrue(dictExistant.getEntries().size() == 1);
    dictSupport.deleteElement(id, value2, new Date().getTime());
    final Dictionary deletedDict = dictSupport.find(id);
    Assert.assertTrue(dictExistant.getEntries().equals(
                                                       deletedDict.getEntries()));
  }
}
