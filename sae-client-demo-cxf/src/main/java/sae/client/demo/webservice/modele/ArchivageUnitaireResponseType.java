
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Valeur de retour de l'opération 'archivage
 *             unitaire'
 * 
 * <p>Java class for archivageUnitaireResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="archivageUnitaireResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idArchive" type="{http://www.cirtil.fr/saeService}uuidType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "archivageUnitaireResponseType", propOrder = {
    "idArchive"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ArchivageUnitaireResponseType {

    @XmlElement(required = true)
    protected String idArchive;

    /**
     * Gets the value of the idArchive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdArchive() {
        return idArchive;
    }

    /**
     * Sets the value of the idArchive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdArchive(String value) {
        this.idArchive = value;
    }

}
