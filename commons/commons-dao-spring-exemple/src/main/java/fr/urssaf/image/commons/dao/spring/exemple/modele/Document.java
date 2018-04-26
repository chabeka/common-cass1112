package fr.urssaf.image.commons.dao.spring.exemple.modele;

// Generated 25 mai 2010 17:42:23 by Hibernate Tools 3.3.0.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Document generated by hbm2java
 */
@Entity
@Table(name = "document")
public class Document implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("PMD.ShortVariable") 
	private Integer id;
	private Auteur auteur;
	private String titre;
	private Date date;
	private Set<Etat> etats = new HashSet<Etat>(0);

	public Document() {
		//constructeur vide
	}

	public Document(String titre, Date date) {
		this.titre = titre;
		this.date = date;
	}

	public Document(Auteur auteur, String titre, Date date, Set<Etat> etats) {
		this.auteur = auteur;
		this.titre = titre;
		this.date = date;
		this.etats = etats;
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
	@JoinColumn(name = "id_auteur")
	public Auteur getAuteur() {
		return this.auteur;
	}

	public void setAuteur(Auteur auteur) {
		this.auteur = auteur;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
	public Set<Etat> getEtats() {
		return this.etats;
	}

	public void setEtats(Set<Etat> etats) {
		this.etats = etats;
	}

}
