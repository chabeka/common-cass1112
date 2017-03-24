package fr.urssaf.image.commons.controller.spring3.jndi.exemple.modele;



import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "document")
public class Document implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("PMD.ShortVariable") 
	private Integer id;
	private String titre;
	private Date date;
	
	public Document() {
		//constructeur vide
	}

	public Document(String titre, Date date) {
		this.titre = titre;
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

	@Column(name = "titre", nullable = false, length = 45)
	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
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
