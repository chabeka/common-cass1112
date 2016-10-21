package fr.urssaf.image.sae.test.divers.parameters;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-local.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-giin69-gns.xml" })
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-prod.xml" })
public class ParametersServiceTest {

   @Autowired
   private ParametersService parametersService; 
   
   @Test
   public void getInfoJournalEvt() throws ParameterNotFoundException {
      System.out.println("Journalisation en cours : " + parametersService.isJournalisationEvtIsRunning());
      System.out.println("Hash precedent : " + parametersService.getJournalisationEvtHashJournPrec());
      System.out.println("Id journal precedent : " + parametersService.getJournalisationEvtIdJournPrec());
   }
   
   @Test
   @Ignore("permet de remettre le job a l'etat à lancer")
   public void updateJournalEvtRunning() {
      parametersService.setJournalisationEvtIsRunning(Boolean.FALSE);
   }
   
   @Test
   public void getInfoPurgeEvt() throws ParameterNotFoundException {
      System.out.println("Purge des evenements en cours : " + parametersService.isPurgeEvtIsRunning());
      System.out.println("Date last purge : " + parametersService.getPurgeEvtDate());
   }
}
