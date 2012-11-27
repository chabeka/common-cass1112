/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;

/**
 * Classe d'implémentation du support {@link RegTechniqueService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class RegTechniqueServiceImpl implements RegTechniqueService {

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<TraceRegTechniqueIndex> lecture(Date dateDebut, Date dateFin,
         int limite) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final TraceRegTechnique lecture(UUID identifiant) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void purge(Date dateDebut, Date dateFin) {
      // TODO Auto-generated method stub

   }

}
