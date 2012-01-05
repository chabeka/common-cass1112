package fr.urssaf.image.sae.igc;

import fr.urssaf.image.sae.igc.service.IgcConfigService;
import fr.urssaf.image.sae.igc.service.IgcDownloadService;
import fr.urssaf.image.sae.igc.service.impl.IgcConfigServiceImpl;
import fr.urssaf.image.sae.igc.service.impl.IgcDownloadServiceImpl;

/**
 * Factory d'instaciation des services du module sae-igc
 * 
 * 
 */
public final class IgcServiceFactory {

   private IgcServiceFactory() {

   }

   /**
    * 
    * @return instance de {@link IgcConfigService}
    */
   public static IgcConfigService createIgcConfigService() {

      return new IgcConfigServiceImpl();
   }

   /**
    * 
    * @return instance de {@link IgcDownloadService}
    */
   public static IgcDownloadService createIgcDownloadService() {

      return new IgcDownloadServiceImpl();
   }

}
