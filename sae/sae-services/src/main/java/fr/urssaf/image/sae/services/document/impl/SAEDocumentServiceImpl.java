package fr.urssaf.image.sae.services.document.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.AbstractMetadata;
import fr.urssaf.image.sae.bo.model.untyped.PaginatedUntypedDocuments;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.services.capture.impl.SAECaptureServiceImpl;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.consultation.impl.SAEConsultationServiceImpl;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.document.SAENoteService;
import fr.urssaf.image.sae.services.document.SAESearchService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentNoteException;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationAffichableParametrageException;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.search.DoublonFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.storage.dfce.annotations.FacadePattern;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;

/**
 * Fournit la façade des implementations des services :<br>
 * <lu> <li>
 * {@link fr.urssaf.image.sae.services.capture.impl.SAECaptureServiceImpl}</li>
 * <li>{@link fr.urssaf.image.sae.services.document.impl.SAESearchServiceImpl}</li>
 * <li>
 * {@link fr.urssaf.image.sae.services.consultation.impl.SAEConsultationServiceImpl}
 * </li>
 * <ul>
 */
@Service
@Qualifier("saeDocumentService")
@FacadePattern(participants = { SAECaptureServiceImpl.class,
      SAEConsultationServiceImpl.class, SAESearchServiceImpl.class }, comment = "Fournit les services des classes participantes")
public class SAEDocumentServiceImpl implements SAEDocumentService {

   // @Autowired
   // @Qualifier("saeCaptureService")
   // private SAECaptureService saeCaptureService;
   @Autowired
   @Qualifier("saeConsultationService")
   private SAEConsultationService saeConsultationService;
   @Autowired
   @Qualifier("saeSearchService")
   private SAESearchService saeSearchService;
   @Autowired
   @Qualifier("saeNoteService")
   private SAENoteService saeNoteService;

   /**
    * {@inheritDoc}
    */
   public final List<UntypedDocument> search(final String requete,
         final List<String> listMetaDesired) throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      return saeSearchService.search(requete, listMetaDesired);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument consultation(UUID idArchive)
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {
      return saeConsultationService.consultation(idArchive);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument consultation(ConsultParams consultParams)
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {
      return saeConsultationService.consultation(consultParams);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<UntypedDocument> search(String requete,
         List<String> listMetaDesired, int maxResult)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx, SAESearchServiceEx {
      return saeSearchService.search(requete, listMetaDesired, maxResult);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument consultationAffichable(
         ConsultParams consultParams) throws SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException {
      return saeConsultationService.consultationAffichable(consultParams);
   }

   /**
    * {@inheritDoc}
    * 
    * @throws UnknownFiltresMetadataEx
    * @throws DoublonFiltresMetadataEx 
    */
   @Override
   public final PaginatedUntypedDocuments searchPaginated(
         List<UntypedMetadata> fixedMetadatas,
         UntypedRangeMetadata varyingMetadata,
         List<AbstractMetadata> listeFiltreEgalite,
         List<AbstractMetadata> listeFiltreDifferent, int nbDocumentsParPage,
         UUID lastIdDoc, List<String> listeDesiredMetadata)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {
      return saeSearchService.searchPaginated(fixedMetadatas, varyingMetadata,
            listeFiltreEgalite, listeFiltreDifferent, nbDocumentsParPage,
            lastIdDoc, listeDesiredMetadata);
   }

   /**
    * {@inheritDoc}
    * 
    * @throws ArchiveInexistanteEx
    */
   @Override
   public final void addDocumentNote(UUID docUuid, String contenu, String login)
         throws SAEDocumentNoteException, ArchiveInexistanteEx {
      saeNoteService.addDocumentNote(docUuid, contenu, login);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<StorageDocumentNote> getDocumentNotes(UUID docUuid)
         throws SAEDocumentNoteException {
      return saeNoteService.getDocumentNotes(docUuid);
   }

}
