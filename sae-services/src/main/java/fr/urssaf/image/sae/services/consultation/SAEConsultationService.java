package fr.urssaf.image.sae.services.consultation;

import java.util.UUID;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;

/**
 * Ensemble des services de consultation du SAE
 * 
 */
public interface SAEConsultationService {

   /**
    * 
    * Service de consultation du SAE
    * 
    * @param idArchive
    *           identifiant unique de l'archive
    * @return Document correspondant à l'identifiant, peut-être null si
    *         l'identifiant n'existe pas dans le SAE
    * @throws SAEConsultationServiceException
    *            une exception est levée lors de la consultation
    */
   UntypedDocument consultation(UUID idArchive)
         throws SAEConsultationServiceException;
}
