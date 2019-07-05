
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Un identifiant de la page renvoyé par la
 *             recherche par iterateur. Il est composé d’une valeur, et
 *             d’un uuid de document
 * 
 * <p>Classe Java pour identifiantPageType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="identifiantPageType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="valeur" type="{http://www.cirtil.fr/saeService}metadonneeValeurType"/&gt;
 *         &lt;element name="idArchive" type="{http://www.cirtil.fr/saeService}uuidType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "identifiantPageType", propOrder = {
    "valeur",
    "idArchive"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class IdentifiantPageType {

    @XmlElement(required = true)
    protected String valeur;
    @XmlElement(required = true)
    protected String idArchive;

    /**
     * Obtient la valeur de la propriété valeur.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * Définit la valeur de la propriété valeur.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValeur(String value) {
        this.valeur = value;
    }

    /**
     * Obtient la valeur de la propriété idArchive.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdArchive() {
        return idArchive;
    }

    /**
     * Définit la valeur de la propriété idArchive.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdArchive(String value) {
        this.idArchive = value;
    }

}
