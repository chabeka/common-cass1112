package fr.urssaf.image.sae.rnd.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.docubase.dfce.commons.LifeCycleEndAction;
import com.docubase.dfce.exception.ObjectAlreadyExistsException;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import net.docubase.toolkit.model.reference.LifeCycleLengthUnit;
import net.docubase.toolkit.model.reference.LifeCycleRule;
import net.docubase.toolkit.model.reference.LifeCycleStep;

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
    * @param dfceServices
    *           Accès aux services DFCE
    */
   public final void updateLifeCycleRule(final TypeDocument typeDoc,
                                         final DFCEServices dfceServices) {

      final String trcPrefix = "updateLifeCycleRule";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      LOGGER.debug("{} - Code du type de doc : {}", new String[] { trcPrefix,
                                                                   typeDoc.getCode() });

      try {

         final LifeCycleRule lifeCycleRule = dfceServices.getLifeCycleRule(
                                                                           typeDoc.getCode());

         // Si le code n'existe pas
         if (lifeCycleRule == null) {
            LOGGER.info("{} - Ajout du code : {}", new String[] { trcPrefix,
                                                                  typeDoc.getCode() });

            final LifeCycleRule newRule = new LifeCycleRule();
            newRule.setDocumentType(typeDoc.getCode());
            // Depuis DFCe 1.7.0, le cycle de vie peut comporter des etapes
            // Coté Ged Nationale, nous n'en aurons qu'une seule
            final LifeCycleStep etape = new LifeCycleStep();
            etape.setLength(typeDoc.getDureeConservation());
            etape.setUnit(LifeCycleLengthUnit.DAY);
            etape.setEndAction(LifeCycleEndAction.DELETE);
            newRule.addStep(etape);

            dfceServices.createNewLifeCycleRule(newRule);

         } else {
            // Si le code existe déjà et que la durée de conservation est
            // différente
            LOGGER.debug("{} - Le code {} existe déjà", new String[] {
                                                                      trcPrefix, typeDoc.getCode() });
            // Depuis DFCe 1.7.0, le cycle de vie peut comporter des etapes
            // Coté Ged Nationale, nous n'en aurons qu'une seule
            final int dureeLifeCycleRule = lifeCycleRule.getSteps().get(0).getLength();
            final int dureeTypeDoc = typeDoc.getDureeConservation();

            if (dureeTypeDoc == dureeLifeCycleRule) {
               LOGGER.debug(
                            "{} - La durée de conservation du code {} est inchangée",
                            new String[] { trcPrefix, typeDoc.getCode() });
            } else {

               final LifeCycleRule ruleToUpdate = new LifeCycleRule();
               ruleToUpdate.setDocumentType(typeDoc.getCode());
               // Depuis DFCe 1.7.0, le cycle de vie peut comporter des etapes
               // Coté Ged Nationale, nous n'en aurons qu'une seule
               final LifeCycleStep etape = new LifeCycleStep();
               etape.setLength(dureeTypeDoc);
               etape.setUnit(LifeCycleLengthUnit.DAY);
               etape.setEndAction(LifeCycleEndAction.DELETE);
               ruleToUpdate.addStep(etape);

               dfceServices.updateLifeCycleRule(ruleToUpdate);
               LOGGER
               .info(
                     "{} - La durée de conservation du code {} a été modifiée ({} => {}) !",
                     new String[] { trcPrefix, typeDoc.getCode(),
                                    Integer.toString(dureeLifeCycleRule),
                                    Integer.toString(dureeTypeDoc) });
            }
         }
      } catch (final ObjectAlreadyExistsException objectExist) {
         LOGGER.warn("{} - Le code {} exite déjà !", new String[] { trcPrefix,
                                                                    typeDoc.getCode() });
      }

      LOGGER.debug(FIN_LOG, trcPrefix);

   }

}
