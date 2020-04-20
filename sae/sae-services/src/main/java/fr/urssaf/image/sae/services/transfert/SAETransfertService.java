package fr.urssaf.image.sae.services.transfert;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Service permettant de réaliser le transfert de documents
 */
public interface SAETransfertService {

   /**
    * Transfert unitaire d'un document
    * 
    * @param idArchive
    *           identifiant unique du document à transférer
    * @throws TransfertException
    *            Erreur levée lorsqu'un erreur survient pendant le transfert
    *            du doc
    * @throws ArchiveAlreadyTransferedException
    *            Erreur levée lorsque le document a déjà été transféré
    * @throws ArchiveInexistanteEx
    *            Erreur levée lorsque l'archive n'a pas été trouvée
    * @throws InsertionIdGedExistantEx
    */
   @PreAuthorize("hasRole('transfert')")
   void transfertDoc(UUID idArchive) throws TransfertException, ArchiveAlreadyTransferedException,
         ArchiveInexistanteEx, InsertionIdGedExistantEx;

   /**
    * Transfert un document, dans le cadre d'un traitement de transfert de masse.
    * Les contrôles de transférabilité ont été faits en amont.
    * 
    * @param document
    *           Le document à transférer. Le travail de récupération des métadonnées actuelles du document, et le merge
    *           avec les métadonnées modifiées ou supprimées à la demande du client a fait en amont.
    */
   void transfertDocMasse(StorageDocument document)
         throws TransfertException, ArchiveAlreadyTransferedException, ArchiveInexistanteEx, ReferentialException,
         RetrievalServiceEx, InvalidSAETypeException, MappingFromReferentialException;

   /**
    * Dans le cadre du traitement de transfert de masse : contrôle que le document peut être transféré.
    * Crée également un StorageDocument à partir des métadonnées du document existant, et des métadonnées
    * que le client veut supprimer.
    * C'est ce StorageDocument qui sera ensuite à la méthode transfertDocMasse pour réaliser le transfert dans
    * une autre étape du traitement.
    * 
    * @param idArchive
    *           Identifiant de l'archive
    * @param listeMetaClient
    *           Liste de métadonnées spécifiées par la client, correspondant au métadonnées à modifier ou à supprimer au cours du transfert
    * @param isReprise
    *           True si c'est un traitement de reprise, false sinon
    * @param idTraitementMasse
    *           Identifiant du traitement de masse
    * @param isSuppression
    *           True si c'est une suppression de document, false sinon (si c'est un transfert)
    * @return Le document à transférer
    * @throws TransfertException
    * @{@link TransfertException}
    * @throws ArchiveAlreadyTransferedException
    * @{@link ArchiveAlreadyTransferedException}
    * @throws ArchiveInexistanteEx
    * @{@link ArchiveInexistanteEx}
    * @throws TraitementRepriseAlreadyDoneException
    * @{@link TraitementRepriseAlreadyDoneException}
    * @throws UnknownCodeRndEx
    * @{@link UnknownCodeRndEx}
    */
   StorageDocument controleDocumentTransfertMasse(final UUID idArchive, final List<UntypedMetadata> listeMetaClient,
                                                  boolean isReprise, UUID idTraitementMasse, boolean isSuppression)
         throws TransfertException, ArchiveAlreadyTransferedException, TraitementRepriseAlreadyDoneException;

   /**
    * Méthode permettant de générer la liste des métadonnées storage contenant
    * les métadonnées de modification et la métadonnée GEL.
    * 
    * @param idArchive
    *           Identifiant document
    * @return la liste des métadonnées storage
    * @throws ReferentialException
    * @{@link ReferentialException}
    * @throws RetrievalServiceEx
    * @{@link RetrievalServiceEx}
    */
   public List<StorageMetadata> getListeStorageMetadatasWithGel(UUID idArchive)
         throws ReferentialException, RetrievalServiceEx;

}
