
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.DirigeantPersMoraleType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.DirigeantPersPhysiqueType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.EntrepriseType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;group ref="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}EntrepriseDirigee_Groupe" maxOccurs="unbounded" minOccurs="0"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entrepriseDirigeeGroupe"
})
@XmlRootElement(name = "RechercherEntreprisesParEntrepriseDirigeanteResponse")
public class RechercherEntreprisesParEntrepriseDirigeanteResponse {

    @XmlElements({
        @XmlElement(name = "entrepriseDirigee", type = EntrepriseType.class),
        @XmlElement(name = "roleDirigeancePP", type = DirigeantPersPhysiqueType.class),
        @XmlElement(name = "roleDirigeancePM", type = DirigeantPersMoraleType.class)
    })
    protected List<Object> entrepriseDirigeeGroupe;

    /**
     * Gets the value of the entrepriseDirigeeGroupe property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entrepriseDirigeeGroupe property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntrepriseDirigeeGroupe().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntrepriseType }
     * {@link DirigeantPersPhysiqueType }
     * {@link DirigeantPersMoraleType }
     * 
     * 
     */
    public List<Object> getEntrepriseDirigeeGroupe() {
        if (entrepriseDirigeeGroupe == null) {
            entrepriseDirigeeGroupe = new ArrayList<Object>();
        }
        return this.entrepriseDirigeeGroupe;
    }

}
