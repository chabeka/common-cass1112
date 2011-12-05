package fr.urssaf.image.sae.storage.dfce.services.support.impl;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.services.support.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

@SuppressWarnings("PMD.MethodNamingConventions")
public class InterruptionTraitementSupportImplTest {

   private InterruptionTraitementSupportImpl support;

   private StorageServiceProvider storageProvider;

   private Date currentDate;

   @Before
   public void before() throws ParseException {

      storageProvider = EasyMock.createMock(StorageServiceProvider.class);
      support = new InterruptionTraitementSupportImpl(storageProvider);

      // on fixe les delais pour les tentatives de reconnexion après la première
      // à 1 seconde!
      support.setDelay(1);

      // on fixe la date courante à 01/01/1999 à 02h00 et 1 seconde!
      currentDate = DateUtils.parseDate("01-01-1999 02:00:01",
            new String[] { "dd-MM-yyyy HH:mm:ss" });

   }

   @After
   public void after() {

      EasyMock.reset(storageProvider);
   }

   private void openConnexion(int tentatives) throws ConnectionServiceEx {

      storageProvider.closeConnexion();

      storageProvider.openConnexion();

      EasyMock.expectLastCall().andThrow(new ConnectionServiceEx()).times(
            tentatives - 1);

      storageProvider.openConnexion();

      EasyMock.replay(storageProvider);
   }

   @Test
   public void interruption_success_after_2tentatives()
         throws ConnectionServiceEx {

      assertInterruptionSuccess(2);

   }

   @Test
   public void interruption_success_after_4tentatives()
         throws ConnectionServiceEx {

      assertInterruptionSuccess(4);

   }

   private void assertInterruptionSuccess(int tentatives)
         throws ConnectionServiceEx {

      openConnexion(2);

      String start = "02:00:00";
      support.interruption(currentDate, start, 2, tentatives);

      // on doit vérifier qu'on ferme bien la connexion
      EasyMock.verify(storageProvider);
   }

   @Test
   public void interruption_failure_after2Tentatives()
         throws ConnectionServiceEx {

      openConnexion(5);

      String start = "02:00:00";

      try {

         support.interruption(currentDate, start, 2, 2);

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

}
