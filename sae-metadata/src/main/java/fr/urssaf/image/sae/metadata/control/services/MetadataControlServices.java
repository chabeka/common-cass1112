package fr.urssaf.image.sae.metadata.control.services;

import java.util.List;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;

/**
 * Fournit les services de contrôle des métadonnées.
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.TooManyMethods")
public interface MetadataControlServices {
   /**
    * Contrôle que la liste des métadonnées sont autorisées à l'archivables lors
    * de la capture.
    * 
    * @param saeDoc
    *           : Un objet métier de type {@link SAEDocument}
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkArchivableMetadata(final SAEDocument saeDoc);

   /**
    * Contrôle que la liste des métadonnées existe dans le référentiel.
    * 
    * @param untypedDoc
    *           : Un objet de type {@link UntypedDocument}
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkExistingMetadata(final UntypedDocument untypedDoc);

   /**
    * Contrôle que chaque terme de la requête existe dans le référentiel.
    * 
    * @param longCodes
    *           : Le code long de la métadonnée
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkExistingQueryTerms(final List<String> longCodes);

   /**
    * Contrôle le type ,le format , la taille max de la valeur de chaque
    * métadonnées.
    * 
    * @param untypedDoc
    *           : Un objet de type {@link UntypedDocument}
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkMetadataValueTypeAndFormat(
         final UntypedDocument untypedDoc);

   /**
    * Contrôle que la liste des métadonnées fournit contient toutes les
    * métadonnées obligatoire à l'archivage.
    * 
    * @param saeDoc
    *           : Un objet métier de type {@link SAEDocument}
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkRequiredForArchivalMetadata(final SAEDocument saeDoc);

   /**
    * Contrôle que la liste des métadonnées fournit contient toutes les
    * métadonnées obligatoire au stockage.
    * 
    * @param saeDoc
    *           : Un objet métier de type {@link SAEDocument}
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkRequiredForStorageMetadata(final SAEDocument saeDoc);

   /**
    * Contrôle que la liste des métadonnées fournit contient toutes les
    * métadonnées obligatoire au stockage.
    * 
    * @param metadatas
    *           : la liste des métadonnées
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkRequiredForStorageMetadataList(
         final List<SAEMetadata> metadatas);

   /**
    * Contrôle que la liste des métadonnées est autorisée à la consultation.
    * métadonnées obligatoire.
    * 
    * @param metadatas
    *           : Liste des métadonnées métiers de type {@link SAEMetadata}
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkConsultableMetadata(
         final List<SAEMetadata> metadatas);

   /**
    * Contrôle que la liste des métadonnées est autorisée pour la recherche.
    * 
    * 
    * @param metadatas
    *           : Liste des métadonnées métiers de type {@link SAEMetadata}
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkSearchableMetadata(final List<SAEMetadata> metadatas);

   /**
    * Contrôle que la liste des métadonnées ne contient pas doublon.
    * 
    * 
    * @param metadatas
    *           : Liste des métadonnées métiers de type {@link SAEMetadata}
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkDuplicateMetadata(
         final List<UntypedMetadata> metadatas);

   /**
    * Contrôle que les valeurs des métadonnées obligatoires sont spécifiées.
    * 
    * @param untypedDoc
    *           : Un objet de type {@link UntypedDocument}
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkMetadataRequiredValue(
         final UntypedDocument untypedDoc);

   /**
    * Vérification de la valeur des métadonnées par rapport aux dictionnaires de
    * données
    * 
    * @param document
    *           le document
    * @return Liste de type MetadataError
    */
   List<MetadataError> checkMetadataValueFromDictionary(
         final UntypedDocument document);

   /**
    * Contrôle que la liste des métadonnées sont autorisées à l'archivables lors
    * de la capture.
    * 
    * @param metadatas
    *           : la liste des métadonnées
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkArchivableMetadataList(
         final List<SAEMetadata> metadatas);

   /**
    * Contrôle que la liste des métadonnées fournit contient toutes les
    * métadonnées obligatoire à l'archivage.
    * 
    * @param metadatas
    *           : la liste des métadonnées
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkRequiredForArchivalMetadataList(
         final List<SAEMetadata> metadatas);

   /**
    * Contrôle que la liste des métadonnées existe dans le référentiel.
    * 
    * @param metadatas
    *           : La liste des métadonnées
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkExistingMetadataList(
         final List<UntypedMetadata> metadatas);

   /**
    * Contrôle que les valeurs des métadonnées obligatoires sont spécifiées.
    * 
    * @param metadatas
    *           : la liste des métadonnées
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkMetadataListRequiredValue(
         final List<UntypedMetadata> metadatas);

   /**
    * Vérification de la valeur des métadonnées par rapport aux dictionnaires de
    * données
    * 
    * @param metadatas
    *           la liste des métadonnées
    * @return Liste de type MetadataError
    */
   List<MetadataError> checkMetadataListValueFromDictionary(
         final List<UntypedMetadata> metadatas);

   /**
    * Contrôle le type ,le format , la taille max de la valeur de chaque
    * métadonnées.
    * 
    * @param metadatas
    *           : la liste des métadonnées
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkMetadataListValueTypeAndFormat(
         final List<UntypedMetadata> metadatas);

   /**
    * Vérifie que les métadonnées passées en paramètre sont toutes modifiables
    * 
    * @param metadatas
    *           liste des métadonnées à contrôler
    * @return une liste d'erreur
    */
   List<MetadataError> checkModifiableMetadataList(
         final List<UntypedMetadata> metadatas);

   /**
    * Contrôle que la liste des métadonnées sont autorisées à l'archivables lors
    * de la capture.
    * 
    * @param metadatas
    *           : La liste des métadonnées
    * @return une liste d’objet de type {@link MetadataError}
    */
   List<MetadataError> checkSupprimableMetadatas(
         final List<UntypedMetadata> metadatas);
}
