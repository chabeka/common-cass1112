
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *       &lt;group ref="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}IndividuEtId_Groupe" maxOccurs="unbounded"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "individuEtIdGroupe"
})
@XmlRootElement(name = "RechercheMultipleIndividuParIdReiResponse")
public class RechercheMultipleIndividuParIdReiResponse {

    @XmlElements({
        @XmlElement(name = "idIndividu", required = true, type = Long.class),
        @XmlElement(name = "individu", required = true, type = IndividuType.class),
        @XmlElement(name = "BusinessFault", namespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", required = true, type = BusinessFaultType.class),
        @XmlElement(name = "TechnicalFault", namespace = "http://www.recouv.fr/services/backend/REI/v2.0/wsdl", required = true, type = TechnicalFaultType.class)
    })
    protected List<Object> individuEtIdGroupe;

    /**
     * Gets the value of the individuEtIdGroupe property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the individuEtIdGroupe property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndividuEtIdGroupe().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * {@link IndividuType }
     * {@link BusinessFaultType }
     * {@link TechnicalFaultType }
     * 
     * 
     */
    public List<Object> getIndividuEtIdGroupe() {
        if (individuEtIdGroupe == null) {
            individuEtIdGroupe = new ArrayList<Object>();
        }
        return this.individuEtIdGroupe;
    }

}
