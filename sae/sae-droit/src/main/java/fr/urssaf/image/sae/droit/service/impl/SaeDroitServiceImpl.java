/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.service.SaeDroitService;

/**
 * Classe d'implémentation du service {@link SaeDroitService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
public class SaeDroitServiceImpl implements SaeDroitService {

   /**
    * {@inheritDoc}
    */
   @Override
   public final SaeDroits loadSaeDroits(String idClient, List<String> pagms) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void createContratService(ServiceContract serviceContract,
         List<Pagm> pagms) {
      // TODO Auto-generated method stub

   }

}
