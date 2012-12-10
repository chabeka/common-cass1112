/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.support.ParametersSupport;
import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;
import fr.urssaf.image.sae.trace.service.ParametersService;

/**
 * Classe d'implémentation de l'interface {@link ParametersService}. Cette
 * classe est un singleton et peut être accessible via le mécanisme d'injection
 * IOC avec l'annotation @Autowired
 * 
 */
@Service
public class ParametersServiceImpl implements ParametersService {

   @Autowired
   private ParametersSupport support;

   @Autowired
   private JobClockSupport clockSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public Parameter loadParameter(ParameterType code)
         throws ParameterNotFoundException {
      return support.find(code);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void saveParameter(Parameter parameter) {
      support.create(parameter, clockSupport.currentCLock());
   }

}
