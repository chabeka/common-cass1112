
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération
 *             'restoreMasse'
 *          
 * 
 * <p>Classe Java pour restoreMasseRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="restoreMasseRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="uuid" type="{http://www.cirtil.fr/saeService}listeUuidType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "restoreMasseRequestType", propOrder = {
    "uuid"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class RestoreMasseRequestType {

    @XmlElement(required = true)
    protected ListeUuidType uuid;

    /**
     * Obtient la valeur de la propriété uuid.
     * 
     * @return
     *     possible object is
     *     {@link ListeUuidType }
     *     
     */
    public ListeUuidType getUuid() {
        return uuid;
    }

    /**
     * Définit la valeur de la propriété uuid.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeUuidType }
     *     
     */
    public void setUuid(ListeUuidType value) {
        this.uuid = value;
    }

}
