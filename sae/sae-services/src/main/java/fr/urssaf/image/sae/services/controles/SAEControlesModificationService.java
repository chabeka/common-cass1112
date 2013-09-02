/**
 * 
 */
package fr.urssaf.image.sae.services.controles;

import java.util.List;

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
import fr.urssaf.image.sae.services.modification.exception.NotModifiableMetadataEx;

/**
 * Service permettant de réaliser des contrôles sur le document avant sa
 * modification
 * 
 */
public interface SAEControlesModificationService {

   /**
    * Réalise les contrôles des métadonnées du document avant sa mise à jour
    * 
    * @param metadatas
    *           la liste des métadonnées à vérifier
    * @throws InvalidValueTypeAndFormatMetadataEx
    *            Au moins une des métadonnées fournies n'est pas du bon type ou
    *            du bon format
    * @throws UnknownMetadataEx
    *            Au moins une des métadonnées n'existe pas
    * @throws DuplicatedMetadataEx
    *            Au moins une des métadonnées est en double dans la liste
    *            fournie
    * @throws NotSpecifiableMetadataEx
    *            Au moins une des métadonnées n'est pas spécifiable à
    *            l'archivage
    * @throws RequiredArchivableMetadataEx
    *            Au moins une des métadonnées requises à l'archivage n'est pas
    *            présente
    * @throws NotArchivableMetadataEx
    *            Au moins une des métadonnées n'est pas archivable
    * @throws ReferentialRndException
    *            Une erreur a eu lieu lors de la récupération des RND
    * @throws UnknownCodeRndEx
    *            Le code RND est inconnu
    * @throws UnknownHashCodeEx
    *            Une erreur a été soulevée lors de la vérification du HASH
    * @throws NotModifiableMetadataEx
    *            Au moins une des métadonnées n'est pas modifiable
    */
   void checkSaeMetadataForUpdate(List<UntypedMetadata> metadatas)
         throws InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx,
         NotModifiableMetadataEx;

   /**
    * Réalise les contrôles des métadonnées à supprimer du document avant sa
    * mise à jour
    * 
    * @param metadatas
    *           Liste des métadonnées à vérifier
    * @throws NotModifiableMetadataEx
    *            Au moins une des métadonnées n'est pas modifiable
    */
   void checkSaeMetadataForDelete(List<UntypedMetadata> metadatas)
         throws NotModifiableMetadataEx, UnknownMetadataEx;

   /**
    * Réalise les contrôles des métadonnées lors de l'opération de modification
    * (mise à jour ou suppression)
    * 
    * @param metadatas
    *           Liste des métadonnées à vérifier
    */
   void checkSaeMetadataForModification(List<UntypedMetadata> metadatas)
         throws DuplicatedMetadataEx;

}
