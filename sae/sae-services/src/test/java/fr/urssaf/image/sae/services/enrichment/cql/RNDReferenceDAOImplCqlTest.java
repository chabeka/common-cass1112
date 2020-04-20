package fr.urssaf.image.sae.services.enrichment.cql;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.service.RndService;
import fr.urssaf.image.sae.services.AbstractServiceCqlTest;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class RNDReferenceDAOImplCqlTest extends AbstractServiceCqlTest {

  private RndService rndService;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @BeforeClass
  public static void beforeClass() throws IOException {
    init = false;

  }

  @Before
  public void before() throws Exception {
    initMetadata();
    modeApiSupport.initTables(ModeGestionAPI.MODE_API.DATASTAX);
  }
  @Test
  public void testActiviteNull() {
    try {
      final String value = rndService.getCodeActivite("1.A.X.X.X");
      Assert.assertTrue("le type d'activité est vide", StringUtils
                        .isBlank(value));

    } catch (final CodeRndInexistantException e) {
      Assert.fail("pas d'exception a lever");
    } catch (final Throwable throwable) {
      Assert.fail("pas d'exception a lever");
    }

  }

  /**
   * Test spécifique sur le type de document des mandats SEPA.<br>
   * <br>
   * Le code RND a été ajouté pour une relivraison en intégration nationale en
   * lot 121111, d'où ce test pour blinder la relivraison.<br>
   * <br>
   * A noter que le code RND a été inclus au SAE AVANT la publication de la
   * version du RND incluant de type de document.<br>
   * <br>
   * L'objectif du test est simplement de s'assurer que le code RND des mandats
   * SEPA est bien pris en compte dans la couche services du SAE.
   * 
   * @throws CodeRndInexistantException
   */
  @Test
  public void testRndMandatSepa() throws CodeRndInexistantException {

    final String codeRnd = "1.2.2.4.12";
    final TypeDocument typeDoc = rndService.getTypeDocument(codeRnd);

    assertEquals("Incohérence entre la DAO et l'objet retourné",
                 "1.2.2.4.12", typeDoc.getCode());
    assertEquals("Le code fonction est incorrect", "1", typeDoc
                 .getCodeFonction());
    assertEquals("Le code activité est incorrect", "2", typeDoc
                 .getCodeActivite());
    assertEquals("Le libellé du code RND est incorret", "MANDAT SEPA",
                 typeDoc.getLibelle());

    // On ne fait pas d'autres vérifications, sinon le test risque d'être

  }

}
