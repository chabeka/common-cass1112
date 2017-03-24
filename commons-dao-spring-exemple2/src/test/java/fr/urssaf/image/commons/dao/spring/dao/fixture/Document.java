package fr.urssaf.image.commons.dao.spring.dao.fixture;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Pour les tests unitaires<br>
 * <br>
 * Objet "métier" Document
 *
 */
@SuppressWarnings("PMD")
@Entity
@Table(name = "document")
public final class Document implements java.io.Serializable {

   private static final long serialVersionUID = 1L;
   
   private static final int TITRE_LENGTH=45;
   private static final int DATE_LENGTH=19;
   
   private Integer id;
	private String titre;
	private Date date;

	
	/**
	 * Constructeur
	 */
	public Document() {
		//constructeur vide
	}


	/**
	 * Constructeur
	 * 
	 * @param id identifiant unique
	 * @param titre titre
	 * @param date date de création
	 */
	public Document(Integer id, String titre, Date date) {
	   this.id = id;
	   this.titre = titre;
	   this.date = date;
	}
	
	
	/**
	 * Identifiant unique
	 * @return Identifiant unique
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	
	/**
	 * Identifiant unique
	 * @param id Identifiant unique
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * Titre
	 * @return Titre
	 */
	@Column(name = "titre", nullable = false, length = TITRE_LENGTH)
	public String getTitre() {
		return this.titre;
	}


	/**
	 * Titre
	 * @param titre Titre
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	
	/**
	 * Date de création
	 * @return Date de création
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date", nullable = false, length = DATE_LENGTH)
	public Date getDate() {
		return this.date;
	}

	
	/**
	 * Date de création
	 * @param date Date de création
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
