package fr.urssaf.image.sae.webservices.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.service.WSConsultationService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectConsultationFactory;
import fr.urssaf.image.sae.webservices.util.CollectionUtils;

/**
 * Implémentation de {@link WSConsultationService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@Service
public class WSConsultationServiceImpl implements WSConsultationService {

   @Autowired
   @Qualifier("documentService")
   private SAEDocumentService saeService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ConsultationResponse consultation(Consultation request)
         throws ConsultationAxisFault {

      ConsultationResponse response;

      UUID uuid = UUID.fromString(request.getConsultation().getIdArchive()
            .getUuidType());

      try {
         UntypedDocument untypedDocument = saeService.consultation(uuid);

         if (untypedDocument == null) {

            throw new ConsultationAxisFault(
                  "Il n'existe aucun document pour l'identifiant d'archivage '"
                        + uuid + "'", "ArchiveNonTrouvee");

         } else {

            List<MetadonneeType> metadatas = new ArrayList<MetadonneeType>();

            for (UntypedMetadata untypedMetadata : CollectionUtils
                  .loadListNotNull(untypedDocument.getUMetadatas())) {

               String code = untypedMetadata.getLongCode();
               String valeur = untypedMetadata.getValue();
               if (untypedMetadata.getValue() == null) {
                  valeur = StringUtils.EMPTY;
               }
               MetadonneeType metadonnee = ObjectTypeFactory
                     .createMetadonneeType(code, valeur);

               metadatas.add(metadonnee);
            }

            byte[] content = untypedDocument.getContent();

            response = ObjectConsultationFactory.createConsultationResponse(
                  content, metadatas);

         }

      } catch (SAEConsultationServiceException e) {
         throw new ConsultationAxisFault(e);
      }

      return response;
   }

}
