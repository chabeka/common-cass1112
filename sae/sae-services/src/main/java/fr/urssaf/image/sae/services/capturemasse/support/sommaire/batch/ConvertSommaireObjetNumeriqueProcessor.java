/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.FichierType;

/**
 * ItemProcessor permettant de transformer un objet <b>Objet Numerique</b> d'un
 * modèle XML vers un objet <b>VirtualReferenceFile</b>
 * 
 */
@Component
public class ConvertSommaireObjetNumeriqueProcessor implements
      ItemProcessor<JAXBElement<FichierType>, VirtualReferenceFile> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConvertSommaireObjetNumeriqueProcessor.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public VirtualReferenceFile process(JAXBElement<FichierType> item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      VirtualReferenceFile reference = new VirtualReferenceFile();
      reference.setFilePath(item.getValue().getCheminEtNomDuFichier());

      LOGGER.debug("{} - fin", trcPrefix);
      return reference;
   }

}
