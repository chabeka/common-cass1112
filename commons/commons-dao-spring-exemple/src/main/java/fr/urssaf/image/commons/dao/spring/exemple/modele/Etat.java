package fr.urssaf.image.commons.dao.spring.exemple.modele;

// Generated 25 mai 2010 17:42:23 by Hibernate Tools 3.3.0.GA

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Etat generated by hbm2java
 */
@Entity
@Table(name = "etat")
public class Etat implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("PMD.ShortVariable") 
	private Integer id;
	private Document document;
	private String libelle;
	private Date date;

	public Etat() {
		//constructeur vide
	}

	public Etat(Document document, String etat, Date date) {
		this.document = document;
		this.libelle = etat;
		this.date = date;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	@SuppressWarnings("PMD.ShortVariable") 
	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_document", nullable = false)
	public Document getDocument() {
		return this.document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Column(name = "etat", nullable = false, length = 45)
	public String getEtat() {
		return this.libelle;
	}

	public void setEtat(String etat) {
		this.libelle = etat;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date", nullable = false, length = 19)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
