package fr.urssaf.image.sae.rnd.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.rnd.dao.support.ServiceProviderSupport;
import fr.urssaf.image.sae.rnd.exception.DfceRuntimeException;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * Classe permettant la mise à jour du cycle de vie des documents dans DFCE
 * 
 * 
 */
@Component
public class DfceSupport {

   @Autowired
   private ServiceProviderSupport serviceProviderSupport;
   
   @Autowired
   private LifeCycleRuleSupport lifeCycleRuleSupport;
   
   /**
    * Met à jour toute la CF LifeCycleRule de DFCE
    * @param listeTypeDocs Liste des types de document à mettre à jour
    * @throws DfceRuntimeException Exception levée lors de la mise à jour de la bdd DFCE 
    */
   public final void updateLifeCycleRule(List<TypeDocument> listeTypeDocs) throws DfceRuntimeException {
      
      try {
         serviceProviderSupport.connect();
      
         for (TypeDocument typeDocument : listeTypeDocs) {
            lifeCycleRuleSupport.updateLifeCycleRule(typeDocument, serviceProviderSupport);
         }
      
         serviceProviderSupport.disconnect();
      } catch (Exception e) {
         throw new DfceRuntimeException(e.getMessage(), e.getCause());
      }
      
   }
   
}
