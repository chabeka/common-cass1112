package fr.urssaf.image.sae.webservices.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationRequestType;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
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
   private static final Logger LOG = LoggerFactory
         .getLogger(WSConsultationServiceImpl.class);
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
      // Traces debug - entrée méthode
      String prefixeTrc = "Opération consultationSecure()";
      LOG.debug("{} - Début", prefixeTrc);
      UUID uuid = UUID.fromString(request.getConsultation().getIdArchive()
            .getUuidType());
      LOG.debug("{} - UUID envoyé par l'application cliente : {}", prefixeTrc,
            uuid);
      try {

         List<String> listDatas = null;
         ConsultationRequestType consultation = request.getConsultation();

         if (consultation.getMetadonnees() != null) {

            listDatas = ObjectTypeFactory.buildMetaCodeFromWS(consultation
                  .getMetadonnees());
         }

         // ObjectTypeFactory.buildMetaCodeFromWS(request.getConsultation()
         // .getMetadonnees().getMetadonneeCode());

         ConsultParams consultParams = new ConsultParams(uuid, listDatas);

         UntypedDocument untypedDocument = saeService
               .consultation(consultParams);

         if (untypedDocument == null) {
            LOG
                  .debug(
                        "{} - L'archive demandée n'a pas été retrouvée dans le SAE ({})",
                        prefixeTrc, uuid);
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
            // Trace à activer pour afficher le résultat de la consultation
            // LOG
            // .debug(
            // "{} - Valeur de retour le contenue du document ({}) et la liste des métadonnés ({})",
            // new Object[] {
            // prefixeTrc,
            // content,
            // FormatUtils.buildMessageFromList(untypedDocument
            // .getUMetadatas()) });
            response = ObjectConsultationFactory.createConsultationResponse(
                  content, metadatas);

         }
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode
      } catch (SAEConsultationServiceException e) {
         throw new ConsultationAxisFault(e);
      
      } catch (UnknownDesiredMetadataEx e) {
         throw new ConsultationAxisFault(e.getMessage(),
               "ConsultationMetadonneesInexistante", e);
      } catch (MetaDataUnauthorizedToConsultEx e) {
         throw new ConsultationAxisFault(e.getMessage(),
               "ConsultationMetadonneesNonAutorisees", e);
      }

      return response;
   }

}
