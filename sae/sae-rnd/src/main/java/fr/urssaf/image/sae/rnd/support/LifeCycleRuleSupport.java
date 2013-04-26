package fr.urssaf.image.sae.rnd.support;

import net.docubase.toolkit.model.reference.LifeCycleLengthUnit;
import net.docubase.toolkit.model.reference.LifeCycleRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;

import fr.urssaf.image.sae.rnd.dao.support.ServiceProviderSupport;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * Service de manipulation du cycle de vie
 * 
 * 
 */
@Component
public class LifeCycleRuleSupport {

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(LifeCycleRuleSupport.class);

   /**
    * Met à jour un enregistrement dans la CF LifeCycleRule
    * 
    * @param typeDoc
    *           Type de document à mettre à jour dans LifeCycleRule
    * @param serviceProviderSupport
    *           Support pour la gestion de la connexion à DFCE
    */
   public final void updateLifeCycleRule(TypeDocument typeDoc,
         ServiceProviderSupport serviceProviderSupport) {

      String trcPrefix = "rechercherJournauxDocument";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      LOGGER.debug("{} - Code du type de doc : {}", new String[] { trcPrefix,
            typeDoc.getCode() });

      try {

         LifeCycleRule lifeCycleRule = serviceProviderSupport
               .getStorageAdministrationService().getLifeCycleRule(
                     typeDoc.getCode());

         // Si le code n'existe pas
         if (lifeCycleRule == null) {
            LOGGER.info("{} - Ajout du code : {}", new String[] { trcPrefix,
                  typeDoc.getCode() });

            serviceProviderSupport
                  .getStorageAdministrationService()
                  .createNewLifeCycleRule(typeDoc.getCode(),
                        typeDoc.getDureeConservation(), LifeCycleLengthUnit.DAY);

         } else {
            // Si le code existe déjà et que la durée de conservation est
            // différente
            if (typeDoc.getDureeConservation() != lifeCycleRule
                  .getLifeCycleLength()) {

               serviceProviderSupport.getStorageAdministrationService()
                     .updateLifeCycleRule(typeDoc.getCode(),
                           typeDoc.getDureeConservation(),
                           LifeCycleLengthUnit.DAY);
               LOGGER
                     .warn(
                           "{} - La durée de conservation du code {} a été modifiée !",
                           new String[] { trcPrefix, typeDoc.getCode() });
            }
         }
      } catch (ObjectAlreadyExistsException objectExist) {
         LOGGER.warn("{} - Le code {} exite déjà !", new String[] { trcPrefix,
               typeDoc.getCode() });
      }

      LOGGER.debug(FIN_LOG, trcPrefix);

   }

}
