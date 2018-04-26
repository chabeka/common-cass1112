
package fr.urssaf.image.commons.webservice.exemple.ssl.document.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bonjourResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bonjourResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="resultat" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bonjourResponseType", propOrder = {

})
public class BonjourResponseType {

    @XmlElement(required = true)
    protected String resultat;

    /**
     * Gets the value of the resultat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultat() {
        return resultat;
    }

    /**
     * Sets the value of the resultat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultat(String value) {
        this.resultat = value;
    }

}
