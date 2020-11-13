
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.TiersDeclarantType;


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
 *         &lt;element name="tiersDeclarants" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}TiersDeclarant_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "tiersDeclarants"
})
@XmlRootElement(name = "RechercherTiersDeclarantsParSirenResponse")
public class RechercherTiersDeclarantsParSirenResponse {

    protected List<TiersDeclarantType> tiersDeclarants;

    /**
     * Gets the value of the tiersDeclarants property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tiersDeclarants property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTiersDeclarants().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TiersDeclarantType }
     * 
     * 
     */
    public List<TiersDeclarantType> getTiersDeclarants() {
        if (tiersDeclarants == null) {
            tiersDeclarants = new ArrayList<TiersDeclarantType>();
        }
        return this.tiersDeclarants;
    }

}