package fr.urssaf.image.sae.commons.support;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-commons-test.xml" })
public class ParametersSupportTest {

   @Autowired
   private ParametersSupport parametersSupport;

@Test(expected = ParameterNotFoundException.class)
   public void testParametreinexistant() throws ParameterNotFoundException {

      parametersSupport.find(ParameterType.JOURNALISATION_EVT_DATE,
            ParameterRowType.TRACABILITE);
      Assert.fail("une exception est attendue");
   }

}
