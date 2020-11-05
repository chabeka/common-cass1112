
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour OptionsRechercheDirigeant_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="OptionsRechercheDirigeant_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="filtre" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}RechercheDirigeantFiltre_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OptionsRechercheDirigeant_Type", propOrder = {
    "filtre"
})
public class OptionsRechercheDirigeantType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected RechercheDirigeantFiltreType filtre;

    /**
     * Obtient la valeur de la propriété filtre.
     * 
     * @return
     *     possible object is
     *     {@link RechercheDirigeantFiltreType }
     *     
     */
    public RechercheDirigeantFiltreType getFiltre() {
        return filtre;
    }

    /**
     * Définit la valeur de la propriété filtre.
     * 
     * @param value
     *     allowed object is
     *     {@link RechercheDirigeantFiltreType }
     *     
     */
    public void setFiltre(RechercheDirigeantFiltreType value) {
        this.filtre = value;
    }

}
