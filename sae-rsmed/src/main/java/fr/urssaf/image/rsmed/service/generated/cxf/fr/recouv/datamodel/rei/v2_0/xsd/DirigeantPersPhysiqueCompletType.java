
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Identique au DirigeantPersPhysique_Type avec les objets lies complets
 * 
 * <p>Classe Java pour DirigeantPersPhysiqueComplet_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="DirigeantPersPhysiqueComplet_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}DirigeantPersPhysique_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="entreprise" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EntrepriseComplete_Type" minOccurs="0"/&gt;
 *         &lt;element name="individu" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IndividuComplet_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirigeantPersPhysiqueComplet_Type", propOrder = {
    "entreprise",
    "individu"
})
public class DirigeantPersPhysiqueCompletType
    extends DirigeantPersPhysiqueType
{

    protected EntrepriseCompleteType entreprise;
    protected IndividuCompletType individu;

    /**
     * Obtient la valeur de la propriété entreprise.
     * 
     * @return
     *     possible object is
     *     {@link EntrepriseCompleteType }
     *     
     */
    public EntrepriseCompleteType getEntreprise() {
        return entreprise;
    }

    /**
     * Définit la valeur de la propriété entreprise.
     * 
     * @param value
     *     allowed object is
     *     {@link EntrepriseCompleteType }
     *     
     */
    public void setEntreprise(EntrepriseCompleteType value) {
        this.entreprise = value;
    }

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

}
