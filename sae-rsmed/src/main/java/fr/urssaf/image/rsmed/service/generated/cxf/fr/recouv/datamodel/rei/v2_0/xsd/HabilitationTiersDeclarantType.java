
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour HabilitationTiersDeclarant_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="HabilitationTiersDeclarant_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Habilitation_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sirenEntrepriseTiersDeclarant" type="{http://cfe.recouv/2008-11/TypeRegent}SIREN_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HabilitationTiersDeclarant_Type", propOrder = {
    "sirenEntrepriseTiersDeclarant"
})
public class HabilitationTiersDeclarantType
    extends HabilitationType
{

    @XmlElement(required = true)
    protected String sirenEntrepriseTiersDeclarant;

    /**
     * Obtient la valeur de la propriété sirenEntrepriseTiersDeclarant.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSirenEntrepriseTiersDeclarant() {
        return sirenEntrepriseTiersDeclarant;
    }

    /**
     * Définit la valeur de la propriété sirenEntrepriseTiersDeclarant.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSirenEntrepriseTiersDeclarant(String value) {
        this.sirenEntrepriseTiersDeclarant = value;
    }

}
