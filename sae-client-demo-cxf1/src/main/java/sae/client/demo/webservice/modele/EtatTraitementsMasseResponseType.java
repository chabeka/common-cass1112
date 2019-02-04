
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for etatTraitementsMasseResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="etatTraitementsMasseResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="traitementsMasse" type="{http://www.cirtil.fr/saeService}listeTraitementsMasseType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "etatTraitementsMasseResponseType", propOrder = {
    "traitementsMasse"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class EtatTraitementsMasseResponseType {

    @XmlElement(required = true)
    protected ListeTraitementsMasseType traitementsMasse;

    /**
     * Gets the value of the traitementsMasse property.
     * 
     * @return
     *     possible object is
     *     {@link ListeTraitementsMasseType }
     *     
     */
    public ListeTraitementsMasseType getTraitementsMasse() {
        return traitementsMasse;
    }

    /**
     * Sets the value of the traitementsMasse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeTraitementsMasseType }
     *     
     */
    public void setTraitementsMasse(ListeTraitementsMasseType value) {
        this.traitementsMasse = value;
    }

}
