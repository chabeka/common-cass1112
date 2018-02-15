package fr.urssaf.image.sae.storage.dfce.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.activation.DataHandler;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.note.Note;
import net.docubase.toolkit.model.reference.ContentRepository;
import net.docubase.toolkit.model.reference.ContentRepository.State;
import net.docubase.toolkit.model.reference.FileReference;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.urssaf.image.sae.commons.utils.InputStreamSource;
import fr.urssaf.image.sae.storage.dfce.exception.MetadonneeInexistante;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageContentRepository;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Fournit des méthodes statiques de conversion des elements DFCE ceux du SAE.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
public final class BeanMapper {

   /**
    * Liste des métadonnées pour lesquelles ne réaliser aucun traitement
    */
   private static final List<String> metasWithoutProcess = Arrays.asList(
         StorageTechnicalMetadatas.NOM_FICHIER.getShortCode(),
         StorageTechnicalMetadatas.TYPE_HASH.getShortCode(),
         StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode(),
         StorageTechnicalMetadatas.DOCUMENT_VIRTUEL.getShortCode(),
         StorageTechnicalMetadatas.NOTE.getShortCode());

   /**
    * Permet de convertir un {@link Document} en {@link StorageDocument}.
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
      return dfceDocumentToStorageDocument(document, desiredMetaDatas,
            serviceDFCE, StringUtils.EMPTY, forConsultion, false);
   }

   /**
    * Permet de convertir un {@link Document} en {@link StorageDocument}.
    * 
    * @param document
    *           : Le document DFCE.
    * @param desiredMetaDatas
    *           : Les métadonnées souhaitées.
    * @param serviceDFCE
    *           : Les services DFCE.
    * @param nomPlateforme
    *           Nom d'instance de la plateforme
    * @param forConsultation
    *           : Paramètre pour remonter l'instance de la plateforme sur
    *           laquelle est réalisée la recherche
    * @param isDocContentAdd
    *           : Paramètre pour récupérer le contenue des documents pour la
    *           consultation.
    * @return une occurrence de StorageDocument
    * @throws StorageException
    *            : Exception levée lorsque qu'un dysfonctionnement se produit.
    * @throws IOException
    *            : Exception levée lorsque qu'un dysfonctionnement se produit
    *            lors des I/O.
    */
   public static StorageDocument dfceDocumentToStorageDocument(
         final Document document, final List<StorageMetadata> desiredMetaDatas,
         final ServiceProvider serviceDFCE, String nomPlateforme,
         boolean forConsultation, boolean isDocContentAdd)
         throws StorageException, IOException {
      // on construit la liste des métadonnées à partir de la liste des
      // métadonnées souhaitées.
      final List<StorageMetadata> metaDatas = storageMetaDatasFromCriterions(
            document, desiredMetaDatas, serviceDFCE, nomPlateforme,
            forConsultation);
      return buildStorageDocument(document, metaDatas, serviceDFCE,
            isDocContentAdd, false);
   }

   /**
    * Permet de convertir un {@link Document} en {@link StorageDocument}.
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
   public static StorageDocument dfceDocumentFromRecycleBinToStorageDocument(
         final Document document, final List<StorageMetadata> desiredMetaDatas,
         final ServiceProvider serviceDFCE, boolean forConsultion,
         boolean isDocContentAdd)
               throws StorageException, IOException {
      // on construit la liste des métadonnées à partir de la liste des
      // métadonnées souhaitées.
      final List<StorageMetadata> metaDatas = storageMetaDatasFromCriterions(
            document, desiredMetaDatas, serviceDFCE, StringUtils.EMPTY,
            forConsultion);
      return buildStorageDocument(document, metaDatas, serviceDFCE,
            isDocContentAdd, true);
   }

   /**
    * Permet de convertir les métadonnées DFCE vers les métadonnées
    * StorageDocument.
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
            document, desiredMetaData, serviceDFCE, StringUtils.EMPTY, false);
      // Création du storage document et ajout des metadonnées
      StorageDocument storageDocument = new StorageDocument(metaDatas);
      // Renseigne l'uuid du document
      if (document != null) {
         storageDocument.setUuid(document.getUuid());
      }

      return storageDocument;
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
    * @param nomPlateforme
    *           Nom d'instance de la plateforme
    * @param forConsultation
    *           : Paramètre pour remonter l'instance de la plateforme sur
    *           laquelle est réalisée la recherche.
    * @return La liste des {@link StorageMetadata} à partir de la liste des
    *         {@link Criterion}.
    * @throws JsonProcessingException
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   private static List<StorageMetadata> storageMetaDatasFromCriterions(
         final Document document, final List<StorageMetadata> desiredMetaData,
         final ServiceProvider serviceDFCE, String nomPlateforme,
         boolean forConsultation)
         throws JsonProcessingException {
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

               // Récupération éventuelle des notes du document
               if (StorageTechnicalMetadatas.NOTE.getShortCode().equals(
                     metadata.getShortCode())) {
                  if (document.hasNote()) {
                     List<StorageDocumentNote> listeStorageDocNotes = new ArrayList<StorageDocumentNote>();
                     List<Note> listeNote = serviceDFCE.getNoteService()
                           .getNotes(document.getUuid());

                     for (Note note : listeNote) {
                        listeStorageDocNotes.add(BeanMapper
                              .dfceNoteToStorageDocumentNote(note));
                     }
                     // Transformation de la liste des notes en JSON
                     ObjectMapper mapper = new ObjectMapper();
                     String listeNotesJSON = mapper
                           .writeValueAsString(listeStorageDocNotes);
                     metadatas.add(new StorageMetadata(metadata.getShortCode(),
                           listeNotesJSON));
                  } else {
                     List<StorageDocumentNote> listeStorageDocNotes = new ArrayList<StorageDocumentNote>();
                     // Transformation de la liste des notes en JSON
                     ObjectMapper mapper = new ObjectMapper();
                     String listeNotesJSON = mapper
                           .writeValueAsString(listeStorageDocNotes);
                     metadatas.add(new StorageMetadata(metadata.getShortCode(),
                           listeNotesJSON));
                  }
                  found = true;
               }

               // si les métadonnées ne sont pas dans DFCE on vérifie si
               // ces
               // métadonnées sont des métadonnées techniques sinon on les
               // retourne avec la valeur vide
               if (!found) {
                  metadatas.add(completedMetadatas(document, metadata,
                        serviceDFCE, nomPlateforme, forConsultation));
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
    * @param isDocContentAdd
    *           : Paramtére pour charger le contenue du document.
    * @param fromRecyclebin
    *           : True si le document provient de la corbeille, false sinon
    * 
    * @throws IOException
    *            Exception levée lorsque qu'un dysfonctionnement se produit lors
    *            des I/O.
    */
   private static StorageDocument buildStorageDocument(final Document document,
         final List<StorageMetadata> listMetaData,
         final ServiceProvider serviceDFCE, boolean isDocContentAdd,
         boolean fromRecyclebin)
               throws IOException {
      // Instance de StorageDocument
      final StorageDocument storageDocument = new StorageDocument();
      if (document != null) {
         if (isDocContentAdd) {
            InputStream docContent = null;
            if (fromRecyclebin) {
               docContent = serviceDFCE.getRecycleBinService().getDocumentFile(
                     document);
            } else {
               docContent = serviceDFCE.getStoreService().getDocumentFile(
                     document);
            }
            InputStreamSource source = new InputStreamSource(docContent);
            storageDocument.setContent(new DataHandler(source));
         }
         String filename = document.getFilename() + "."
               + document.getExtension();
         storageDocument.setCreationDate(document.getCreationDate());
         storageDocument.setTitle(document.getTitle());
         storageDocument.setUuid(document.getUuid());
         storageDocument.setMetadatas(listMetaData);
         storageDocument.setFileName(filename);
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
    * @param file
    *           : tableau contenant le nom du fichier et l'extension
    * @return Un document DFCE à partir d'un storageDocment.
    * @throws ParseException
    *            Exception si le parsing de la date ne se passe pas bien.
    * @throws MetadonneeInexistante
    *            Exception levée si la métadonnée n'exista pas dans DFCE
    */
   // CHECKSTYLE:OFF
   public static Document storageDocumentToDfceDocument(final Base baseDFCE,
         final StorageDocument storageDocument, String[] file)
               throws ParseException, MetadonneeInexistante {

      Document document = createDocument(storageDocument.getMetadatas(),
            baseDFCE, file);

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
    *            Exception levée si la métadonnée n'exista pas dans DFCE
    */
   public static Document virtualStorageDocumentToDfceDocument(
         final Base baseDFCE, final VirtualStorageDocument storageDocument)
               throws ParseException, MetadonneeInexistante {

      String[] file = { storageDocument.getReferenceFile().getName(),
            storageDocument.getReferenceFile().getExtension() };

      Document document = createDocument(storageDocument.getMetadatas(),
            baseDFCE, file);

      return document;
   }

   private static Document createDocument(List<StorageMetadata> metadatas,
         Base baseDFCE, String[] file) throws MetadonneeInexistante {
      BaseCategory baseCategory = null;
      Date dateCreation = new Date();
      final Document document = ToolkitFactory.getInstance().createDocument(
            baseDFCE, file[0], file[1]);

      for (StorageMetadata storageMetadata : Utils.nullSafeIterable(metadatas)) {

         // ici on exclut toutes les métadonnées techniques
         final StorageTechnicalMetadatas technical = Utils
               .technicalMetadataFinder(storageMetadata.getShortCode());

         if (technical.getShortCode().equals(
               StorageTechnicalMetadatas.IDGED.getShortCode())) {
            // -- On définit l'uuid du document si fournit dans la liste des
            // métadonnées
            document.setUuid((UUID) storageMetadata.getValue());

         } else if (technical.getShortCode().equals(
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
               StorageTechnicalMetadatas.HASH.getShortCode())) {
            // On ne fait rien

            // FIXME on force le passage en minuscule le HASH
            storageMetadata.setValue(StringUtils
                  .lowerCase((String) storageMetadata.getValue()));

         } else if (!metasWithoutProcess.contains(technical.getShortCode())) {
            baseCategory = baseDFCE.getBaseCategory(storageMetadata
                  .getShortCode().trim());

            if (baseCategory == null) {
               throw new MetadonneeInexistante("La métadonnée "
                     + storageMetadata.getShortCode()
                     + " n'existe pas dans DFCE");

            } else {
               document.addCriterion(baseCategory, storageMetadata.getValue());
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
    * @param nomPlateforme
    *           Nom d'instance de la plateforme
    * @param forConsultation
    * @return Le bean {@link StorageMetadata} représentant la métadonnée traitée
    */
   // CHECKSTYLE:OFF
   private static StorageMetadata completedMetadatas(final Document document,
         final StorageMetadata metadata, final ServiceProvider serviceDFCE,
         String nomPlateforme, boolean forConsultation) {
      StorageMetadata metadataFound = null;

      final StorageTechnicalMetadatas technical = Utils
            .technicalMetadataFinder(metadata.getShortCode());

      if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.IDGED.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getUuid());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DATE_CREATION.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getCreationDate());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DATE_MODIFICATION.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getModificationDate());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DATE_ARCHIVE.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getArchivageDate());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getLifeCycleReferenceDate());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DUREE_CONSERVATION.getShortCode())) {
         // Depuis DFCe 1.7.0, le cycle de vie peut comporter des etapes
         // Coté Ged Nationale, nous n'en aurons qu'une seule
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               serviceDFCE.getStorageAdministrationService()
               .getLifeCycleRule(document.getType()).getSteps().get(0)
               .getLength());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.TITRE.getShortCode())) {

         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getTitle());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.TYPE.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getType());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.TYPE_HASH.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getDigestAlgorithm());

      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.HASH.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getDigest());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.VERSION_NUMBER.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getVersion());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.START_PAGE.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getStartPage());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.END_PAGE.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getEndPage());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.TAILLE_FICHIER.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.getSize());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.NOM_FICHIER.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(), document
               .getFilename().concat(".").concat(document.getExtension()));
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DOCUMENT_VIRTUEL.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               document.isVirtual());
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.GEL.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               serviceDFCE.getStoreService().isFrozen(document));
      } else if (technical.getShortCode().equals(
            StorageTechnicalMetadatas.DOC_FORMAT_ORIGINE.getShortCode())) {
         if (document.getAttachments().size() > 0) {
            metadataFound = new StorageMetadata(metadata.getShortCode(), true);
         } else {
            metadataFound = new StorageMetadata(metadata.getShortCode(), false);
         }
         // On ne traite la metadonnée Instance que lorsque l'on est en
         // recherche ou consultation.
      } else if (forConsultation
            && technical.getShortCode().equals(
            StorageTechnicalMetadatas.NOM_INSTANCE_PLATEFORME.getShortCode())) {
         metadataFound = new StorageMetadata(metadata.getShortCode(),
               nomPlateforme);
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
      FileReference impl = fileReference;

      referenceFile.setDigest(impl.getDigest());
      referenceFile.setDigestAlgorithm(impl.getDigestAlgorithm());
      referenceFile.setExtension(impl.getExtension());
      referenceFile.setName(impl.getName());
      referenceFile.setSize(impl.getSize());
      referenceFile.setUuid(impl.getUuid());

      StorageContentRepository contentRepo = new StorageContentRepository();
      contentRepo.setName(impl.getContentRepository().getName());
      contentRepo
      .setColumnFamily(impl.getContentRepository().getColumnFamily());
      contentRepo.setState(impl.getContentRepository().getState().getState());
      referenceFile.setContentRepository(contentRepo);

      return referenceFile;

   }

   /**
    * Transforme un objet {@link StorageReferenceFile} en {@link FileReference}
    * 
    * @param referenceFile
    *           le fichier de référence métier
    * @return le fichier de référence DFCE
    */
   public static FileReference storageReferenceFileToFileReference(
         StorageReferenceFile referenceFile) {

      FileReference impl = new FileReference();
      impl.setDigest(referenceFile.getDigest());
      impl.setDigestAlgorithm(referenceFile.getDigestAlgorithm());
      impl.setExtension(referenceFile.getExtension());
      impl.setName(referenceFile.getName());
      impl.setSize(referenceFile.getSize());
      impl.setUuid(referenceFile.getUuid());

      ContentRepository contentRepo = new ContentRepository();
      contentRepo.setName(referenceFile.getContentRepository().getName());
      contentRepo.setColumnFamily(referenceFile.getContentRepository()
            .getColumnFamily());
      if (referenceFile.getContentRepository().getState() == 0) {
         contentRepo.setState(State.PENDING);
      } else if (referenceFile.getContentRepository().getState() == 1) {
         contentRepo.setState(State.MOUNTED);
      }
      impl.setContentRepository(contentRepo);

      return impl;
   }

   /**
    * Transforme un objet {@link Note} en {@link StorageDocumentNote}
    * 
    * @param note
    *           La note DFCE à convertir
    * @return L'objet StorageDocumentNote correspondant à l'objet Note
    */
   public static StorageDocumentNote dfceNoteToStorageDocumentNote(Note note) {
      StorageDocumentNote storageDocNote = new StorageDocumentNote(
            note.getUuid(), note.getDocUUID(), note.getContent(),
            note.getCreationDate(), note.getAlias());

      return storageDocNote;
   }

}
