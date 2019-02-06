
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Un traitement de masse
 * 
 * <p>Java class for traitementMasseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="traitementMasseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idJob" type="{http://www.cirtil.fr/saeService}uuidType"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateCreation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="etat" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreDocuments" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateReservation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateDebut" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateFin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
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
    "message"
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

    /**
     * Gets the value of the idJob property.
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
     * Sets the value of the idJob property.
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
     * Gets the value of the type property.
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
     * Sets the value of the type property.
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
     * Gets the value of the dateCreation property.
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
     * Sets the value of the dateCreation property.
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
     * Gets the value of the etat property.
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
     * Sets the value of the etat property.
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
     * Gets the value of the nombreDocuments property.
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
     * Sets the value of the nombreDocuments property.
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
     * Gets the value of the dateReservation property.
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
     * Sets the value of the dateReservation property.
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
     * Gets the value of the dateDebut property.
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
     * Sets the value of the dateDebut property.
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
     * Gets the value of the dateFin property.
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
     * Sets the value of the dateFin property.
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
     * Gets the value of the message property.
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
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

}
