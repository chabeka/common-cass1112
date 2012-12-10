/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;

/**
 * Service de gestion des paramètres
 * 
 */
public interface ParametersService {

   /**
    * Renvoie le paramètre avec le nom passé en argument
    * 
    * @param code
    *           nom du paramètre
    * @return le paramètre de configuration
    * @throws ParameterNotFoundException
    *            exception levée lorsque le paramètre n'est pas trouvé
    */
   Parameter loadParameter(ParameterType code)
         throws ParameterNotFoundException;

   /**
    * Créé ou modifie le paramètre passé en argument
    * 
    * @param parameter
    *           le paramètre à créer ou modifier
    */
   void saveParameter(Parameter parameter);

}
