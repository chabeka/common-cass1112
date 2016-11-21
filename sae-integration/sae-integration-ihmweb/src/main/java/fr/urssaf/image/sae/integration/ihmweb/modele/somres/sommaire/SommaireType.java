//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.11.17 à 03:49:54 PM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.sommaire;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeDocumentsType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat.ListeDocumentsVirtuelsType;


/**
 * <p>Classe Java pour sommaireType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="sommaireType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="batchMode" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}batchModeType"/>
 *         &lt;element name="dateCreation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="restitutionUuids" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
    "restitutionUuids",
    "documents",
    "documentsVirtuels"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SommaireType {

    @XmlElement(required = true)
    protected BatchModeType batchMode;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateCreation;
    protected String description;
    @XmlElementRef(name = "restitutionUuids", namespace = "http://www.cirtil.fr/sae/sommaireXml", type = JAXBElement.class)
    protected JAXBElement<Boolean> restitutionUuids;
    @XmlElement(required = true)
    protected ListeDocumentsType documents;
    @XmlElement(required = true)
    protected ListeDocumentsVirtuelsType documentsVirtuels;

    /**
     * Obtient la valeur de la propriété batchMode.
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
     * Définit la valeur de la propriété batchMode.
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
     * Obtient la valeur de la propriété dateCreation.
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
     * Définit la valeur de la propriété dateCreation.
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
     * Obtient la valeur de la propriété description.
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
     * Définit la valeur de la propriété description.
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
     * Obtient la valeur de la propriété restitutionUuids.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getRestitutionUuids() {
        return restitutionUuids;
    }

    /**
     * Définit la valeur de la propriété restitutionUuids.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setRestitutionUuids(JAXBElement<Boolean> value) {
        this.restitutionUuids = value;
    }

    /**
     * Obtient la valeur de la propriété documents.
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
     * Définit la valeur de la propriété documents.
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
     * Obtient la valeur de la propriété documentsVirtuels.
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
     * Définit la valeur de la propriété documentsVirtuels.
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
