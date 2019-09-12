
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d’entrée de l’opération 'stockage
 *             Unitaire'
 * 
 * <p>Classe Java pour stockageUnitaireRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="stockageUnitaireRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeType"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="urlEcdeDoc" type="{http://www.cirtil.fr/saeService}ecdeUrlType"/&gt;
 *           &lt;element name="dataFileDoc" type="{http://www.cirtil.fr/saeService}dataFileType"/&gt;
 *         &lt;/choice&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="urlEcdeDocOrigine" type="{http://www.cirtil.fr/saeService}ecdeUrlType"/&gt;
 *           &lt;element name="dataFileAttached" type="{http://www.cirtil.fr/saeService}dataFileType"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
    @XmlSchemaType(name = "anyURI")
    protected String urlEcdeDoc;
    protected DataFileType dataFileDoc;
    @XmlSchemaType(name = "anyURI")
    protected String urlEcdeDocOrigine;
    protected DataFileType dataFileAttached;

    /**
     * Obtient la valeur de la propriété metadonnees.
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
     * Définit la valeur de la propriété metadonnees.
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
     * Obtient la valeur de la propriété urlEcdeDoc.
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
     * Définit la valeur de la propriété urlEcdeDoc.
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
     * Obtient la valeur de la propriété dataFileDoc.
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
     * Définit la valeur de la propriété dataFileDoc.
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
     * Obtient la valeur de la propriété urlEcdeDocOrigine.
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
     * Définit la valeur de la propriété urlEcdeDocOrigine.
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
     * Obtient la valeur de la propriété dataFileAttached.
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
     * Définit la valeur de la propriété dataFileAttached.
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
