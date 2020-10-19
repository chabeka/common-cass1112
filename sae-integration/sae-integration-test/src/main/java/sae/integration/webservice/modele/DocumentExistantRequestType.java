
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération de vérification de l'existance du document
 * 
 * <p>Classe Java pour documentExistantRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="documentExistantRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idGed" type="{http://www.cirtil.fr/saeService}uuidType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentExistantRequestType", propOrder = {
    "idGed"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class DocumentExistantRequestType {

    @XmlElement(required = true)
    protected String idGed;

    /**
     * Obtient la valeur de la propriété idGed.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdGed() {
        return idGed;
    }

    /**
     * Définit la valeur de la propriété idGed.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdGed(final String value) {
        idGed = value;
    }

}
