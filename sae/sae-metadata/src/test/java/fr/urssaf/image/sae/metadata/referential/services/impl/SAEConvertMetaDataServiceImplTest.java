/**
 * 
 */
package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class SAEConvertMetaDataServiceImplTest {

  @Autowired
  private SAEConvertMetadataService service;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  @Before
  public void setUp() {
    modeApiCqlSupport.initTables(MODE_API.HECTOR);
  }

  @Test
  public void testConversion() throws LongCodeNotFoundException {

    final String[] tabLongCode = new String[] { "Siret" };

    final Map<String, String> shortList = service.longCodeToShortCode(Arrays
                                                                      .asList(tabLongCode));

    Assert.assertNotNull("la map n'est pas nulle", shortList);

    Assert.assertTrue("un seul élément doit être retourné",
                      shortList.size() == 1);

    Assert.assertEquals("Le code court doit valoir srt pour Siret", shortList
                        .keySet().toArray()[0], "srt");

  }

  @Test
  public void testConversionFailCodeNotExists() {

    final String[] tabLongCode = new String[] { "codeInexistantEnBase" };

    try {
      service.longCodeToShortCode(Arrays.asList(tabLongCode));
      Assert.fail("une exception doit être levée");

    } catch (final LongCodeNotFoundException e) {
      Assert.assertNotNull(e.getListCode());
      Assert.assertTrue("un élément dans la liste des éléments non trouvés",
                        e.getListCode().size() == 1);
      Assert.assertEquals("L'élément doit être codeInexistantEnBase",
                          "codeInexistantEnBase", e.getListCode().get(0));

    }
  }
}
