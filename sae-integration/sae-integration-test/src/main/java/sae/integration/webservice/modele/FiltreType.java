
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Filtre de la recherche par iterateur.
 * 
 * <p>Classe Java pour filtreType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="filtreType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="equalFilter" type="{http://www.cirtil.fr/saeService}listeMetadonneeType" minOccurs="0"/&gt;
 *         &lt;element name="notEqualFilter" type="{http://www.cirtil.fr/saeService}listeMetadonneeType" minOccurs="0"/&gt;
 *         &lt;element name="rangeFilter" type="{http://www.cirtil.fr/saeService}listeRangeMetadonneeType" minOccurs="0"/&gt;
 *         &lt;element name="notInRangeFilter" type="{http://www.cirtil.fr/saeService}listeRangeMetadonneeType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
     * Obtient la valeur de la propriété equalFilter.
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
     * Définit la valeur de la propriété equalFilter.
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
     * Obtient la valeur de la propriété notEqualFilter.
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
     * Définit la valeur de la propriété notEqualFilter.
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
     * Obtient la valeur de la propriété rangeFilter.
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
     * Définit la valeur de la propriété rangeFilter.
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
     * Obtient la valeur de la propriété notInRangeFilter.
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
     * Définit la valeur de la propriété notInRangeFilter.
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
