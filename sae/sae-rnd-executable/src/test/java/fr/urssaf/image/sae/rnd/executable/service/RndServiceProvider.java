package fr.urssaf.image.sae.rnd.executable.service;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.rnd.service.MajCorrespondancesService;
import fr.urssaf.image.sae.rnd.service.MajRndService;

/**
 * Fournisseur de singleton des mocks des services du SAE.<br>
 * 
 * 
 */
public final class RndServiceProvider {

   private static MajRndService majRndService;
   private static MajCorrespondancesService majCorrespondancesService;

   private RndServiceProvider() {

   }

   /**
    * 
    * @return instance unique de TraitementAsynchroneService
    */
   public static MajRndService getInstanceMajRndService() {

      synchronized (RndServiceProvider.class) {

         if (majRndService == null) {

            majRndService = EasyMock.createMock(MajRndService.class);

         }

      }

      return majRndService;

   }
   
   /**
    * 
    * @return instance unique de TraitementAsynchroneService
    */
   public static MajCorrespondancesService getInstanceMajCorrespondancesService() {

      synchronized (RndServiceProvider.class) {

         if (majCorrespondancesService == null) {

            majCorrespondancesService = EasyMock.createMock(MajCorrespondancesService.class);

         }

      }

      return majCorrespondancesService;

   }
}
