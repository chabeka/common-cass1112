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
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.modification.SAEModificationService;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.webservices.exception.ModificationAxisFault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.service.WSModificationService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModificationFactory;
import fr.urssaf.image.sae.webservices.util.WsMessageRessourcesUtils;

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

  @Autowired
  private WsMessageRessourcesUtils wsMessageRessourcesUtils;

  /**
   * {@inheritDoc}
   */
  @Override
  public ModificationResponse modification(final Modification request)
      throws ModificationAxisFault {
    final String trcPrefix = "modification";
    LOGGER.debug("{} - début", trcPrefix);

    // Vérification que la liste des métadonnées n'est pas vide
    final ListeMetadonneeType listeMeta = request.getModification()
        .getMetadonnees();
    verifListeMetaNonVide(listeMeta);

    final List<UntypedMetadata> metas = convertListeMetasWebServiceToService(listeMeta);

    final String uuid = request.getModification().getUuid().getUuidType();
    final UUID idArchive = UUID.fromString(uuid);

    try {
      modificationService.modification(idArchive, metas);

    } catch (final ReferentialRndException exception) {
      throw new ModificationAxisFault("ErreurInterne",
                                      wsMessageRessourcesUtils.recupererMessage("ws.capture.error",
                                                                                null), exception);

    } catch (final UnknownCodeRndEx exception) {

      throw new ModificationAxisFault("ModificationCodeRndInterdit",
                                      exception.getMessage(), exception);

    } catch (final InvalidValueTypeAndFormatMetadataEx exception) {
      throw new ModificationAxisFault(
                                      "ModificationMetadonneeFormatTypeNonValide", exception
                                      .getMessage(), exception);

    } catch (final UnknownMetadataEx exception) {
      throw new ModificationAxisFault("ModificationMetadonneeInconnue",
                                      exception.getMessage(), exception);

    } catch (final DuplicatedMetadataEx exception) {
      throw new ModificationAxisFault("ModificationMetadonneeDoublon",
                                      exception.getMessage(), exception);

    } catch (final NotSpecifiableMetadataEx exception) {
      throw new ModificationAxisFault("ModificationMetadonneeNonModifiable",
                                      exception.getMessage(), exception);

    } catch (final RequiredArchivableMetadataEx exception) {
      throw new ModificationAxisFault("ModificationMetadonneeObligatoire",
                                      exception.getMessage(), exception);

    } catch (final UnknownHashCodeEx exception) {
      throw new ModificationAxisFault("CaptureHashErreur", exception
                                      .getMessage(), exception);

    } catch (final NotModifiableMetadataEx exception) {
      throw new ModificationAxisFault("ModificationMetadonneeNonModifiable",
                                      exception.getMessage(), exception);

    } catch (final ModificationException exception) {
      if (exception.getMessage().contains("RetrievalServiceEx")) {
        throw new ModificationAxisFault(exception);
      } else {
        throw new ModificationAxisFault("ErreurInterneModification",
                                        exception
                                        .getMessage(),
                                        exception.getCause());
      }

    } catch (final ArchiveInexistanteEx exception) {
      throw new ModificationAxisFault("ModificationArchiveNonTrouvee",
                                      exception.getMessage(), exception);

    } catch (final MetadataValueNotInDictionaryEx exception) {
      throw new ModificationAxisFault("ModificationMetadonneeDictionnaire",
                                      exception.getMessage(), exception);

    } catch (final ReferentialException e) {
      throw new ModificationAxisFault("ModificationMetadonneeReferential",
                                      e.getMessage(), e);
    } catch (final RetrievalServiceEx e) {
      throw new ModificationAxisFault("ModificationMetadonneeReferential",
                                      e.getMessage(), e);
    }

    final ModificationResponse response = ObjectModificationFactory
        .createModificationResponse();

    LOGGER.debug("{} - fin", trcPrefix);

    return response;
  }

  private List<UntypedMetadata> convertListeMetasWebServiceToService(
                                                                     final ListeMetadonneeType listeMetaWs) {

    if (listeMetaWs == null) {
      return null;
    } else {
      return ObjectTypeFactory.buildMetaFromWS(listeMetaWs);
    }

  }

  private void verifListeMetaNonVide(final ListeMetadonneeType listeMeta)
      throws ModificationAxisFault {
    final String prefixeTrc = "verifListeMetaNonVide()";
    LOGGER
    .debug(
           "{} - Début de la vérification : La liste des métadonnées fournies par l'application n'est pas vide",
           prefixeTrc);
    if (listeMeta.getMetadonnee() == null) {
      LOGGER.debug("{} - {}", prefixeTrc, wsMessageRessourcesUtils
                   .recupererMessage("ws.modification.metadata.is.empty", null));
      throw new ModificationAxisFault("ModificationMetadonneesVide",
                                      wsMessageRessourcesUtils.recupererMessage(
                                                                                "ws.modification.metadata.is.empty", null));
    }
    LOGGER
    .debug(
           "{} - Fin de la vérification : La liste des métadonnées fournies par l'application n'est pas vide",
           prefixeTrc);
  }

}
