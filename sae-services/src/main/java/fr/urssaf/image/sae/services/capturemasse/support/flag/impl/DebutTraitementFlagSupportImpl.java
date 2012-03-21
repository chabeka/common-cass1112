/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.impl;

import java.io.File;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.support.flag.DebutTraitementFlagSupport;
import fr.urssaf.image.sae.services.capturemasse.support.flag.model.DebutTraitementFlag;

/**
 * implémentation du support {@link DebutTraitementFlagSupport}
 * 
 */
@Component
public class DebutTraitementFlagSupportImpl implements
      DebutTraitementFlagSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public void writeDebutTraitementFlag(
         DebutTraitementFlag debutTraitementFlag, File ecdeDirectory) {
      // FIXME FBON - Implémentation writeDebutTraitementFlag

   }

}
