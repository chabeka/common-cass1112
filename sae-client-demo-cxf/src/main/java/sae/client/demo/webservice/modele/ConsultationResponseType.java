
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultationResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultationResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objetNumerique" type="{http://www.cirtil.fr/saeService}objetNumeriqueConsultationType"/>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultationResponseType", propOrder = {
    "objetNumerique",
    "metadonnees"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ConsultationResponseType {

    @XmlElement(required = true)
    protected ObjetNumeriqueConsultationType objetNumerique;
    @XmlElement(required = true)
    protected ListeMetadonneeType metadonnees;

    /**
     * Gets the value of the objetNumerique property.
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
     * Sets the value of the objetNumerique property.
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
     * Gets the value of the metadonnees property.
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
     * Sets the value of the metadonnees property.
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
