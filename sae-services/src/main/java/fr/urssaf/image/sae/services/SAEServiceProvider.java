package fr.urssaf.image.sae.services;

import fr.urssaf.image.sae.services.document.SAEDocumentService;

/**
 * Fournit l’ensemble des services : <br>
 * <li>{@link SAEDocumentService Capture,Recherche,Consultation}</li><br>
 */
public interface SAEServiceProvider {
   /**
    * @return La façade des services : <lu><br>
    *         <li>
    *         {@link fr.urssaf.image.sae.services.document.impl.SAECaptureServiceImpl
    *         Capture}</li> <br>
    *         <li>
    *         {@link fr.urssaf.image.sae.services.document.impl.SAESearchServiceImpl
    *         Recherche}</li><li>
    *         {@link SAEConsultationServiceImpl
    *         Consultation}</li>
    *         <ul>
    */
   SAEDocumentService getSaeDocumentService();

}
