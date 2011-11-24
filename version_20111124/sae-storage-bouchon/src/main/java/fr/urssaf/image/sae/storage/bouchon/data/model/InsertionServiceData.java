package fr.urssaf.image.sae.storage.bouchon.data.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Classe permettant de désérialiser les données définies pour les services
 * d'insertions. <li>Attribut insertionData : Donnée pour le service d'insertion
 * </li> <li>Attribut bulkInsertionData : Donnée pour le service d'insertion en
 * masse</li>
 * 
 * @author akenore
 * 
 */
@XStreamAlias(value = "insertionServiceData")
public class InsertionServiceData {
	
	private  String insertionData;
	@XStreamImplicit(itemFieldName = "bulkInsertionData")
	private List<BulkInsertionData> bulkInsertionData;
	/**
	 * 
	 * @param insertionData : Donnée pour le service d'insertion
	 * @param bulkInsertionData : Donnée pour le service d'insertion en masse
	 */
	public InsertionServiceData(final String insertionData,
			final List<BulkInsertionData> bulkInsertionData) {
			this.insertionData = insertionData;
		this.bulkInsertionData=bulkInsertionData;
	}
	/**
	 * initialise la donnée pour le service d'insertion
	 * @param insertionData : La donnée pour le service d'insertion
	 */
	public final void setInsertionData(final String insertionData) {
		this.insertionData = insertionData;
	}
	/**
	 * Retourne la donnée pour le service d'insertion
	 * @return La donnée pour le service d'insertion
	 */
	public final String getInsertionData() {
		return insertionData;
	}
	/**
	 * 
	 * Initialise La donnée pour le service d'insertion en masse
	 * @param bulkInsertionData La donnée pour le service d'insertion en masse
	 */
	public final void setBulkInsertionData(final List<BulkInsertionData> bulkInsertionData) {
		this.bulkInsertionData = bulkInsertionData;
	}
	/**
	 * Retourne La donnée pour le service d'insertion en masse
	 * @return La donnée pour le service d'insertion en masse
	 */
	public final List<BulkInsertionData> getBulkInsertionData() {
		return bulkInsertionData;
	}

	
}
