/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.service.SaePagmaService;

/**
 * Classe d'implémentation du service {@link SaePagmaService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaePagmaServiceImpl implements SaePagmaService {

   /**
    * {@inheritDoc}
    */
   @Override
   public void createPagma(Pagma pagma) {
      // TODO Auto-generated method stub

   }

}
