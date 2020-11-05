
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.CorrespondantResumeType;


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
 *         &lt;element name="correspondant" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CorrespondantResume_Type"/&gt;
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
    "correspondant"
})
@XmlRootElement(name = "CreerCorrespondant")
public class CreerCorrespondant {

    @XmlElement(required = true)
    protected CorrespondantResumeType correspondant;

    /**
     * Obtient la valeur de la propriété correspondant.
     * 
     * @return
     *     possible object is
     *     {@link CorrespondantResumeType }
     *     
     */
    public CorrespondantResumeType getCorrespondant() {
        return correspondant;
    }

    /**
     * Définit la valeur de la propriété correspondant.
     * 
     * @param value
     *     allowed object is
     *     {@link CorrespondantResumeType }
     *     
     */
    public void setCorrespondant(CorrespondantResumeType value) {
        this.correspondant = value;
    }

}
