
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour HabilitationInterne_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="HabilitationInterne_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Habilitation_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idTeledepCorrespondantInterne" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdTeledep_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HabilitationInterne_Type", propOrder = {
    "idTeledepCorrespondantInterne"
})
public class HabilitationInterneType
    extends HabilitationType
{

    @XmlElement(required = true)
    protected String idTeledepCorrespondantInterne;

    /**
     * Obtient la valeur de la propriété idTeledepCorrespondantInterne.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTeledepCorrespondantInterne() {
        return idTeledepCorrespondantInterne;
    }

    /**
     * Définit la valeur de la propriété idTeledepCorrespondantInterne.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTeledepCorrespondantInterne(String value) {
        this.idTeledepCorrespondantInterne = value;
    }

}
