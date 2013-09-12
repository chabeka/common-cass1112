//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.09.12 à 05:03:21 PM CEST 
//


package fr.urssaf.image.sae.integration.meta.modele.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour DictionnaireType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="DictionnaireType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="valeurs" type="{http://www.cirtil.fr/saeIntegration/meta}ListeValeursDictionnaireType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DictionnaireType", propOrder = {
    "code",
    "valeurs"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class DictionnaireType {

    @XmlElement(required = true)
    protected String code;
    @XmlElement(required = true)
    protected ListeValeursDictionnaireType valeurs;

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
     * Obtient la valeur de la propriété valeurs.
     * 
     * @return
     *     possible object is
     *     {@link ListeValeursDictionnaireType }
     *     
     */
    public ListeValeursDictionnaireType getValeurs() {
        return valeurs;
    }

    /**
     * Définit la valeur de la propriété valeurs.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeValeursDictionnaireType }
     *     
     */
    public void setValeurs(ListeValeursDictionnaireType value) {
        this.valeurs = value;
    }

}
