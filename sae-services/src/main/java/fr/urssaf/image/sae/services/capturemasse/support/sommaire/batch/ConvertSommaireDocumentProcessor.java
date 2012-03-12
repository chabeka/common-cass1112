/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import javax.xml.bind.JAXBElement;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.DocumentType;

/**
 * Item processor pour convertir un document sommaire d'un modèle objet XML vers
 * un modèle objet métier
 * 
 */
@Component
public class ConvertSommaireDocumentProcessor implements ItemProcessor<JAXBElement<DocumentType>, UntypedDocument> {

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument process(JAXBElement<DocumentType> item)
         throws Exception {
      // TODO Auto-generated method stub
      return null;
   }

}
