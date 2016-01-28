/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.lang.ArrayUtils;

import fr.urssaf.image.sae.bo.model.AbstractMetadata;
import fr.urssaf.image.sae.bo.model.untyped.PaginatedUntypedDocuments;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentAttachment;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentAttachmentEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentNoteException;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationAffichableParametrageException;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;

/**
 * 
 * 
 */
public class SAEDocumentTestImpl implements SAEDocumentService {

   private Object[] consultUUIDResult;
   private int consultUUIDIndex = 0;
   private Object[] consultParamsResult;
   private int consultParamsIndex = 0;
   private Object[] searchResult;
   private int searchIndex = 0;
   private Object[] searchMaxResult;
   private int searchMaxIndex = 0;

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument consultation(UUID idArchive)
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {

      Object object = getResult(consultUUIDResult, consultUUIDIndex);

      if (object == null) {
         return null;
      } else if (object instanceof SAEConsultationServiceException) {
         throw (SAEConsultationServiceException) object;
      } else if (object instanceof UnknownDesiredMetadataEx) {
         throw (UnknownDesiredMetadataEx) object;
      } else if (object instanceof MetaDataUnauthorizedToConsultEx) {
         throw (MetaDataUnauthorizedToConsultEx) object;
      }

      return (UntypedDocument) object;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument consultation(ConsultParams consultParams)
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {

      Object object = getResult(consultParamsResult, consultParamsIndex);

      if (object == null) {
         return null;
      } else if (object instanceof SAEConsultationServiceException) {
         throw (SAEConsultationServiceException) object;
      } else if (object instanceof UnknownDesiredMetadataEx) {
         throw (UnknownDesiredMetadataEx) object;
      } else if (object instanceof MetaDataUnauthorizedToConsultEx) {
         throw (MetaDataUnauthorizedToConsultEx) object;
      }

      return (UntypedDocument) object;
   }

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public final List<UntypedDocument> search(String requete,
         List<String> listMetaDesired) throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx, SAESearchServiceEx {

      Object object = getResult(searchResult, searchIndex);

      if (object == null) {
         return null;
      } else if (object instanceof MetaDataUnauthorizedToSearchEx) {
         throw (MetaDataUnauthorizedToSearchEx) object;
      } else if (object instanceof MetaDataUnauthorizedToConsultEx) {
         throw (MetaDataUnauthorizedToConsultEx) object;
      } else if (object instanceof UnknownDesiredMetadataEx) {
         throw (UnknownDesiredMetadataEx) object;
      } else if (object instanceof UnknownLuceneMetadataEx) {
         throw (UnknownLuceneMetadataEx) object;
      } else if (object instanceof SyntaxLuceneEx) {
         throw (SyntaxLuceneEx) object;
      } else if (object instanceof SAESearchServiceEx) {
         throw (SAESearchServiceEx) object;
      }

      return (List<UntypedDocument>) object;
   }

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public final List<UntypedDocument> search(String requete,
         List<String> listMetaDesired, int maxResult)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx, SAESearchServiceEx {

      Object object = getResult(searchMaxResult, searchMaxIndex);

      if (object == null) {
         return null;
      } else if (object instanceof MetaDataUnauthorizedToSearchEx) {
         throw (MetaDataUnauthorizedToSearchEx) object;
      } else if (object instanceof MetaDataUnauthorizedToConsultEx) {
         throw (MetaDataUnauthorizedToConsultEx) object;
      } else if (object instanceof UnknownDesiredMetadataEx) {
         throw (UnknownDesiredMetadataEx) object;
      } else if (object instanceof UnknownLuceneMetadataEx) {
         throw (UnknownLuceneMetadataEx) object;
      } else if (object instanceof SyntaxLuceneEx) {
         throw (SyntaxLuceneEx) object;
      } else if (object instanceof SAESearchServiceEx) {
         throw (SAESearchServiceEx) object;
      }

      return (List<UntypedDocument>) object;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public PaginatedUntypedDocuments searchPaginated(
         List<UntypedMetadata> fixedMetadatas,
         UntypedRangeMetadata varyingMetadata,
         List<AbstractMetadata> equalsFilters,
         List<AbstractMetadata> notEqualsFilters, int nbDocumentsParPage,
         UUID lastIdDoc, List<String> listeDesiredMetadata)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx {

      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument consultationAffichable(
         ConsultParams consultParams) throws SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException {

      Object object = getResult(consultParamsResult, consultParamsIndex);

      if (object == null) {
         return null;
      } else if (object instanceof SAEConsultationServiceException) {
         throw (SAEConsultationServiceException) object;
      } else if (object instanceof UnknownDesiredMetadataEx) {
         throw (UnknownDesiredMetadataEx) object;
      } else if (object instanceof MetaDataUnauthorizedToConsultEx) {
         throw (MetaDataUnauthorizedToConsultEx) object;
      }

      return (UntypedDocument) object;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDocumentNote(UUID docUuid, String contenu, String login)
         throws SAEDocumentNoteException, ArchiveInexistanteEx {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<StorageDocumentNote> getDocumentNotes(UUID docUuid)
         throws SAEDocumentNoteException {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * @param searchMaxResult2
    * @return
    * @throws Exception
    * @throws Throwable
    */
   private Object getResult(Object[] objects, int index) {

      if (ArrayUtils.isEmpty(objects) || index >= objects.length) {
         throw new RuntimeException(
               "impossible de récupérer le comportement attendu");
      }

      Object object = objects[index];
      index++;

      return object;
   }

   /**
    * @param consultUUIDResult
    *           the consultUUIDResult to set
    */
   public final void setConsultUUIDResult(Object[] consultUUIDResult) {
      this.consultUUIDResult = consultUUIDResult;
   }

   /**
    * @param consultParamsResult
    *           the consultParamsResult to set
    */
   public final void setConsultParamsResult(Object[] consultParamsResult) {
      this.consultParamsResult = consultParamsResult;
   }

   /**
    * @param searchResult
    *           the searchResult to set
    */
   public final void setSearchResult(Object[] searchResult) {
      this.searchResult = searchResult;
   }

   /**
    * @param searchMaxResult
    *           the searchMaxResult to set
    */
   public final void setSearchMaxResult(Object[] searchMaxResult) {
      this.searchMaxResult = searchMaxResult;
   }

   @Override
   public void addDocumentAttachmentBinaire(UUID docUuid, String docName,
         String extension, DataHandler contenu) throws SAEDocumentAttachmentEx,
         ArchiveInexistanteEx, EmptyDocumentEx, EmptyFileNameEx {
      // TODO Auto-generated method stub

   }

   @Override
   public void addDocumentAttachmentUrl(UUID docUuid, URI ecdeURL)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx {
      // TODO Auto-generated method stub

   }

   @Override
   public UntypedDocumentAttachment getDocumentAttachment(UUID docUuid)
         throws SAEDocumentAttachmentEx {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void addDocumentAttachmentBinaireRollbackParent(UUID docUuid,
         String docName, String extension, DataHandler contenu)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx, EmptyDocumentEx,
         EmptyFileNameEx {
      // TODO Auto-generated method stub

   }

   @Override
   public void addDocumentAttachmentUrlRollbackParent(UUID docUuid, URI ecdeURL)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx,
         CaptureBadEcdeUrlEx {
      // TODO Auto-generated method stub

   }

}
