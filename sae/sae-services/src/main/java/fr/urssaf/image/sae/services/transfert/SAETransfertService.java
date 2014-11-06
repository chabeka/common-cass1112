package fr.urssaf.image.sae.services.transfert;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;


/**
 * 
 * Service permettant de réaliser le transfert de documents
 * 
 */
public interface SAETransfertService {
   
   /**
    * Supprime le document donné
    * 
    * @param idArchive
    *           identifiant unique du document à transférer
    * @throws TransfertException
    *            Erreur levée lorsqu'un erreur survient pendant le transfert du doc
    * @throws ArchiveAlreadyTransferedException
    *            Erreur levée lorsque le document a déjà été transféré
    * @throws ArchiveInexistanteEx
    *            Erreur levée lorsque l'archive n'a pas été trouvée
    */
   @PreAuthorize("hasRole('transfert')")
   public void transfertDoc(UUID idArchive) throws TransfertException,
      ArchiveAlreadyTransferedException, ArchiveInexistanteEx;
}
