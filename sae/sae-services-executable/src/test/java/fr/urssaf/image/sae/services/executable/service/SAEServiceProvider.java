package fr.urssaf.image.sae.services.executable.service;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;

/**
 * Fournisseur de singleton des mocks des services du SAE.<br>
 * 
 * 
 */
public final class SAEServiceProvider {

   private static TraitementAsynchroneService traitementService;

   private SAEServiceProvider() {

   }

   /**
    * 
    * @return instance unique de TraitementAsynchroneService
    */
   public static TraitementAsynchroneService getInstanceTraitementAsynchroneService() {

      synchronized (SAEServiceProvider.class) {

         if (traitementService == null) {

            traitementService = EasyMock
                  .createMock(TraitementAsynchroneService.class);

         }

      }

      return traitementService;

   }
}