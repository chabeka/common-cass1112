
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.DirigeantPersPhysiqueType;
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
 *         &lt;group ref="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}IndividuDirigeant_Groupe" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "individuDirigeantGroupe"
})
@XmlRootElement(name = "RechercherDirigeantsDeEntrepriseResponse")
public class RechercherDirigeantsDeEntrepriseResponse {

    @XmlElements({
        @XmlElement(name = "individuDirigeant", type = IndividuType.class),
        @XmlElement(name = "dirigeantPP", type = DirigeantPersPhysiqueType.class)
    })
    protected List<Object> individuDirigeantGroupe;

    /**
     * Gets the value of the individuDirigeantGroupe property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the individuDirigeantGroupe property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndividuDirigeantGroupe().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndividuType }
     * {@link DirigeantPersPhysiqueType }
     * 
     * 
     */
    public List<Object> getIndividuDirigeantGroupe() {
        if (individuDirigeantGroupe == null) {
            individuDirigeantGroupe = new ArrayList<Object>();
        }
        return this.individuDirigeantGroupe;
    }

}
