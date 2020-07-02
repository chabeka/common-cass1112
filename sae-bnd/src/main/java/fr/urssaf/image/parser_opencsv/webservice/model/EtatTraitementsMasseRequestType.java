
package fr.urssaf.image.parser_opencsv.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour etatTraitementsMasseRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="etatTraitementsMasseRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="listeUuid" type="{http://www.cirtil.fr/saeService}listeUuidType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "etatTraitementsMasseRequestType", propOrder = {
    "listeUuid"
})
public class EtatTraitementsMasseRequestType {

    @XmlElement(required = true)
    protected ListeUuidType listeUuid;

    /**
     * Obtient la valeur de la propriété listeUuid.
     * 
     * @return
     *     possible object is
     *     {@link ListeUuidType }
     *     
     */
    public ListeUuidType getListeUuid() {
        return listeUuid;
    }

    /**
     * Définit la valeur de la propriété listeUuid.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeUuidType }
     *     
     */
    public void setListeUuid(ListeUuidType value) {
        this.listeUuid = value;
    }

}
