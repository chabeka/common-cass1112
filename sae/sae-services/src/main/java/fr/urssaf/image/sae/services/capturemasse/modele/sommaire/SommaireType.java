//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.02 at 04:48:49 PM CET 
//


package fr.urssaf.image.sae.services.capturemasse.modele.sommaire;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ListeDocumentsType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ListeDocumentsVirtuelsType;


/**
 * <p>Java class for sommaireType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sommaireType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="batchMode" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}batchModeType"/>
 *         &lt;element name="dateCreation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documents" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeDocumentsType"/>
 *         &lt;element name="documentsVirtuels" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeDocumentsVirtuelsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sommaireType", propOrder = {
    "batchMode",
    "dateCreation",
    "description",
    "documents",
    "documentsVirtuels"
})
@SuppressWarnings("PMD")
public class SommaireType {

    @XmlElement(required = true)
    protected BatchModeType batchMode;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateCreation;
    protected String description;
    @XmlElement(required = true)
    protected ListeDocumentsType documents;
    @XmlElement(required = true)
    protected ListeDocumentsVirtuelsType documentsVirtuels;

    /**
     * Gets the value of the batchMode property.
     * 
     * @return
     *     possible object is
     *     {@link BatchModeType }
     *     
     */
    public BatchModeType getBatchMode() {
        return batchMode;
    }

    /**
     * Sets the value of the batchMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BatchModeType }
     *     
     */
    public void setBatchMode(BatchModeType value) {
        this.batchMode = value;
    }

    /**
     * Gets the value of the dateCreation property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCreation() {
        return dateCreation;
    }

    /**
     * Sets the value of the dateCreation property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCreation(XMLGregorianCalendar value) {
        this.dateCreation = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the documents property.
     * 
     * @return
     *     possible object is
     *     {@link ListeDocumentsType }
     *     
     */
    public ListeDocumentsType getDocuments() {
        return documents;
    }

    /**
     * Sets the value of the documents property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeDocumentsType }
     *     
     */
    public void setDocuments(ListeDocumentsType value) {
        this.documents = value;
    }

    /**
     * Gets the value of the documentsVirtuels property.
     * 
     * @return
     *     possible object is
     *     {@link ListeDocumentsVirtuelsType }
     *     
     */
    public ListeDocumentsVirtuelsType getDocumentsVirtuels() {
        return documentsVirtuels;
    }

    /**
     * Sets the value of the documentsVirtuels property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeDocumentsVirtuelsType }
     *     
     */
    public void setDocumentsVirtuels(ListeDocumentsVirtuelsType value) {
        this.documentsVirtuels = value;
    }

}
