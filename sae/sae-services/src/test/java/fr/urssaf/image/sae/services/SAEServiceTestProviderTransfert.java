package fr.urssaf.image.sae.services;

import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;

/**
 * Service pour fournir des méthodes communes pour les tests de l'artefact<br>
 * <br>
 * La classe peut-être injecté par {@link Autowired}
 * 
 * 
 */
public class SAEServiceTestProviderTransfert {


   /**
    * initialise la façade des services de sae-storage
    * 
    * @param dfceConnection
    *           connection à DFCE
    * @throws ConnectionServiceEx
    *            une exception est levée lors de l'ouverture de la connexion
    */
   public static SAEServiceTestProvider createSAEServiceTestProviderTransfert(DFCEConnection dfceConnectionTransfert) 
         throws ConnectionServiceEx {
      return new SAEServiceTestProvider(dfceConnectionTransfert);
   }
}
