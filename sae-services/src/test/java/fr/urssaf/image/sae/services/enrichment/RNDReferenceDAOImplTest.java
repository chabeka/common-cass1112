/**
 * 
 */
package fr.urssaf.image.sae.services.enrichment;

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

/**
 * 
 * 
 */
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

}
