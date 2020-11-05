
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Les correspondants sont des personnes physiques employees par une entreprise. Les correspondants peuvent etre habilites ( des habilitations internes) sur la totalite ou partie des comptes l'entreprise a laquelle ils appartiennent ou  bien sur la totalite ou partie des comptes qui dependent de l'entreprise a laquelle ils appartiennent. Un correspondant employe par une entreprise tiers-declarante herite des habilitations de son entreprise.
 * 
 * <p>Classe Java pour Correspondant_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Correspondant_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CorrespondantResume_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="habilitations" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}HabilitationInterne_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Correspondant_Type", propOrder = {
    "habilitations"
})
public class CorrespondantType
    extends CorrespondantResumeType
{

    protected List<HabilitationInterneType> habilitations;

    /**
     * Gets the value of the habilitations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the habilitations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHabilitations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HabilitationInterneType }
     * 
     * 
     */
    public List<HabilitationInterneType> getHabilitations() {
        if (habilitations == null) {
            habilitations = new ArrayList<HabilitationInterneType>();
        }
        return this.habilitations;
    }

}
