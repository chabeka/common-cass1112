package fr.urssaf.image.commons.dfce.service;

import junit.framework.Assert;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-connection-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class DFCEConnectionServiceTest {

   @Autowired
   private DFCEConnectionService dfceConnectionService;

   @Autowired
   @Qualifier(value="dfceConnection")
   private DFCEConnection dfceConnection;

   private ServiceProvider serviceProvider;

   @After
   public void after() {

      if (serviceProvider != null) {

         serviceProvider.disconnect();

      }
   }

   @Test
   public void DFCEConnectionService_success() {

      serviceProvider = dfceConnectionService.openConnection();

      Assert.assertTrue("le serveur " + dfceConnection.getServerUrl()
            + " doit être up!", serviceProvider.isServerUp());

      Assert.assertTrue("Une session sur " + dfceConnection.getServerUrl()
            + " doit être active!", serviceProvider.isSessionActive());

      Base base = serviceProvider.getBaseAdministrationService().getBase(
            "SAE-INT");

      Assert.assertNotNull("La base SAE-TEST doit être exister!", base);

   }

}
