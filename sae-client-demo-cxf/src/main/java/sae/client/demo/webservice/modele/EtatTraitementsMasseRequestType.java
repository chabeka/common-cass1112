
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for etatTraitementsMasseRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="etatTraitementsMasseRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listeUuid" type="{http://www.cirtil.fr/saeService}listeUuidType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "etatTraitementsMasseRequestType", propOrder = {
    "listeUuid"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class EtatTraitementsMasseRequestType {

    @XmlElement(required = true)
    protected ListeUuidType listeUuid;

    /**
     * Gets the value of the listeUuid property.
     * 
     * @return
     *     possible object is
     *     {@link ListeUuidType }
     *     
     */
    public ListeUuidType getListeUuid() {
        return listeUuid;
    }

    /**
     * Sets the value of the listeUuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeUuidType }
     *     
     */
    public void setListeUuid(ListeUuidType value) {
        this.listeUuid = value;
    }

}
