package fr.urssaf.image.sae.format.referentiel.dao.support;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.ReferentielFormatSupportBouchon;



/**
 * 
 * Classe test pour {@link ReferentielFormatSupport} sur les RutimeExceptions
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class RefFormatSupportRuntimeTest {

   @Autowired
   private ReferentielFormatSupportBouchon refFormatSupportBouchon;
   
   @Autowired
   private JobClockSupport jobClock;
   
   @Test
   @Ignore
   public void deleteFailure() {
      
      try {
         
         refFormatSupportBouchon.delete("idFormat", jobClock.currentCLock());
        
         Assert.fail("Une exception ReferentielRuntimeException aurait dû être levée");
      } 
         catch (ReferentielRuntimeException ex) {
            Assert.assertEquals("DELETE - Erreur : Le message de l'exception est incorrect", 
                  "Impossible de supprimer le format", 
                   ex.getMessage());
      }
      
      
   }
   
   
}
