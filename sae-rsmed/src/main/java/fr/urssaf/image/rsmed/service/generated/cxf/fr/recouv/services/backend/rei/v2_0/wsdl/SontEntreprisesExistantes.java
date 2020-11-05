
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="siren" type="{http://cfe.recouv/2008-11/TypeRegent}SIREN_Type" maxOccurs="50"/&gt;
 *         &lt;element name="activesUniquement" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
    "siren",
    "activesUniquement"
})
@XmlRootElement(name = "SontEntreprisesExistantes")
public class SontEntreprisesExistantes {

    @XmlElement(required = true)
    protected List<String> siren;
    protected boolean activesUniquement;

    /**
     * Gets the value of the siren property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the siren property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSiren().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSiren() {
        if (siren == null) {
            siren = new ArrayList<String>();
        }
        return this.siren;
    }

    /**
     * Obtient la valeur de la propriété activesUniquement.
     * 
     */
    public boolean isActivesUniquement() {
        return activesUniquement;
    }

    /**
     * Définit la valeur de la propriété activesUniquement.
     * 
     */
    public void setActivesUniquement(boolean value) {
        this.activesUniquement = value;
    }

}
