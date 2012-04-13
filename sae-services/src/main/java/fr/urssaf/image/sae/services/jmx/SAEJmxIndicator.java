package fr.urssaf.image.sae.services.jmx;

/**
 * interface fournissant les indicateurs jmx du système d'archivage.
 * 
 * @author akenore
 * 
 */
public interface SAEJmxIndicator {
	/**
	 * 
	 *@return Retourne l'id de traitement du client.
	 */
	String retrieveExternalTreatmentId();
	/**
	 * 
	 * @return Retourne l'état d'avancement de l'archivage en masse.
	 */
	String retrieveBulkProgress();

}
