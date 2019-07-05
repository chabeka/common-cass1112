
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pingString" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pingString"
})
@XmlRootElement(name = "PingResponse")
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class PingResponse {

    @XmlElement(required = true)
    protected String pingString;

    /**
     * Obtient la valeur de la propriété pingString.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPingString() {
        return pingString;
    }

    /**
     * Définit la valeur de la propriété pingString.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPingString(String value) {
        this.pingString = value;
    }

}
