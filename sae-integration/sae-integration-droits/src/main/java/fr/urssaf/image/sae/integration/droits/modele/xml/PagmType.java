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
 * <p>Classe Java pour PagmType complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="PagmType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pagma" type="{http://www.cirtil.fr/saeIntegration/droit}PagmaType"/>
 *         &lt;element name="pagmp" type="{http://www.cirtil.fr/saeIntegration/droit}PagmpType"/>
 *         &lt;element name="parametres" type="{http://www.cirtil.fr/saeIntegration/droit}ListeParametresPagmType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PagmType", propOrder = {
    "code",
    "description",
    "pagma",
    "pagmp",
    "parametres"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class PagmType {

    @XmlElement(required = true)
    protected String code;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected PagmaType pagma;
    @XmlElement(required = true)
    protected PagmpType pagmp;
    @XmlElement(required = true)
    protected ListeParametresPagmType parametres;

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
     * Obtient la valeur de la propriété pagma.
     * 
     * @return
     *     possible object is
     *     {@link PagmaType }
     *     
     */
    public PagmaType getPagma() {
        return pagma;
    }

    /**
     * Définit la valeur de la propriété pagma.
     * 
     * @param value
     *     allowed object is
     *     {@link PagmaType }
     *     
     */
    public void setPagma(PagmaType value) {
        this.pagma = value;
    }

    /**
     * Obtient la valeur de la propriété pagmp.
     * 
     * @return
     *     possible object is
     *     {@link PagmpType }
     *     
     */
    public PagmpType getPagmp() {
        return pagmp;
    }

    /**
     * Définit la valeur de la propriété pagmp.
     * 
     * @param value
     *     allowed object is
     *     {@link PagmpType }
     *     
     */
    public void setPagmp(PagmpType value) {
        this.pagmp = value;
    }

    /**
     * Obtient la valeur de la propriété parametres.
     * 
     * @return
     *     possible object is
     *     {@link ListeParametresPagmType }
     *     
     */
    public ListeParametresPagmType getParametres() {
        return parametres;
    }

    /**
     * Définit la valeur de la propriété parametres.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeParametresPagmType }
     *     
     */
    public void setParametres(ListeParametresPagmType value) {
        this.parametres = value;
    }

}
