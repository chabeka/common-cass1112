//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.01.16 à 02:11:30 PM CET 
//


package fr.urssaf.image.sae.integration.droits.modele.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour AjoutPagmType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="AjoutPagmType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="csIssuer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pagms" type="{http://www.cirtil.fr/saeIntegration/droit}ListePagmType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AjoutPagmType", propOrder = {
    "csIssuer",
    "pagms"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class AjoutPagmType {

    @XmlElement(required = true)
    protected String csIssuer;
    @XmlElement(required = true)
    protected ListePagmType pagms;

    /**
     * Obtient la valeur de la propriété csIssuer.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsIssuer() {
        return csIssuer;
    }

    /**
     * Définit la valeur de la propriété csIssuer.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsIssuer(String value) {
        this.csIssuer = value;
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

}
