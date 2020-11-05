
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.IndividuType;


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
 *         &lt;element name="individu" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Individu_Type" minOccurs="0"/&gt;
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
    "individu"
})
@XmlRootElement(name = "RechercherIndividuParIdReiResponse")
public class RechercherIndividuParIdReiResponse {

    protected IndividuType individu;

    /**
     * Obtient la valeur de la propriété individu.
     * 
     * @return
     *     possible object is
     *     {@link IndividuType }
     *     
     */
    public IndividuType getIndividu() {
        return individu;
    }

    /**
     * Définit la valeur de la propriété individu.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividuType }
     *     
     */
    public void setIndividu(IndividuType value) {
        this.individu = value;
    }

}
