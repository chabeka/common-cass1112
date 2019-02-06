
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rechercheRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rechercheRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requete" type="{http://www.cirtil.fr/saeService}requeteRechercheType"/>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeCodeType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rechercheRequestType", propOrder = {
    "requete",
    "metadonnees"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class RechercheRequestType {

    @XmlElement(required = true)
    protected String requete;
    @XmlElement(required = true)
    protected ListeMetadonneeCodeType metadonnees;

    /**
     * Gets the value of the requete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequete() {
        return requete;
    }

    /**
     * Sets the value of the requete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequete(String value) {
        this.requete = value;
    }

    /**
     * Gets the value of the metadonnees property.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeCodeType }
     *     
     */
    public ListeMetadonneeCodeType getMetadonnees() {
        return metadonnees;
    }

    /**
     * Sets the value of the metadonnees property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeCodeType }
     *     
     */
    public void setMetadonnees(ListeMetadonneeCodeType value) {
        this.metadonnees = value;
    }

}
