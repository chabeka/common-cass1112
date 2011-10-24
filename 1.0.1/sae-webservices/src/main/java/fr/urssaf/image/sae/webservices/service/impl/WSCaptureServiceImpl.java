package fr.urssaf.image.sae.webservices.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.EcdeUrlSommaireType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSCaptureService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectArchivageUnitaireFactory;
import fr.urssaf.image.sae.webservices.util.MessageRessourcesUtils;

/**
 * Implémentation de {@link WSCaptureService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@Service
public class WSCaptureServiceImpl implements WSCaptureService {

   @Autowired
   private SAECaptureService captureService;
   @Autowired
   @Qualifier("saeBulkCaptureService")
   private SAEBulkCaptureService saeBulkCaptureService;

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final ArchivageUnitaireResponse archivageUnitaire(
         ArchivageUnitaire request) throws CaptureAxisFault {

      // vérification que la liste des métadonnées n'est pas vide
      if (request.getArchivageUnitaire().getMetadonnees().getMetadonnee() == null) {

         throw new CaptureAxisFault("CaptureMetadonneesVide",
        		 MessageRessourcesUtils.recupererMessage("ws.capture.metadata.is.empty", null));
      }

      ArchivageUnitaireResponse response;

      URI ecdeURL = URI.create(request.getArchivageUnitaire().getEcdeUrl()
            .getEcdeUrlType().toString());

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();

      for (MetadonneeType metadonnee : request.getArchivageUnitaire()
            .getMetadonnees().getMetadonnee()) {

         metadatas.add(createUntypedMetadata(metadonnee));
      }

      UUID uuid = capture(metadatas, ecdeURL);

      response = ObjectArchivageUnitaireFactory
            .createArchivageUnitaireResponse(uuid);

      return response;
   }

   private UntypedMetadata createUntypedMetadata(MetadonneeType metadonnee) {

      return new UntypedMetadata(metadonnee.getCode().getMetadonneeCodeType(),
            metadonnee.getValeur().getMetadonneeValeurType());
   }

   private UUID capture(List<UntypedMetadata> metadatas, URI ecdeURL)
         throws CaptureAxisFault {

      try {
         return captureService.capture(metadatas, ecdeURL);
      } catch (SAECaptureServiceEx e) {
         throw new CaptureAxisFault(
               "ErreurInterneCapture",
               MessageRessourcesUtils.recupererMessage("ws.capture.error", null),
               e);

      } catch (RequiredStorageMetadataEx e) {

         throw new CaptureAxisFault("ErreurInterneCapture", e.getMessage(), e);

      } catch (InvalidValueTypeAndFormatMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesFormatTypeNonValide", e
               .getMessage(), e);

      } catch (UnknownMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesInconnu",
               e.getMessage(), e);

      } catch (DuplicatedMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesDoublon",
               e.getMessage(), e);

      } catch (NotSpecifiableMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesInterdites", e
               .getMessage(), e);

      } catch (EmptyDocumentEx e) {

         throw new CaptureAxisFault("CaptureFichierVide", e.getMessage(), e);

      } catch (RequiredArchivableMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesArchivageObligatoire", e
               .getMessage(), e);

      } catch (ReferentialRndException e) {

         throw new CaptureAxisFault("ErreurInterne",
        		 MessageRessourcesUtils.recupererMessage("ws.capture.error", null), e);

      } catch (UnknownCodeRndEx e) {

         throw new CaptureAxisFault("CaptureCodeRndInterdit", e.getMessage(), e);

      } catch (NotArchivableMetadataEx e) {

         throw new CaptureAxisFault(
               "ErreurInterneCapture",
               MessageRessourcesUtils.recupererMessage("ws.capture.error", null),
               e);
      } catch (UnknownHashCodeEx e) {
         throw new CaptureAxisFault("CaptureHashErreur", e.getMessage(), e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ArchivageMasseResponse archivageEnMasse(ArchivageMasse request) {
      EcdeUrlSommaireType ecdeUrlWs = request.getArchivageMasse()
            .getUrlSommaire();
      String ecdeUrl = ecdeUrlWs.getEcdeUrlSommaireType().toString();
      // Appel du service, celui-ci doit rendre la main rapidement
      // (traitement dans un autre thread)
      saeBulkCaptureService.bulkCapture(ecdeUrl);
      // On prend acte de la demande,
      // le retour se fera via le fichier resultats.xml de l'ECDE
      return ObjectStorageResponseFactory.createArchivageMasseResponse();
   }
}
