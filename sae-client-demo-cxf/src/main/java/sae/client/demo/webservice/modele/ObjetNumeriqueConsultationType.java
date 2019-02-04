
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Un objet numérique représenté soit un flux
 *             binaire encodé en base 64, soit par une URL de consultation
 *             directe
 * 
 * <p>Java class for objetNumeriqueConsultationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="objetNumeriqueConsultationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="url" type="{http://www.cirtil.fr/saeService}urlConsultationDirecteType"/>
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
@XmlType(name = "objetNumeriqueConsultationType", propOrder = {
    "url",
    "contenu"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ObjetNumeriqueConsultationType {

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
