
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Individu pour la CMAF
 * 
 * <p>Classe Java pour IndividuLieAuRole_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="IndividuLieAuRole_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="individu" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IndividuResume_Type" minOccurs="0"/&gt;
 *         &lt;element name="estEmbarque" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndividuLieAuRole_Type", propOrder = {
    "individu",
    "estEmbarque"
})
public class IndividuLieAuRoleType {

    protected IndividuResumeType individu;
    protected Boolean estEmbarque;

    /**
     * Obtient la valeur de la propriété individu.
     * 
     * @return
     *     possible object is
     *     {@link IndividuResumeType }
     *     
     */
    public IndividuResumeType getIndividu() {
        return individu;
    }

    /**
     * Définit la valeur de la propriété individu.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividuResumeType }
     *     
     */
    public void setIndividu(IndividuResumeType value) {
        this.individu = value;
    }

    /**
     * Obtient la valeur de la propriété estEmbarque.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstEmbarque() {
        return estEmbarque;
    }

    /**
     * Définit la valeur de la propriété estEmbarque.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstEmbarque(Boolean value) {
        this.estEmbarque = value;
    }

}
