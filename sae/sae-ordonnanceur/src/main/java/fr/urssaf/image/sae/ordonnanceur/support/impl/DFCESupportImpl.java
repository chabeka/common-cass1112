package fr.urssaf.image.sae.ordonnanceur.support.impl;

import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;

/**
 * Support pour les traitements en lien avec DFCE
 * 
 * 
 */
@Component
public class DFCESupportImpl implements DFCESupport {

   private final DFCEConnectionService dfceService;

   /**
    * 
    * @param dfceService
    *           service de connexion à DFCE
    */
   @Autowired
   public DFCESupportImpl(DFCEConnectionService dfceService) {

      this.dfceService = dfceService;
   }

   /**
    * L'implémentation est un copié/collé de l'artefact sae-webservices dans le
    * composant exploitation
    * 
    * {@inheritDoc}
    */
   public final boolean isDfceUp() {

      boolean isServUp;

      try {

         ServiceProvider serviceProvider = dfceService.openConnection();

         try {

            isServUp = serviceProvider.isServerUp();

         } finally {

            serviceProvider.disconnect();

         }

      } catch (RuntimeException e) {

         isServUp = false;
      }

      return isServUp;
   }

}
