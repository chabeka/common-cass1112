//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.02.25 à 08:59:53 AM CET 
//


package fr.urssaf.image.sae.lotinstallmaj.modele.metadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour IndexReference complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="IndexReference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="composition" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="aCreerGNT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="aCreerGNS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="aIndexerVide" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndexReference", propOrder = {
    "nom",
    "composition",
    "aCreerGNT",
    "aCreerGNS",
    "aIndexerVide"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class IndexReference {

    @XmlElement(required = true)
    protected String nom;
    @XmlElement(required = true)
    protected String composition;
    @XmlElement(required = true)
    protected String aCreerGNT;
    @XmlElement(required = true)
    protected String aCreerGNS;
    @XmlElement(required = true)
    protected String aIndexerVide;

    /**
     * Obtient la valeur de la propriété nom.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit la valeur de la propriété nom.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNom(String value) {
        this.nom = value;
    }

    /**
     * Obtient la valeur de la propriété composition.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComposition() {
        return composition;
    }

    /**
     * Définit la valeur de la propriété composition.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComposition(String value) {
        this.composition = value;
    }

    /**
     * Obtient la valeur de la propriété aCreerGNT.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACreerGNT() {
        return aCreerGNT;
    }

    /**
     * Définit la valeur de la propriété aCreerGNT.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACreerGNT(String value) {
        this.aCreerGNT = value;
    }

    /**
     * Obtient la valeur de la propriété aCreerGNS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACreerGNS() {
        return aCreerGNS;
    }

    /**
     * Définit la valeur de la propriété aCreerGNS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACreerGNS(String value) {
        this.aCreerGNS = value;
    }

    /**
     * Obtient la valeur de la propriété aIndexerVide.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAIndexerVide() {
        return aIndexerVide;
    }

    /**
     * Définit la valeur de la propriété aIndexerVide.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAIndexerVide(String value) {
        this.aIndexerVide = value;
    }

}
