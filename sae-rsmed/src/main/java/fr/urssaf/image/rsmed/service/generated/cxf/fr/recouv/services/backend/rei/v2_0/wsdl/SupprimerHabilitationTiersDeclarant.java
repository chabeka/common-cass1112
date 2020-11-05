
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.HabilitationTiersDeclarantType;


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
 *         &lt;element name="habilitation" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}HabilitationTiersDeclarant_Type"/&gt;
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
@XmlRootElement(name = "SupprimerHabilitationTiersDeclarant")
public class SupprimerHabilitationTiersDeclarant {

    @XmlElement(required = true)
    protected HabilitationTiersDeclarantType habilitation;

    /**
     * Obtient la valeur de la propriété habilitation.
     * 
     * @return
     *     possible object is
     *     {@link HabilitationTiersDeclarantType }
     *     
     */
    public HabilitationTiersDeclarantType getHabilitation() {
        return habilitation;
    }

    /**
     * Définit la valeur de la propriété habilitation.
     * 
     * @param value
     *     allowed object is
     *     {@link HabilitationTiersDeclarantType }
     *     
     */
    public void setHabilitation(HabilitationTiersDeclarantType value) {
        this.habilitation = value;
    }

}
