
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour TiersDeclarant_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="TiersDeclarant_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EntrepriseResume_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeRole" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="correspondant" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CorrespondantResume_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="habilitations" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}HabilitationTiersDeclarant_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TiersDeclarant_Type", propOrder = {
    "codeRole",
    "correspondant",
    "habilitations"
})
public class TiersDeclarantType
    extends EntrepriseResumeType
{

    protected Integer codeRole;
    protected List<CorrespondantResumeType> correspondant;
    protected List<HabilitationTiersDeclarantType> habilitations;

    /**
     * Obtient la valeur de la propriété codeRole.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodeRole() {
        return codeRole;
    }

    /**
     * Définit la valeur de la propriété codeRole.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodeRole(Integer value) {
        this.codeRole = value;
    }

    /**
     * Gets the value of the correspondant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the correspondant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCorrespondant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CorrespondantResumeType }
     * 
     * 
     */
    public List<CorrespondantResumeType> getCorrespondant() {
        if (correspondant == null) {
            correspondant = new ArrayList<CorrespondantResumeType>();
        }
        return this.correspondant;
    }

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
     * {@link HabilitationTiersDeclarantType }
     * 
     * 
     */
    public List<HabilitationTiersDeclarantType> getHabilitations() {
        if (habilitations == null) {
            habilitations = new ArrayList<HabilitationTiersDeclarantType>();
        }
        return this.habilitations;
    }

}
