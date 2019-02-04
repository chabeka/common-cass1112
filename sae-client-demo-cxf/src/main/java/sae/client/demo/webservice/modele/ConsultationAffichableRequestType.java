
package sae.client.demo.webservice.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultationAffichableRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultationAffichableRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idArchive" type="{http://www.cirtil.fr/saeService}uuidType"/>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/saeService}listeMetadonneeCodeType" minOccurs="0"/>
 *         &lt;element name="numeroPage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nombrePages" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultationAffichableRequestType", propOrder = {
    "idArchive",
    "metadonnees",
    "numeroPage",
    "nombrePages"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ConsultationAffichableRequestType {

    @XmlElement(required = true)
    protected String idArchive;
    protected ListeMetadonneeCodeType metadonnees;
    protected Integer numeroPage;
    protected Integer nombrePages;

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

    /**
     * Gets the value of the numeroPage property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroPage() {
        return numeroPage;
    }

    /**
     * Sets the value of the numeroPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroPage(Integer value) {
        this.numeroPage = value;
    }

    /**
     * Gets the value of the nombrePages property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNombrePages() {
        return nombrePages;
    }

    /**
     * Sets the value of the nombrePages property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNombrePages(Integer value) {
        this.nombrePages = value;
    }

}
