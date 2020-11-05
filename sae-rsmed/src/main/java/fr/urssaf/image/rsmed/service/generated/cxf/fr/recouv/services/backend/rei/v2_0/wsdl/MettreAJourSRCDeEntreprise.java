
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

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
 *         &lt;element name="idEntreprise" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}IdREI_Type"/&gt;
 *         &lt;element name="codeSRC" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeSRC_Type"/&gt;
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
    "idEntreprise",
    "codeSRC"
})
@XmlRootElement(name = "MettreAJourSRCDeEntreprise")
public class MettreAJourSRCDeEntreprise {

    protected long idEntreprise;
    @XmlElement(required = true)
    protected String codeSRC;

    /**
     * Obtient la valeur de la propriété idEntreprise.
     * 
     */
    public long getIdEntreprise() {
        return idEntreprise;
    }

    /**
     * Définit la valeur de la propriété idEntreprise.
     * 
     */
    public void setIdEntreprise(long value) {
        this.idEntreprise = value;
    }

    /**
     * Obtient la valeur de la propriété codeSRC.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeSRC() {
        return codeSRC;
    }

    /**
     * Définit la valeur de la propriété codeSRC.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeSRC(String value) {
        this.codeSRC = value;
    }

}
