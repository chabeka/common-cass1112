
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour etatTraitementsMasseResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="etatTraitementsMasseResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="traitementsMasse" type="{http://www.cirtil.fr/saeService}listeTraitementsMasseType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "etatTraitementsMasseResponseType", propOrder = {
    "traitementsMasse"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class EtatTraitementsMasseResponseType {

    @XmlElement(required = true)
    protected ListeTraitementsMasseType traitementsMasse;

    /**
     * Obtient la valeur de la propriété traitementsMasse.
     * 
     * @return
     *     possible object is
     *     {@link ListeTraitementsMasseType }
     *     
     */
    public ListeTraitementsMasseType getTraitementsMasse() {
        return traitementsMasse;
    }

    /**
     * Définit la valeur de la propriété traitementsMasse.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeTraitementsMasseType }
     *     
     */
    public void setTraitementsMasse(ListeTraitementsMasseType value) {
        this.traitementsMasse = value;
    }

}
