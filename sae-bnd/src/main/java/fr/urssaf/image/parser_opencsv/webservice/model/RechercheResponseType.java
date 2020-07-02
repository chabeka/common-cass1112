
package fr.urssaf.image.parser_opencsv.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour rechercheResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="rechercheResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resultats" type="{http://www.cirtil.fr/saeService}listeResultatRechercheType"/&gt;
 *         &lt;element name="resultatTronque" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rechercheResponseType", propOrder = {
    "resultats",
    "resultatTronque"
})
public class RechercheResponseType {

    @XmlElement(required = true)
    protected ListeResultatRechercheType resultats;
    protected boolean resultatTronque;

    /**
     * Obtient la valeur de la propriété resultats.
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
     * Définit la valeur de la propriété resultats.
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
     * Obtient la valeur de la propriété resultatTronque.
     * 
     */
    public boolean isResultatTronque() {
        return resultatTronque;
    }

    /**
     * Définit la valeur de la propriété resultatTronque.
     * 
     */
    public void setResultatTronque(boolean value) {
        this.resultatTronque = value;
    }

}
