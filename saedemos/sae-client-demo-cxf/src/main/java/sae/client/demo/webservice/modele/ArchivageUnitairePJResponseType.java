
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Valeur de retour de l'opération 'archivage
 *             Unitaire PJ'
 * 
 * <p>Classe Java pour archivageUnitairePJResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="archivageUnitairePJResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
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
@XmlType(name = "archivageUnitairePJResponseType", propOrder = {
    "idArchive"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ArchivageUnitairePJResponseType {

    @XmlElement(required = true)
    protected String idArchive;

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
