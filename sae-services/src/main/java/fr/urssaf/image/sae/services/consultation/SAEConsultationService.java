package fr.urssaf.image.sae.services.consultation;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
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
    * @throws MetaDataUnauthorizedToConsultEx
    *            une exception est levée lorsque la metadata n'est pas
    *            consultable
    * @throws UnknownDesiredMetadataEx
    *            une exception est levée lorsque la metadata est inconnue
    */
   UntypedDocument consultation(UUID idArchive)
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx;

   /**
    * 
    * Service de consultation du SAE
    * 
    * @param consultParams
    *           Objet regroupant les paramètres nécessaires à la consultation
    * @return Document correspondant à l'identifiant, peut-être null si
    *         l'identifiant n'existe pas dans le SAE
    * @throws SAEConsultationServiceException
    *            une exception est levée lors de la consultation
    * @throws MetaDataUnauthorizedToConsultEx
    *            une exception est levée lorsque la metadata n'est pas
    *            consultable
    * @throws UnknownDesiredMetadataEx
    *            une exception est levée lorsque la metadata est inconnue
    */
   @PreAuthorize("hasRole('consultation')")
   UntypedDocument consultation(ConsultParams consultParams)
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx;
}
