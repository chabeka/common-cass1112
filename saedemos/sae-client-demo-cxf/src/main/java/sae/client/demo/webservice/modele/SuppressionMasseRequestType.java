
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération
 *             'suppressionMasse'
 * 
 * <p>Java class for suppressionMasseRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="suppressionMasseRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requete" type="{http://www.cirtil.fr/saeService}requeteRechercheType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "suppressionMasseRequestType", propOrder = {
    "requete"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SuppressionMasseRequestType {

    @XmlElement(required = true)
    protected String requete;

    /**
     * Gets the value of the requete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequete() {
        return requete;
    }

    /**
     * Sets the value of the requete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequete(String value) {
        this.requete = value;
    }

}
