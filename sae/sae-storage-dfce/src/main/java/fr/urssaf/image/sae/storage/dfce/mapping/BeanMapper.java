package fr.urssaf.image.sae.storage.dfce.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.FileReference;
import net.docubase.toolkit.model.reference.impl.FileReferenceImpl;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.storage.dfce.exception.MetadonneeInexistante;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Fournit des méthodes statiques de conversion des elements DFCE ceux du SAE.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
public final class BeanMapper {

   /**
    * Permet de convertir un {@link Document} en {@link StorageDocument}.<br/>
    * 
    * @param document
    *           : Le document DFCE.
    * @param desiredMetaDatas
    *           : Les métadonnées souhaitées.
    * @param serviceDFCE
    *           : Les services DFCE.
    * @param forConsultion
    *           : Paramètre pour récupérer le contenue des documents pour la
    *           consultation.
    * @return une occurrence de StorageDocument
    * @throws StorageException
    *            : Exception levée lorsque qu'un dysfonctionnement se produit.
    * @throws IOException
    *            : Exception levée lorsque qu'un dysfonctionnement se produit
    *            lors des I/O.
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public static StorageDocument dfceDocumentToStorageDocument(
         final Document document, final List<StorageMetadata> desiredMetaDatas,
         final ServiceProvider serviceDFCE, boolean forConsultion)
         throws StorageException, IOException {
      // on construit la liste des métadonnées à partir de la liste des
      // métadonnées souhaitées.
      final List<StorageMetadata> metaDatas = storageMetaDatasFromCriterions(
            document, desiredMetaDatas, serviceDFCE);
      return buildStorageDocument(document, metaDatas, serviceDFCE,
            forConsultion);
   }

   /**
    * Permet de convertir les métadonnées DFCE vers les métadonnées
    * StorageDocument.<br/>
    * 
    * @param serviceDFCE
    *           : Les services DFCE.
    * @param document
    *           : Le document DFCE.
    * @param desiredMetaData
    *           : Les métadonnées souhaitées.
    * @return une occurrence de StorageDocument contenant uniquement les
    *         métadonnées.
    * @throws StorageException
    *            : Exception levée lorsque qu'un dysfonctionnement se produit.
    * @throws IOException
    *            : Exception levée lorsque qu'un dysfonctionnement se produit
    *            lors des I/O.
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public static StorageDocument dfceMetaDataToStorageDocument(
         final Document document, final List<StorageMetadata> desiredMetaData,
         final ServiceProvider serviceDFCE) throws StorageException,
         IOException {
      final List<StorageMetadata> metaDatas = storageMetaDatasFromCriterions(
            document, desiredMetaData, serviceDFCE);
      return new StorageDocument(metaDatas);
   }

   /**
    * Construit la liste des {@link StorageMetadata} à partir de la liste des
    * {@link Criterion}.
    * 
    * @param serviceDFCE
    *           : Les services DFCE.
    * @param document
    *           : Le document DFCE.
    * @param desiredMetaData
    *           : La liste des métadonnées souhaitées.
    * @return La liste des {@link StorageMetadata} à partir de la liste des
    *         {@link Criterion}.
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   private static List<StorageMetadata> storageMetaDatasFromCriterions(
         final Document document, final List<StorageMetadata> desiredMetaData,
         final ServiceProvider serviceDFCE) {
      final Set<StorageMetadata> metadatas = new HashSet<StorageMetadata>();
      if (document != null) {
         final List<Criterion> criterions = document.getAllCriterions();
         // dans le cas de l'insertion d'un document
         if (desiredMetaData == null) {
            for (Criterion criterion : Utils.nullSafeIterable(criterions)) {
               metadatas.add(new StorageMetadata(criterion.getCategoryName(),
                     criterion.getWord()));
            }
         } else {
            // Traitement pour filtrer sur la liste des métadonnées
            // souhaitées.
            for (StorageMetadata metadata : Utils
                  .nullSafeIterable(desiredMetaData)) {
               boolean found = false;
               for (Criterion criterion : Utils.nullSafeIterable(criterions)) {
                  if (criterion.getCategoryName().equalsIgnoreCase(
                        metadata.getShortCode())) {
                     metadatas.add(new StorageMetadata(metadata.getShortCode(),
                           criterion.getWord()));
                     found = true;
                     break;
                  }
               }
               // si les métadonnées ne sont pas dans DFCE on vérifie si
               // ces
               // métadonnées sont des métadonnées techniques sinon on les
               // retourne avec la valeur vide
               if (!found) {
                  metadatas.add(completedMetadatas(document, metadata,
                        serviceDFCE));
               }
            }

         }
      }
      return new ArrayList<StorageMetadata>(metadatas);
   }

   /**
    * @param shortCode
    *           : Le code court.
    * @param storageDocument
    *           : Le document
    * @return le nom du fichier ainsi que l'extension
    * @throws ParseException
    *            Exception lévée lorsque le parsing du nom du fichier ne se
    *            passe pas bien.
    */
   public static String[] findFileNameAndExtension(
         final StorageDocument storageDocument, final String shortCode)
         throws ParseException {
      String value = null;
      for (StorageMetadata storageMetadata : Utils
            .nullSafeIterable(storageDocument.getMetadatas())) {
         // ici on exclut toutes les métadonnées techniques
         if (shortCode.equals(storageMetadata.getShortCode().trim())
               && storageMetadata.getValue() != null) {
            value = String.valueOf(storageMetadata.getValue());
            break;
         }
      }

      return new String[] { FilenameUtils.getBaseName(value),
            FilenameUtils.getExtension(value) };
   }

   /**
    * Construit une occurrence de storageDocument à partir d'un document DFCE.
    * 
    * @param document
    *           : Le document DFCE.
    * @param listMetaData
    *           : La liste des métadonnées.
    * @param serviceDFCE
    *           : Les services DFCE.
    * @param forConsultation
    *           : Paramtére pour charger le contenue du document.
    * 
    * @throws IOException
    *            Exception levée lorsque qu'un dysfonctionnement se produit lors
    *            des I/O.
    */
   private static StorageDocument buildStorageDocument(final Document document,
         final List<StorageMetadata> listMetaData,
         final ServiceProvider serviceDFCE, boolean forConsultation)
         throws IOException {
      // Instance de StorageDocument
      final StorageDocument storageDocument = new StorageDocument();
      if (document != null) {
         if (forConsultation) {
            final InputStream docContent = serviceDFCE.getStoreService()
                  .getDocumentFile(document);
            storageDocument.setContent(IOUtils.toByteArray(docContent));
         }
         storageDocument.setCreationDate(document.getCreationDate());
         storageDocument.setTitle(document.getTitle());
         storageDocument.setUuid(document.getUuid());
         storageDocument.setMetadatas(listMetaData);
      }
      return storageDocument;
   }

   /**
    * Permet de convertir {@link StorageDocument} en {@link Document}.
    * 
    * @param baseDFCE
    *           : La base dfce
    * @param storageDocument
    *           : Un StorageDocment.
    * @return Un document DFCE à partir d'un storageDocment.
    * @throws ParseException
    *            Exception si le parsing de la date ne se passe pas bien.
    * @throws MetadonneeInexistante 
    */
   // CHECKSTYLE:OFF
   public static Document storageDocumentToDfceDocument(final Base baseDFCE,
         final StorageDocument storageDocument) throws ParseException, MetadonneeInexistante {

      Document document = createDocument(storageDocument.getMetadatas(),
            baseDFCE);

      return document;
   }

   /**
    * Permet de convertir {@link VirtualStorageDocument} en {@link Document}.
    * 
    * @param baseDFCE
    *           : La base dfce
    * @param storageDocument
    *           : Un VirtualStorageDocment.
    * @return Un document DFCE à partir d'un VirtualStorageDocment.
    * @throws ParseException
    *            Exception si le parsing de la date ne se passe pas bien.
    * @throws MetadonneeInexistante 
    */
   public static Document virtualStorageDocumentToDfceDocument(
         final Base baseDFCE, final VirtualStorageDocument storageDocument)
         throws ParseException, MetadonneeInexistante {

      Document document = createDocument(storageDocument.getMetadatas(),
            baseDFCE);

      return document;
   }

   private static Document createDocument(List<StorageMetadata> metadatas,
         Base baseDFCE) throws MetadonneeInexistante {
      BaseCategory baseCategory = null;
      Date dateCreation = new Date();
      final Document document = ToolkitFactory.getInstance().createDocumentTag(
            baseDFCE);
      for (StorageMetadata storageMetadata : Utils.nullSafeIterable(metadatas)) {

         // ici on exclut toutes les métadonnées techniques
         final StorageTechnicalMetadatas technical = Utils
               .technicalMetadataFinder(storageMetadata.getShortCode());

         if (technical.getShortCode().equals(
               StorageTechnicalMetadatas.TITRE.getShortCode())) {

            document.setTitle(String.valueOf(storageMetadata.getValue()));
         } else if (technical.getShortCode().equals(
               StorageTechnicalMetadatas.DATE_CREATION.getShortCode())) {

            // Si la date de creation est définie on remplace la date du
            // jour par la dite date
            if (storageMetadata.getValue() != null) {
               dateCreation = (Date) storageMetadata.getValue();
            }
            document.setCreationDate(dateCreation);
         } else if (technical.getShortCode().equals(
               StorageTechnicalMetadatas.TYPE.getShortCode())) {

            document.setType(String.valueOf(storageMetadata.getValue()));
         } else if (technical.getShortCode()
               .equals(
                     StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION
                           .getShortCode())) {
            document.setLifeCycleReferenceDate((Date) storageMetadata
                  .getValue());
         } else if (technical.getShortCode().equals(
               StorageTechnicalMetadatas.NOM_FICHIER.getShortCode())) {
            // On ne fait rien
         } else if (technical.getShortCode().equals(
               StorageTechnicalMetadatas.HASH.getShortCode())) {
            // On ne fait rien

            // FIXME on force le passage en minuscule le HASH
            storageMetadata.setValue(StringUtils
                  .lowerCase((String) storageMetadata.getValue()));

         } else if (technical.getShortCode().equals(
               StorageTechnicalMetadatas.TYPE_HASH.getShortCode())) {
            // On ne fait rien
         } else if (technical.getShortCode().equals(
               StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode())) {
            // On ne fait rien
         } else if (technical.getShortCode().equals(
               StorageTechnicalMetadatas.DOCUMENT_VIRTUEL.getShortCode())) {
            // On ne fait rien
         } else {
            baseCategory = baseDFCE.getBaseCategory(storageMetadata
                  .getShortCode().trim());

            if (baseCategory != null) {
               document.addCriterion(baseCategory, storageMetadata.getValue());
            } else {
               throw new MetadonneeInexistante("La métadonnée "
                     + storageMetadata.getShortCode()
                     + " n'existe pas dans DFCE");
            }
         }
      }

      return document;
   }

   // CHECKSTYLE:ON
   /** Cette classe n'est pas faite pour être instanciée. */
   private BeanMapper() {
      assert false;
   }

   /**
    * Permet d'extraire la métadonnée technique à partir de la métadonnée
    * souhaitée.
    * 
    * @param serviceDFCE
    *           : Les services DFCE.
    * @param document
    *           : le document retourné par DFCE.
    * @param metadata
    *           : La métadonnée désirés.
    * @return
    */
   // CHECKSTYLE:OFF
   private static StorageMetadata completedMetadatas(final Document document,
         final StorageMetadata metadata, final ServiceProvider serviceDFCE) {
      StorageMetadata metadataFound = null;

      final StorageTechnicalMetadatas technical = Utils
            .technicalMetadataFinder(metadata.getShortCode());

      if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DATE_CREATION.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getCreationDate());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DATE_MODIFICATION.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getModificationDate());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getArchivageDate());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getLifeCycleReferenceDate());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DUREE_CONSERVATION.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               serviceDFCE.getStorageAdministrationService().getLifeCycleRule(
                     document.getType()).getLifeCycleLength());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.TITRE.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getTitle());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.TYPE.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getType());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.TYPE_HASH.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getDigestAlgorithm());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.HASH.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getDigest());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.VERSION_NUMBER.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getVersion());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.START_PAGE.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getStartPage());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.END_PAGE.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getEndPage());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.TAILLE_FICHIER.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getSize());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.NOM_FICHIER.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getFilename().concat(".").concat(document.getExtension()));
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DOCUMENT_VIRTUEL.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .isVirtual());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.GEL.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               serviceDFCE.getStoreService().isFrozen(document));
      } else {

         metadataFound = new StorageMetadata(metadata.getShortCode(), "");
      }
      return metadataFound;
   }

   /**
    * Transforme un objet {@link FileReference} en {@link StorageReferenceFile}
    * 
    * @param fileReference
    *           le fichier de référence DFCE
    * @return le fichier de référence métier
    */
   public static StorageReferenceFile fileReferenceToStorageReferenceFile(
         FileReference fileReference) {

      StorageReferenceFile referenceFile = new StorageReferenceFile();
      FileReferenceImpl impl = (FileReferenceImpl) fileReference;

      referenceFile.setDigest(impl.getDigest());
      referenceFile.setDigestAlgorithm(impl.getDigestAlgorithm());
      referenceFile.setExtension(impl.getExtension());
      referenceFile.setName(impl.getName());
      referenceFile.setSize(impl.getSize());
      referenceFile.setUuid(impl.getUuid());

      return referenceFile;

   }

   /**
    * Transforme un objet {@link StorageReferenceFile} en {@link FileReference}
    * 
    * @param fileReference
    *           le fichier de référence métier
    * @return le fichier de référence DFCE
    */
   public static FileReference storageReferenceFileToFileReference(
         StorageReferenceFile referenceFile) {

      FileReferenceImpl impl = new FileReferenceImpl();
      impl.setDigest(referenceFile.getDigest());
      impl.setDigestAlgorithm(referenceFile.getDigestAlgorithm());
      impl.setExtension(referenceFile.getExtension());
      impl.setName(referenceFile.getName());
      impl.setSize(referenceFile.getSize());
      impl.setUuid(referenceFile.getUuid());

      return (FileReference) impl;
   }
}
