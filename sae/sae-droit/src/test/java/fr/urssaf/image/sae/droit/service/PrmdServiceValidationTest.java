/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaePrmd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class PrmdServiceValidationTest {

  @Autowired
  private PrmdService prmdService;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  @Before
  public void start() throws Exception {
    modeApiCqlSupport.initTables(MODE_API.HECTOR);
  }

  @Test
  public void checkIsPermittedMetasObligatoires() {
    try {
      prmdService.isPermitted(null,
                              new ArrayList<SaePrmd>());

      Assert.fail("Exception attendue");
    } catch (final IllegalArgumentException e) {
      Assert.assertTrue(
                        "le message doit concerner la liste des métadonnées", e
                        .getMessage().contains("liste des métadonnées"));
    }
  }

  @Test
  public void checkIsPermittedPrmdObligatoires() {
    try {
      final List<UntypedMetadata> list = new ArrayList<>();
      list.add(new UntypedMetadata("M", "V"));
      prmdService.isPermitted(list, new ArrayList<SaePrmd>());

      Assert.fail("Exception attendue");
    } catch (final IllegalArgumentException e) {
      Assert.assertTrue("le message doit concerner la liste des prmd", e
                        .getMessage().contains("liste des prmd"));
    }
  }

  @Test
  public void checkCreateLuceneObligatoires() {
    try {
      prmdService.createLucene(StringUtils.EMPTY, new ArrayList<SaePrmd>());

      Assert.fail("Exception attendue");
    } catch (final IllegalArgumentException e) {
      Assert.assertTrue("le message doit concerner la requete", e
                        .getMessage().contains("requete"));
    }
  }

  @Test
  public void checkCreatePrmdObligatoires() {
    try {
      prmdService.createLucene("e", new ArrayList<SaePrmd>());

      Assert.fail("Exception attendue");
    } catch (final IllegalArgumentException e) {
      Assert.assertTrue("le message doit concerner la liste des prmd", e
                        .getMessage().contains("liste des prmd"));
    }
  }

  @Test
  public void checkAddDomaineMetasObligatoires() throws UnexpectedDomainException, InvalidPagmsCombinaisonException {
    try {
      prmdService.addDomaine(null, new ArrayList<SaePrmd>());
      Assert.fail("Exception attendue");
    } catch (final IllegalArgumentException e) {
      Assert.assertTrue(
                        "le message doit concerner la liste des métadonnées", e
                        .getMessage().contains("liste des métadonnées"));
    }
  }

  @Test
  public void checkAddDomainePrmdObligatoires() throws UnexpectedDomainException, InvalidPagmsCombinaisonException {
    try {
      final List<UntypedMetadata> list = new ArrayList<>();
      list.add(new UntypedMetadata("M", "V"));
      prmdService.addDomaine(list, new ArrayList<SaePrmd>());

      Assert.fail("Exception attendue");
    } catch (final IllegalArgumentException e) {
      Assert.assertTrue("le message doit concerner la liste des prmd", e
                        .getMessage().contains("liste des prmd"));
    }
  }
}
