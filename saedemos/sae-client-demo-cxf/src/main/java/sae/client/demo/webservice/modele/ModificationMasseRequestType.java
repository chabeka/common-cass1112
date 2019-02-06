
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Param�tres d'entr�es de l'op�ration
 *             'modificationMasse'
 * 
 * <p>Java class for modificationMasseRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="modificationMasseRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeTraitement" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="urlSommaire" type="{http://www.cirtil.fr/saeService}ecdeUrlSommaireType"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="typeHash" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modificationMasseRequestType", propOrder = {
    "codeTraitement",
    "urlSommaire",
    "hash",
    "typeHash"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ModificationMasseRequestType {

    @XmlElement(required = true)
    protected String codeTraitement;
    @XmlElement(required = true)
    protected String urlSommaire;
    @XmlElement(required = true)
    protected String hash;
    @XmlElement(required = true)
    protected String typeHash;

    /**
     * Gets the value of the codeTraitement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTraitement() {
        return codeTraitement;
    }

    /**
     * Sets the value of the codeTraitement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTraitement(String value) {
        this.codeTraitement = value;
    }

    /**
     * Gets the value of the urlSommaire property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlSommaire() {
        return urlSommaire;
    }

    /**
     * Sets the value of the urlSommaire property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlSommaire(String value) {
        this.urlSommaire = value;
    }

    /**
     * Gets the value of the hash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
    }

    /**
     * Gets the value of the typeHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeHash() {
        return typeHash;
    }

    /**
     * Sets the value of the typeHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeHash(String value) {
        this.typeHash = value;
    }

}