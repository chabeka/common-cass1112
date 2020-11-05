
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
 *         &lt;element name="siren" type="{http://cfe.recouv/2008-11/TypeRegent}SIREN_Type"/&gt;
 *         &lt;element name="activeUniquement" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
    "siren",
    "activeUniquement"
})
@XmlRootElement(name = "EstEntrepriseExistante")
public class EstEntrepriseExistante {

    @XmlElement(required = true)
    protected String siren;
    protected boolean activeUniquement;

    /**
     * Obtient la valeur de la propriété siren.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiren() {
        return siren;
    }

    /**
     * Définit la valeur de la propriété siren.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiren(String value) {
        this.siren = value;
    }

    /**
     * Obtient la valeur de la propriété activeUniquement.
     * 
     */
    public boolean isActiveUniquement() {
        return activeUniquement;
    }

    /**
     * Définit la valeur de la propriété activeUniquement.
     * 
     */
    public void setActiveUniquement(boolean value) {
        this.activeUniquement = value;
    }

}
