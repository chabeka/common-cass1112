/**
 * 
 */
package fr.urssaf.image.sae.webservices.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.Transfert;
import fr.cirtil.www.saeservice.TransfertResponse;
import fr.cirtil.www.saeservice.TransfertResponseType;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.webservices.exception.TransfertAxisFault;
import fr.urssaf.image.sae.webservices.service.WSTransfertService;

/**
 * Classe d'implémentation de l'interface {@link WSTransfertService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC et l'annotation @Autowired.
 * 
 */
@Service
public class WSTransfertServiceImpl implements WSTransfertService {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(WSTransfertServiceImpl.class);
   
   /**
    * Service permettant de réaliser le transfert du document
    */
   @Autowired
   private SAETransfertService transfertService;

   /**
    * {@inheritDoc}
    */
   @Override
   public TransfertResponse transfert(Transfert request)
         throws TransfertAxisFault {
      String trcPrefix = "transfert";
      LOGGER.debug("{} - début", trcPrefix);

      String uuid = request.getTransfert().getUuid().getUuidType();
      UUID idArchive = UUID.fromString(uuid);

      try {
         transfertService.transfertDoc(idArchive);
      } catch (ArchiveInexistanteEx e) {
         throw new TransfertAxisFault("ArchiveNonTrouvee", e.getMessage(), e);
      } catch (ArchiveAlreadyTransferedException e) {
         throw new TransfertAxisFault("ArchiveDejaTransferee", e.getMessage(), e);
      } catch (TransfertException e) {
         throw new TransfertAxisFault("ErreurInterneTransfert", e.getMessage(), e);
      } catch (Exception e) {
         throw new TransfertAxisFault("ErreurInterne", e.getMessage(), e);
      }

      TransfertResponseType responseType = new TransfertResponseType();
      TransfertResponse response = new TransfertResponse();
      response.setTransfertResponse(responseType);

      LOGGER.debug("{} - fin", trcPrefix);
      return response;
   }
}
