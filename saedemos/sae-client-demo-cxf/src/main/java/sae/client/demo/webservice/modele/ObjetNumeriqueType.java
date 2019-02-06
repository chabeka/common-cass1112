
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Un objet numérique peut être représenté soit
 *             par son URL ECDE, soit par un flux binaire encodé en base64
 * 
 * <p>Java class for objetNumeriqueType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="objetNumeriqueType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="url" type="{http://www.cirtil.fr/saeService}ecdeUrlType"/>
 *           &lt;element name="contenu" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "objetNumeriqueType", propOrder = {
    "url",
    "contenu"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ObjetNumeriqueType {

    protected String url;
    protected byte[] contenu;

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the contenu property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getContenu() {
        return contenu;
    }

    /**
     * Sets the value of the contenu property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setContenu(byte[] value) {
        this.contenu = ((byte[]) value);
    }

}
