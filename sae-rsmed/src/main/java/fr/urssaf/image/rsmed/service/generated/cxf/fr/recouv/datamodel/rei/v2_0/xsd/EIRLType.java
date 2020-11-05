
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Entrepreneur Individuel a Responsabilite Limitee
 * 
 * <p>Classe Java pour EIRL_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="EIRL_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="noRegistreEIRL" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoEIRLCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeActivite" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://cfe.recouv/2008-11/TypeRegent}Num_Type"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EIRL_Type", propOrder = {
    "noRegistreEIRL",
    "codeActivite"
})
public class EIRLType {

    protected NoEIRLCertificationType noRegistreEIRL;
    protected String codeActivite;

    /**
     * Obtient la valeur de la propriété noRegistreEIRL.
     * 
     * @return
     *     possible object is
     *     {@link NoEIRLCertificationType }
     *     
     */
    public NoEIRLCertificationType getNoRegistreEIRL() {
        return noRegistreEIRL;
    }

    /**
     * Définit la valeur de la propriété noRegistreEIRL.
     * 
     * @param value
     *     allowed object is
     *     {@link NoEIRLCertificationType }
     *     
     */
    public void setNoRegistreEIRL(NoEIRLCertificationType value) {
        this.noRegistreEIRL = value;
    }

    /**
     * Obtient la valeur de la propriété codeActivite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeActivite() {
        return codeActivite;
    }

    /**
     * Définit la valeur de la propriété codeActivite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeActivite(String value) {
        this.codeActivite = value;
    }

}
