/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.impl;

import java.util.UUID;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.RollbackSupport;

/**
 * implémentation du support {@link RollbackSupport}
 * 
 */
@Component
public class RollbackSupportImpl implements RollbackSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public void rollback(UUID identifiant) {
      // FIXME FBON - implémentation rollback

   }

}
