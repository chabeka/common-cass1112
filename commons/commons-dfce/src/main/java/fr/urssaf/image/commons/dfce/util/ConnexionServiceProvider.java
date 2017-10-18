/**
 * 
 */
package fr.urssaf.image.commons.dfce.util;

import java.util.Hashtable;

import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.exception.DFCEConnectionServiceException;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;

/**
 * Provider du service de connexion de DFCe
 */
@Component
public class ConnexionServiceProvider {

   private final Hashtable<DFCEConnection, ServiceProvider> serviceProviderMap = new Hashtable<DFCEConnection, ServiceProvider>();

   /**
    * Methode permettant d'ajouter un service de connection DFCe dans la Map.
    * 
    * @param cnxParameters
    *           Parametre de connection
    * @param serviceProvider
    *           Service provider
    * 
    * @throws DFCEConnectionServiceException
    *            @{@link DFCEConnectionServiceException}
    */
   public void addServiceProvider(DFCEConnection cnxParameters,
         ServiceProvider serviceProvider) throws DFCEConnectionServiceException {
      String prefix = "addServiceProvider";
      if (serviceProvider == null || cnxParameters == null) {
         throw new DFCEConnectionServiceException(
               String.format(
                     "{} - Le service provider ou les paramètres de connection ne peuvent être null",
                     prefix));
      }

      if (!serviceProviderMap.contains(cnxParameters)) {
         serviceProviderMap.put(cnxParameters, serviceProvider);
      }
   }

   /**
    * Methode permettant de supprimer un service de connection DFCe dans la Map.
    */
   public void removeServiceProvider(DFCEConnection cnxParameters) {
      if (cnxParameters != null) {
         serviceProviderMap.remove(cnxParameters);
      }
   }

   /**
    * 
    * Methode permettant de récupérer le service de connection DFCe dans la Map
    * à partir des parametres de connection.
    * 
    * @param cnxParameters
    *           Parametre de connection
    * @return le service de connection à DFCE.
    */
   public ServiceProvider getServiceProviderByConnectionParams(
         DFCEConnection cnxParameters) {
      if (cnxParameters != null) {
         return serviceProviderMap.get(cnxParameters);
      } else {
         return null;
      }
   }

}
