package fr.urssaf.image.sae.services.jmx.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;
import fr.urssaf.image.sae.services.jmx.SAEJmxIndicator;
import fr.urssaf.image.sae.storage.model.jmx.BulkProgress;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;

/**
 * Implémente les services Jmx
 * 
 * @author akenore
 * 
 */
@Component
@Qualifier("jmxIndicatorService")
public class SAEJmxIndicatorImpl implements SAEJmxIndicator {
	@Autowired
	@Qualifier("saeBulkCaptureService")
	private SAEBulkCaptureService bulkCaptureService;

	/**
	 * {@inheritDoc}
	 */
	public String retrieveExternalTreatmentId() {
		String IdTreatement = BulkProgress.NO_TREATMENT_ID.toString();
		if (bulkCaptureService.isActive()) {
			JmxIndicator indicator = bulkCaptureService
					.retrieveJmxSAEBulkCaptureIndicator();
			if (indicator != null) {
				if (!indicator.getJmxTreatmentState().equals(
						BulkProgress.READING_DOCUMENTS)
						&& !indicator.getJmxTreatmentState().equals(
								BulkProgress.BEGIN_OF_ARCHIVE)) {
					IdTreatement = indicator.getJmxExternalIdTreatment();
				} else {
					IdTreatement = BulkProgress.NO_TREATMENT_ID_FOUND_BEFORE_READING.getMessage();
				}
			}
		} else {
			IdTreatement = BulkProgress.NO_TREATMENT_ID_NO_BULK_STORAGE_BEING
					.getMessage();
		}
		return IdTreatement;
	}

	/**
	 * {@inheritDoc}
	 */
	public String retrieveBulkProgress() {
		String progress = BulkProgress.NO_BULK_STORAGE_BEING.getMessage();
		if (bulkCaptureService.isActive()) {
			JmxIndicator indicator = bulkCaptureService
					.retrieveJmxSAEBulkCaptureIndicator();
			if (indicator != null) {
				progress = indicator.getJmxTreatmentState().getMessage();
				if (indicator.getJmxTreatmentState().equals(
						BulkProgress.INSERTION_DOCUMENTS)
						|| indicator.getJmxTreatmentState().equals(
								BulkProgress.DELETION_DOCUMENTS)) {
					if (indicator.getJmxStorageIndex() != 0
							&& indicator.getJmxCountDocument() != 0) {
						progress = formatMessage(progress,
								indicator.getJmxStorageIndex(),
								indicator.getJmxCountDocument());
					}
				} else if (indicator.getJmxTreatmentState().equals(
						BulkProgress.CONTROL_DOCUMENTS)) {
					progress = formatMessage(progress,
							indicator.getJmxControlIndex(),
							indicator.getJmxCountDocument());
				}
			}
		}
		return progress;
	}

	/**
	 * Permet de formatter le message.
	 * 
	 * @param progress
	 *            : L'etat d'avancement.
	 * @param min
	 *            : Le minimum.
	 * @param max
	 *            : Le maximum.
	 * @return le Message jmx.
	 */
	private String formatMessage(final String progress, final int min,
			final int max) {
		StringBuilder sb = new StringBuilder();
		sb.append(progress);
		sb.append(" [ ");
		sb.append(String.valueOf(min));
		sb.append(" document(s)");
		sb.append(" sur ");
		sb.append(String.valueOf(max));
		sb.append(" document(s)");
		sb.append(" ]");
		return sb.toString();
	}

	/**
	 * @param bulkCaptureService
	 *            : Capture en masse.
	 */
	public void setBulkCaptureService(SAEBulkCaptureService bulkCaptureService) {
		this.bulkCaptureService = bulkCaptureService;
	}

	/**
	 * @return Capture en masse
	 */
	public SAEBulkCaptureService getBulkCaptureService() {
		return bulkCaptureService;
	}

}
