
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Paramètre d'entrée de l'opération 'archivage
 *             unitaire'
 * 
 * <p>Classe Java pour archivageUnitaireRequestType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="archivageUnitaireRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ecdeUrl" type="{http://www.cirtil.fr/saeService}ecdeUrlType"/&gt;
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "archivageUnitaireRequestType", propOrder = {
    "ecdeUrl",
    "metadonnees"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ArchivageUnitaireRequestType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String ecdeUrl;
    @XmlElement(required = true)
    protected ListeMetadonneeType metadonnees;

    /**
     * Obtient la valeur de la propriété ecdeUrl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcdeUrl() {
        return ecdeUrl;
    }

    /**
     * Définit la valeur de la propriété ecdeUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcdeUrl(String value) {
        this.ecdeUrl = value;
    }

    /**
     * Obtient la valeur de la propriété metadonnees.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public ListeMetadonneeType getMetadonnees() {
        return metadonnees;
    }

    /**
     * Définit la valeur de la propriété metadonnees.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public void setMetadonnees(ListeMetadonneeType value) {
        this.metadonnees = value;
    }

}
