/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.service.SaePagmpService;

/**
 * Classe d'implémentation du service {@link SaePagmpService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaePagmpServiceImpl implements SaePagmpService {

   /**
    * {@inheritDoc}
    */
   @Override
   public void createPagmp(Pagmp pagmp) {
      // TODO Auto-generated method stub

   }

}
