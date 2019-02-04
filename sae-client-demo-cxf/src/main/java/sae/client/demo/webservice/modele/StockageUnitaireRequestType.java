
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d’entrée de l’opération 'stockage
 *             Unitaire'
 * 
 * <p>Java class for stockageUnitaireRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stockageUnitaireRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeType"/>
 *         &lt;choice>
 *           &lt;element name="urlEcdeDoc" type="{http://www.cirtil.fr/saeService}ecdeUrlType"/>
 *           &lt;element name="dataFileDoc" type="{http://www.cirtil.fr/saeService}dataFileType"/>
 *         &lt;/choice>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="urlEcdeDocOrigine" type="{http://www.cirtil.fr/saeService}ecdeUrlType"/>
 *           &lt;element name="dataFileAttached" type="{http://www.cirtil.fr/saeService}dataFileType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stockageUnitaireRequestType", propOrder = {
    "metadonnees",
    "urlEcdeDoc",
    "dataFileDoc",
    "urlEcdeDocOrigine",
    "dataFileAttached"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class StockageUnitaireRequestType {

    @XmlElement(required = true)
    protected ListeMetadonneeType metadonnees;
    protected String urlEcdeDoc;
    protected DataFileType dataFileDoc;
    protected String urlEcdeDocOrigine;
    protected DataFileType dataFileAttached;

    /**
     * Gets the value of the metadonnees property.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public ListeMetadonneeType getMetadonnees() {
        return metadonnees;
    }

    /**
     * Sets the value of the metadonnees property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public void setMetadonnees(ListeMetadonneeType value) {
        this.metadonnees = value;
    }

    /**
     * Gets the value of the urlEcdeDoc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlEcdeDoc() {
        return urlEcdeDoc;
    }

    /**
     * Sets the value of the urlEcdeDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlEcdeDoc(String value) {
        this.urlEcdeDoc = value;
    }

    /**
     * Gets the value of the dataFileDoc property.
     * 
     * @return
     *     possible object is
     *     {@link DataFileType }
     *     
     */
    public DataFileType getDataFileDoc() {
        return dataFileDoc;
    }

    /**
     * Sets the value of the dataFileDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataFileType }
     *     
     */
    public void setDataFileDoc(DataFileType value) {
        this.dataFileDoc = value;
    }

    /**
     * Gets the value of the urlEcdeDocOrigine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlEcdeDocOrigine() {
        return urlEcdeDocOrigine;
    }

    /**
     * Sets the value of the urlEcdeDocOrigine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlEcdeDocOrigine(String value) {
        this.urlEcdeDocOrigine = value;
    }

    /**
     * Gets the value of the dataFileAttached property.
     * 
     * @return
     *     possible object is
     *     {@link DataFileType }
     *     
     */
    public DataFileType getDataFileAttached() {
        return dataFileAttached;
    }

    /**
     * Sets the value of the dataFileAttached property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataFileType }
     *     
     */
    public void setDataFileAttached(DataFileType value) {
        this.dataFileAttached = value;
    }

}
