package fr.urssaf.image.sae.services.document.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.batch.BulkCaptureJob;
import fr.urssaf.image.sae.services.batch.BulkCaptureJobWrapper;
import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;

/**
 * Fournit l'implémentation des services pour la capture.<BR />
 * 
 * @author rhofir.
 */
@Service
@Qualifier("saeBulkCaptureService")
public class SAEBulkCaptureServiceImpl implements SAEBulkCaptureService {
	private ThreadPoolTaskExecutor taskExecutor;
	private EcdeServices ecdeServices;
	private BulkCaptureJob bulkCaptureJob;

	/**
	 * 
	 * @param taskExecutor
	 *            : un objet de type {@link ThreadPoolTaskExecutor}
	 */
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	/**
	 * 
	 * @param taskExecutor
	 *            : un objet de type {@link ThreadPoolTaskExecutor}
	 */
	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	/**
	 * {@inheritDoc} puisqu'on est dans une thread séparée
	 */
	public final void bulkCapture(String urlEcde) {
		BulkCaptureJobWrapper bulkWrapper = new BulkCaptureJobWrapper(urlEcde,
				ecdeServices, bulkCaptureJob);
		taskExecutor.execute(bulkWrapper);

	}

	@Autowired
	public SAEBulkCaptureServiceImpl(
			@Qualifier("ecdeServices") final EcdeServices ecdeServices,
			@Qualifier("bulkCaptureJob") final BulkCaptureJob bulkCaptureJob,
			final ThreadPoolTaskExecutor taskExecutor) {
		
		this.ecdeServices = ecdeServices;
		this.bulkCaptureJob = bulkCaptureJob;
		this.taskExecutor = taskExecutor;

	}

}
