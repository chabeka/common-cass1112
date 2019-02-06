
package sae.client.demo.webservice.modele;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de range de métadonnées
 * 
 * <p>Java class for listeRangeMetadonneeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listeRangeMetadonneeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rangeMetadonnee" type="{http://www.cirtil.fr/saeService}rangeMetadonneeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
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
