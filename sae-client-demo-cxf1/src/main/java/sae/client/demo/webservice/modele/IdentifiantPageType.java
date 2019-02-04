
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Un identifiant de la page renvoyé par la
 *             recherche par iterateur. Il est composé d’une valeur, et
 *             d’un uuid de document
 * 
 * <p>Java class for identifiantPageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="identifiantPageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="valeur" type="{http://www.cirtil.fr/saeService}metadonneeValeurType"/>
 *         &lt;element name="idArchive" type="{http://www.cirtil.fr/saeService}uuidType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "identifiantPageType", propOrder = {
    "valeur",
    "idArchive"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class IdentifiantPageType {

    @XmlElement(required = true)
    protected String valeur;
    @XmlElement(required = true)
    protected String idArchive;

    /**
     * Gets the value of the valeur property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * Sets the value of the valeur property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValeur(String value) {
        this.valeur = value;
    }

    /**
     * Gets the value of the idArchive property.
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
     * Sets the value of the idArchive property.
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
