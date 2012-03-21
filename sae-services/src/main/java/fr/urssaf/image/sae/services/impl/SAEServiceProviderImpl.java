package fr.urssaf.image.sae.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.SAEServiceProvider;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.storage.dfce.annotations.FacadePattern;

/**
 * Fournit la façade des implementations des services :<br>
 * <lu><br>
 * <li>{@link SAECaptureServiceImpl Capture}</li> <br>
 * <li>{@link SAESearchServiceImplOld Recherche}</li><li>
 * {@link SAEConsultationServiceImpl Consultation}</li>
 * <ul>
 * 
 * @author akenore,rhofir.
 */
@Service
@Qualifier("saeServiceProvider")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "PMD.LongVariable" })
@FacadePattern(participants = { SAEDocumentService.class }, comment = "Fournit les services des classes participantes")
public class SAEServiceProviderImpl implements SAEServiceProvider {
   @Autowired
   @Qualifier("saeDocumentService")
   private SAEDocumentService saeDocumentService;

   /**
    * @return La façade des services : <lu><br>
    *         <li>
    *         {@link fr.urssaf.image.sae.services.document.impl.SAECaptureServiceImpl
    *         Capture}</li> <br>
    *         <li>
    *         {@link fr.urssaf.image.sae.services.document.impl.SAESearchServiceImplOld
    *         Recherche}</li><li>
    *         {@link fr.urssaf.image.sae.services.consultation.impl.SAEConsultationServiceImpl
    *         Consultation}</li>
    *         <ul>
    */
   public final SAEDocumentService getSaeDocumentService() {
      return saeDocumentService;
   }

   /**
    * @param saeDocumentService
    *           : La façade des services :<lu><br>
    *           <li>{@link SAECaptureServiceImpl Capture}</li> <br>
    *           <li>{@link SAESearchServiceImplOld Recherche}</li><li>
    *           {@link SAEConsultationServiceImpl Consultation}</li>
    *           <ul>
    */
   public final void setSaeDocumentService(SAEDocumentService saeDocumentService) {
      this.saeDocumentService = saeDocumentService;
   }

}
