
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour rechercheParIterateurResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="rechercheParIterateurResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resultats" type="{http://www.cirtil.fr/saeService}listeResultatRechercheType"/&gt;
 *         &lt;element name="identifiantPageSuivante" type="{http://www.cirtil.fr/saeService}identifiantPageType" minOccurs="0"/&gt;
 *         &lt;element name="dernierePage" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rechercheParIterateurResponseType", propOrder = {
    "resultats",
    "identifiantPageSuivante",
    "dernierePage"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class RechercheParIterateurResponseType {

    @XmlElement(required = true)
    protected ListeResultatRechercheType resultats;
    protected IdentifiantPageType identifiantPageSuivante;
    protected boolean dernierePage;

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
     * Obtient la valeur de la propriété identifiantPageSuivante.
     * 
     * @return
     *     possible object is
     *     {@link IdentifiantPageType }
     *     
     */
    public IdentifiantPageType getIdentifiantPageSuivante() {
        return identifiantPageSuivante;
    }

    /**
     * Définit la valeur de la propriété identifiantPageSuivante.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentifiantPageType }
     *     
     */
    public void setIdentifiantPageSuivante(IdentifiantPageType value) {
        this.identifiantPageSuivante = value;
    }

    /**
     * Obtient la valeur de la propriété dernierePage.
     * 
     */
    public boolean isDernierePage() {
        return dernierePage;
    }

    /**
     * Définit la valeur de la propriété dernierePage.
     * 
     */
    public void setDernierePage(boolean value) {
        this.dernierePage = value;
    }

}
