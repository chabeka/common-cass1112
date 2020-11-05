
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Code geo (avec indication si le pays n'est pas connu de RPA)
 * 
 * <p>Classe Java pour REICodeGeoINSEE_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="REICodeGeoINSEE_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeGeoINSEE" type="{http://cfe.recouv/2008-11/TypeRegent}CodeGeoINSEE_Type" minOccurs="0"/&gt;
 *         &lt;element name="paysInconnu" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REICodeGeoINSEE_Type", propOrder = {
    "codeGeoINSEE",
    "paysInconnu"
})
public class REICodeGeoINSEEType {

    protected String codeGeoINSEE;
    @XmlElement(defaultValue = "false")
    protected boolean paysInconnu;

    /**
     * Obtient la valeur de la propriété codeGeoINSEE.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeGeoINSEE() {
        return codeGeoINSEE;
    }

    /**
     * Définit la valeur de la propriété codeGeoINSEE.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeGeoINSEE(String value) {
        this.codeGeoINSEE = value;
    }

    /**
     * Obtient la valeur de la propriété paysInconnu.
     * 
     */
    public boolean isPaysInconnu() {
        return paysInconnu;
    }

    /**
     * Définit la valeur de la propriété paysInconnu.
     * 
     */
    public void setPaysInconnu(boolean value) {
        this.paysInconnu = value;
    }

}
