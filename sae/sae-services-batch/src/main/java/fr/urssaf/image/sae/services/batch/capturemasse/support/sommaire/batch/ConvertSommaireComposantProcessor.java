/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.batch;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedVirtualDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.model.SaeComposantVirtuelType;
import fr.urssaf.image.sae.services.batch.capturemasse.model.SaeListVirtualReferenceFile;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.MetadonneeType;

/**
 * ItemProcessor permettant de transformer un objet <b>composant</b> d'un modèle
 * XML vers un objet {@link UntypedVirtualDocument}
 * 
 */
@Component
public class ConvertSommaireComposantProcessor
      implements
      ItemProcessor<JAXBElement<SaeComposantVirtuelType>, UntypedVirtualDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConvertSommaireComposantProcessor.class);

   @Autowired
   private SaeListVirtualReferenceFile listVirtualReferenceFile;

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedVirtualDocument process(
         JAXBElement<SaeComposantVirtuelType> item) throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      final UntypedVirtualDocument untypedDoc = new UntypedVirtualDocument();

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
      untypedDoc.setuMetadatas(listUM);

      untypedDoc.setIndex(item.getValue().getIndex());
      untypedDoc.setStartPage(item.getValue().getNumeroPageDebut());
      untypedDoc.setEndPage(item.getValue().getNumeroPageDebut()
            + item.getValue().getNombreDePages() - 1);
      untypedDoc.setReference(listVirtualReferenceFile.get(item.getValue()
            .getIndex()));

      LOGGER.debug("{} - fin", trcPrefix);
      return untypedDoc;
   }
}
