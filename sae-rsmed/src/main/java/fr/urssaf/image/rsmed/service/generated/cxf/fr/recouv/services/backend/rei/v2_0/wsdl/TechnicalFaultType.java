
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour TechnicalFault_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="TechnicalFault_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="faultName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="originatingError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="errorTrace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TechnicalFault_Type", propOrder = {
    "errorCode",
    "faultName",
    "originatingError",
    "errorTrace"
})
public class TechnicalFaultType {

    protected String errorCode;
    protected String faultName;
    protected String originatingError;
    protected String errorTrace;

    /**
     * Obtient la valeur de la propriété errorCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Définit la valeur de la propriété errorCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Obtient la valeur de la propriété faultName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaultName() {
        return faultName;
    }

    /**
     * Définit la valeur de la propriété faultName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaultName(String value) {
        this.faultName = value;
    }

    /**
     * Obtient la valeur de la propriété originatingError.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginatingError() {
        return originatingError;
    }

    /**
     * Définit la valeur de la propriété originatingError.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginatingError(String value) {
        this.originatingError = value;
    }

    /**
     * Obtient la valeur de la propriété errorTrace.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorTrace() {
        return errorTrace;
    }

    /**
     * Définit la valeur de la propriété errorTrace.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorTrace(String value) {
        this.errorTrace = value;
    }

}
