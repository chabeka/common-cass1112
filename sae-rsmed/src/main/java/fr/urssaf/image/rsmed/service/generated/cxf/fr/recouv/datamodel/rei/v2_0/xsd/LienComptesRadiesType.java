
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Lien entre comptes radiés et etablissement
 * 
 * <p>Classe Java pour LienComptesRadies_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="LienComptesRadies_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nbComptesRadies" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="LienCompteRadie" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}LienCompteRadie_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LienComptesRadies_Type", propOrder = {
    "nbComptesRadies",
    "lienCompteRadie"
})
public class LienComptesRadiesType {

    protected Integer nbComptesRadies;
    @XmlElement(name = "LienCompteRadie")
    protected List<LienCompteRadieType> lienCompteRadie;

    /**
     * Obtient la valeur de la propriété nbComptesRadies.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbComptesRadies() {
        return nbComptesRadies;
    }

    /**
     * Définit la valeur de la propriété nbComptesRadies.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbComptesRadies(Integer value) {
        this.nbComptesRadies = value;
    }

    /**
     * Gets the value of the lienCompteRadie property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lienCompteRadie property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLienCompteRadie().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LienCompteRadieType }
     * 
     * 
     */
    public List<LienCompteRadieType> getLienCompteRadie() {
        if (lienCompteRadie == null) {
            lienCompteRadie = new ArrayList<LienCompteRadieType>();
        }
        return this.lienCompteRadie;
    }

}
