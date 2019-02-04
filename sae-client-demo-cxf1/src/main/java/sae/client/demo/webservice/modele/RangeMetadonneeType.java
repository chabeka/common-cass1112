
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Un range de métadonnée définie par un code,
 *             une valeur minimum et une valeur maximum
 * 
 * <p>Java class for rangeMetadonneeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rangeMetadonneeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code" type="{http://www.cirtil.fr/saeService}metadonneeCodeType"/>
 *         &lt;element name="valeurMin" type="{http://www.cirtil.fr/saeService}metadonneeValeurType"/>
 *         &lt;element name="valeurMax" type="{http://www.cirtil.fr/saeService}metadonneeValeurType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rangeMetadonneeType", propOrder = {
    "code",
    "valeurMin",
    "valeurMax"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class RangeMetadonneeType {

    @XmlElement(required = true)
    protected String code;
    @XmlElement(required = true)
    protected String valeurMin;
    @XmlElement(required = true)
    protected String valeurMax;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the valeurMin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValeurMin() {
        return valeurMin;
    }

    /**
     * Sets the value of the valeurMin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValeurMin(String value) {
        this.valeurMin = value;
    }

    /**
     * Gets the value of the valeurMax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValeurMax() {
        return valeurMax;
    }

    /**
     * Sets the value of the valeurMax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValeurMax(String value) {
        this.valeurMax = value;
    }

}
