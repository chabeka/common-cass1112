//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.2 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.09.04 à 10:35:02 AM CEST 
//


package sae.integration.xml.modele;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour sommaireType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="sommaireType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="batchMode" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}batchModeType"/&gt;
 *         &lt;element name="dateCreation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="restitutionUuids" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="documents" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeDocumentsType"/&gt;
 *           &lt;element name="documentsMultiAction" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeDocumentsTypeMultiAction"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="documentsVirtuels" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeDocumentsVirtuelsType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sommaireType", namespace = "http://www.cirtil.fr/sae/sommaireXml", propOrder = {
    "batchMode",
    "dateCreation",
    "description",
    "restitutionUuids",
    "documents",
    "documentsMultiAction",
    "documentsVirtuels"
})
public class SommaireType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BatchModeType batchMode;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateCreation;
    protected String description;
    @XmlElementRef(name = "restitutionUuids", namespace = "http://www.cirtil.fr/sae/sommaireXml", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> restitutionUuids;
    protected ListeDocumentsType documents;
    protected ListeDocumentsTypeMultiAction documentsMultiAction;
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
     * Obtient la valeur de la propriété documentsMultiAction.
     * 
     * @return
     *     possible object is
     *     {@link ListeDocumentsTypeMultiAction }
     *     
     */
    public ListeDocumentsTypeMultiAction getDocumentsMultiAction() {
        return documentsMultiAction;
    }

    /**
     * Définit la valeur de la propriété documentsMultiAction.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeDocumentsTypeMultiAction }
     *     
     */
    public void setDocumentsMultiAction(ListeDocumentsTypeMultiAction value) {
        this.documentsMultiAction = value;
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
