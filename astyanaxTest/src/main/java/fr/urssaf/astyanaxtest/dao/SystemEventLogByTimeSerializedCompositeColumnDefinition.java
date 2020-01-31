package fr.urssaf.astyanaxtest.dao;

import java.util.Date;
import java.util.UUID;

import com.netflix.astyanax.annotations.Component;

/**
 * Classe servant à la définir les éléments composant les noms de colonnes pour
 * la CF SystemEventLogByTimeSerialized
 *
 */
public class SystemEventLogByTimeSerializedCompositeColumnDefinition {

	private @Component(ordinal = 0) Date date;
	private @Component(ordinal = 1) UUID eventUUID;

	public SystemEventLogByTimeSerializedCompositeColumnDefinition() {	}

	public SystemEventLogByTimeSerializedCompositeColumnDefinition(Date dateValue,
			UUID eventUUID) {
		this.date = dateValue;
		this.eventUUID = eventUUID;
	}

	public void setDateValue(Date dateValue) {
		this.date = dateValue;
	}

	public Date getDateValue() {
		return date;
	}

	public void setEventUUID(UUID eventUUID) {
		this.eventUUID = eventUUID;
	}

	public UUID getEventUUID() {
		return eventUUID;
	}

}
