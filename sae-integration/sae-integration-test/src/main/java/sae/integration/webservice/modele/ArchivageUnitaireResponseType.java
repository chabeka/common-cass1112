
package sae.integration.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Valeur de retour de l'opération 'archivage
 *             unitaire'
 * 
 * <p>Classe Java pour archivageUnitaireResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="archivageUnitaireResponseType"&gt;
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
@XmlType(name = "archivageUnitaireResponseType", propOrder = {
    "idArchive"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ArchivageUnitaireResponseType {

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
    public void setIdArchive(final String value) {
        idArchive = value;
    }

}
