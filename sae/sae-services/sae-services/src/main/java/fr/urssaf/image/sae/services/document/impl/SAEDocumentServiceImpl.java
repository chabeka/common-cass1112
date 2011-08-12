package fr.urssaf.image.sae.services.document.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.exception.SAECaptureServiceEx;
import fr.urssaf.image.sae.exception.SAEConsultationServiceEx;
import fr.urssaf.image.sae.exception.SAESearchServiceEx;
import fr.urssaf.image.sae.model.SAELuceneCriteria;
import fr.urssaf.image.sae.model.UntypedDocument;
import fr.urssaf.image.sae.services.document.SAECaptureService;
import fr.urssaf.image.sae.services.document.SAEConsultationService;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.document.SAESearchService;
import fr.urssaf.image.sae.storage.dfce.annotations.FacadePattern;

/**
 * Fournit la façade des implementations des services :<br>
 * <lu><br>
 * <li>{@link SAECaptureServiceImpl Capture}</li> <br>
 * <li>{@link SAESearchServiceImpl Recherche}</li><li>
 * {@link SAEConsultationServiceImpl Consultation}</li>
 * <ul>
 * 
 * @author akenore,rhofir.
 */
@Service
@Qualifier("saeDocumentService")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "PMD.LongVariable" })
@FacadePattern(participants = { SAECaptureServiceImpl.class,
      SAEConsultationServiceImpl.class, SAESearchServiceImpl.class }, comment = "Fournit les services des classes participantes")
public class SAEDocumentServiceImpl implements SAEDocumentService {

   @Autowired
   @Qualifier("saeCaptureService")
   private SAECaptureService saeCaptureService;
   @Autowired
   @Qualifier("saeConsultationService")
   private SAEConsultationService saeConsultationService;
   @Autowired
   @Qualifier("saeSearchService")
   private SAESearchService saeSearchService;

   /**
    * {@inheritDoc}
    */
   public final void bulkCapture(final String urlEcde)
         throws SAECaptureServiceEx {
      saeCaptureService.bulkCapture(urlEcde);
   }

   /**
    * {@inheritDoc}
    */
   public final String capture(final UntypedDocument unTypedDoc)
         throws SAECaptureServiceEx {
      return saeCaptureService.capture(unTypedDoc);
   }

   /**
    * {@inheritDoc}
    */
   public final List<UntypedDocument> search(
         final SAELuceneCriteria sAELuceneCriteria) throws SAESearchServiceEx {
      return saeSearchService.search(sAELuceneCriteria);
   }

   /**
    * {@inheritDoc}
    */
   public final UntypedDocument consultation(final UntypedDocument untypedDoc)
         throws SAEConsultationServiceEx {
      return saeConsultationService.consultation(untypedDoc);
   }

}
