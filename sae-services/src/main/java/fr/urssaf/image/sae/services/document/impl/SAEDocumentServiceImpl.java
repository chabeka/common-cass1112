package fr.urssaf.image.sae.services.document.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capture.impl.SAECaptureServiceImpl;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.consultation.impl.SAEConsultationServiceImpl;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.document.SAESearchService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.storage.dfce.annotations.FacadePattern;

/**
 * Fournit la façade des implementations des services :<br>
 * <lu><br>
 * <li>{@link SAECaptureServiceImpl Capture}</li> <br>
 * <li>{@link SAESearchServiceImplOld Recherche}</li><li>
 * {@link SAEConsultationServiceImpl Consultation}</li>
 * <ul>
 * 
 * @author akenore,rhofir, lbaadj.
 */
@Service
@Qualifier("saeDocumentService")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "PMD.LongVariable" })
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
}
