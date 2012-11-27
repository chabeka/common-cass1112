/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;

/**
 * Classe d'implémentation du support {@link RegSecuriteService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class RegSecuriteServiceImpl implements RegSecuriteService {

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<TraceRegSecuriteIndex> lecture(Date dateDebut, Date dateFin,
         int limite) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final TraceRegSecurite lecture(UUID identifiant) {
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
