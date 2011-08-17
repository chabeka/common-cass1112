package fr.urssaf.image.sae.bo.model;

/**
 * Énumération contenant tous les types manipulés dans le SAE.
 * 
 * @author akenore
 * 
 */
public enum SAEMetadataType {
	// Le type String
	STRING("String"),
	// Le type boolean
	BOOLEAN("Boolean"),
	// Le type integer
	INTEGER("Integer"),
	// Le type decimal
	DECIMAL("Decimal"),
	// Le type date
	DATE("Date"),
	// Le type dateTime
	DATETIME("NotImplemended"),
	// Le type double
	DOUBLE("Double"),
	// Le type long
	LONG("Long"),
	// Le type uuid
	UUID("UUID"),
	// Le type float
	FLOAT("Float");
	private String type;

	/**
	 * @param type
	 *            : Le nom simple du type
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * @return Le nom simple du type
	 */
	public String getType() {
		return type;
	}

	SAEMetadataType(final String type) {
		this.type = type;
	}

}
