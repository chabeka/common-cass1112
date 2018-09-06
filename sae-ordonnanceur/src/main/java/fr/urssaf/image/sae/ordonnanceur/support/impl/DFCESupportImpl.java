package fr.urssaf.image.sae.ordonnanceur.support.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;

/**
 * Support pour les traitements en lien avec DFCE
 *
 *
 */
@Component
public class DFCESupportImpl implements DFCESupport {

   private final DFCEServices dfceServices;

   /**
    *
    * @param dfceService
    *           service de connexion à DFCE
    */
   @Autowired
   public DFCESupportImpl(final DFCEServices dfceServices) {

      this.dfceServices = dfceServices;
   }

   /**
    * L'implémentation est un copié/collé de l'artefact sae-webservices dans le
    * composant exploitation
    *
    * {@inheritDoc}
    */
   @Override
   public final boolean isDfceUp() {

      boolean isServUp;

      try {
         isServUp = dfceServices.isServerUp();

      } catch (final RuntimeException e) {

         isServUp = false;
      }

      return isServUp;
   }

}
