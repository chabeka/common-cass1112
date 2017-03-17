/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.sommaire.batch;

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
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.DocumentType;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.MetadonneeType;

/**
 * Item processor pour convertir un document sommaire d'un modèle objet XML vers
 * un modèle objet métier pour le service de modification en masse
 * 
 */
@Component
public class ConvertMetaDocumentModificationProcessor implements
      ItemProcessor<JAXBElement<DocumentType>, UntypedDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConvertMetaDocumentModificationProcessor.class);

   private static final String PREFIXE_TRC = "ConvertMetaDocumentModificationProcessor.process()";

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument process(final JAXBElement<DocumentType> item)
         throws Exception {

      LOGGER.debug("{} - Début", PREFIXE_TRC);
      LOGGER.debug("{} - Début du mapping de l'objet Jaxb représentant "
            + "le document vers un objet métier UntypedDocument", PREFIXE_TRC);

      final UntypedDocument untypedDoc = new UntypedDocument();

      final List<MetadonneeType> metaDataType = item.getValue()
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

      untypedDoc.setUuid(UUID.fromString(item.getValue().getObjetNumerique().getUUID()));

      LOGGER
            .debug(
                  "{} - Fin du mapping de l'objet Jaxb représentant le sommaire.xml vers un objet métier Sommaire",
                  PREFIXE_TRC);
      return untypedDoc;
   }
}
