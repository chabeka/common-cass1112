
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Identique au Collaborateur_Type avec des individus complet
 * 
 * <p>Classe Java pour CollaborateurComplet_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CollaborateurComplet_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Collaborateur_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="individu" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IndividuComplet_Type" minOccurs="0"/&gt;
 *         &lt;element name="individuDirigeant" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IndividuComplet_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollaborateurComplet_Type", propOrder = {
    "individu",
    "individuDirigeant"
})
public class CollaborateurCompletType
    extends CollaborateurType
{

    protected IndividuCompletType individu;
    protected IndividuCompletType individuDirigeant;

    /**
     * Obtient la valeur de la propriété individu.
     * 
     * @return
     *     possible object is
     *     {@link IndividuCompletType }
     *     
     */
    public IndividuCompletType getIndividu() {
        return individu;
    }

    /**
     * Définit la valeur de la propriété individu.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividuCompletType }
     *     
     */
    public void setIndividu(IndividuCompletType value) {
        this.individu = value;
    }

    /**
     * Obtient la valeur de la propriété individuDirigeant.
     * 
     * @return
     *     possible object is
     *     {@link IndividuCompletType }
     *     
     */
    public IndividuCompletType getIndividuDirigeant() {
        return individuDirigeant;
    }

    /**
     * Définit la valeur de la propriété individuDirigeant.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividuCompletType }
     *     
     */
    public void setIndividuDirigeant(IndividuCompletType value) {
        this.individuDirigeant = value;
    }

}
