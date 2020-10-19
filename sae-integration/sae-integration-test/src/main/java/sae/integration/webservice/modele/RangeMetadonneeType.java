
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Un range de métadonnée définie par un code,
 *             une valeur minimum et une valeur maximum
 *          
 * 
 * <p>Classe Java pour rangeMetadonneeType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="rangeMetadonneeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code" type="{http://www.cirtil.fr/saeService}metadonneeCodeType"/&gt;
 *         &lt;element name="valeurMin" type="{http://www.cirtil.fr/saeService}metadonneeValeurType"/&gt;
 *         &lt;element name="valeurMax" type="{http://www.cirtil.fr/saeService}metadonneeValeurType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
     * Obtient la valeur de la propriété code.
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
     * Définit la valeur de la propriété code.
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
     * Obtient la valeur de la propriété valeurMin.
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
     * Définit la valeur de la propriété valeurMin.
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
     * Obtient la valeur de la propriété valeurMax.
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
     * Définit la valeur de la propriété valeurMax.
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
