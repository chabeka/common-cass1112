
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération 'archivage
 *             Unitaire PJ'
 *          
 * 
 * <p>Classe Java pour archivageUnitairePJRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="archivageUnitairePJRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeType"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="ecdeUrl" type="{http://www.cirtil.fr/saeService}ecdeUrlType"/&gt;
 *           &lt;element name="dataFile" type="{http://www.cirtil.fr/saeService}dataFileType"/&gt;
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
    @XmlSchemaType(name = "anyURI")
    protected String ecdeUrl;
    protected DataFileType dataFile;

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
     * Obtient la valeur de la propriété ecdeUrl.
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
     * Définit la valeur de la propriété ecdeUrl.
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
     * Obtient la valeur de la propriété dataFile.
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
     * Définit la valeur de la propriété dataFile.
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
