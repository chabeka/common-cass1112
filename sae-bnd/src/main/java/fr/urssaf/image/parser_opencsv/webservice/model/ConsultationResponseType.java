
package fr.urssaf.image.parser_opencsv.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour consultationResponseType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="consultationResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="objetNumerique" type="{http://www.cirtil.fr/saeService}objetNumeriqueConsultationType"/&gt;
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
@XmlType(name = "consultationResponseType", propOrder = {
    "objetNumerique",
    "metadonnees"
})
public class ConsultationResponseType {

    @XmlElement(required = true)
    protected ObjetNumeriqueConsultationType objetNumerique;
    @XmlElement(required = true)
    protected ListeMetadonneeType metadonnees;

    /**
     * Obtient la valeur de la propriété objetNumerique.
     * 
     * @return
     *     possible object is
     *     {@link ObjetNumeriqueConsultationType }
     *     
     */
    public ObjetNumeriqueConsultationType getObjetNumerique() {
        return objetNumerique;
    }

    /**
     * Définit la valeur de la propriété objetNumerique.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjetNumeriqueConsultationType }
     *     
     */
    public void setObjetNumerique(ObjetNumeriqueConsultationType value) {
        this.objetNumerique = value;
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
