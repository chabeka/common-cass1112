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

import fr.cirtil.www.saeservice.GetDocFormatOrigine;
import fr.cirtil.www.saeservice.GetDocFormatOrigineResponse;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentAttachment;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentAttachmentEx;
import fr.urssaf.image.sae.webservices.exception.GetDocFormatOrigineAxisFault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.service.WSDocumentAttacheService;
import fr.urssaf.image.sae.webservices.service.WSNoteService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectGetDocFormatOrigineFactory;
import fr.urssaf.image.sae.webservices.util.CollectionUtils;

/**
 * Implémentation de {@link WSNoteService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Service
public final class WSDocumentAttacheServiceImpl implements
      WSDocumentAttacheService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSDocumentAttacheServiceImpl.class);

   @Autowired
   @Qualifier("documentService")
   private SAEDocumentService saeService;

   @Override
   public GetDocFormatOrigineResponse getDocFormatOrigine(
         GetDocFormatOrigine request) throws GetDocFormatOrigineAxisFault {

      // Traces debug - entrée méthode
      String prefixeTrc = "getDocFormatOrigine()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // Lecture de l'UUID depuis l'objet de requête de la couche ws
      UUID docUuid = UUID.fromString(request.getGetDocFormatOrigine()
            .getIdDoc().getUuidType());
      LOG.debug("{} - UUID envoyé par l'application cliente : {}", prefixeTrc,
            docUuid);

      LOG.debug("{} - Récupération du document rattaché", prefixeTrc);

      try {
         UntypedDocumentAttachment uDocAtt;

         uDocAtt = saeService.getDocumentAttachment(docUuid);

         // Si le document ne possède pas de document attaché au format d'origine
         if (uDocAtt == null) {
            LOG.debug(
                  "{} - L'archive demandée ne possède pas de document attaché au format d'origine ({})",
                  prefixeTrc, docUuid);
            throw new GetDocFormatOrigineAxisFault(
                  "Il n'existe aucun document au format d'origine pour l'identifiant d'archivage '"
                        + docUuid + "'", "AucunDocFormatOrigine");
         } else {
            // Conversion de l'objet UntypedDocument en un objet de la couche web
            // service
            List<MetadonneeType> metadatas = convertListeMetasServiceToWebService(uDocAtt.getUMetadatas());
            // Construction de l'objet de réponse
            GetDocFormatOrigineResponse response = ObjectGetDocFormatOrigineFactory
                  .createGetDocFormatOrigineResponse(uDocAtt.getContent(), metadatas);
            if (response == null) {
               LOG.debug("{} - Valeur de retour : null", prefixeTrc);
            } else {
               // TODO
               // LOG.debug("{} - Valeur de retour : \"{}\"", prefixeTrc,
               // response
               // .getStockageUnitaireResponse().getIdGed().getUuidType());
            }
            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);

            // Renvoie l'objet de réponse de la couche web service
            return response;
         }

      } catch (SAEDocumentAttachmentEx e) {
         throw new GetDocFormatOrigineAxisFault(e);
      } catch (ArchiveInexistanteEx e) {
         throw new GetDocFormatOrigineAxisFault("ArchiveNonTrouvee",
               e.getMessage(), e);
      }

   }
   
   private List<MetadonneeType> convertListeMetasServiceToWebService(
         List<UntypedMetadata> listeMetasService) {

      List<MetadonneeType> metadatas = new ArrayList<MetadonneeType>();

      for (UntypedMetadata untypedMetadata : CollectionUtils
            .loadListNotNull(listeMetasService)) {

         String code = untypedMetadata.getLongCode();
         String valeur = untypedMetadata.getValue();
         if (untypedMetadata.getValue() == null) {
            valeur = StringUtils.EMPTY;
         }
         MetadonneeType metadonnee = ObjectTypeFactory.createMetadonneeType(
               code, valeur);

         metadatas.add(metadonnee);
      }

      return metadatas;

   }

}
