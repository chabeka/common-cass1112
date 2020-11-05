
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.HabilitationInterneType;


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
 *         &lt;element name="habilitation" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}HabilitationInterne_Type"/&gt;
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
    "habilitation"
})
@XmlRootElement(name = "SupprimerHabilitationCorrespondant")
public class SupprimerHabilitationCorrespondant {

    @XmlElement(required = true)
    protected HabilitationInterneType habilitation;

    /**
     * Obtient la valeur de la propriété habilitation.
     * 
     * @return
     *     possible object is
     *     {@link HabilitationInterneType }
     *     
     */
    public HabilitationInterneType getHabilitation() {
        return habilitation;
    }

    /**
     * Définit la valeur de la propriété habilitation.
     * 
     * @param value
     *     allowed object is
     *     {@link HabilitationInterneType }
     *     
     */
    public void setHabilitation(HabilitationInterneType value) {
        this.habilitation = value;
    }

}
