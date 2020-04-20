package fr.urssaf.image.sae.webservices.service;

import org.easymock.EasyMock;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.metadata.exceptions.IndexCompositeException;
import fr.urssaf.image.sae.metadata.referential.services.IndexCompositeService;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.capturemasse.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.controles.traces.TracesControlesSupport;
import fr.urssaf.image.sae.services.copie.SAECopieService;
import fr.urssaf.image.sae.services.documentExistant.SAEDocumentExistantService;
import fr.urssaf.image.sae.services.metadata.MetadataService;
import fr.urssaf.image.sae.services.modification.SAEModificationService;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;

/**
 * Implémentation des Mocks des services SAE
 */
@Component
public class SAEServiceFactory {

  /**
   * @return instance de {@link SAECaptureService}
   */
  public final SAECaptureService createSAECaptureService() {

    final SAECaptureService service = EasyMock.createMock(SAECaptureService.class);

    return service;
  }

  /**
   * @return instance de {@link SAECopieService}
   */
  public final SAECopieService createSAECopieService() {

    final SAECopieService service = EasyMock.createMock(SAECopieService.class);

    return service;
  }

  public final SAEDocumentExistantService createSAEDocumentExistantService() {

    final SAEDocumentExistantService service = EasyMock.createMock(SAEDocumentExistantService.class);

    return service;
  }

  /**
   * @return instance de {@link SAEModificationService}
   */
  public final SAEModificationService createSAEModificationService() {

    final SAEModificationService service = EasyMock
                                                   .createMock(SAEModificationService.class);

    return service;
  }

  /**
   * @return instance de {@link SAESuppressionService}
   */
  public final SAESuppressionService createSAESuppressionService() {

    final SAESuppressionService service = EasyMock
                                                  .createMock(SAESuppressionService.class);

    return service;
  }

  /**
   * @return instance de {@link SAEControlesCaptureService}
   */
  public final SAEControlesCaptureService createSAEControlesCaptureService() {

    final SAEControlesCaptureService service = EasyMock
                                                       .createMock(SAEControlesCaptureService.class);

    return service;
  }

  /**
   * @return instance de {@link SAEControleSupportService}
   */
  public final SAEControleSupportService createSAEControlesSupportService() {

    final SAEControleSupportService service = EasyMock
                                                      .createMock(SAEControleSupportService.class);

    return service;
  }

  /**
   * @return instance de {@link TraitementAsynchroneService}
   */
  public final TraitementAsynchroneService createTraitementAsynchroneService() {

    final TraitementAsynchroneService service = EasyMock
                                                        .createMock(TraitementAsynchroneService.class);

    return service;
  }

  /**
   * @return instance de EcdeServices
   */
  public final EcdeServices createEcdeServices() {

    final EcdeServices service = EasyMock.createMock(EcdeServices.class);

    return service;
  }

  /**
   * @return instance de MetadataService
   */
  public final MetadataService createMetadataService() {

    final MetadataService service = EasyMock.createMock(MetadataService.class);

    return service;
  }
  
  /**
   * @return instance de MetadataService
   */
  public final WSMetadataService createWSMetadataService() {

    final WSMetadataService service = EasyMock.createMock(WSMetadataService.class);

    return service;
  }

  /**
   * @return instance de TracesControlesSupport
   */
  public final TracesControlesSupport createTracesControlesSupport() {

    final TracesControlesSupport support = EasyMock
                                                   .createMock(TracesControlesSupport.class);

    return support;
  }

  /**
   * @return instance de SAETransfertService
   */
  public final SAETransfertService createSAETransfertService() {

    final SAETransfertService transfertSce = EasyMock
                                                     .createMock(SAETransfertService.class);

    return transfertSce;
  }

  /**
   * @return instance de IndexCompositeService
   * @throws IndexCompositeException
   * @{@link IndexCompositeException}
   */
  public final IndexCompositeService createIndexCompositeService() throws IndexCompositeException {
    final IndexCompositeService indexCompositeService = EasyMock
                                                                .createMock(IndexCompositeService.class);
    return indexCompositeService;
  }
}
