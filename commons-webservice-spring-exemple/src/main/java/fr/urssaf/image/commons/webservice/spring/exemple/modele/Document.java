package fr.urssaf.image.commons.webservice.spring.exemple.modele;

import java.util.Date;
import java.util.Set;

public class Document {

   @SuppressWarnings("PMD.ShortVariable")
	private int id;
	
	private String titre;
	
	private Date openDate;
	
	private Date closeDate;
	
	private Etat etat;
	
	private int level;
	
	private boolean flag;
	
	private Set<Etat> etats;
	
	private String comment;

	public int getId() {
		return id;
	}

	@SuppressWarnings("PMD.ShortVariable")
	public void setId(int id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Etat getEtat() {
		return etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@SuppressWarnings("PMD.BooleanGetMethodName")
	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setEtats(Set<Etat> etats) {
		this.etats = etats;
	}

	public Set<Etat> getEtats() {
		return etats;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}
	
	
	
}
