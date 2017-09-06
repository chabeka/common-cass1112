package fr.urssaf.image.commons.dfce.service;

import net.docubase.toolkit.service.ServiceProvider;

/**
 * Service de connexion à DFCE
 * 
 * 
 */
public interface DFCEConnectionService {

   /**
    * Ouvre une connexion sur DFCE
    * 
    * @return une instance du toolkit client DFCE
    */
   ServiceProvider openConnection();
}
