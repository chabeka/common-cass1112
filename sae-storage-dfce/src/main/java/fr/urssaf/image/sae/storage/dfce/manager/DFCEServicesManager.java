package fr.urssaf.image.sae.storage.dfce.manager;

import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import net.docubase.toolkit.service.ServiceProvider;

/**
 * Permet de fabriquer et détruire les services DFCE.
 * 
 * @author akenore
 * 
 */
public interface DFCEServicesManager {
   /**
    * 
    * @return Les services de DFCE.
    * 
    */
   ServiceProvider getDFCEService();

   /**
    * Etablit la connexion avec DFCE, de manière synchronized. Si une connexion
    * a déjà été ouverte, alors on la réutilise s'il n'y a pas eu coupure de
    * cette connexion depuis.
    * 
    * @throws ConnectionServiceEx
    *            Exception levée lorsque la connexion ne s'est pas bien
    *            déroulée.
    * 
    */
   void getConnection() throws ConnectionServiceEx;

   /**
    * Etablit la connexion avec DFCE, de manière synchronized. Si une connexion
    * a déjà été ouverte, alors on la réutilise s'il n'y a pas eu coupure de
    * cette connexion depuis et que l'on ne force pas la reconnexion.
    * 
    * @param forceReconnection
    *           flag permettant de forcer la reconnexion à DFCE, même si la
    *           méthode a détecté que la connexion était toujours active
    * @throws ConnectionServiceEx
    *            Exception levée lorsque la connexion ne s'est pas bien
    *            déroulée.
    */
   void getConnection(boolean forceReconnection) throws ConnectionServiceEx;

   /**
    * 
    * @return True si la connection est active.
    * 
    * 
    */
   boolean isActive();

   /**
    * 
    * Ferme la connexion des services DFCE.
    */
   void closeConnection();
}
