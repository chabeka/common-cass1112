package fr.urssaf.image.sae.services.enrichment;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.enrichment.dao.RNDReferenceDAO;
import fr.urssaf.image.sae.services.enrichment.xml.model.TypeDocument;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class RNDReferenceDAOImplTest {

   private RNDReferenceDAO rndReferenceDAO;

   @Test
   public void testActiviteNull() {
      try {
         String value = rndReferenceDAO.getActivityCodeByRnd("1.A.X.X.X");
         Assert.assertTrue("le type d'activité est vide", StringUtils
               .isBlank(value));

      } catch (ReferentialRndException e) {
         Assert.fail("pas d'exception a lever");
      } catch (UnknownCodeRndEx e) {
         Assert.fail("pas d'exception a lever");
      } catch (Throwable throwable) {
         Assert.fail("pas d'exception a lever");
      }

   }

   /**
    * @param rndReferenceDAO
    *           the rndReferenceDAO to set
    */
   @Autowired
   public final void setRndReferenceDAO(
         @Qualifier("rndReferenceDAO") RNDReferenceDAO rndReferenceDAO) {
      this.rndReferenceDAO = rndReferenceDAO;
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
    */
   @Test
   public void testRndMandatSepa() throws ReferentialRndException,
         UnknownCodeRndEx {

      String codeRnd = "1.2.2.4.12";
      TypeDocument typeDoc = rndReferenceDAO.getTypeDocument(codeRnd);

      assertEquals("Incohérence entre la DAO et l'objet retourné",
            "1.2.2.4.12", typeDoc.getRndCode());
      assertEquals("Le code fonction est incorrect", "1", typeDoc
            .getFonctionCode());
      assertEquals("Le code activité est incorrect", "2", typeDoc
            .getActivityCode());
      assertEquals("Le libellé du code RND est incorret", "MANDAT SEPA",
            typeDoc.getRndDescription());

      // On ne fait pas d'autres vérifications, sinon le test risque d'être

   }

}
