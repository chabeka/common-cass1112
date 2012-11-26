/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;

/**
 * Classe d'implémentation du support {@link DispatcheurService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC avec l'annotation @Autowired
 * 
 */
@Service
public class DispatcheurServiceImpl implements DispatcheurService {

   /**
    * {@inheritDoc}
    */
   @Override
   public void ajouterTrace(TraceToCreate trace) {
      // TODO Auto-generated method stub

   }

}
