//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.09.17 à 04:50:06 PM CEST 
//


package fr.urssaf.image.sae.integration.droits.modele.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour CsType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="issuer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="viduree" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="pagms" type="{http://www.cirtil.fr/saeIntegration/droit}ListePagmType"/>
 *         &lt;element name="lstCnPki" type="{http://www.cirtil.fr/saeIntegration/droit}ListeCnPkiType"/>
 *         &lt;element name="verifCnCert" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="lstCnCert" type="{http://www.cirtil.fr/saeIntegration/droit}ListeCnCertType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CsType", propOrder = {
    "issuer",
    "code",
    "description",
    "viduree",
    "pagms",
    "lstCnPki",
    "verifCnCert",
    "lstCnCert"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class CsType {

    @XmlElement(required = true)
    protected String issuer;
    @XmlElement(required = true)
    protected String code;
    @XmlElement(required = true)
    protected String description;
    protected long viduree;
    @XmlElement(required = true)
    protected ListePagmType pagms;
    @XmlElement(required = true)
    protected ListeCnPkiType lstCnPki;
    protected boolean verifCnCert;
    @XmlElement(required = true)
    protected ListeCnCertType lstCnCert;

    /**
     * Obtient la valeur de la propriété issuer.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Définit la valeur de la propriété issuer.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuer(String value) {
        this.issuer = value;
    }

    /**
     * Obtient la valeur de la propriété code.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Définit la valeur de la propriété code.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Obtient la valeur de la propriété description.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la valeur de la propriété description.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Obtient la valeur de la propriété viduree.
     * 
     */
    public long getViduree() {
        return viduree;
    }

    /**
     * Définit la valeur de la propriété viduree.
     * 
     */
    public void setViduree(long value) {
        this.viduree = value;
    }

    /**
     * Obtient la valeur de la propriété pagms.
     * 
     * @return
     *     possible object is
     *     {@link ListePagmType }
     *     
     */
    public ListePagmType getPagms() {
        return pagms;
    }

    /**
     * Définit la valeur de la propriété pagms.
     * 
     * @param value
     *     allowed object is
     *     {@link ListePagmType }
     *     
     */
    public void setPagms(ListePagmType value) {
        this.pagms = value;
    }

    /**
     * Obtient la valeur de la propriété lstCnPki.
     * 
     * @return
     *     possible object is
     *     {@link ListeCnPkiType }
     *     
     */
    public ListeCnPkiType getLstCnPki() {
        return lstCnPki;
    }

    /**
     * Définit la valeur de la propriété lstCnPki.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeCnPkiType }
     *     
     */
    public void setLstCnPki(ListeCnPkiType value) {
        this.lstCnPki = value;
    }

    /**
     * Obtient la valeur de la propriété verifCnCert.
     * 
     */
    public boolean isVerifCnCert() {
        return verifCnCert;
    }

    /**
     * Définit la valeur de la propriété verifCnCert.
     * 
     */
    public void setVerifCnCert(boolean value) {
        this.verifCnCert = value;
    }

    /**
     * Obtient la valeur de la propriété lstCnCert.
     * 
     * @return
     *     possible object is
     *     {@link ListeCnCertType }
     *     
     */
    public ListeCnCertType getLstCnCert() {
        return lstCnCert;
    }

    /**
     * Définit la valeur de la propriété lstCnCert.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeCnCertType }
     *     
     */
    public void setLstCnCert(ListeCnCertType value) {
        this.lstCnCert = value;
    }

}
