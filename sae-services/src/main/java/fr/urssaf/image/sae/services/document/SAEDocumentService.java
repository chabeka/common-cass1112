package fr.urssaf.image.sae.services.document;

import fr.urssaf.image.sae.services.consultation.SAEConsultationService;

/**
 * Fournit l’ensemble des services : <br>
 * <li>
 * {@link SAESearchService Recherche}</li><br>
 * <li>
 * {@link SAEConsultationService Consultation}</li> <li>
 * {@link SAENoteService gestion des notes}</li>
 */
public interface SAEDocumentService extends SAESearchService,
      SAEConsultationService, SAENoteService {
}
