
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération 'archivage
 *             Unitaire PJ'
 * 
 * <p>Java class for archivageUnitairePJRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="archivageUnitairePJRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeType"/>
 *         &lt;choice>
 *           &lt;element name="ecdeUrl" type="{http://www.cirtil.fr/saeService}ecdeUrlType"/>
 *           &lt;element name="dataFile" type="{http://www.cirtil.fr/saeService}dataFileType"/>
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
@XmlType(name = "archivageUnitairePJRequestType", propOrder = {
    "metadonnees",
    "ecdeUrl",
    "dataFile"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ArchivageUnitairePJRequestType {

    @XmlElement(required = true)
    protected ListeMetadonneeType metadonnees;
    protected String ecdeUrl;
    protected DataFileType dataFile;

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
     * Gets the value of the ecdeUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcdeUrl() {
        return ecdeUrl;
    }

    /**
     * Sets the value of the ecdeUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcdeUrl(String value) {
        this.ecdeUrl = value;
    }

    /**
     * Gets the value of the dataFile property.
     * 
     * @return
     *     possible object is
     *     {@link DataFileType }
     *     
     */
    public DataFileType getDataFile() {
        return dataFile;
    }

    /**
     * Sets the value of the dataFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataFileType }
     *     
     */
    public void setDataFile(DataFileType value) {
        this.dataFile = value;
    }

}
