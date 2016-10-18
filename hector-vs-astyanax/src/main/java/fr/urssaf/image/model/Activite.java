package fr.urssaf.image.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Objet modele representant une activite
 * 
 * @author Cedric
 */
public class Activite {
	
	private Date date;
	
	private Map<String, String> infos = new HashMap<String, String>();
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Map<String, String> getInfos() {
		return infos;
	}

	public void setInfos(Map<String, String> infos) {
		this.infos = infos;
	}
}
