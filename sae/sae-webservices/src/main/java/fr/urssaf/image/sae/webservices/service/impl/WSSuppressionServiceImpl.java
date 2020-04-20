/**
 * 
 */
package fr.urssaf.image.sae.webservices.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.Suppression;
import fr.cirtil.www.saeservice.SuppressionResponse;
import fr.cirtil.www.saeservice.SuppressionResponseType;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;
import fr.urssaf.image.sae.webservices.exception.SuppressionAxisFault;
import fr.urssaf.image.sae.webservices.service.WSSuppressionService;

/**
 * Classe d'implémentation de l'interface {@link WSSuppressionService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC et l'annotation @Autowired.
 * 
 */
@Service
public class WSSuppressionServiceImpl implements WSSuppressionService {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(WSSuppressionServiceImpl.class);

  @Autowired
  private SAESuppressionService suppressionService;

  /**
   * {@inheritDoc}
   */
  @Override
  public SuppressionResponse suppression(final Suppression request)
      throws SuppressionAxisFault {
    final String trcPrefix = "suppressionSecure";
    LOGGER.debug("{} - début", trcPrefix);

    final String uuid = request.getSuppression().getUuid().getUuidType();
    final UUID idArchive = UUID.fromString(uuid);

    try {
      suppressionService.suppression(idArchive);

    } catch (final SuppressionException exception) {
      if (exception.getMessage() != null && exception.getMessage().contains("RetrievalServiceEx")) {
        throw new SuppressionAxisFault(exception);
      } else {
        throw new SuppressionAxisFault("ErreurInterneSuppression",
                                       exception
                                       .getMessage(),
                                       exception);
      }
    } catch (final ArchiveInexistanteEx exception) {
      throw new SuppressionAxisFault("SuppressionArchiveNonTrouvee",
                                     exception.getMessage(), exception);
    }

    final SuppressionResponseType responseType = new SuppressionResponseType();
    final SuppressionResponse response = new SuppressionResponse();
    response.setSuppressionResponse(responseType);

    LOGGER.debug("{} - fin", trcPrefix);
    return response;
  }
}
