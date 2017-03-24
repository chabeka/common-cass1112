
package fr.urssaf.image.commons.webservice.exemple.ssl.document.modele;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for multiplieRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="multiplieRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="valeur1" type="{http://www.w3.org/2001/XMLSchema}integer" form="unqualified"/>
 *         &lt;element name="valeur2" type="{http://www.w3.org/2001/XMLSchema}integer" form="unqualified"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "multiplieRequestType", propOrder = {

})
public class MultiplieRequestType {

    @XmlElement(required = true)
    protected BigInteger valeur1;
    @XmlElement(required = true)
    protected BigInteger valeur2;

    /**
     * Gets the value of the valeur1 property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getValeur1() {
        return valeur1;
    }

    /**
     * Sets the value of the valeur1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setValeur1(BigInteger value) {
        this.valeur1 = value;
    }

    /**
     * Gets the value of the valeur2 property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getValeur2() {
        return valeur2;
    }

    /**
     * Sets the value of the valeur2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setValeur2(BigInteger value) {
        this.valeur2 = value;
    }

}
