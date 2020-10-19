
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Un traitement de masse
 * 
 * <p>Classe Java pour traitementMasseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="traitementMasseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idJob" type="{http://www.cirtil.fr/saeService}uuidType"/&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dateCreation" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="etat" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="nombreDocuments" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dateReservation" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dateDebut" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dateFin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="toCheckFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="toCheckFlagRaison" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "traitementMasseType", propOrder = {
    "idJob",
    "type",
    "dateCreation",
    "etat",
    "nombreDocuments",
    "dateReservation",
    "dateDebut",
    "dateFin",
    "message",
    "toCheckFlag",
    "toCheckFlagRaison"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class TraitementMasseType {

    @XmlElement(required = true)
    protected String idJob;
    @XmlElement(required = true)
    protected String type;
    @XmlElement(required = true)
    protected String dateCreation;
    @XmlElement(required = true)
    protected String etat;
    @XmlElement(required = true)
    protected String nombreDocuments;
    @XmlElement(required = true)
    protected String dateReservation;
    @XmlElement(required = true)
    protected String dateDebut;
    @XmlElement(required = true)
    protected String dateFin;
    @XmlElement(required = true)
    protected String message;
    protected boolean toCheckFlag;
    @XmlElement(required = true)
    protected String toCheckFlagRaison;

    /**
     * Obtient la valeur de la propriété idJob.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdJob() {
        return idJob;
    }

    /**
     * Définit la valeur de la propriété idJob.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdJob(String value) {
        this.idJob = value;
    }

    /**
     * Obtient la valeur de la propriété type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Définit la valeur de la propriété type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Obtient la valeur de la propriété dateCreation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * Définit la valeur de la propriété dateCreation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateCreation(String value) {
        this.dateCreation = value;
    }

    /**
     * Obtient la valeur de la propriété etat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEtat() {
        return etat;
    }

    /**
     * Définit la valeur de la propriété etat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEtat(String value) {
        this.etat = value;
    }

    /**
     * Obtient la valeur de la propriété nombreDocuments.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreDocuments() {
        return nombreDocuments;
    }

    /**
     * Définit la valeur de la propriété nombreDocuments.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreDocuments(String value) {
        this.nombreDocuments = value;
    }

    /**
     * Obtient la valeur de la propriété dateReservation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateReservation() {
        return dateReservation;
    }

    /**
     * Définit la valeur de la propriété dateReservation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateReservation(String value) {
        this.dateReservation = value;
    }

    /**
     * Obtient la valeur de la propriété dateDebut.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * Définit la valeur de la propriété dateDebut.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateDebut(String value) {
        this.dateDebut = value;
    }

    /**
     * Obtient la valeur de la propriété dateFin.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * Définit la valeur de la propriété dateFin.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateFin(String value) {
        this.dateFin = value;
    }

    /**
     * Obtient la valeur de la propriété message.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Définit la valeur de la propriété message.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Obtient la valeur de la propriété toCheckFlag.
     * 
     */
    public boolean isToCheckFlag() {
        return toCheckFlag;
    }

    /**
     * Définit la valeur de la propriété toCheckFlag.
     * 
     */
    public void setToCheckFlag(boolean value) {
        this.toCheckFlag = value;
    }

    /**
     * Obtient la valeur de la propriété toCheckFlagRaison.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToCheckFlagRaison() {
        return toCheckFlagRaison;
    }

    /**
     * Définit la valeur de la propriété toCheckFlagRaison.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToCheckFlagRaison(String value) {
        this.toCheckFlagRaison = value;
    }

}
