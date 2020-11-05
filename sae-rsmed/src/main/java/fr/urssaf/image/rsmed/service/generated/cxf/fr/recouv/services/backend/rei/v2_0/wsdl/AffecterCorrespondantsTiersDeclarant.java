
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.CorrespondantResumeType;


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
 *         &lt;element name="sirenTiersDeclarant" type="{http://cfe.recouv/2008-11/TypeRegent}SIREN_Type"/&gt;
 *         &lt;element name="correspondants" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CorrespondantResume_Type" maxOccurs="unbounded"/&gt;
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
    "sirenTiersDeclarant",
    "correspondants"
})
@XmlRootElement(name = "AffecterCorrespondantsTiersDeclarant")
public class AffecterCorrespondantsTiersDeclarant {

    @XmlElement(required = true)
    protected String sirenTiersDeclarant;
    @XmlElement(required = true)
    protected List<CorrespondantResumeType> correspondants;

    /**
     * Obtient la valeur de la propriété sirenTiersDeclarant.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSirenTiersDeclarant() {
        return sirenTiersDeclarant;
    }

    /**
     * Définit la valeur de la propriété sirenTiersDeclarant.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSirenTiersDeclarant(String value) {
        this.sirenTiersDeclarant = value;
    }

    /**
     * Gets the value of the correspondants property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the correspondants property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCorrespondants().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CorrespondantResumeType }
     * 
     * 
     */
    public List<CorrespondantResumeType> getCorrespondants() {
        if (correspondants == null) {
            correspondants = new ArrayList<CorrespondantResumeType>();
        }
        return this.correspondants;
    }

}
