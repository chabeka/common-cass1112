
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Requête principale de la recherche par
 *             iterateur.
 * 
 * <p>Java class for requetePrincipaleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="requetePrincipaleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fixedMetadatas" type="{http://www.cirtil.fr/saeService}listeMetadonneeType"/>
 *         &lt;element name="varyingMetadata" type="{http://www.cirtil.fr/saeService}rangeMetadonneeType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requetePrincipaleType", propOrder = {
    "fixedMetadatas",
    "varyingMetadata"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class RequetePrincipaleType {

    @XmlElement(required = true)
    protected ListeMetadonneeType fixedMetadatas;
    @XmlElement(required = true)
    protected RangeMetadonneeType varyingMetadata;

    /**
     * Gets the value of the fixedMetadatas property.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public ListeMetadonneeType getFixedMetadatas() {
        return fixedMetadatas;
    }

    /**
     * Sets the value of the fixedMetadatas property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public void setFixedMetadatas(ListeMetadonneeType value) {
        this.fixedMetadatas = value;
    }

    /**
     * Gets the value of the varyingMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link RangeMetadonneeType }
     *     
     */
    public RangeMetadonneeType getVaryingMetadata() {
        return varyingMetadata;
    }

    /**
     * Sets the value of the varyingMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link RangeMetadonneeType }
     *     
     */
    public void setVaryingMetadata(RangeMetadonneeType value) {
        this.varyingMetadata = value;
    }

}
