
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération
 *             'suppressionMasse'
 * 
 * <p>Classe Java pour suppressionMasseRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="suppressionMasseRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requete" type="{http://www.cirtil.fr/saeService}requeteRechercheType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "suppressionMasseRequestType", propOrder = {
    "requete"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SuppressionMasseRequestType {

    @XmlElement(required = true)
    protected String requete;

    /**
     * Obtient la valeur de la propriété requete.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequete() {
        return requete;
    }

    /**
     * Définit la valeur de la propriété requete.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequete(String value) {
        this.requete = value;
    }

}
