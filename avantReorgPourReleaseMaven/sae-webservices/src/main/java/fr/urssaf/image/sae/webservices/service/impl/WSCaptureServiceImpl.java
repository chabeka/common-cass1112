package fr.urssaf.image.sae.webservices.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
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
   private static final Logger LOG = LoggerFactory
         .getLogger(WSCaptureServiceImpl.class);
   @Autowired
   private SAECaptureService captureService;

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final ArchivageUnitaireResponse archivageUnitaire(
         ArchivageUnitaire request) throws CaptureAxisFault {
      String prefixeTrc = "archivageUnitaire()";
      LOG.debug("{} - Début", prefixeTrc);
      // vérification que la liste des métadonnées n'est pas vide
      LOG
            .debug(
                  "{} - Début de la vérification : La liste des métadonnées fournies par l'application n'est pas vide",
                  prefixeTrc);
      if (request.getArchivageUnitaire().getMetadonnees().getMetadonnee() == null) {
         LOG.debug("{} - {}", prefixeTrc, MessageRessourcesUtils
               .recupererMessage("ws.capture.metadata.is.empty", null));
         throw new CaptureAxisFault("CaptureMetadonneesVide",
               MessageRessourcesUtils.recupererMessage(
                     "ws.capture.metadata.is.empty", null));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : La liste des métadonnées fournies par l'application n'est pas vide",
                  prefixeTrc);
      ArchivageUnitaireResponse response;
      URI ecdeURL = URI.create(request.getArchivageUnitaire().getEcdeUrl()
            .getEcdeUrlType().toString());
      LOG.debug("{} - URI ECDE: {}", prefixeTrc, ecdeURL);
      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      for (MetadonneeType metadonnee : request.getArchivageUnitaire()
            .getMetadonnees().getMetadonnee()) {
         metadatas.add(createUntypedMetadata(metadonnee));
      }
      LOG.debug("{} - Liste des métadonnées : \"{}\"", prefixeTrc,
            buildMessageFromList(metadatas));
      UUID uuid = capture(metadatas, ecdeURL);
      LOG.debug("{} - Valeur de retour du service SAECaptureService : \"{}\"",
            prefixeTrc, uuid);
      response = ObjectArchivageUnitaireFactory
            .createArchivageUnitaireResponse(uuid);
      if (response != null) {
         LOG.debug("{} - Valeur de retour : \"{}\"", prefixeTrc, response
               .getArchivageUnitaireResponse().getIdArchive().getUuidType());
      } else {
         LOG.debug("{} - Valeur de retour : null", prefixeTrc);
      }
      LOG.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
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

         throw new CaptureAxisFault("ErreurInterne", MessageRessourcesUtils
               .recupererMessage("ws.capture.error", null), e);

      } catch (UnknownCodeRndEx e) {

         throw new CaptureAxisFault("CaptureCodeRndInterdit", e.getMessage(), e);

      } catch (UnknownHashCodeEx e) {

         throw new CaptureAxisFault("CaptureHashErreur", e.getMessage(), e);

      } catch (CaptureBadEcdeUrlEx e) {
         
         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);
         
      } catch (CaptureEcdeUrlFileNotFoundEx e) {

         throw new CaptureAxisFault("CaptureUrlEcdeFichierIntrouvable", e
               .getMessage(), e);

      } catch (SAECaptureServiceEx e) {

         throw new CaptureAxisFault("ErreurInterneCapture",
               MessageRessourcesUtils
                     .recupererMessage("ws.capture.error", null), e);

      } catch (NotArchivableMetadataEx e) {

         throw new CaptureAxisFault("ErreurInterneCapture",
               MessageRessourcesUtils
                     .recupererMessage("ws.capture.error", null), e);
      }

   }

   /**
    * Construit une chaîne qui comprends l'ensemble des objets à afficher dans
    * les logs. <br>
    * Exemple : "UntypedMetadata[code long:=Titre,value=Attestation],
    * UntypedMetadata[code long:=DateCreation,value=2011-09-01],
    * UntypedMetadata[code long:=ApplicationProductrice,value=ADELAIDE]"
    * 
    * @param <T>
    *           le type d'objet
    * @param list
    *           : liste des objets à afficher.
    * @return Une chaîne qui représente l'ensemble des objets à afficher.
    */
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
