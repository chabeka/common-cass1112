package fr.urssaf.image.sae.igc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.urssaf.image.sae.igc.service.impl.IgcConfigServiceImpl;
import fr.urssaf.image.sae.igc.service.impl.IgcDownloadServiceImpl;

public class IgcServiceFactoryTest {

   @Test
   public void createIgcConfigService() {

      assertEquals("ne renvoie pas la bonne implémentation",
            IgcConfigServiceImpl.class, IgcServiceFactory.createIgcConfigService()
                  .getClass());
   }

   @Test
   public void createIgcDownloadService() {

      assertEquals("ne renvoie pas la bonne implémentation",
            IgcDownloadServiceImpl.class, IgcServiceFactory.createIgcDownloadService()
                  .getClass());
   }
}
