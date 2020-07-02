
package fr.urssaf.image.parser_opencsv.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour rechercheNbResResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="rechercheNbResResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resultats" type="{http://www.cirtil.fr/saeService}listeResultatRechercheNbResType"/&gt;
 *         &lt;element name="nbResultats" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
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
@XmlType(name = "rechercheNbResResponseType", propOrder = {
    "resultats",
    "nbResultats",
    "resultatTronque"
})
public class RechercheNbResResponseType {

    @XmlElement(required = true)
    protected ListeResultatRechercheNbResType resultats;
    protected int nbResultats;
    protected boolean resultatTronque;

    /**
     * Obtient la valeur de la propriété resultats.
     * 
     * @return
     *     possible object is
     *     {@link ListeResultatRechercheNbResType }
     *     
     */
    public ListeResultatRechercheNbResType getResultats() {
        return resultats;
    }

    /**
     * Définit la valeur de la propriété resultats.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeResultatRechercheNbResType }
     *     
     */
    public void setResultats(ListeResultatRechercheNbResType value) {
        this.resultats = value;
    }

    /**
     * Obtient la valeur de la propriété nbResultats.
     * 
     */
    public int getNbResultats() {
        return nbResultats;
    }

    /**
     * Définit la valeur de la propriété nbResultats.
     * 
     */
    public void setNbResultats(int value) {
        this.nbResultats = value;
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
