
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Retour de l'op�ration
 *             de copie.
 * 
 * <p>Java class for copieResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="copieResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idGed" type="{http://www.cirtil.fr/saeService}uuidType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "copieResponseType", propOrder = {
    "idGed"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class CopieResponseType {

    @XmlElement(required = true)
    protected String idGed;

    /**
     * Gets the value of the idGed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdGed() {
        return idGed;
    }

    /**
     * Sets the value of the idGed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdGed(String value) {
        this.idGed = value;
    }

}
