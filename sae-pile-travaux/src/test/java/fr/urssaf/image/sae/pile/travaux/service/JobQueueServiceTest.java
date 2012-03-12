package fr.urssaf.image.sae.pile.travaux.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class JobQueueServiceTest {

   @Autowired
   private JobQueueService jobQueueService;

   @Test
   public void addJob_success() {

   }

   @Test
   public void reserveJob_success() throws JobDejaReserveException,
         JobInexistantException {

   }

   @Test
   public void reserveJob_failure_jobDejaReserveException()
         throws JobDejaReserveException, JobInexistantException {

   }

   @Test
   public void reserveJob_failure_jobInexistantException()
         throws JobDejaReserveException, JobInexistantException {

   }

   @Test
   public void startingJob_success() {

   }

   @Test
   public void endingJob_success() {

   }

}
