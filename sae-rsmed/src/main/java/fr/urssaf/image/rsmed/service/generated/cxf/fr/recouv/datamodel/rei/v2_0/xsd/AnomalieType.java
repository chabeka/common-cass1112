
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Anomalie
 * 
 * <p>Classe Java pour Anomalie_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Anomalie_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Code" type="{http://cfe.recouv/2008-11/TypeRegent}AlphaNum_Type"/&gt;
 *         &lt;element name="Message" type="{http://cfe.recouv/2008-11/TypeRegent}AlphaNum_Type"/&gt;
 *         &lt;element name="Gravite" type="{http://cfe.recouv/2008-11/TypeRegent}AlphaNum_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Anomalie_Type", propOrder = {
    "code",
    "message",
    "gravite"
})
public class AnomalieType {

    @XmlElement(name = "Code", required = true)
    protected String code;
    @XmlElement(name = "Message", required = true)
    protected String message;
    @XmlElement(name = "Gravite", required = true)
    protected String gravite;

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
     * Obtient la valeur de la propriété message.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Définit la valeur de la propriété message.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Obtient la valeur de la propriété gravite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGravite() {
        return gravite;
    }

    /**
     * Définit la valeur de la propriété gravite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGravite(String value) {
        this.gravite = value;
    }

}
