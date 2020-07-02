
package fr.urssaf.image.parser_opencsv.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour recuperationMetadonneesResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="recuperationMetadonneesResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeDispoType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recuperationMetadonneesResponseType", propOrder = {
    "metadonnees"
})
public class RecuperationMetadonneesResponseType {

    @XmlElement(required = true)
    protected ListeMetadonneeDispoType metadonnees;

    /**
     * Obtient la valeur de la propriété metadonnees.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeDispoType }
     *     
     */
    public ListeMetadonneeDispoType getMetadonnees() {
        return metadonnees;
    }

    /**
     * Définit la valeur de la propriété metadonnees.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeDispoType }
     *     
     */
    public void setMetadonnees(ListeMetadonneeDispoType value) {
        this.metadonnees = value;
    }

}
