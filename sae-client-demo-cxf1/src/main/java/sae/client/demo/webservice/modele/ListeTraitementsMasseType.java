
package sae.client.demo.webservice.modele;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de traitements de masse
 * 
 * <p>Java class for listeTraitementsMasseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listeTraitementsMasseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="traitementMasse" type="{http://www.cirtil.fr/saeService}traitementMasseType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeTraitementsMasseType", propOrder = {
    "traitementMasse"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ListeTraitementsMasseType {

    protected List<TraitementMasseType> traitementMasse;

    /**
     * Gets the value of the traitementMasse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the traitementMasse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTraitementMasse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TraitementMasseType }
     * 
     * 
     */
    public List<TraitementMasseType> getTraitementMasse() {
        if (traitementMasse == null) {
            traitementMasse = new ArrayList<TraitementMasseType>();
        }
        return this.traitementMasse;
    }

}
