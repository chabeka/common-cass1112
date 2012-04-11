/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.DocumentType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.MetadonneeType;

/**
 * Item processor pour convertir un document sommaire d'un modèle objet XML vers
 * un modèle objet métier
 * 
 */
@Component
public class ConvertSommaireDocumentProcessor implements
      ItemProcessor<JAXBElement<DocumentType>, UntypedDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConvertSommaireDocumentProcessor.class);

   private static final String PREFIXE_TRC = "ConvertSommaireDocumentProcessor.process()";

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

      String filePath = item.getValue().getObjetNumerique()
            .getCheminEtNomDuFichier();
      filePath = FilenameUtils.separatorsToSystem(filePath);

      untypedDoc.setFilePath(filePath);

      LOGGER
            .debug(
                  "{} - Fin du mapping de l'objet Jaxb représentant le sommaire.xml vers un objet métier Sommaire",
                  PREFIXE_TRC);
      return untypedDoc;
   }
}
