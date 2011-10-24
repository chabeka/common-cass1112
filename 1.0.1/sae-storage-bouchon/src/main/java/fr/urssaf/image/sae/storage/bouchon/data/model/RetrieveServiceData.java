package fr.urssaf.image.sae.storage.bouchon.data.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Classe permettant de désérialiser les données définies pour les services de
 * récupération. <li>Attribut retrieveDoc : Donnée pour le service de
 * récupération du flux binaire</li> <li>Attribut retrieveMetaData : Données
 * pour le service de récupération des métadonnées</li> <li>Attribut
 * retrieveDocMetaData : Données pour le service de récupération du flux binaire
 * et des métadonnées</li>
 * 
 * @author akenore
 * 
 */
@XStreamAlias(value = "retrieveServiceData")
public class RetrieveServiceData {
	@XStreamImplicit(itemFieldName = "retrieveDoc")
	private List<RetrieveDoc> retrieveDoc;
	@XStreamImplicit(itemFieldName = "retrieveMetaData")
	private List<RetrieveMetaData> retrieveMetaData;
	@XStreamImplicit(itemFieldName = "retrieveDocMetaData")
	@SuppressWarnings("PMD.LongVariable")
	private List<RetrieveDocMetaData> retrieveDocMetaData;

	/**
	 * Constructeur
	 * 
	 * @param retrieveDoc
	 *            : Donnée pour le service de récupération du flux binaire
	 * @param retrieveMetaData
	 *            : Données pour le service de récupération des métadonnées
	 * @param retrieveDocMetaData
	 *            :Données pour le service de récupération du flux binaire et
	 *            des métadonnées
	 */
	@SuppressWarnings("PMD.LongVariable")
	public RetrieveServiceData(final List<RetrieveDoc> retrieveDoc,
			final List<RetrieveMetaData> retrieveMetaData,
			final List<RetrieveDocMetaData> retrieveDocMetaData) {
		this.retrieveDoc = retrieveDoc;
		this.retrieveMetaData = retrieveMetaData;
		this.retrieveDocMetaData = retrieveDocMetaData;
	}

	/**
	 * Initialise la donnée pour le service de récupération du flux binaire
	 * 
	 * @param retrieveDoc
	 *            : Donnée pour le service de récupération du flux binaire
	 */
	public final void setRetrieveDoc(final List<RetrieveDoc> retrieveDoc) {
		this.retrieveDoc = retrieveDoc;
	}

	/**
	 * Retourne la donnée pour le service de récupération du flux binaire
	 * 
	 * @return La donnée pour le service de récupération du flux binaire
	 */
	public final List<RetrieveDoc> getRetrieveDoc() {
		return retrieveDoc;
	}

	/**
	 * @param retrieveMetaData
	 *            : La données pour le service de récupération des métadonnées
	 */
	public final void setRetrieveMetaData(final List<RetrieveMetaData> retrieveMetaData) {
		this.retrieveMetaData = retrieveMetaData;
	}

	/**
	 * @return la Données pour le service de récupération des métadonnées
	 */
	public final List<RetrieveMetaData> getRetrieveMetaData() {
		return retrieveMetaData;
	}

	/**
	 * @param retrieveDocMetaData
	 *            La données pour le service de récupération du flux binaire et
	 *            des métadonnées
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setRetrieveDocMetaData(
			final List<RetrieveDocMetaData> retrieveDocMetaData) {
		this.retrieveDocMetaData = retrieveDocMetaData;
	}

	/**
	 * @return La données pour le service de récupération du flux binaire et
	 *            des métadonnées
	 */
	public final List<RetrieveDocMetaData> getRetrieveDocMetaData() {
		return retrieveDocMetaData;
	}

}
