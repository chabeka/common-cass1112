
package sae.client.demo.webservice.modele;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de range de métadonnées
 * 
 * <p>Classe Java pour listeRangeMetadonneeType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeRangeMetadonneeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rangeMetadonnee" type="{http://www.cirtil.fr/saeService}rangeMetadonneeType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeRangeMetadonneeType", propOrder = {
    "rangeMetadonnee"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ListeRangeMetadonneeType {

    protected List<RangeMetadonneeType> rangeMetadonnee;

    /**
     * Gets the value of the rangeMetadonnee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rangeMetadonnee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRangeMetadonnee().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RangeMetadonneeType }
     * 
     * 
     */
    public List<RangeMetadonneeType> getRangeMetadonnee() {
        if (rangeMetadonnee == null) {
            rangeMetadonnee = new ArrayList<RangeMetadonneeType>();
        }
        return this.rangeMetadonnee;
    }

}
