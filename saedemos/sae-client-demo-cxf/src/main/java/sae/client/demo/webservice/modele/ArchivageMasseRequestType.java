
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération
 *             'archivageMasse'
 * 
 * <p>Java class for archivageMasseRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="archivageMasseRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="urlSommaire" type="{http://www.cirtil.fr/saeService}ecdeUrlSommaireType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "archivageMasseRequestType", propOrder = {
    "urlSommaire"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ArchivageMasseRequestType {

    @XmlElement(required = true)
    protected String urlSommaire;

    /**
     * Gets the value of the urlSommaire property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlSommaire() {
        return urlSommaire;
    }

    /**
     * Sets the value of the urlSommaire property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlSommaire(String value) {
        this.urlSommaire = value;
    }

}
