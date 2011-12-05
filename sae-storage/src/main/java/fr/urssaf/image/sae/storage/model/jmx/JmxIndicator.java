package fr.urssaf.image.sae.storage.model.jmx;
/**
 * Cette classe contient l'ensemble des indicateurs JMX.
 * 
 * @author akenore
 * 
 */

public class JmxIndicator {

	private int jmxStorageIndex;
	private int jmxControlIndex;
	private String jmxExternalIdTreatment;
	private BulkProgress jmxTreatmentState;
	private int jmxCountDocument;

	/**
	 * @return L'index du document en cours de stockage.
	 */
	public final int getJmxStorageIndex() {
		return jmxStorageIndex;
	}

	/**
	 * @param jmxStorageIndex
	 *            : L'index du document en cours de stockage.
	 */
	public final void setJmxStorageIndex(int jmxStorageIndex) {
		this.jmxStorageIndex = jmxStorageIndex;
	}

	/**
	 * @return L'index du document en cours de contrôle.
	 */
	public final int getJmxControlIndex() {
		return jmxControlIndex;
	}

	/**
	 * @param jmxControlIndex
	 *            : L'index du document en cours de contrôle.
	 */
	public final void setJmxControlIndex(int jmxControlIndex) {
		this.jmxControlIndex = jmxControlIndex;
	}

	/**
	 * @return L'id de traitement du client.
	 */
	public final String getJmxExternalIdTreatment() {
		return jmxExternalIdTreatment;
	}

	/**
	 * @param jmxExternalIdTreatment
	 *            : L'id de traitement du client.
	 */
	public final void setJmxExternalIdTreatment(String jmxExternalIdTreatment) {
		this.jmxExternalIdTreatment = jmxExternalIdTreatment;
	}

	/**
	 * @return L'état d'avancement du traitement en masse.
	 */
	public final BulkProgress getJmxTreatmentState() {
		return jmxTreatmentState;
	}

	/**
	 * @param jmxTreatmentState
	 *            : L'état d'avancement du traitement en masse.
	 */
	public final void setJmxTreatmentState(BulkProgress jmxTreatmentState) {
		this.jmxTreatmentState = jmxTreatmentState;
	}

	/**
	 * @return Le nombre de document.
	 */
	public final int getJmxCountDocument() {
		return jmxCountDocument;
	}

	/**
	 * @param jmxCountDocument
	 *            : Le nombre de document.
	 */
	public final void setJmxCountDocument(int jmxCountDocument) {
		this.jmxCountDocument = jmxCountDocument;
	}
}