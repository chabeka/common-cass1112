package fr.urssaf.image.sae.webservices.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.Copie;
import fr.cirtil.www.saeservice.CopieResponse;
import fr.cirtil.www.saeservice.CopieResponseType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.services.copie.SAECopieService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.copie.SAECopieServiceException;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.webservices.exception.CopieAxisFault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.service.WSCopieService;
import fr.urssaf.image.sae.webservices.util.WsMessageRessourcesUtils;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Service
public class WSCopieServiceImpl implements WSCopieService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSCopieServiceImpl.class);

   @Autowired
   @Qualifier("saeCopieService")
   private SAECopieService saeService;

   @Autowired
   private WsMessageRessourcesUtils wsMessageRessourcesUtils;

   @SuppressWarnings("unused")
   @Override
   public CopieResponse copie(Copie request) throws CopieAxisFault,
         ArchiveInexistanteEx, SAEConsultationServiceException,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         ReferentialException, SAECopieServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException {
      // TODO Auto-generated method stub
      String prefixeTrc = "copie()";
      UUID uuid = UUID.fromString(request.getCopie().getIdGed().getUuidType());
      LOG.debug("{} - UUID envoyé par l'application cliente : {}", "copie()",
            uuid);

      // Lecture des métadonnées depuis l'objet de requête de la couche ws
      ListeMetadonneeType listeMeta = request.getCopie().getMetadonnees();

      // Conversion de la liste des métadonnées d'un type vers un autre
      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      if (listeMeta.getMetadonnee() != null) {
         metadatas = convertListeMeta(listeMeta);
      } else {
         metadatas.add(new UntypedMetadata());
      }

      try {
         UUID idCopie = saeService.copie(uuid, metadatas);
         LOG.debug("{} - UUID : \"{}\"", idCopie);
         CopieResponse response = createCopieResponse();
         CopieResponseType responseType = response.getCopieResponse();
         responseType.setIdGed(ObjectTypeFactory.createUuidType(idCopie));
         if (response == null) {
            LOG.debug("{} - Valeur de retour : null", prefixeTrc);
         } else {
            LOG.debug("{} - Valeur de retour idGed: \"{}\"", prefixeTrc,
                  response.getCopieResponse().getIdGed());
         }
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode
         return response;

      } catch (SAEConsultationServiceException e) {
         throw new CopieAxisFault("ErreurSAEConsultationService",
               e.getMessage(), e);
      } catch (SAECaptureServiceEx e) {
         throw new CopieAxisFault("ErreurSAECaptureService", e.getMessage(), e);
      } catch (ReferentialRndException e) {
         throw new CopieAxisFault("ErreurReferentialRnd", e.getMessage(), e);
      } catch (UnknownCodeRndEx e) {
         throw new CopieAxisFault("UnknownCodeRnd", e.getMessage(), e);
      } catch (ReferentialException e) {
         throw new CopieAxisFault("Referential", e.getMessage(), e);
      } catch (SAECopieServiceException e) {
         throw new CopieAxisFault("SAECopieService", e.getMessage(), e);
      } catch (UnknownDesiredMetadataEx e) {
         throw new CopieAxisFault("UnknownDesiredMetadata", e.getMessage(), e);
      } catch (MetaDataUnauthorizedToConsultEx e) {
         throw new CopieAxisFault("MetaDataUnauthorizedToConsult",
               e.getMessage(), e);
      } catch (RequiredStorageMetadataEx e) {
         throw new CopieAxisFault("RequiredStorageMetadata", e.getMessage(), e);
      } catch (InvalidValueTypeAndFormatMetadataEx e) {
         throw new CopieAxisFault("InvalidValueTypeAndFormatMetadata",
               e.getMessage(), e);
      } catch (UnknownMetadataEx e) {
         throw new CopieAxisFault("UnknownMetadata", e.getMessage(), e);
      } catch (DuplicatedMetadataEx e) {
         throw new CopieAxisFault("DuplicatedMetadata", e.getMessage(), e);
      } catch (NotSpecifiableMetadataEx e) {
         throw new CopieAxisFault("NotSpecifiableMetadata", e.getMessage(), e);
      } catch (EmptyDocumentEx e) {
         throw new CopieAxisFault("EmptyDocument", e.getMessage(), e);
      } catch (RequiredArchivableMetadataEx e) {
         throw new CopieAxisFault("RequiredArchivableMetadata", e.getMessage(),
               e);
      } catch (NotArchivableMetadataEx e) {
         throw new CopieAxisFault("NotArchivableMetadata", e.getMessage(), e);
      } catch (UnknownHashCodeEx e) {
         throw new CopieAxisFault("UnknownHashCode", e.getMessage(), e);
      } catch (EmptyFileNameEx e) {
         throw new CopieAxisFault("EmptyFileName", e.getMessage(), e);
      } catch (MetadataValueNotInDictionaryEx e) {
         throw new CopieAxisFault("MetadataValueNotInDictionary",
               e.getMessage(), e);
      } catch (UnknownFormatException e) {
         throw new CopieAxisFault("UnknownFormat", e.getMessage(), e);
      } catch (ValidationExceptionInvalidFile e) {
         throw new CopieAxisFault("ValidationExceptionInvalidFile",
               e.getMessage(), e);
      } catch (UnexpectedDomainException e) {
         throw new CopieAxisFault("UnexpectedDomain", e.getMessage(), e);
      } catch (InvalidPagmsCombinaisonException e) {
         throw new CopieAxisFault("InvalidPagmsCombinaison", e.getMessage(), e);
      } catch (CaptureExistingUuuidException e) {
         throw new CopieAxisFault("CaptureExistingUuuid", e.getMessage(), e);
      }
   }

   private static CopieResponse createCopieResponse() {

      CopieResponse response = new CopieResponse();
      CopieResponseType responseType = new CopieResponseType();
      response.setCopieResponse(responseType);

      return response;
   }

   private List<UntypedMetadata> convertListeMeta(ListeMetadonneeType listeMeta) {

      String prefixeTrc = "convertitListeMeta()";

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      for (MetadonneeType metadonnee : listeMeta.getMetadonnee()) {
         if (metadonnee != null)
            metadatas.add(createUntypedMetadata(metadonnee));
      }
      LOG.debug("{} - Liste des métadonnées : \"{}\"", prefixeTrc,
            buildMessageFromList(metadatas));

      return metadatas;

   }

   private UntypedMetadata createUntypedMetadata(MetadonneeType metadonnee) {

      return new UntypedMetadata(metadonnee.getCode().getMetadonneeCodeType(),
            metadonnee.getValeur().getMetadonneeValeurType());
   }

   private <T> String buildMessageFromList(Collection<T> list) {
      final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
            ToStringStyle.SIMPLE_STYLE);
      for (T o : Utils.nullSafeIterable(list)) {
         if (o != null) {
            toStrBuilder.append(o.toString());
         }
      }
      return toStrBuilder.toString();
   }
}
