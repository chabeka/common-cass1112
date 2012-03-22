package fr.urssaf.image.sae.storage.dfce.services.support.impl;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.storage.dfce.services.support.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;

@SuppressWarnings("PMD.MethodNamingConventions")
public class InterruptionTraitementSupportImplTest {

   private InterruptionTraitementSupportImpl support;

   private DFCEServicesManager dfceManager;

   private DateTime currentDate;

   private JmxIndicator jmxIndicator;

   @Before
   public void before() {

      dfceManager = EasyMock.createMock(DFCEServicesManager.class);
      support = new InterruptionTraitementSupportImpl(dfceManager);

      // on fixe les delais pour les tentatives de reconnexion après la première
      // à 1 seconde!
      support.setDelay(1000);

      // on fixe la date courante à 01/01/1999 à 02h00 et 1 seconde!
      DateTimeFormatter formatter = DateTimeFormat
            .forPattern("dd-MM-yyyy HH:mm:ss");

      currentDate = DateTime.parse("01-01-1999 02:00:01", formatter);

      // on instancie jmxIndicator
      jmxIndicator = new JmxIndicator();

   }

   @After
   public void after() {

      EasyMock.reset(dfceManager);
   }

   private void openConnexion(int failures) throws ConnectionServiceEx {

      // dfceManager.closeConnection();

      dfceManager.getConnection();

      if (failures > 0) {

         EasyMock.expectLastCall().andThrow(new ConnectionServiceEx()).times(
               failures);

         dfceManager.getConnection();

      }

      EasyMock.replay(dfceManager);
   }

   @Test
   public void interruption_success_first_tentative()
         throws ConnectionServiceEx, InterruptionTraitementException {

      assertInterruptionSuccess(4, 0);

   }

   @Test
   public void interruption_success_last_tentative()
         throws ConnectionServiceEx, InterruptionTraitementException {

      assertInterruptionSuccess(2, 1);

   }

   @Test
   public void interruption_success_after_3tentatives()
         throws ConnectionServiceEx, InterruptionTraitementException {

      assertInterruptionSuccess(4, 2);

   }

   private void assertInterruptionSuccess(int tentatives, int failures)
         throws ConnectionServiceEx, InterruptionTraitementException {

      openConnexion(failures);

      String start = "02:00:00";

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setStart(start);
      interruptionConfig.setDelay(2);
      interruptionConfig.setTentatives(tentatives);

      support.interruption(currentDate, interruptionConfig, jmxIndicator);

      // on doit vérifier qu'on ferme bien la connexion
      EasyMock.verify(dfceManager);
   }

   @Test
   public void interruption_failure_with_2tentatives()
         throws ConnectionServiceEx {

      openConnexion(5);

      String start = "02:00:00";

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setStart(start);
      interruptionConfig.setDelay(2);
      interruptionConfig.setTentatives(2);

      try {

         support.interruption(currentDate, interruptionConfig, jmxIndicator);

         Assert.fail("On s'attend à lever une exception de type "
               + InterruptionTraitementException.class);

      } catch (InterruptionTraitementException e) {

         Assert
               .assertEquals(
                     "le message de l'exception est inattendu",
                     "Après une déconnexion DFCE programmée à 02:00:00 il est impossible de reprendre le traitement après 2 secondes et 2 tentatives.",
                     e.getMessage());
      }

   }

   @Test
   public void hasInterrupted_true() {

      String date1 = "25-12-1999 03:00:02";

      Assert.assertTrue("le traitement doit être interrompue pour " + date1,
            hasInterrupted(date1));

   }

   @Test
   public void hasInterrupted_false() {

      String date1 = "25-12-1999 02:59:59";

      Assert.assertFalse("le traitement ne doit pas être interrompue pour "
            + date1, hasInterrupted(date1));

      String date2 = "25-12-1999 03:02:00";

      Assert.assertFalse("le traitement ne doit pas être interrompue pour "
            + date2, hasInterrupted(date2));
   }

   private boolean hasInterrupted(String textDate) {

      DateTime currentDate = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss")
            .parseDateTime(textDate);

      String startTime = "03:00:00";
      int delay = 120;

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setTentatives(2);
      interruptionConfig.setStart(startTime);
      interruptionConfig.setDelay(delay);

      return support.hasInterrupted(currentDate, interruptionConfig);

   }

}
