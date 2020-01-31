package fr.urssaf.astyanaxtest.dao;

import java.util.UUID;

import com.netflix.astyanax.annotations.Component;

/**
 * Classe servant à la définir les éléments composant la clé de la CF IndexCounter
 *
 */
public class IndexCounterKey {

	private @Component(ordinal = 0) String   categoryName;
	private @Component(ordinal = 1) UUID     baseUUID;
	private @Component(ordinal = 2) String   action;

	public IndexCounterKey() {	}

	public IndexCounterKey(String categoryName, UUID baseUUID, String action) {
		this.categoryName = categoryName;
		this.baseUUID = baseUUID;
		this.action = action;
	}


	public String getCategoryName() {
		return categoryName;
	}

	public UUID getBaseUUID() {
		return baseUUID;
	}
	public String getAction() {
		return action;
	}

}
