package fr.urssaf.image.sae.mapping.services;

import java.util.List;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Interface qui fournit des services de conversion entre objet du modèle et
 * objet technique de stockage.
 * 
 * @author akenore
 * 
 */

public interface MappingDocumentService {
   /**
    * Service de conversion d’un objet de type SAEDocument vers un objet de
    * type. StorageDocument.
    * 
    * @param saeDoc
    *           : Le document métier.
    * @return un objet de type {@link StorageDocument}
    * @throws InvalidSAETypeException
    *            Exception levée lorsque la conversion n’aboutit pas.
    */

   StorageDocument saeDocumentToStorageDocument(final SAEDocument saeDoc)
         throws InvalidSAETypeException;

   /**
    * Service de conversion d’un objet de type StorageDocument vers un objet de
    * type. SAEDocument .
    * 
    * @param storageDoc
    *           : Le document technique de stockage.
    * @return Un objet de type {@link SAEDocument} avec la liste des métadonnées
    *         sans code long.
    * 
    * @throws InvalidSAETypeException
    *            Exception levée lorsque la conversion n’aboutit pas.
    * @throws MappingFromReferentialException
    *            Exception levée lorsque la récupération de la métadata du
    *            référentiel n'abouti pas
    */
   SAEDocument storageDocumentToSaeDocument(final StorageDocument storageDoc)
         throws InvalidSAETypeException, MappingFromReferentialException;

   /**
    * Service de conversion d’un objet de type {@link SAEDocument} vers un objet
    * de type {@link fr.urssaf.image.sae.bo.model.untyped.UntypedDocument}.
    * 
    * @param saeDoc
    *           : Le document métier.
    * @return un objet de type
    *         {@link fr.urssaf.image.sae.bo.model.untyped.UntypedDocument}
    * @throws InvalidSAETypeException
    *            Exception levée lorsque la conversion ne se passe pas bien.
    * @throws MappingFromReferentialException
    *            Exception levée lorsque la récupération de la métadata du
    *            référentiel n'abouti pas
    */
   UntypedDocument saeDocumentToUntypedDocument(final SAEDocument saeDoc)
         throws InvalidSAETypeException, MappingFromReferentialException;

   /**
    * Service de conversion d’un objet de type {@link UntypedDocument}
    * 
    * }vers un objet de type{@link SAEDocument}.
    * 
    * @param untyped
    *           : un document de type
    *           {@link fr.urssaf.image.sae.bo.model.untyped.UntypedDocument}
    * @return un objet de type {@link SAEDocument}
    * @throws InvalidSAETypeException
    *            Exception levée lorsque la conversion ne se passe pas bien.
    * @throws MappingFromReferentialException
    *            Exception levée lorsque la récupération de la métadata du
    *            référentiel n'abouti pas
    */
   SAEDocument untypedDocumentToSaeDocument(final UntypedDocument untyped)
         throws InvalidSAETypeException, MappingFromReferentialException;

   /**
    * Service de conversion d’un objet de type {@link StorageDocument}
    * 
    * }vers un objet de type{@link UntypedDocument}.
    * 
    * @param storage
    *           : un document de type {@link StorageDocument}.
    * @return un objet de type {@link UntypedDocument}
    * @throws InvalidSAETypeException
    *            Exception levée lorsque la conversion ne se passe pas bien.
    * @throws MappingFromReferentialException
    *            Exception levée lorsque la récupération de la métadata du
    *            référentiel n'abouti pas
    */
   UntypedDocument storageDocumentToUntypedDocument(
         final StorageDocument storage) throws InvalidSAETypeException,
         MappingFromReferentialException;

   /**
    * Transforme un objet typé en objet à archiver. Ne transforme pas l'objet de
    * référence, car ceci n'est pas possible. Il sera à traiter de manière
    * indépendante
    * 
    * @param document
    *           document à transformer
    * @return le document à archiver
    * @throws InvalidSAETypeException
    *            Erreur levée si un problème de conversion est soulevé
    */
   VirtualStorageDocument saeVirtualDocumentToVirtualStorageDocument(
         SAEVirtualDocument document) throws InvalidSAETypeException;

   /**
    * Service de conversion d’une liste d'objets de type {@link UntypedMetadata}
    * 
    * }vers une liste d'objets de type{@link SAEMetadata}.
    * 
    * @param metadatas
    *           : la liste des métadonnées
    *           {@link fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata}
    * @return un objet de type liste de {@link SAEMetadata}
    * @throws InvalidSAETypeException
    *            Exception levée lorsque la conversion ne se passe pas bien.
    * @throws MappingFromReferentialException
    *            Exception levée lorsque la récupération de la métadata du
    *            référentiel n'abouti pas
    */
   List<SAEMetadata> untypedMetadatasToSaeMetadatas(
         final List<UntypedMetadata> metadatas) throws InvalidSAETypeException,
         MappingFromReferentialException;
}
