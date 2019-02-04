
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rechercheResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rechercheResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultats" type="{http://www.cirtil.fr/saeService}listeResultatRechercheType"/>
 *         &lt;element name="resultatTronque" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rechercheResponseType", propOrder = {
    "resultats",
    "resultatTronque"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class RechercheResponseType {

    @XmlElement(required = true)
    protected ListeResultatRechercheType resultats;
    protected boolean resultatTronque;

    /**
     * Gets the value of the resultats property.
     * 
     * @return
     *     possible object is
     *     {@link ListeResultatRechercheType }
     *     
     */
    public ListeResultatRechercheType getResultats() {
        return resultats;
    }

    /**
     * Sets the value of the resultats property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeResultatRechercheType }
     *     
     */
    public void setResultats(ListeResultatRechercheType value) {
        this.resultats = value;
    }

    /**
     * Gets the value of the resultatTronque property.
     * 
     */
    public boolean isResultatTronque() {
        return resultatTronque;
    }

    /**
     * Sets the value of the resultatTronque property.
     * 
     */
    public void setResultatTronque(boolean value) {
        this.resultatTronque = value;
    }

}
