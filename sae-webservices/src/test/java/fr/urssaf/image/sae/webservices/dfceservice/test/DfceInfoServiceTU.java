package fr.urssaf.image.sae.webservices.dfceservice.test;

import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.exploitation.service.DfceInfoService;

/**
 * Classe de service d'informations sur l'etat de DFCE.
 * 
 *
 */
@Service
public final class DfceInfoServiceTU implements DfceInfoService {

   /**
    * Retourne le boolean representant la valeur de Dfce.isServerUp
    *  
    * @return si dfce is up 
    */
   public boolean isDfceUp() {
      return true;
   }
   
   
   /**
    * Retourne true si le couple DFCE/Cassandra est actif
    *                 
    * @return boolean                
    */
   public boolean isDfceConsultationUp() {
      
      return true;
   }


   @Override
   public ServiceProvider getDfceServiceProviderAndConnect() {
      return null;
   }

}
