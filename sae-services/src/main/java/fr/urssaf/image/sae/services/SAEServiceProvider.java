package fr.urssaf.image.sae.services;

import fr.urssaf.image.sae.services.document.SAEDocumentService;

/**
 * Fournit l’ensemble des services : <br>
 * <li>{@link SAEDocumentService Capture,Recherche,Consultation}</li><br>
 */
public interface SAEServiceProvider {
   /**
    * @return La façade des services : <lu><li>
    *         {@link fr.urssaf.image.sae.services.capture.impl.SAECaptureServiceImpl}
    *         </li><li>
    *         {@link fr.urssaf.image.sae.services.document.impl.SAESearchServiceImpl
    *         Recherche}</li><li>
    *         {@link fr.urssaf.image.sae.services.consultation.impl.SAEConsultationServiceImpl}</li>
    *         <ul>
    */
   SAEDocumentService getSaeDocumentService();

}
