/**
 * 
 */
package fr.urssaf.image.sae.metadata.referential.services.impl.cql;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.services.SAEControlMetadataService;

/**
 * 
 * 
 */
public class SAEControlMetadaCqlServiceImplTest extends AbstractMetadataCqlTest {

  @Autowired
  private SAEControlMetadataService service;


  @Test
  public void metaDataExists() {

    try {
      service.controlLongCodeExist(Arrays.asList(new String[] { "Siret" }));
    } catch (final ReferentialException e) {
      Assert.fail("pas d'exception à lever");
    } catch (final LongCodeNotFoundException e) {
      Assert.fail("pas d'exception à lever");
    }

  }

  @Test
  public void metaDataExistsButRequestedTwice() {

    try {
      service.controlLongCodeExist(Arrays.asList(new String[] { "Siret", "Siret", "Siret" }));
    } catch (final ReferentialException e) {
      Assert.fail("pas d'exception à lever");
    } catch (final LongCodeNotFoundException e) {
      Assert.fail("pas d'exception à lever");
    }

  }

  @Test
  public void metaDataNotExists() {

    try {
      service.controlLongCodeExist(Arrays
                                   .asList(new String[] { "codeInexistantEnBase" }));
      Assert.fail("une exception doit être levée");
    } catch (final ReferentialException e) {
      Assert.fail("pas d'exception à lever");
    } catch (final LongCodeNotFoundException e) {
      Assert.assertNotNull(e.getListCode());
      Assert.assertTrue("un élément dans la liste des éléments non trouvés",
                        e.getListCode().size() == 1);
      Assert.assertEquals("L'élément doit être codeInexistantEnBase",
                          "codeInexistantEnBase", e.getListCode().get(0));
    }
  }

  @Test
  public void metaDataNotExistsButRequestedTwice() {

    try {
      service.controlLongCodeExist(Arrays
                                   .asList(new String[] { "codeInexistantEnBase", "codeInexistantEnBase", "codeInexistantEnBase" }));
      Assert.fail("une exception doit être levée");
    } catch (final ReferentialException e) {
      Assert.fail("pas d'exception à lever");
    } catch (final LongCodeNotFoundException e) {
      Assert.assertNotNull(e.getListCode());
      Assert.assertTrue("un élément dans la liste des éléments non trouvés",
                        e.getListCode().size() == 1);
      Assert.assertEquals("L'élément doit être codeInexistantEnBase",
                          "codeInexistantEnBase", e.getListCode().get(0));
    }

  }

  @Test
  public void testMetaDataConsultable() {

    try {
      service.controlLongCodeIsAFConsultation(Arrays
                                              .asList(new String[] { "Siret" }));
    } catch (final ReferentialException e) {
      Assert.fail("pas d'exception à lever");
    } catch (final LongCodeNotFoundException e) {
      Assert.fail("pas d'exception à lever");
    }
  }

  @Test
  public void testMetaDataConsultableButRequestedTwice() {

    try {
      service.controlLongCodeIsAFConsultation(Arrays
                                              .asList(new String[] { "Siret", "Siret", "Siret" }));
    } catch (final ReferentialException e) {
      Assert.fail("pas d'exception à lever");
    } catch (final LongCodeNotFoundException e) {
      Assert.fail("pas d'exception à lever");
    }
  }

  @Test
  public void testMetaDataNonConsultable() {
    try {
      service.controlLongCodeIsAFConsultation(Arrays
                                              .asList(new String[] { "StartPage" }));
      Assert.fail("une exception doit être levée");
    } catch (final ReferentialException e) {
      Assert.fail("pas d'exception à lever");
    } catch (final LongCodeNotFoundException e) {
      Assert.assertNotNull(e.getListCode());
      Assert.assertTrue("un élément dans la liste des éléments non trouvés",
                        e.getListCode().size() == 1);
      Assert.assertEquals("L'élément doit être StartPage", "StartPage", e
                          .getListCode().get(0));
    }
  }

  @Test
  public void testMetaDataNonConsultableButRequestedTwice() {
    try {
      service.controlLongCodeIsAFConsultation(Arrays
                                              .asList(new String[] { "StartPage", "StartPage", "StartPage" }));
      Assert.fail("une exception doit être levée");
    } catch (final ReferentialException e) {
      Assert.fail("pas d'exception à lever");
    } catch (final LongCodeNotFoundException e) {
      Assert.assertNotNull(e.getListCode());
      Assert.assertTrue("un élément dans la liste des éléments non trouvés",
                        e.getListCode().size() == 1);
      Assert.assertEquals("L'élément doit être StartPage", "StartPage", e
                          .getListCode().get(0));
    }
  }

}
