/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.service.SaePrmdService;

/**
 * Classe d'implémentation du service {@link SaePrmdService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaePrmdServiceImpl implements SaePrmdService {

   /**
    * {@inheritDoc}
    */
   @Override
   public void createPrmd(Prmd prmd) {
      // TODO Auto-generated method stub
   }

}
