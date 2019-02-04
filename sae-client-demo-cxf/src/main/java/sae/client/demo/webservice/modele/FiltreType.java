
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Filtre de la recherche par iterateur.
 * 
 * <p>Java class for filtreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="filtreType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="equalFilter" type="{http://www.cirtil.fr/saeService}listeMetadonneeType" minOccurs="0"/>
 *         &lt;element name="notEqualFilter" type="{http://www.cirtil.fr/saeService}listeMetadonneeType" minOccurs="0"/>
 *         &lt;element name="rangeFilter" type="{http://www.cirtil.fr/saeService}listeRangeMetadonneeType" minOccurs="0"/>
 *         &lt;element name="notInRangeFilter" type="{http://www.cirtil.fr/saeService}listeRangeMetadonneeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filtreType", propOrder = {
    "equalFilter",
    "notEqualFilter",
    "rangeFilter",
    "notInRangeFilter"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class FiltreType {

    protected ListeMetadonneeType equalFilter;
    protected ListeMetadonneeType notEqualFilter;
    protected ListeRangeMetadonneeType rangeFilter;
    protected ListeRangeMetadonneeType notInRangeFilter;

    /**
     * Gets the value of the equalFilter property.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public ListeMetadonneeType getEqualFilter() {
        return equalFilter;
    }

    /**
     * Sets the value of the equalFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public void setEqualFilter(ListeMetadonneeType value) {
        this.equalFilter = value;
    }

    /**
     * Gets the value of the notEqualFilter property.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public ListeMetadonneeType getNotEqualFilter() {
        return notEqualFilter;
    }

    /**
     * Sets the value of the notEqualFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public void setNotEqualFilter(ListeMetadonneeType value) {
        this.notEqualFilter = value;
    }

    /**
     * Gets the value of the rangeFilter property.
     * 
     * @return
     *     possible object is
     *     {@link ListeRangeMetadonneeType }
     *     
     */
    public ListeRangeMetadonneeType getRangeFilter() {
        return rangeFilter;
    }

    /**
     * Sets the value of the rangeFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeRangeMetadonneeType }
     *     
     */
    public void setRangeFilter(ListeRangeMetadonneeType value) {
        this.rangeFilter = value;
    }

    /**
     * Gets the value of the notInRangeFilter property.
     * 
     * @return
     *     possible object is
     *     {@link ListeRangeMetadonneeType }
     *     
     */
    public ListeRangeMetadonneeType getNotInRangeFilter() {
        return notInRangeFilter;
    }

    /**
     * Sets the value of the notInRangeFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeRangeMetadonneeType }
     *     
     */
    public void setNotInRangeFilter(ListeRangeMetadonneeType value) {
        this.notInRangeFilter = value;
    }

}
