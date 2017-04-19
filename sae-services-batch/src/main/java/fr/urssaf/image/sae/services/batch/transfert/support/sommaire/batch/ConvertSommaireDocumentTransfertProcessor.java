package fr.urssaf.image.sae.services.batch.transfert.support.sommaire.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.DocumentTypeMultiAction;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.MetadonneeType;

/**
 * Processor permettant la conversion du sommaire en un Bean utilisable
 * 
 *
 */
@Component
public class ConvertSommaireDocumentTransfertProcessor implements
      ItemProcessor<JAXBElement<DocumentTypeMultiAction>, UntypedDocument> {

   /**
    * Logger
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConvertSommaireDocumentTransfertProcessor.class);

   private static final String PREFIXE_TRC = "convertSommaireDocumentTransfertProcessor.process()";

   @Override
   public UntypedDocument process(
         final JAXBElement<DocumentTypeMultiAction> itemTransfert)
         throws Exception {

      LOGGER.debug("{} - Début", PREFIXE_TRC);
      LOGGER.debug("{} - Début du mapping de l'objet Jaxb représentant "
            + "le document vers un objet métier UntypedDocument", PREFIXE_TRC);

      final UntypedDocument untypedDoc = new UntypedDocument();

      untypedDoc.setUuid(UUID.fromString(itemTransfert.getValue()
            .getObjetNumerique().getUUID()));

      if (itemTransfert.getValue().getMetadonnees() != null) {
         if (itemTransfert.getValue().getMetadonnees().getMetadonnee() != null
               || !itemTransfert.getValue().getMetadonnees().getMetadonnee()
                     .isEmpty()) {
            final List<MetadonneeType> metaDataType = itemTransfert.getValue()
                  .getMetadonnees().getMetadonnee();
            final List<UntypedMetadata> listUM = new ArrayList<UntypedMetadata>();

            UntypedMetadata untypedMetadata;
            for (MetadonneeType metadonneeType : metaDataType) {
               untypedMetadata = new UntypedMetadata();
               untypedMetadata.setLongCode(metadonneeType.getCode());
               untypedMetadata.setValue(metadonneeType.getValeur());
               listUM.add(untypedMetadata);
            }
            untypedDoc.setUMetadatas(listUM);
         }
      } else {
         untypedDoc.setUMetadatas(new ArrayList<UntypedMetadata>());
      }

      untypedDoc.setBatchActionType(itemTransfert.getValue().getTypeAction()
            .value());

      LOGGER.debug(
            "{} - Fin du mapping de l'objet Jaxb représentant le sommaire.xml vers un objet métier Sommaire",
            PREFIXE_TRC);

      return untypedDoc;
   }

}
