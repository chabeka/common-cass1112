/**
 * 
 */
package fr.urssaf.image.sae.webservices.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.Modification;
import fr.cirtil.www.saeservice.ModificationResponse;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.modification.SAEModificationService;
import fr.urssaf.image.sae.services.modification.exception.NotModifiableMetadataEx;
import fr.urssaf.image.sae.webservices.exception.ModificationAxisFault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.service.WSModificationService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModificationFactory;
import fr.urssaf.image.sae.webservices.util.MessageRessourcesUtils;

/**
 * Classe d'implémentation de l'interface {@link WSModificationService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC et l'annotation @Autowired
 * 
 */
@Service
public class WSModificationServiceImpl implements WSModificationService {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(WSModificationServiceImpl.class);

   @Autowired
   private SAEModificationService modificationService;

   /**
    * {@inheritDoc}
    */
   @Override
   public ModificationResponse modification(Modification request)
         throws ModificationAxisFault {
      String trcPrefix = "modification";
      LOGGER.debug("{} - début", trcPrefix);

      List<UntypedMetadata> metas = convertListeMetasWebServiceToService(request
            .getModification().getMetadonnees());

      String uuid = request.getModification().getUuid().getUuidType();
      UUID idArchive = UUID.fromString(uuid);

      try {
         modificationService.modification(idArchive, metas);

      } catch (ReferentialRndException exception) {
         throw new ModificationAxisFault("ErreurInterne",
               MessageRessourcesUtils
                     .recupererMessage("ws.capture.error", null), exception);

      } catch (UnknownCodeRndEx exception) {

         throw new ModificationAxisFault("CaptureCodeRndInterdit", exception
               .getMessage(), exception);

      } catch (InvalidValueTypeAndFormatMetadataEx exception) {
         throw new ModificationAxisFault(
               "CaptureMetadonneesFormatTypeNonValide", exception.getMessage(),
               exception);

      } catch (UnknownMetadataEx exception) {
         throw new ModificationAxisFault("CaptureMetadonneesInconnu", exception
               .getMessage(), exception);

      } catch (DuplicatedMetadataEx exception) {
         throw new ModificationAxisFault("CaptureMetadonneesDoublon", exception
               .getMessage(), exception);

      } catch (NotSpecifiableMetadataEx exception) {
         throw new ModificationAxisFault("CaptureMetadonneesInterdites",
               exception.getMessage(), exception);

      } catch (RequiredArchivableMetadataEx exception) {
         throw new ModificationAxisFault(
               "CaptureMetadonneesArchivageObligatoire",
               exception.getMessage(), exception);

      } catch (NotArchivableMetadataEx exception) {
         throw new ModificationAxisFault("ErreurInterneCapture",
               MessageRessourcesUtils
                     .recupererMessage("ws.capture.error", null), exception);

      } catch (UnknownHashCodeEx exception) {
         throw new ModificationAxisFault("CaptureHashErreur", exception
               .getMessage(), exception);

      } catch (NotModifiableMetadataEx exception) {
         throw new ModificationAxisFault("ModificationMetadonneeNonModifiable",
               exception.getMessage(), exception);

      } catch (ModificationException exception) {
         throw new ModificationAxisFault("ErreurInterneModification", exception
               .getMessage(), exception);
      }

      ModificationResponse response = ObjectModificationFactory
            .createModificationResponse();

      LOGGER.debug("{} - fin", trcPrefix);

      return response;
   }

   private List<UntypedMetadata> convertListeMetasWebServiceToService(
         ListeMetadonneeType listeMetaWs) {

      if (listeMetaWs == null) {
         return null;
      } else {
         return ObjectTypeFactory.buildMetaFromWS(listeMetaWs);
      }

   }
}
